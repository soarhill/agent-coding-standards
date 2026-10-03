param(
    [Parameter(Mandatory=$true)][string]$Worktree,
    [Parameter(Mandatory=$true)][string]$Case,
    [Parameter(Mandatory=$true)][ValidateSet('A','B')][string]$Label
)
$ErrorActionPreference = 'Stop'
$base = '2f1007632c29d74bc4e385d9f2d94de3790a2cbc'
$id = $Case.Substring(0,2) + '-' + $Label
$outDir = Join-Path $PSScriptRoot ('runs/' + $id)
New-Item -ItemType Directory -Path $outDir -Force | Out-Null
$head = (git -C $Worktree rev-parse HEAD).Trim()
if ($head -ne $base) { throw "Unexpected starting commit: $head" }
$statusBefore = @(git -C $Worktree status --porcelain=v1 --untracked-files=all)
$untracked = @(git -C $Worktree ls-files --others --exclude-standard)
$generated = @($untracked | Where-Object { $_ -match '\.class$' })
$untrackedSource = @($untracked | Where-Object { $_ -notmatch '\.class$' })
foreach ($p in $untrackedSource) {
    git -C $Worktree add --intent-to-add -- $p
    if ($LASTEXITCODE -ne 0) { throw "Cannot include untracked source: $p" }
}
$changed = @(git -C $Worktree diff --name-only HEAD --)
$diffPath = Join-Path $outDir 'change.diff'
git -C $Worktree diff --binary --unified=1000 --no-ext-diff "--output=$diffPath" HEAD --
if ($LASTEXITCODE -ne 0) { throw 'Diff capture failed' }
$numstat = @(git -C $Worktree diff --numstat HEAD --)
$diffCheck = @(git -C $Worktree diff --check HEAD -- 2>&1 | ForEach-Object { $_.ToString() })
$diffCheckExit = $LASTEXITCODE
$prefix = 'benchmarks/cases/' + $Case + '/'
$scopeFiles = @($changed | Where-Object { -not $_.StartsWith($prefix) })
$testFiles = @($changed | Where-Object { $_ -match 'Test\.java$|\.test\.[^/]+$|(^|/)TASK\.md$|(^|/)(fixtures?|tests?)/' })
$manifestFiles = @($changed | Where-Object { $_ -match '(^|/)(package(-lock)?\.json|pom\.xml|build\.gradle|.*lock.*)$' })
$preflight = Get-Content -LiteralPath (Join-Path $PSScriptRoot 'PREFLIGHT.json') -Raw | ConvertFrom-Json
$frozenChanges = @($preflight.frozen_files | Where-Object {
    $rel = $_.path
    # Task implementations are intentionally mutable only in worker worktrees.
    $isProtected = $rel.StartsWith('skill/') -or $rel.StartsWith('standards/') -or $rel -match 'Test\.java$|\.test\.[^/]+$|(^|/)TASK\.md$|(^|/)package\.json$|benchmarks/(rubric|AUTOPILOT)\.md$'
    $isProtected -and ((-not (Test-Path -LiteralPath (Join-Path $Worktree $rel))) -or (Get-FileHash -LiteralPath (Join-Path $Worktree $rel) -Algorithm SHA256).Hash.ToLowerInvariant() -ne $_.sha256)
} | ForEach-Object { $_.path })
$testNames = @{
    '01-complex-validation' = 'RecommendationCriteriaNormalizerTest'
    '02-mechanical-mapping' = 'AvailableRoomCriteriaAssemblerTest'
    '03-error-null-contract' = 'ProfileServiceTest'
    '06-scope-control' = 'OrderServiceTest'
}
$command = $null
if ($testNames.ContainsKey($Case)) { $command = 'javac *.java && java ' + $testNames[$Case] }
elseif ($Case -eq '04-promise-sse') { $command = 'node --test chat-session.test.mjs' }
$verification = [ordered]@{ command = $command; cwd = $prefix.TrimEnd('/'); status = 'not_provided'; exit_code = $null; stdout = ''; stderr = ''; duration_ms = $null }
if ($command) {
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = 'cmd.exe'
    $psi.Arguments = '/d /c ' + $command
    $psi.WorkingDirectory = Join-Path $Worktree $prefix
    $psi.UseShellExecute = $false
    $psi.CreateNoWindow = $true
    $psi.RedirectStandardOutput = $true
    $psi.RedirectStandardError = $true
    $process = New-Object System.Diagnostics.Process
    $process.StartInfo = $psi
    $watch = [System.Diagnostics.Stopwatch]::StartNew()
    [void]$process.Start()
    $stdoutTask = $process.StandardOutput.ReadToEndAsync()
    $stderrTask = $process.StandardError.ReadToEndAsync()
    $process.WaitForExit()
    $verification.stdout = $stdoutTask.Result
    $verification.stderr = $stderrTask.Result
    $verification.exit_code = $process.ExitCode
    $verification.status = if ($process.ExitCode -eq 0) { 'passed' } else { 'failed' }
    $watch.Stop()
    $verification.duration_ms = $watch.ElapsedMilliseconds
    $process.Dispose()
}
$metadata = [ordered]@{
    run_id = $id
    case = $Case
    anonymous_label = $Label
    starting_commit = $head
    changed_files = $changed
    diff_numstat = $numstat
    verification = $verification
    factual_checks = [ordered]@{
        outside_case_changed_files = $scopeFiles
        test_or_task_changed_files = $testFiles
        dependency_manifest_changed_files = $manifestFiles
        protected_file_hash_mismatches = $frozenChanges
        whitespace_check_exit_code = $diffCheckExit
        whitespace_check_output = $diffCheck
        generated_class_files_excluded_from_diff = $generated
        additional_source_files = $untrackedSource
    }
}
$metadata | ConvertTo-Json -Depth 10 | Set-Content -LiteralPath (Join-Path $outDir 'metadata.json') -Encoding UTF8
$statusBefore | Set-Content -LiteralPath (Join-Path $outDir 'git-status.txt') -Encoding UTF8
$verification.stdout | Set-Content -LiteralPath (Join-Path $outDir 'verification.stdout.txt') -Encoding UTF8
$verification.stderr | Set-Content -LiteralPath (Join-Path $outDir 'verification.stderr.txt') -Encoding UTF8
$metadata | ConvertTo-Json -Depth 10
