public record UserClientResponse(boolean success, boolean found, UserDto data, String errorMessage) {
    public static UserClientResponse found(UserDto user) {
        return new UserClientResponse(true, true, user, null);
    }

    public static UserClientResponse notFound() {
        return new UserClientResponse(true, false, null, null);
    }

    public static UserClientResponse failed(String message) {
        return new UserClientResponse(false, false, null, message);
    }
}
