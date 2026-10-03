import java.util.List;

public record RoomCandidateQuery(
        Long cityId,
        Long districtId,
        Integer minRent,
        Integer maxRent,
        Integer excludedMinRent,
        Integer excludedMaxRent,
        Integer minArea,
        Integer maxArea,
        Integer excludedMinArea,
        Integer excludedMaxArea,
        List<Integer> floors,
        List<Integer> excludedFloors,
        List<String> requiredFacilities,
        List<String> excludedFacilities,
        List<String> requiredLabels,
        List<String> excludedLabels) {
}
