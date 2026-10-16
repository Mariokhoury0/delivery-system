package application;

public enum UserType {
    CUSTOMER("Customer"),
    RIDER("Rider"),
    RESTAURANT_ADMIN("Restaurant Admin");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}