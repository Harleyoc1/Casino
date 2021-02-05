package com.harleyoconnor.casino.users;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harley O'Connor
 */
public final class Users {

    public static final List<User> USERS = new ArrayList<>();

    public static void getFromFile () {
        // This shouldn't be called if we have already obtained the users during this session.
        if (USERS.size() > 0)
            return;

        // Get users from file.
    }

    public static boolean doesUsernameExist (String username) {
        return USERS.stream().map(User::getUsername).anyMatch(username::equalsIgnoreCase);
    }

}
