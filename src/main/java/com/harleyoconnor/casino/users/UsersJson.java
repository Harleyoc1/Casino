package com.harleyoconnor.casino.users;

import com.harleyoconnor.casino.AppConstants;
import com.harleyoconnor.javautilities.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Harley O'Connor
 */
public final class UsersJson {

    // Key constants.
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String BALANCE_KEY = "balance";

    // A tab of space for making the Json more readable.
    private static final String SPACE = "   ";

    // The path of the users Json file.
    private static final String USERS_PATH = AppConstants.DATA_PATH + "users.json";

    private final JSONParser parser = new JSONParser(); // The Json parser instance.
    private final JSONArray usersList; // The list of users.
    private final File usersFile; // The Json file object.

    public UsersJson() {
        this.usersFile = FileUtils.getFile(USERS_PATH, false);
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

    /**
     * Writes the user data given to the users Json file. Note that this is writing the data, not merging it
     * with the current contents of the file, so the full list is needed.
     *
     * @param users The list of users.
     */
    public void writeUserData (List<User> users) {
        StringBuilder dataBuilder = new StringBuilder();

        // Create opening square bracket for array of users.
        dataBuilder.append("[\n");

        for (int i = 0; i < users.size(); i++) {
            final User user = users.get(i);

            // Create opening curly brace for current user's Json object.
            dataBuilder.append(SPACE + "{\n");

            // Write keys and values for the username, password, and balance.
            this.appendJsonElement(dataBuilder, USERNAME_KEY, "\"" + user.getUsername() + "\"", false);
            this.appendJsonElement(dataBuilder, PASSWORD_KEY, "\"" + user.getPasswordHandler().getPassword() + "\"", false);
            this.appendJsonElement(dataBuilder, BALANCE_KEY, Long.toString(user.getBitcoins()), true);

            // Create closing curly brace for current user's Json object.
            dataBuilder.append(SPACE + "}").append(i == users.size() - 1 ? "" : ",").append("\n");
        }

        // Create closing square bracket for array of users.
        dataBuilder.append("]");

        this.writeData(dataBuilder.toString());
    }

    /**
     * Appends a Json element to the given builder from the given key and value. Also appends an EOL and a
     * comma if lastOfObject is true.
     *
     * @param builder The String Builder to append to.
     * @param key The key of the Json element.
     * @param value The value of the Json element.
     * @param lastOfObject True if it is the last element of the object, false if not.
     */
    private void appendJsonElement(StringBuilder builder, String key, String value, boolean lastOfObject) {
        builder.append(SPACE + SPACE).append("\"").append(key).append("\": ").append(value).append(lastOfObject ? "" : ",").append("\n");
    }

    /**
     * Writes the string of data given to the users Json file.
     *
     * @param dataToWrite The String of Json data.
     */
    private void writeData (String dataToWrite) {
        try {
            // Create the buffered writer, which will handle writing data to the users file.
            BufferedWriter writer = Files.newBufferedWriter(this.usersFile.toPath(), StandardCharsets.UTF_8);

            writer.write(dataToWrite); // Write the actual data.
            writer.close(); // Close the writer to tell the OS we have finished writing to it (and to clean it for GC).
        } catch (IOException e) {
            throw new RuntimeException("Error writing user data to file.");
        }
    }

}
