# Naming and organization validation

This note records the external evidence used to add naming/package/module guidance to the runtime Skill.

The goal is **not** to copy one company's complete style guide. The goal is to keep the cross-project rules that improve readability and navigation while leaving architecture and repository-specific conventions to the project.

## Sources reviewed

### Alibaba Java Coding Guidelines / P3C

Source:

- https://github.com/alibaba/p3c
- https://github.com/alibaba/p3c/blob/master/p3c-pmd/README.md
- https://github.com/alibaba/Alibaba-Java-Coding-Guidelines

Relevant guidance:

- class names use UpperCamelCase;
- methods, parameters, fields, and local variables use lowerCamelCase;
- constants use uppercase words separated by underscores;
- Java package names are lowercase;
- uncommon abbreviations should be avoided;
- the published-library guidance uses product/module-oriented artifact naming.

Alibaba also contains organization-specific rules such as singular package names and `Impl` suffixes for some interfaces. Those are **not** promoted to universal runtime rules because other mature codebases use different coherent conventions.

## Google Java Style Guide

Source:

- https://google.github.io/styleguide/javaguide.html

Relevant guidance:

- package and module names use lowercase letters/digits and avoid underscores;
- class names use UpperCamelCase;
- method names use lowerCamelCase;
- names should follow consistent identifier conventions instead of custom prefixes/suffixes.

This independently supports the lowercase package/module rule while avoiding Alibaba-specific conventions.

## Oracle Java package naming guidance

Source:

- https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html

Relevant guidance:

- package names are lowercase;
- reverse Internet domain names are a longstanding convention for globally unique package prefixes.

The cited Oracle tutorial is explicitly JDK 8-era material, so it is used only as historical support for the established package-prefix convention, not as a source for newer Java design rules.

## Apache Maven conventions

Source:

- https://maven.apache.org/maven-conventions.html

Relevant guidance:

- when joining an existing Maven product, follow naming patterns already established by sibling projects;
- group/artifact identifiers intended for public distribution should use predictable naming;
- Maven's current convention recommends lowercase letters, digits, and hyphens for these identifiers.

This supports "follow the repository first" and lowercase/hyphenated artifact naming rather than imposing a new product taxonomy.

## Gradle project-structure guidance

Sources:

- https://docs.gradle.org/current/userguide/organizing_gradle_projects.html
- https://docs.gradle.org/current/userguide/best_practices_structuring_builds.html
- https://docs.gradle.org/current/userguide/best_practices_general.html

Relevant guidance:

- project structure should be clear, consistent, and meaningful;
- standard conventions should be preferred unless there is a strong reason to deviate;
- logical project names and physical project locations should stay aligned;
- avoid unnecessarily deep project structures;
- multi-project boundaries should follow natural boundaries rather than arbitrary file-count thresholds.

This supports module-name/path alignment and treating new build modules as meaningful boundaries rather than cosmetic folders.

## Vue official style guide

Sources:

- https://vuejs.org/style-guide/rules-essential.html
- https://vuejs.org/style-guide/rules-strongly-recommended.html

Relevant guidance:

- user component names should be multi-word except conventional root components;
- one component per file is strongly recommended in normal build-system projects;
- SFC filenames should consistently use PascalCase or kebab-case;
- full words are preferred over unclear abbreviations;
- tightly coupled child components should make the parent relationship visible in their names.

The Vue guide itself says mindful deviations are acceptable, so the runtime Skill keeps repository consistency above blind normalization.

## npm package-name guidance

Source:

- https://docs.npmjs.com/package-name-guidelines/

Relevant guidance:

- published package names should be descriptive and lowercase;
- package naming should avoid confusingly similar names.

The runtime Skill applies this only when a task actually creates or renames a published npm package.

## Runtime synthesis

The runtime Skill therefore adopts these rules:

1. **Meaning first:** names should reveal concept/responsibility/behavior.
2. **Repository first:** preserve a coherent local convention instead of importing a competing style.
3. **Avoid cognitive noise:** prefer full words; avoid vague catch-all names and uncommon abbreviations.
4. **Java packages:** lowercase; preserve the existing root namespace; do not force organization-specific singular/`Impl` conventions.
5. **Build modules:** follow sibling naming; keep logical names and paths aligned; do not invent modules only for tidiness.
6. **Vue files/components:** multi-word component names, consistent SFC casing, full words, and visible parent-child naming where applicable.
7. **Scope guardrail:** naming cleanup does not authorize unrelated renames or architecture restructuring.

These are coding/organization quality rules. Decisions such as feature-vs-layer package architecture, bounded-context design, service decomposition, or whether a codebase should become multi-module remain outside this Skill's authority.
