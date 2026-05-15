package a3;

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

public class OverheadCameraController {

    private Engine engine;
    private Camera camera;
    private GameObject avatar;
    private float cameraElevation;
    private float cameraX;
    private float cameraZ;
    private float speed;

    public OverheadCameraController(Camera cam, String gamepadName, Engine e) {
        engine = e;
        camera = cam;
        cameraElevation = 70.0f;
        cameraX = 0.0f;
        cameraZ = 0.0f;
        speed = 1f;
        // setupInputs(gamepadName);
        // updateCameraPosition();
    }

    private void setupInputs(String gamepadName) {
        InputManager im = engine.getInputManager();

        //setup keyboard
        CameraVertAction vertActionF = new CameraVertAction();
        vertActionF.setUp(false, true);
        CameraVertAction vertActionB = new CameraVertAction();
        vertActionB.setUp(false, false);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.I, vertActionF, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.K, vertActionB, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        CameraHoriAction horiActionF = new CameraHoriAction();
        horiActionF.setUp(false, true);
        CameraHoriAction horiActionB = new CameraHoriAction();
        horiActionB.setUp(false, false);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.L, horiActionF, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.J, horiActionB, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        CameraElevationAction eleActionF = new CameraElevationAction();
        eleActionF.setUp(false, true);
        CameraElevationAction eleActionB = new CameraElevationAction();
        eleActionB.setUp(false, false);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.O, eleActionF, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.U, eleActionB, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //set up gamepad
        try {
            CameraPadAction padAction = new CameraPadAction();
            im.associateAction(gamepadName, net.java.games.input.Component.Identifier.Axis.POV, padAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

            CameraElevationAction eleActionPadF = new CameraElevationAction();
            eleActionPadF.setUp(false, true);
            CameraElevationAction eleActionPadB = new CameraElevationAction();
            eleActionPadB.setUp(false, false);
            im.associateAction(gamepadName, net.java.games.input.Component.Identifier.Button._4, eleActionPadF, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
            im.associateAction(gamepadName, net.java.games.input.Component.Identifier.Button._5, eleActionPadB, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        } catch (Exception e) {
            // System.out.println("dummy");
        }

    }

    private void pointCameraDown() {
        // camera.setLocation(new Vector3f(0f, 20f, 0f));
        camera.setU(new Vector3f(1f, 0f, 0f));
        camera.setV(new Vector3f(0f, 0f, -1f));
        camera.setN(new Vector3f(0f, -1f, 0f));
    }

    public void updateCameraPosition() {
        pointCameraDown();
        if (cameraElevation >= 100) { //camera elevation locked between 10 to 100
            cameraElevation = 100;
        } else if (cameraElevation <= 10) {
            cameraElevation = 10;
        }
        Vector3f newLoc = new Vector3f(cameraX, cameraElevation, cameraZ);
        // System.out.println(newLoc);

        camera.setLocation(newLoc);
    }

    private class CameraVertAction extends AbstractInputAction {

        private Boolean isController = false;
        private Boolean isForward = false;

        public void setUp(Boolean c, Boolean f) {
            isController = c;
            isForward = f;
        }

        public void performAction(float time, Event event) {
            float movAmount = 0f;
            // System.out.print(event.getValue());
            if (isController) {
                // if (event.getValue() < -speed) {
                //     movAmount = speed;
                // } else {
                //     if (event.getValue() > speed) {
                //         movAmount = -speed;
                //     } else {
                //         movAmount = 0.0f;
                //     }
                // }
                if (event.getValue() == 0.25f) {
                    movAmount = -speed;
                } else if (event.getValue() == 0.75f) {
                    movAmount = speed;
                }

            } else if (isForward) {
                movAmount = -speed;
            } else {
                movAmount = speed;
            }

            // System.out.println(event.getValue());
            cameraZ += movAmount;
            // updateCameraPosition();

        }

    }

    private class CameraElevationAction extends AbstractInputAction {

        private Boolean isController = false;
        private Boolean isForward = false;

        public void setUp(Boolean c, Boolean f) {
            isController = c;
            isForward = f;
        }

        public void performAction(float time, Event event) {
            float movAmount = 0f;
            // System.out.println(event.getValue());

            if (isController) {
                if (event.getValue() < -speed) {
                    movAmount = -speed;
                } else {
                    if (event.getValue() > speed) {
                        movAmount = speed;
                        // movAmount = -speed;
                    } else {
                        movAmount = 0.0f;
                    }
                }
            } else if (isForward) {
                movAmount = speed;
                // movAmount = -speed;
            } else {
                movAmount = -speed;
                // movAmount = speed;
            }

            cameraElevation += movAmount;
            // updateCameraPosition();
        }
    }

    private class CameraHoriAction extends AbstractInputAction {

        private Boolean isController = false;
        private Boolean isForward = false;

        public void setUp(Boolean c, Boolean f) {
            isController = c;
            isForward = f;
        }

        public void performAction(float time, Event event) {
            float movAmount = 0f;
            if (isController) {
                // if (event.getValue() < -speed) {
                //     movAmount = -speed;
                // } else {
                //     if (event.getValue() > speed) {
                //         movAmount = speed;
                //     } else {
                //         movAmount = 0.0f;
                //     }
                // }
                if (event.getValue() == 0.01f) {
                    movAmount = -speed;
                } else if (event.getValue() == 0.5f) {
                    movAmount = speed;
                }
            } else if (isForward) {
                movAmount = speed;
            } else {
                movAmount = -speed;
            }

            cameraX += movAmount;
            // cameraRadius = cameraRadius % 30;
            // updateCameraPosition();
        }
    }

    private class CameraPadAction extends AbstractInputAction {

        public void performAction(float time, Event event) {

            float movAmountX = 0f;
            float movAmountZ = 0f;
            if (event.getValue() == 1f) {
                movAmountX = -speed;
            } else if (event.getValue() == 0.5f) {
                movAmountX = speed;
            } else if (event.getValue() == 0.25f) {
                movAmountZ = -speed;
            } else if (event.getValue() == 0.75f) {
                movAmountZ = speed;
            }

            // System.out.println(event.getValue());
            cameraZ += movAmountZ;
            cameraX += movAmountX;
            // updateCameraPosition();

        }

    }

}
