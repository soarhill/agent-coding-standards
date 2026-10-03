import java.util.ArrayList;
import java.util.List;

public final class AvailableRoomCriteriaAssembler {
    private AvailableRoomCriteriaAssembler() {
    }

    public static AvailableRoomCriteria from(RoomCandidateQuery query) {
        if (query == null) {
            return new AvailableRoomCriteria(
                    null, null,
                    null, null, null, null,
                    null, null, null, null,
                    List.of(), List.of(),
                    List.of(), List.of(),
                    List.of(), List.of(),
                    true);
        }

        return new AvailableRoomCriteria(
                query.cityId(),
                query.districtId(),
                query.minRent(),
                query.maxRent(),
                query.excludedMinRent(),
                query.excludedMaxRent(),
                query.minArea(),
                query.maxArea(),
                query.excludedMinArea(),
                query.excludedMaxArea(),
                copyIntegers(query.floors()),
                copyIntegers(query.excludedFloors()),
                copyStrings(query.requiredFacilities()),
                copyStrings(query.excludedFacilities()),
                copyStrings(query.requiredLabels()),
                List.of(),
                false);
    }

    private static List<Integer> copyIntegers(List<Integer> values) {
        return values == null ? List.of() : new ArrayList<>(values);
    }

    private static List<String> copyStrings(List<String> values) {
        return values == null ? List.of() : new ArrayList<>(values);
    }
}
