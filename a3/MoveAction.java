package a3;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.Vector4f;
import org.joml.Vector3f;

import org.joml.*;


public class MoveAction extends AbstractInputAction{
    private MyGame game;
    private GameObject av;
    private Vector3f oldPosition, newPosition;
    private Vector4f fwdDirection;
    private Boolean isController;
    private Boolean isForward;
    private float speed = 8f;
    
    public MoveAction(MyGame g, Boolean c, Boolean f) {
        // System.out.println("initialized fwd action");
        game = g;
        isController = c;
        isForward = f;
    }

    public MoveAction(MyGame g, Boolean c) {
        // System.out.println("initialized fwd action");
        game = g;
        isController = c;
    }

    @Override 
    public void performAction(float time, Event e) {
        float keyValue = e.getValue();
        // System.out.println(keyValue);
        if (keyValue > -.2 && keyValue < .2){
            return;
        }

        if (isController) {
            move(time, -keyValue);
        }
        else if (isForward) {
            move(time, keyValue);
        }
        else {
            move(time, -keyValue);
        }
    }

    private void move(float time, float keyVal) {
        av = game.getAvatar();
        oldPosition = av.getWorldLocation();
        fwdDirection = new Vector4f(0f, 0f, 1f, 1f);
        fwdDirection.mul(av.getWorldRotation());
        fwdDirection.mul(speed*time*keyVal);
        newPosition = oldPosition.add(fwdDirection.x(), 0, fwdDirection.z());
        av.setLocalLocation(newPosition);
    }


}
