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

    private JSONArray getUsersList() {
        try {
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

    public List<User> readUsers() {
        final List<User> users = new ArrayList<>();

        for (Object object : usersList) {
            if (object instanceof JSONObject) {
                User user = this.readUser((JSONObject) object);
                if (user != null) users.add(user);
            } else {
                System.err.println("User entry in users Json is not a Json object.");
            }
        }

        return users;
    }

    @Nullable
    private User readUser (JSONObject jsonObject) {
        String username = this.getStringFromKey(jsonObject, USERNAME_KEY);
        String password = this.getStringFromKey(jsonObject, PASSWORD_KEY);

        if (username == null || password == null)
            return null;

        PasswordHandler passwordHandler = new PasswordHandler(password);
        return new User(username, passwordHandler);
    }

    @Nullable
    private String getStringFromKey(JSONObject jsonObject, String key) {
        if (!jsonObject.containsKey(key)) {
            System.err.println("User did not contain key \"" + key + "\" in Users Json.");
            return null;
        }

        Object object = jsonObject.get(key);
        return object instanceof String ? (String) object : null;
    }

    public void writeUser (User user) {

    }

}
