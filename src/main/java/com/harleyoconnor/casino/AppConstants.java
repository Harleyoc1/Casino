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
    public static final String DATA_PATH = FileUtils.RESOURCES_PATH + "data/";
    public static final String STYLESHEETS_PATH = "stylesheets/";

    // File paths.
    public static final String DEFAULT_STYLESHEET_PATH = STYLESHEETS_PATH + "default.css";


    // Style classes.
    public static final String TITLE_CLASS = "title";
    public static final String BODY_CLASS = "body";
    public static final String INVISIBLE_BUTTON_CLASS = "invisible-button";

}
