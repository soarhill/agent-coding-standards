public class UserRpcService {
    private final UserClient userClient;

    public UserRpcService(UserClient userClient) {
        this.userClient = userClient;
    }

    public UserDto findUser(long id) {
        try {
            UserClientResponse response = userClient.findById(id);
            if (response == null || !response.success()) {
                return null;
            }
            if (!response.found()) {
                return null;
            }
            return response.data();
        } catch (RuntimeException e) {
            System.err.println("user lookup failed: " + e.getMessage());
            return null;
        }
    }
}
