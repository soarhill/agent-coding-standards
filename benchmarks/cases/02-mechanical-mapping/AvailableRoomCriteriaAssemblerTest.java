import java.util.ArrayList;
import java.util.List;

public class AvailableRoomCriteriaAssemblerTest {
    public static void main(String[] args) {
        ArrayList<String> excludedLabels = new ArrayList<>(List.of("noisy", "dark"));

        RoomCandidateQuery query = new RoomCandidateQuery(
                1L, 2L,
                1000, 3000, 1500, 1800,
                20, 80, 30, 35,
                List.of(3, 4), List.of(1),
                List.of("wifi"), List.of("smoking"),
                List.of("south-facing"), excludedLabels);

        AvailableRoomCriteria criteria = AvailableRoomCriteriaAssembler.from(query);

        check(criteria.cityId().equals(1L), "city");
        check(criteria.requiredLabels().equals(List.of("south-facing")), "required labels");
        check(criteria.excludedLabels().equals(List.of("noisy", "dark")), "excluded labels");

        excludedLabels.add("top-floor");
        check(criteria.excludedLabels().equals(List.of("noisy", "dark")), "defensive copy");

        AvailableRoomCriteria impossible = AvailableRoomCriteriaAssembler.from(null);
        check(impossible.impossible(), "null query impossible");
        check(impossible.requiredFacilities().isEmpty(), "empty collections");

        System.out.println("PASS");
    }

    private static void check(boolean ok, String label) {
        if (!ok) {
            throw new AssertionError(label);
        }
    }
}
