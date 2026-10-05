import java.util.List;
import java.util.Set;

public class MatchAssemblerTest {
    public static void main(String[] args) {
        MatchAssembler assembler = new MatchAssembler();

        List<MetaRow> rows = List.of(
                new MetaRow(1L, "OpenAI", "Backend"),
                new MetaRow(2L, null, "Java"),
                new MetaRow(3L, "", ""),
                new MetaRow(4L, "Anthropic", "Infra"));

        List<Match> actual = assembler.build(
                rows,
                List.of(1L, 4L),
                Set.of(1L, 2L, 4L));

        List<Match> expected = List.of(
                new Match(1L, "company", "OpenAI", false),
                new Match(2L, "role", "Java", true),
                new Match(4L, "company", "Anthropic", false));

        check(expected.equals(actual), "matches");
        System.out.println("PASS");
    }

    private static void check(boolean ok, String label) {
        if (!ok) {
            throw new AssertionError(label);
        }
    }
}
