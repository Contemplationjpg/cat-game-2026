package a3;

import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.Vector4f;
import org.joml.Vector3f;

import org.joml.*;

public class PhotoAction extends AbstractInputAction {

    private MyGame game;

    public PhotoAction(MyGame g) {
        // System.out.println("initialized fwd action");
        game = g;
    }

    @Override
    public void performAction(float time, Event e) {
        if (game.detectHome()) {
            game.placePhotos();
        } else {
            game.takePhoto();
        }
    }

}
