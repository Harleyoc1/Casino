package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.Casino;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public abstract class MenuScreen {

    protected final Casino casino;
    protected final Stage stage;
    protected final Scene scene;
    protected final MenuScreen previousScreen;
    protected final Pane layout;

    public MenuScreen(Casino casino, Stage stage, Scene scene, MenuScreen previousScreen) {
        this.casino = casino;
        this.stage = stage;
        this.scene = scene;
        this.previousScreen = previousScreen;

        this.layout = this.setupScreen();
    }

    /**
     * Sets the title of the scene to the result of <tt>this.getTitle()</tt>.
     */
    private void setTitle () {
        this.casino.setTitle(this.getTitle());
    }

    /**
     * Sets the title of the scene and sets the root of the scene to <tt>this.layout</tt>.
     */
    public void show () {
        this.setTitle();
        this.scene.setRoot(this.layout);
    }

    /**
     * Sub-classes should implement this to set up their main layout, instantiating and configuring
     * a {@link Pane}. This {@link Pane} will be assigned to <tt>this.layout</tt> in the constructor.
     *
     * @return The {@link Pane}, which will be set to <tt>this.layout</tt>.
     */
    protected abstract Pane setupScreen ();

    /**
     * Sub-classes should implement this to set the name of the screen, which will be used for the
     * title of the scene.
     *
     * @return The title of the scene.
     */
    public abstract String getTitle();

}
