public class ProfileService {
    private final UserRpcService userRpcService;

    public ProfileService(UserRpcService userRpcService) {
        this.userRpcService = userRpcService;
    }

    public String getDisplayName(long userId) {
        UserDto user = userRpcService.findUser(userId);
        return user.nickname();
    }
}
