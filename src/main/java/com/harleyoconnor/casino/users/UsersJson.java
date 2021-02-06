package com.harleyoconnor.casino.users;

import com.harleyoconnor.javautilities.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Harley O'Connor
 */
public final class UsersJson {

    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String BALANCE_KEY = "balance";

    private final JSONParser parser = new JSONParser();
    private final JSONArray usersList;
    private final File usersFile;

    public UsersJson() {
        this.usersFile = FileUtils.getFile("users.json");
        this.usersList = this.getUsersList();
    }

    /**
     * Gets Json array from users file, or throws an error. <tt>usersFile</tt> must be set before
     * calling this.
     *
     * @return The Json array.
     */
    private JSONArray getUsersList() {
        try {
            // Grab Json array from the Json parser.
            return (JSONArray) this.parser.parse(new FileReader(this.usersFile));
        } catch (IOException e) {
            System.err.println("Error constructing file reader for users file.");
        } catch (ParseException e) {
            System.err.println("Error parsing file reader for users file.");
        } catch (ClassCastException e) {
            System.err.println("Error casting parsed Json file as a Json Array.");
        }
        throw new RuntimeException("Unable to get Json Object for users file. See logs for details.");
    }

    /**
     * Reads all users from the Json file.
     *
     * @return A {@link List} of {@link User} objects.
     */
    public List<User> readUsers() {
        final List<User> users = new ArrayList<>();

        // Loop through the Json array.
        for (Object object : this.usersList) {
            // Check the object in the Json array is a Json object.
            if (object instanceof JSONObject) {
                User user = this.readUser((JSONObject) object);

                // Add user object to the list if there were no errors obtaining it.
                if (user != null)
                    users.add(user);
            } else {
                // Prints an error entry was not a Json object, as this should not happen.
                System.err.println("User entry in users Json is not a Json object.");
            }
        }

        return users;
    }

    /**
     * Reads user data from Json object.
     *
     * @param jsonObject The Json object.
     * @return A {@link User} object, or null if there was an error obtaining required values.
     */
    @Nullable
    private User readUser (JSONObject jsonObject) {
        String username = this.getStringFromKey(jsonObject, USERNAME_KEY);
        String password = this.getStringFromKey(jsonObject, PASSWORD_KEY);
        Long balance = this.getLongFromKey(jsonObject, BALANCE_KEY);

        // Return null if any of the values were not set.
        if (username == null || password == null || balance == null)
            return null;

        PasswordHandler passwordHandler = new PasswordHandler(password);
        return new User(username, passwordHandler).setBitcoins(balance);
    }

    /**
     * Gets a {@link String} from the given key.
     *
     * @param jsonObject The Json object.
     * @param key They key for the desired String.
     * @return The {@link String} obtained, or null if there was an error obtaining it.
     */
    @Nullable
    private String getStringFromKey (JSONObject jsonObject, String key) {
        return this.getFromKey(jsonObject, key, String.class);
    }

    /**
     * Gets a {@link Long} from the given key.
     *
     * @param jsonObject The Json object.
     * @param key The key for the desired value.
     * @return The {@link Long} obtained, or null if there was an error obtaining it.
     */
    @Nullable
    private Long getLongFromKey (JSONObject jsonObject, String key) {
        return this.getFromKey(jsonObject, key, Long.class);
    }

    /**
     * Gets value of type given from given key within Json Object given, logging errors in console if
     * the value is not present or the value is not of the given type.
     *
     * @param jsonObject The Json object.
     * @param key The key for the desired value.
     * @param valueType The class of the type of value to get.
     * @param <T> The type of value to get.
     * @return The value obtained, or null if there was an error obtaining it.
     */
    @Nullable
    @SuppressWarnings("unchecked") // Remove warning about unchecked cast, as we are checking the instance anyway.
    private <T> T getFromKey (JSONObject jsonObject, String key, Class<T> valueType) {
        // Check the Json object contains the key given.
        if (!jsonObject.containsKey(key)) {
            System.err.println("User did not contain key \"" + key + "\" in Users Json.");
            return null;
        }

        // Get the actual object from the key.
        Object object = jsonObject.get(key);

        // Check object is of type given.
        if (!valueType.isInstance(object)) {
            System.err.println("Value of key \"" + key + "\" was not of required type \"" + valueType.getName() + "\".");
            return null;
        }

        // Cast the object to the type given. This is safe as we checked isInstance above.
        return (T) object;
    }

    public void writeUser (User user) {

    }

}
