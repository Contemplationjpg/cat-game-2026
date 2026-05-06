// Made by M. Saechao
package tage;

import a3.MyGame;
import tage.input.*;
import org.joml.*;
import org.joml.Vector3f;
import tage.*;
import tage.Engine.*;
import java.lang.Math;
import java.util.concurrent.ThreadPoolExecutor;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.Vector4f;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;

;

public class CameraOrbitController {

    private Engine engine;
    private Camera camera;
    private MyGame game;
    private GameObject avatar;
    private float cameraAzimuth;
    private float cameraElevation;
    private float cameraRadius;
    private float speed;
    private int[] currentTile;

    /**
     * Creates CameraOrbitController that controls a camera and orbits it around
     * a specified GameObject, its rotation independent from the GameObject
     */
    public CameraOrbitController(Camera cam, MyGame g, String gamepadName, Engine e) {
        engine = e;
        camera = cam;
        game = g;
        avatar = game.getAvatar();
        // avatar = av;
        cameraAzimuth = 0.0f;
        cameraElevation = 20.0f;
        cameraRadius = 2.0f;
        speed = 2f;
        setupInputs(gamepadName);
        updateCameraPosition();
    }

    /**
     * Sets up keyboard and gamepad inputs for the camera controller
     */
    private void setupInputs(String gamepadName) {
        InputManager im = engine.getInputManager();

        //setup keyboard
        OrbitAzimuthAction azmActionF = new OrbitAzimuthAction();
        azmActionF.setUp(false, true);
        OrbitAzimuthAction azmActionB = new OrbitAzimuthAction();
        azmActionB.setUp(false, false);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.RIGHT, azmActionF, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.LEFT, azmActionB, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        OrbitElevationAction eleActionF = new OrbitElevationAction();
        eleActionF.setUp(false, true);
        OrbitElevationAction eleActionB = new OrbitElevationAction();
        eleActionB.setUp(false, false);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.UP, eleActionF, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.DOWN, eleActionB, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        OrbitRadiusAction radActionF = new OrbitRadiusAction();
        radActionF.setUp(false, true);
        OrbitRadiusAction radActionB = new OrbitRadiusAction();
        radActionB.setUp(false, false);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.Q, radActionF, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.E, radActionB, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //set up gamepad
        try {
            OrbitAzimuthAction azmActionPad = new OrbitAzimuthAction();
            azmActionPad.setUp(true, true);
            im.associateAction(gamepadName, net.java.games.input.Component.Identifier.Axis.RX, azmActionPad, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

            OrbitElevationAction eleActionPad = new OrbitElevationAction();
            eleActionPad.setUp(true, true);
            im.associateAction(gamepadName, net.java.games.input.Component.Identifier.Axis.RY, eleActionPad, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

            OrbitRadiusAction radActionPadF = new OrbitRadiusAction();
            radActionPadF.setUp(false, true);
            im.associateAction(gamepadName, net.java.games.input.Component.Identifier.Button._6, radActionPadF, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

            OrbitRadiusAction radActionPadB = new OrbitRadiusAction();
            radActionPadB.setUp(false, false);
            im.associateAction(gamepadName, net.java.games.input.Component.Identifier.Button._7, radActionPadB, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        } catch (Exception e) {
            // System.err.println("cam orbit con err");
        }

    }

    /**
     * Updates Camera Position based on current azimuth, elevation, and radius
     * values. Also limits camera elevation and radius.
     */
    public void updateCameraPosition() {
        currentTile = game.getCursorManager().getCursorPos();

        Vector3f tileLoc = game.getGrid()[currentTile[0]][currentTile[1]].getPosition();

//code to match relative position to dolphin rotation------------
        // Vector3f avatarRot = avatar.getWorldForwardVector();
        // double avatarAngle = Math.toDegrees((double) avatarRot.angleSigned(new Vector3f(0, 0, -1), new Vector3f(0, 1, 0)));
        // float totalAz = cameraAzimuth - (float) avatarAngle;
//------------------cleaning camera azimuth, elevation, and radius---------------------------
        cameraAzimuth = cameraAzimuth % 360; //azimuth loops

        // cameraElevation %= 90;
        if (cameraElevation >= 70) { //camera elevation locked between -70 to 70
            cameraElevation = 70;
        } else if (cameraElevation <= -70) {
            cameraElevation = -70;
        }

        if (cameraRadius >= 30) { //camera radius locked between 2 to 30
            cameraRadius = 30;
        } else if (cameraRadius <= 2) {
            cameraRadius = 2;
        }

        float totalAz = cameraAzimuth;
        double theta = Math.toRadians(totalAz);
        double phi = Math.toRadians(cameraElevation);
        float x = cameraRadius * (float) (Math.cos(phi) * Math.sin(theta));
        float y = cameraRadius * (float) (Math.sin(phi));
        float z = cameraRadius * (float) (Math.cos(phi) * Math.cos(theta));

        // Vector3f newLoc = new Vector3f(x, y, z).add(avatar.getWorldLocation());
        Vector3f newLoc = new Vector3f(x, y, z).add(tileLoc);

        if (newLoc.y() < 1) {
            newLoc = new Vector3f(newLoc.x, 1, newLoc.z);
            // camera.lookAt(avatar);
            camera.lookAt(tileLoc);
            // return;
        }

        camera.setLocation(newLoc);
        // camera.lookAt(avatar);
        camera.lookAt(tileLoc);
    }

    private class OrbitAzimuthAction extends AbstractInputAction {

        private Boolean isController = false;
        private Boolean isForward = false;

        /**
         * Sets isController and isForward to specified values to enable
         * reuability for both digital and analog inputs
         */
        public void setUp(Boolean c, Boolean f) {
            isController = c;
            isForward = f;
        }

        public void performAction(float time, Event event) {
            float rotAmount = 0f;
            if (isController) {
                if (event.getValue() < -0.2f) {
                    rotAmount = -speed;
                } else {
                    if (event.getValue() > 0.2f) {
                        rotAmount = speed;
                    } else {
                        rotAmount = 0.0f;
                    }
                }
            } else if (isForward) {
                rotAmount = speed;
            } else {
                rotAmount = -speed;
            }

            // System.out.println(event.getValue());
            cameraAzimuth += rotAmount;
            // updateCameraPosition();

        }

    }

    private class OrbitElevationAction extends AbstractInputAction {

        private Boolean isController = false;
        private Boolean isForward = false;

        /**
         * Sets isController and isForward to specified values to enable
         * reuability for both digital and analog inputs
         */
        public void setUp(Boolean c, Boolean f) {
            isController = c;
            isForward = f;
        }

        public void performAction(float time, Event event) {
            float rotAmount = 0f;
            // System.out.println(event.getValue());
            if (isController) {
                if (event.getValue() < -0.2f) {
                    rotAmount = -speed;
                } else {
                    if (event.getValue() > 0.2f) {
                        rotAmount = speed;
                        // rotAmount = -speed;
                    } else {
                        rotAmount = 0.0f;
                    }
                }
            } else if (isForward) {
                rotAmount = speed;
                // rotAmount = -speed;
            } else {
                rotAmount = -speed;
                // rotAmount = speed;
            }

            cameraElevation += rotAmount;
            // updateCameraPosition();
        }
    }

    private class OrbitRadiusAction extends AbstractInputAction {

        private Boolean isController = false;
        private Boolean isForward = false;

        /**
         * Sets isController and isForward to specified values to enable
         * reuability for both digital and analog inputs
         */
        public void setUp(Boolean c, Boolean f) {
            isController = c;
            isForward = f;
        }

        public void performAction(float time, Event event) {
            float distAmount = 0f;
            if (isController) {
                if (event.getValue() < -0.2f) {
                    distAmount = -speed;
                } else {
                    if (event.getValue() > 0.2f) {
                        distAmount = speed;
                    } else {
                        distAmount = 0.0f;
                    }
                }
            } else if (isForward) {
                distAmount = speed;
            } else {
                distAmount = -speed;
            }

            cameraRadius += distAmount;
            // cameraRadius = cameraRadius % 30;
            // updateCameraPosition();
        }
    }

}
