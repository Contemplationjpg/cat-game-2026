package a3;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.Vector4f;
import org.joml.Vector3f;
import org.joml.Matrix4f;

import org.joml.*;


public class TurnAction extends AbstractInputAction{
    private MyGame game;
    private GameObject av;
    private Vector3f upVector, fwdVector, rightVector;
    private Camera cam;
    private float speed = 3f;
    private Boolean isController;
    private Boolean isRight;
    
    
    public TurnAction(MyGame g, Boolean c, Boolean r) {
        // System.out.println("initialized turn action");
        game = g;
        isController = c;
        isRight = r;
    }
    public TurnAction(MyGame g, Boolean c) {
        // System.out.println("initialized turn action");
        game = g;
        isController = c;
        isRight = false;
    }

    @Override 
    public void performAction(float time, Event e) {
        float keyValue = e.getValue();
        // System.out.println(keyValue);
        if (keyValue > -.2 && keyValue < .2){
            return;
        }

        if (isController) {
            turn(time*speed, -keyValue);
        }
        else if (isRight) {
            turn(time*speed, -keyValue);
        }
        else {
            turn(time*speed, keyValue);
        }

        // System.out.println("turn standing");
        // cam = game.getCamera();
		// rightVector = cam.getU();
		// upVector = cam.getV();
		// fwdVector = cam.getN();
		// rightVector.rotateAxis(-speed*keyValue, upVector.x(), upVector.y(), upVector.z());
		// fwdVector.rotateAxis(-speed*keyValue, upVector.x(), upVector.y(), upVector.z());
        // cam.setU(rightVector);
        // cam.setN(fwdVector);
    }

    public void turn(float time, float keyVal) {
        av = game.getAvatar();
        av.globalYaw(speed*keyVal*time); 
    }


}
