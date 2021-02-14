package com.harleyoconnor.casino;

import com.harleyoconnor.javautilities.FileUtils;

/**
 * Holds common constants for this program.
 *
 * @author Harley O'Connor
 */
public final class AppConstants {

    // Basic app metadata.
    public static final String APP_NAME = "Casino";
    public static final String APP_VERSION = "0.0.1";


    // File prefix.
    public static final String FILE_PREFIX = "file:";

    // Directory paths.
    public static final String ASSETS_PATH = FileUtils.ASSETS_PATH;
    public static final String TEXTURES_PATH = ASSETS_PATH + "textures/";
    public static final String DATA_PATH = FileUtils.RESOURCES_PATH + "data/";
    public static final String STYLESHEETS_PATH = "stylesheets/";

    // File paths.
    public static final String DEFAULT_STYLESHEET_PATH = STYLESHEETS_PATH + "default.css";
    public static final String ACCOUNT_STYLESHEET_PATH = STYLESHEETS_PATH + "account-screen.css";


    // Style classes.
    public static final String TITLE_CLASS = "title";
    public static final String BODY_CLASS = "body";
    public static final String INVISIBLE_BUTTON_CLASS = "invisible-button";
    public static final String WHITE_TEXT = "white-text";
    public static final String WHITE_BUTTON = "white-button";


    // Window constants.
    public static final int MIN_WIDTH = 500;
    public static final int MIN_HEIGHT = 300;
    public static final int DEFAULT_WIDTH = 900;
    public static final int DEFAULT_HEIGHT = 540;


    // Positioning constants.
    public static final int DEFAULT_PADDING = 15;
    public static final int DEFAULT_SPACING = 5;


    // If the user cannot afford the minimum bet, they must start over.
    public static final int MIN_BET = 50;

}
