package com.harleyoconnor.casino.menus;

import com.harleyoconnor.casino.Casino;
import com.harleyoconnor.casino.animations.SlideAnimation;
import com.harleyoconnor.casino.animations.TranslateAxis;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Harley O'Connor
 */
public abstract class MenuScreen {

    protected final Casino casino;
    protected final Stage stage;
    protected final Scene scene;
    protected final MenuScreen previousScreen;
    protected final StackPane parentView;
    protected Pane layout;

    /**
     * Simplified constructor which defaults to automatically setting up the screen (calling setupScreen)
     * and does not have a previous screen.
     *
     * @param casino The {@link Casino} object.
     * @param stage The primary {@link Stage}.
     * @param scene The main {@link Scene}.
     */
    public MenuScreen(Casino casino, Stage stage, StackPane parentView, Scene scene) {
        this(casino, stage, scene, parentView, null);
    }

    /**
     * Simplified constructor which defaults to automatically setting up the screen (calling setupScreen).
     *
     * @param casino The {@link Casino} object.
     * @param stage The primary {@link Stage}.
     * @param scene The main {@link Scene}.
     * @param previousScreen The previous {@link MenuScreen} (or null if there wasn't one).
     */
    public MenuScreen(Casino casino, Stage stage, Scene scene, StackPane parentView, MenuScreen previousScreen) {
        this(casino, stage, scene, parentView, previousScreen, true);
    }

    /**
     * Default constructor for a {@link MenuScreen}.
     *
     * @param casino The {@link Casino} object.
     * @param stage The primary {@link Stage}.
     * @param scene The main {@link Scene}.
     * @param previousScreen The previous {@link MenuScreen} (or null if there wasn't one).
     * @param autoSetUpScreen Whether or not to automatically call setupScreen in the constructor.
     */
    public MenuScreen(Casino casino, Stage stage, Scene scene, StackPane parentView, MenuScreen previousScreen, boolean autoSetUpScreen) {
        this.casino = casino;
        this.stage = stage;
        this.scene = scene;
        this.parentView = parentView;
        this.previousScreen = previousScreen;

        if (autoSetUpScreen)
            this.layout = this.addSceneStyleClass(this.setupScreen());
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
        this.parentView.getChildren().add(this.addSceneStyleClass(this.layout));
    }

    /**
     * Transitions to given {@link MenuScreen} with a {@link SlideAnimation} from the negative x-axis (left).
     *
     * @param newScreen The new {@link MenuScreen} to display.
     */
    public void toNewScreen (MenuScreen newScreen) {
        this.toNewScreen(newScreen, TranslateAxis.X, false);
    }

    /**
     * Transitions to given {@link MenuScreen} with a {@link SlideAnimation} over the given {@link TranslateAxis}
     * from positive or negative direction.
     *
     * @param newScreen The new {@link MenuScreen} to display.
     * @param slideAxis The {@link TranslateAxis} to slide on.
     * @param slideFromPositive True if it should slide from the positive direction.
     */
    public void toNewScreen (MenuScreen newScreen, TranslateAxis slideAxis, boolean slideFromPositive) {
        // If the main layout in the new screen isn't initialised, initialise it.
        if (newScreen.layout == null)
            newScreen.layout = newScreen.setupScreen();

        // Offset the new layout.
        slideAxis.getTranslateProperty(newScreen.layout).setValue(slideAxis == TranslateAxis.X ? (slideFromPositive ? 1 : -1 ) * this.scene.getWidth() : (slideFromPositive ? 1 : -1) * this.scene.getHeight());

        // Show the new layout (it will be offset so not yet visible).
        newScreen.show();

        // Make the animation so it slides over this layout.
        new SlideAnimation<>(newScreen.layout, slideAxis, 0, 1000, Interpolator.EASE_BOTH).setOnFinish(event -> this.onSlideOutFinished(event, newScreen)).play();
    }

    /**
     * Executes when the {@link SlideAnimation} has finished sliding this screen in.
     *
     * @param event The {@link ActionEvent}.
     */
    public void onSlideInFinished (ActionEvent event) { }

    /**
     * Executes when the {@link SlideAnimation} is finished sliding this screen out.
     *
     * @param event The {@link ActionEvent}.
     * @param newScreen The new {@link MenuScreen} to display.
     */
    public void onSlideOutFinished (ActionEvent event, MenuScreen newScreen) {
        this.parentView.getChildren().remove(this.layout);
        newScreen.onSlideInFinished(event);
    }

    public Pane addSceneStyleClass (Pane pane) {
        pane.getStyleClass().add("menu-screen");
        return pane;
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
