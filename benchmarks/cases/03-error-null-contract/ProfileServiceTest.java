public class ProfileServiceTest {
    public static void main(String[] args) {
        ProfileService found = service(id -> UserClientResponse.found(new UserDto(id, "Ada")));
        check("Ada".equals(found.getDisplayName(1L)), "found user");

        ProfileService missing = service(id -> UserClientResponse.notFound());
        check("Anonymous".equals(missing.getDisplayName(2L)), "missing user");

        ProfileService failedResponse = service(id -> UserClientResponse.failed("timeout"));
        expectUpstreamFailure(failedResponse, 3L);

        ProfileService thrownFailure = service(id -> {
            throw new IllegalStateException("connection reset");
        });
        expectUpstreamFailure(thrownFailure, 4L);

        System.out.println("PASS");
    }

    private static ProfileService service(UserClient client) {
        return new ProfileService(new UserRpcService(client));
    }

    private static void expectUpstreamFailure(ProfileService service, long id) {
        try {
            service.getDisplayName(id);
            throw new AssertionError("expected upstream failure");
        } catch (UpstreamUserServiceException expected) {
            check(expected.getMessage() != null && !expected.getMessage().isBlank(), "failure message");
        }
    }

    private static void check(boolean ok, String label) {
        if (!ok) {
            throw new AssertionError(label);
        }
    }
}
