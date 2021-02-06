package com.harleyoconnor.casino.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Harley O'Connor
 */
public final class Users {

    public static final int MIN_PASSWORD_LENGTH = 8;

    private static final List<User> USERS = new ArrayList<>();
    private static final UsersJson USERS_JSON = new UsersJson();

    public static void getFromFile () {
        // This shouldn't be called if we have already obtained the users during this session.
        if (USERS.size() > 0)
            return;

        // Get users from file.
        USERS.addAll(USERS_JSON.readUsers());
    }

    public static void register(User user) {
        // This should be checked before registering a user anyway, but just in case.
        if (doesUsernameExist(user.getUsername()))
            return;

        USERS.add(user);
    }

    public static Optional<User> find(String username) {
        return USERS.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public static boolean doesUsernameExist (String username) {
        return USERS.stream().map(User::getUsername).anyMatch(username::equalsIgnoreCase);
    }

}
