package com.harleyoconnor.casino.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Harley O'Connor
 */
public final class Users {

    /** The minimum password length. Used when signing up to the system. */
    public static final int MIN_PASSWORD_LENGTH = 8;

    /** The array list of registered users. */
    private static final List<User> USERS = new ArrayList<>();

    /** The {@link UsersJson} object. */
    private static final UsersJson USERS_JSON = new UsersJson();

    /**
     * Gets all users from the users Json file.
     */
    public static void getFromFile () {
        // This shouldn't be called if we have already obtained the users during this session.
        if (USERS.size() > 0)
            return;

        // Get users from file.
        USERS.addAll(USERS_JSON.readUsers());
    }

    /**
     * Registers a new user object, and writes it to the users Json file.
     *
     * @param user The user object to register.
     */
    public static void register(User user) {
        // This should be checked before registering a user anyway, but just in case.
        if (doesUsernameExist(user.getUsername()))
            return;

        // Add user to the user list.
        USERS.add(user);

        // Update the user Json file.
        USERS_JSON.writeUserData(USERS);
    }

    /**
     * Writes all current user data to the user Json file. This should be called whenever a User object is changed.
     */
    public static void updated () {
        // Update the user Json file.
        USERS_JSON.writeUserData(USERS);
    }

    /**
     * Finds the user from the given username if they exist.
     *
     * @param username The username of the user.
     * @return An optional, containing the user object if it exists.
     */
    public static Optional<User> find(String username) {
        return USERS.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    /**
     * Checks if given username exists.
     *
     * @param username The username of the user.
     * @return True if the given username exists, false if not.
     */
    public static boolean doesUsernameExist (String username) {
        return USERS.stream().map(User::getUsername).anyMatch(username::equalsIgnoreCase);
    }

}
