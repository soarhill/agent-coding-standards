import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MatchAssembler {
    private static final String SEP = "\u0000";
    private static final char PLACEHOLDER = '\u0000';

    public List<Match> build(List<MetaRow> rows, List<Long> fullCoverIds, Set<Long> questionMatchedIds) {
        Map<Long, Long> fullCover = new HashMap<>();
        for (Long id : fullCoverIds) {
            fullCover.put(id, id);
        }

        List<Match> result = new ArrayList<>();
        for (MetaRow row : rows) {
            boolean dispersed = questionMatchedIds.contains(row.id()) && !fullCover.containsKey(row.id());
            String meta = meta(row);
            if (meta == null) {
                continue;
            }

            int sep = meta.indexOf(PLACEHOLDER);
            result.add(new Match(
                    row.id(),
                    meta.substring(0, sep),
                    meta.substring(sep + 1),
                    dispersed));
        }
        return result;
    }

    private String meta(MetaRow row) {
        if (row.company() != null && !row.company().isBlank()) {
            return "company" + SEP + row.company();
        }
        if (row.role() != null && !row.role().isBlank()) {
            return "role" + SEP + row.role();
        }
        return null;
    }
}
