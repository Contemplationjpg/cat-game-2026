package a3;

import org.joml.*;

import tage.*;
import tage.shapes.*;

import tage.input.*;
import tage.input.action.*;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;
// import tage.rml.Matrix4f;
import org.joml.Matrix4f;
import tage.rml.Vector2f;
import tage.rml.Vector3;
// import tage.rml.Vector3f;
import org.joml.Vector3f;
import org.joml.Vector4f;
// import tage.rml.Vector4f;

import tage.shapes.ImportedModel;
import tage.shapes.Sphere;

import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import java.util.Arrays;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;
import tage.nodeControllers.RotationController;
import tage.nodeControllers.ScaleController;

public class MyGame extends VariableFrameRateGame {

    private static Engine engine;

    private boolean paused = false;
    private int counter = 0;
    private double lastFrameTime, currFrameTime, deltaTime;

    private CameraOrbitController orbitController;
    private OverheadCameraController overheadController;

    private GameObject dol;
    private ObjShape dolS;
    private TextureImage doltx;

    private GameObject plan1, plan2, plan3;
    private ObjShape planS1, planS2, planS3;
    private TextureImage brick;
    private TextureImage gas;
    private TextureImage purplebrick;

    private GameObject x, y, z;
    private ObjShape linxS, linyS, linzS;

    private GameObject house;
    private ObjShape houseS;
    private TextureImage bluebrick;

    private GameObject pho1, pho2, pho3;
    private ObjShape phoS1, phoS2, phoS3;

    private GameObject homepho1, homepho2, homepho3;
    private ObjShape homephoS1, homephoS2, homephoS3;

    private GameObject plane;
    private ObjShape planeS;

    private Light lightP1, lightP2, lightP3, lightH;

    private InputManager im;

    private float[] turnDirY; //Y axis turn is left and right
    private final float turnSpeedY = 0.08f;

    private float[] turnDirX; //X axis turn is up and down
    private final float turnSpeedX = 0.08f;

    private Vector3f loc, fwd, up, right, newLocation;
    private Camera cam, overheadCam;
    private Viewport camVp, overheadVp;

    private NodeController sc1, sc2, sc3, rc1, rc2, rc3;
    String dispStr1 = "";
    String dispStr2 = "";

    private float[] planSize = {
        1.5f,
        2f,
        3f
    };

    private final Vector3f[] planetPos = {
        new Vector3f(0, 0, -10),
        new Vector3f(-8, 0, 20),
        new Vector3f(12, 0, -5),};

    private Boolean[] unlockedPhotos = {
        false, false, false
    };

    private Boolean[] placedPhotos = {
        false, false, false
    };

    private final Vector3f homePos = new Vector3f(0, 0, 5);

    private boolean gameWon;
    private boolean gameLost;

    public MyGame() {
        super();
    }

    public static void main(String[] args) {
        MyGame game = new MyGame();
        engine = new Engine(game);
        engine.initializeSystem();
        game.buildGame();
        game.startGame();
        // FindComponents f = new FindComponents();
        // f.listControllers();
    }

    @Override
    public void createViewports() {
        (engine.getRenderSystem()).addViewport("MAIN", 0, 0, 1f, 1f);
        (engine.getRenderSystem()).addViewport("OVERHEAD", 0.65f, 0.0f, 0.35f, 0.35f);

        camVp = (engine.getRenderSystem()).getViewport("MAIN");
        overheadVp = (engine.getRenderSystem()).getViewport("OVERHEAD");
        cam = (engine.getRenderSystem().getViewport("MAIN").getCamera());
        overheadCam = (engine.getRenderSystem().getViewport("OVERHEAD").getCamera());

        overheadVp.setHasBorder(true);
        overheadVp.setBorderWidth(3);
        overheadVp.setBorderColor(0f, 1f, 0f);

        overheadCam.setLocation(new Vector3f(0, 15, 0));
        overheadCam.setU(new Vector3f(1, 0, 0));
        overheadCam.setV(new Vector3f(0, 0, -1));
        overheadCam.setN(new Vector3f(0, -1, 0));

    }

    @Override
    public void loadShapes() {
        // dolS = new ImportedModel("dolphinHighPoly.obj");
        dolS = new ImportedModel("rat.obj");

        // planS1 = new Sphere();
        // planS2 = new Sphere();
        // planS3 = new Sphere();
        planS1 = new ManualPyramid();
        planS2 = new ManualPyramid();
        planS3 = new ManualPyramid();

        linxS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(50f, 0f, 0f));
        linyS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 50f, 0f));
        linzS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, -50f));

        houseS = new ManualHouse();

        phoS1 = new Plane();
        phoS2 = new Plane();
        phoS3 = new Plane();

        planeS = new Plane();
    }

    @Override
    public void loadTextures() {
        // doltx = new TextureImage("Dolphin_HighPolyUV.jpg");
        doltx = new TextureImage("rat.png");
        brick = new TextureImage("brick1.jpg");
        gas = new TextureImage("gaseous.jpg");
        bluebrick = new TextureImage("bluebrick.jpg");
        purplebrick = new TextureImage("purplebrick.jpg");
    }

    @Override
    public void buildObjects() {
        Matrix4f initialTranslation, initialScale;

        // build dolphin in the center of the window
        dol = new GameObject(GameObject.root(), dolS, doltx);
        initialTranslation = (new Matrix4f()).translation(0, 2, 0);
        initialScale = (new Matrix4f()).scaling(3.0f);
        dol.setLocalTranslation(initialTranslation);
        dol.setLocalScale(initialScale);

        plan1 = new GameObject(GameObject.root(), planS1, purplebrick);
        plan2 = new GameObject(GameObject.root(), planS2, bluebrick);
        plan3 = new GameObject(GameObject.root(), planS3, brick);
        Matrix4f initTransPlan1, initScalePlan1;
        initTransPlan1 = (new Matrix4f()).translation(planetPos[0].x, planetPos[0].y + planSize[0] + 0.05f, planetPos[0].z);
        initScalePlan1 = (new Matrix4f()).scale(planSize[0]);
        plan1.setLocalTranslation(initTransPlan1);
        plan1.setLocalScale(initScalePlan1);
        Matrix4f initTransPlan2, initScalePlan2;
        initTransPlan2 = (new Matrix4f()).translation(planetPos[1].x, planetPos[1].y + planSize[1] + 0.05f, planetPos[1].z);
        initScalePlan2 = (new Matrix4f()).scale(planSize[1]);
        plan2.setLocalTranslation(initTransPlan2);
        plan2.setLocalScale(initScalePlan2);
        Matrix4f initTransPlan3, initScalePlan3;
        initTransPlan3 = (new Matrix4f()).translation(planetPos[2].x, planetPos[2].y + planSize[2] + 0.05f, planetPos[2].z);
        initScalePlan3 = (new Matrix4f()).scale(planSize[2]);
        plan3.setLocalTranslation(initTransPlan3);
        plan3.setLocalScale(initScalePlan3);

        x = new GameObject(GameObject.root(), linxS);
        y = new GameObject(GameObject.root(), linyS);
        z = new GameObject(GameObject.root(), linzS);
        (x.getRenderStates()).setColor(new Vector3f(1f, 0f, 0f));
        (y.getRenderStates()).setColor(new Vector3f(0f, 1f, 0f));
        (z.getRenderStates()).setColor(new Vector3f(0f, 0f, 1f));
        (x.getRenderStates()).disableRendering();
        (y.getRenderStates()).disableRendering();
        (z.getRenderStates()).disableRendering();

        house = new GameObject(GameObject.root(), houseS, brick);
        house.setLocalTranslation((new Matrix4f()).translation(0, 0, 5));
        house.getRenderStates().hasLighting(true);
        Matrix4f initScaleHome = (new Matrix4f()).scale(2.0f);
        house.setLocalScale(initScaleHome);

        pho1 = new GameObject(dol, phoS1, purplebrick);
        pho1.applyParentRotationToPosition(true);
        (pho1.getRenderStates()).isTransparent(true);
        (pho1.getRenderStates()).setOpacity(0f);
        Matrix4f initTransPho1 = (new Matrix4f()).translation(0.5f, 500f, 0.5f);
        Matrix4f initScalePho1 = (new Matrix4f()).scale(0.25f);
        pho1.setLocalTranslation(initTransPho1);
        pho1.setLocalScale(initScalePho1);
        pho1.pitch(0.5f * (float) Math.PI);
        // pho1.yaw(180);

        pho2 = new GameObject(dol, phoS2, bluebrick);
        pho2.applyParentRotationToPosition(true);
        (pho2.getRenderStates()).isTransparent(true);
        (pho2.getRenderStates()).setOpacity(0f);
        Matrix4f initTransPho2 = (new Matrix4f()).translation(0.5f, 500f, 1.5f);
        Matrix4f initScalePho2 = (new Matrix4f()).scale(0.25f);
        pho2.setLocalTranslation(initTransPho2);
        pho2.setLocalScale(initScalePho2);
        pho2.pitch(0.5f * (float) Math.PI);
        // pho2.yaw(180);

        pho3 = new GameObject(dol, phoS3, brick);
        pho3.applyParentRotationToPosition(true);
        (pho3.getRenderStates()).isTransparent(true);
        (pho3.getRenderStates()).setOpacity(0f);
        Matrix4f initTransPho3 = (new Matrix4f()).translation(0.5f, 500f, 2.5f);
        Matrix4f initScalePho3 = (new Matrix4f()).scale(0.25f);
        pho3.setLocalTranslation(initTransPho3);
        pho3.setLocalScale(initScalePho3);
        pho3.pitch(0.5f * (float) Math.PI);
        // pho3.yaw(180);

        // pho2 = new GameObject(dol, phoS2, brick);
        // (pho2.getRenderStates()).isTransparent(true);
        // pho3 = new GameObject(dol, phoS3, brick);
        // (pho3.getRenderStates()).isTransparent(true);
        homepho1 = new GameObject(house, phoS1, purplebrick);
        homepho1.applyParentRotationToPosition(true);
        homepho1.applyParentScaleToPosition(false);
        // (homepho1.getRenderStates()).isTransparent(true);
        // (homepho1.getRenderStates()).setOpacity(0f);
        (homepho1.getRenderStates()).disableRendering();
        Matrix4f initTranshomepho1 = (new Matrix4f()).translation(0.5f, 2f, 3f);
        Matrix4f initScalehomepho1 = (new Matrix4f()).scale(0.5f);
        homepho1.setLocalTranslation(initTranshomepho1);
        // homepho1.setLocalScale(initScalehomepho1);
        homepho1.pitch(0.5f * (float) Math.PI);

        homepho2 = new GameObject(house, phoS2, bluebrick);
        homepho2.applyParentRotationToPosition(true);
        homepho2.applyParentScaleToPosition(false);
        // (homepho2.getRenderStates()).isTransparent(true);
        // (homepho2.getRenderStates()).setOpacity(0f);
        (homepho2.getRenderStates()).disableRendering();
        Matrix4f initTranshomepho2 = (new Matrix4f()).translation(3.5f, 2f, 3f);
        Matrix4f initScalehomepho2 = (new Matrix4f()).scale(0.5f);
        homepho2.setLocalTranslation(initTranshomepho2);
        // homepho2.setLocalScale(initScalehomepho2);
        homepho2.pitch(0.5f * (float) Math.PI);

        homepho3 = new GameObject(house, phoS3, brick);
        homepho3.applyParentRotationToPosition(true);
        homepho3.applyParentScaleToPosition(false);
        // (homepho3.getRenderStates()).isTransparent(true);
        // (homepho3.getRenderStates()).setOpacity(0f);
        (homepho3.getRenderStates()).disableRendering();
        Matrix4f initTranshomepho3 = (new Matrix4f()).translation(6.5f, 2f, 3f);
        Matrix4f initScalehomepho3 = (new Matrix4f()).scale(0.5f);
        homepho3.setLocalTranslation(initTranshomepho3);
        // homepho3.setLocalScale(initScalehomepho3);
        homepho3.pitch(0.5f * (float) Math.PI);

        plane = new GameObject(GameObject.root(), planeS, gas);
        Matrix4f initScalePlane = (new Matrix4f()).scale(50f);
        plane.setLocalScale(initScalePlane);

    }

    @Override
    public void initializeLights() {
        Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);
        lightP1 = new Light();
        lightP1.setLocation(new Vector3f(0f, 10.0f, -10.0f));
        (engine.getSceneGraph()).addLight(lightP1);
        lightP2 = new Light();
        lightP2.setLocation(new Vector3f(-8f, 10f, 20f));
        (engine.getSceneGraph()).addLight(lightP2);
        lightP3 = new Light();
        lightP3.setLocation(new Vector3f(12f, 10f, -5.0f));
        (engine.getSceneGraph()).addLight(lightP3);
        lightH = new Light();
        lightH.setLocation(new Vector3f(0f, 10f, 5.0f));
        (engine.getSceneGraph()).addLight(lightH);
    }

    @Override
    public void initializeGame() {
        im = engine.getInputManager();

        lastFrameTime = System.currentTimeMillis();
        currFrameTime = System.currentTimeMillis();
        (engine.getRenderSystem()).setWindowDimensions(1900, 1000);

        // ------------- positioning the camera -------------
        String gamepadName = (im.getFirstGamepadName());

        orbitController = new CameraOrbitController(cam, dol, gamepadName, engine);
        overheadController = new OverheadCameraController(overheadCam, gamepadName, engine);

        turnDirY = new float[2];
        turnDirX = new float[2];
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setLocation(new Vector3f(0f, 0f, 5f));
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setU(new Vector3f(1f, 0f, 0f));
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setV(new Vector3f(0f, 1f, 0f));
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setN(new Vector3f(0f, 0f, -1f));

        // ---------------- input section ------------------
        PhotoAction photoAction = new PhotoAction(this);

        XYZAction xyzAction = new XYZAction(this);

        MoveAction moveActionPad = new MoveAction(this, true);
        MoveAction moveActionKeyF = new MoveAction(this, false, true);
        MoveAction moveActionKeyB = new MoveAction(this, false, false);

        TurnAction turnActionPad = new TurnAction(this, true);
        TurnAction turnActionKeyR = new TurnAction(this, false, true);
        TurnAction turnActionKeyL = new TurnAction(this, false, false);

        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._2, photoAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.SPACE, photoAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._9, xyzAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key._2, xyzAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Y, moveActionPad, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.W, moveActionKeyF, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.S, moveActionKeyB, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.X, turnActionPad, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.D, turnActionKeyR, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.A, turnActionKeyL, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //-----------------------------
        sc1 = new ScaleController(engine, 2.0f);
        sc1.addTarget(pho1);
        (engine.getSceneGraph()).addNodeController(sc1);
        sc1.toggle();
        sc2 = new ScaleController(engine, 2.0f);
        sc2.addTarget(pho2);
        (engine.getSceneGraph()).addNodeController(sc2);
        sc2.toggle();
        sc3 = new ScaleController(engine, 2.0f);
        sc3.addTarget(pho3);
        (engine.getSceneGraph()).addNodeController(sc3);
        sc3.toggle();

        rc1 = new RotationController(engine, new Vector3f(0, 1, 0), 0.001f);
        rc1.addTarget(plan1);
        (engine.getSceneGraph()).addNodeController(rc1);
        // rc1.toggle();

        rc2 = new RotationController(engine, new Vector3f(0, 1, 0), 0.001f);
        rc2.addTarget(plan2);
        (engine.getSceneGraph()).addNodeController(rc2);
        // rc2.toggle();

        rc3 = new RotationController(engine, new Vector3f(0, 1, 0), 0.001f);
        rc3.addTarget(plan3);
        (engine.getSceneGraph()).addNodeController(rc3);
        // rc3.toggle();

    }

    @Override
    public void update() {

        //---------------update time----------
        lastFrameTime = currFrameTime;
        currFrameTime = System.currentTimeMillis();
        if (!paused) {
            deltaTime = (currFrameTime - lastFrameTime) / 1000;
        } else {
            deltaTime = 0.0f;
        }

        // System.out.println(deltaTime);
        //-----------------update inputs-------------
        im.update((float) deltaTime);

        //-----------camera lock on dolphin------------
        // loc = dol.getWorldLocation();
        // fwd = dol.getWorldForwardVector();
        // up = dol.getWorldUpVector();
        // right = dol.getWorldRightVector();
        // cam.setU(right);
        // cam.setV(up);
        // cam.setN(fwd);
        // cam.setLocation(loc.add(up.mul(1.3f)).add(fwd.mul(-2.5f)));
        // newLocation = cam.getLocation();
        orbitController.updateCameraPosition();
        overheadController.updateCameraPosition();
        // System.out.println(cam.getLocation());

        //---------------------HUD-------------------------
        // build and set HUD
        // int elapsTimeSec = Math.round((float) elapsTime);
        // String elapsTimeStr = Integer.toString(elapsTimeSec);
        // String counterStr = Integer.toString(counter);
        dispStr1 = "Photos Taken: " + photoCount() + "/3\t" + "Photos Placed: " + placedCount() + "/3";
        String dispStr3 = "";
        if (winCheck()) {
            dispStr3 = "You Win!";
            paused = false;
        } else if (gameLost) {
            dispStr3 = "You Lose!";
            paused = true;
        }

        Vector3f dolPos = dol.getWorldLocation();
        dispStr2 = "X: " + String.format("%.2f", dolPos.x()) + " Y: " + String.format("%.2f", dolPos.y()) + " Z: " + String.format("%.2f", dolPos.z());

        Vector3f hud1Color = new Vector3f(1, 0, 0);
        Vector3f hud2Color = new Vector3f(1, 1, 1);
        (engine.getHUDmanager()).setHUD1(dispStr1, hud1Color, 15, 15);
        int screenBoundsX = (engine.getRenderSystem()).getBounds().width;
        int screenBoundsY = (engine.getRenderSystem()).getBounds().height;

        double HUD2X = (screenBoundsX / 10) * 6.5;
        // double HUD2Y = (screenBoundsY/10)*0.2;

        (engine.getHUDmanager()).setHUD2(dispStr2, hud2Color, (int) HUD2X, 15);

        double HUD3X = (screenBoundsX / 2);
        double HUD3Y = (screenBoundsY / 2);
        (engine.getHUDmanager()).setHUD3(dispStr3, hud2Color, (int) HUD3X, (int) HUD3Y);

        detectCollision();

    }

    private int photoCount() {
        int count = 0;
        for (int i = 0; i < unlockedPhotos.length; i++) {
            if (unlockedPhotos[i]) {
                count++;
            }
        }
        return count;
    }

    private int placedCount() {
        int count = 0;
        for (int i = 0; i < placedPhotos.length; i++) {
            if (placedPhotos[i]) {
                count++;
            }
        }
        return count;
    }

    public GameObject getAvatar() {
        return dol;
    }

    public Camera getCamera() {
        return (engine.getRenderSystem().getViewport("MAIN").getCamera());
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public Boolean detectPlanet() {
        Vector3f dolPos = dol.getWorldLocation();
        Boolean hasDetected = false;
        for (int i = 0; i < planetPos.length; i++) {
            if (detectDistance(dolPos, planetPos[i]) < planSize[i] + 5) {
                unlockedPhotos[i] = true;
                hasDetected = true;
                // System.out.println("near " + i);
            }
        }
        return hasDetected;

    }

    private void detectCollision() {
        Vector3f dolPos = dol.getWorldLocation();
        for (int i = 0; i < planetPos.length; i++) {
            if (detectDistance(dolPos, new Vector3f(planetPos[i].x, planetPos[i].y + planSize[i], planetPos[i].z)) < planSize[i]) {
                gameLost = true;
                // System.out.println("close to " + i);
            }
        }
    }

    public double detectDistance(org.joml.Vector3f a, org.joml.Vector3f b) {
        try {
            double x = a.x() - b.x();
            x = Math.pow(x, 2);
            double y = a.y() - b.y();
            y = Math.pow(y, 2);
            double z = a.z() - b.z();
            z = Math.pow(z, 2);
            double out = x + y + z;
            out = Math.sqrt(out);
            return out;
        } catch (Exception e) {
            return -1;
        }
    }

    public Boolean detectHome() {
        Vector3f dolPos = dol.getWorldLocation();
        if (detectDistance(dolPos, homePos) < 8) {
            return true;
        }
        return false;
    }

    public boolean detectDolphin() {
        Vector3f dolPos = dol.getWorldLocation();
        Vector3f cameraPos = cam.getLocation();
        if (detectDistance(dolPos, cameraPos) < 2) {
            return true;
        }
        return false;
    }

    public void placePhotos() {
        if (unlockedPhotos[0] && !placedPhotos[0]) {
            placedPhotos[0] = true;
            (homepho1.getRenderStates()).setOpacity(1f);
            (homepho1.getRenderStates()).enableRendering();
            // System.out.println("placed photo 1");
        }
        if (unlockedPhotos[1] && !placedPhotos[1]) {
            placedPhotos[1] = true;
            (homepho2.getRenderStates()).enableRendering();
            // System.out.println("placed photo 2");
        }
        if (unlockedPhotos[2] && !placedPhotos[2]) {
            placedPhotos[2] = true;
            (homepho3.getRenderStates()).enableRendering();
            // System.out.println("placed photo 3");
        }

        photoCheck();

    }

    private void photoCheck() {

        if (unlockedPhotos[0] && !placedPhotos[0]) {
            // System.out.println("got photo 1");
            (pho1.getRenderStates()).setOpacity(1f);
            Matrix4f transPho1 = (new Matrix4f()).translation(0.5f, 0.5f, 0.0f);
            pho1.setLocalTranslation(transPho1);
            rc1.enable();

        } else {
            // System.out.println("no photo 1");
            (pho1.getRenderStates()).setOpacity(0f);
            Matrix4f transPho1 = (new Matrix4f()).translation(0.5f, 500f, 0.5f);
            pho1.setLocalTranslation(transPho1);
        }
        if (unlockedPhotos[1] && !placedPhotos[1]) {
            // System.out.println("got photo 2");
            (pho2.getRenderStates()).setOpacity(1f);
            Matrix4f transPho2 = (new Matrix4f()).translation(0.5f, 0.5f, 1f);
            pho2.setLocalTranslation(transPho2);
            rc2.enable();
        } else {
            // System.out.println("no photo 2");
            (pho2.getRenderStates()).setOpacity(0f);
            Matrix4f transPho2 = (new Matrix4f()).translation(0.5f, 500f, 1.5f);
            pho2.setLocalTranslation(transPho2);
        }
        if (unlockedPhotos[2] && !placedPhotos[2]) {
            // System.out.println("got photo 3");
            (pho3.getRenderStates()).setOpacity(1f);
            Matrix4f transPho3 = (new Matrix4f()).translation(0.5f, 0.5f, 2f);
            pho3.setLocalTranslation(transPho3);
            rc3.enable();
        } else {
            // System.out.println("no photo 3");
            (pho3.getRenderStates()).setOpacity(0f);
            Matrix4f transPho3 = (new Matrix4f()).translation(0.5f, 500f, 4.5f);
            pho3.setLocalTranslation(transPho3);
        }
    }

    public void takePhoto() {
        detectPlanet();
        photoCheck();

        // System.out.println("unlocked photos: " + Arrays.toString(unlockedPhotos));
        // System.out.println("placed photos: " + Arrays.toString(placedPhotos));
    }

    private Boolean winCheck() {
        if (placedPhotos[0] && placedPhotos[1] && placedPhotos[2]) {
            gameWon = true;
            return true;
        }
        return false;
    }

    public void hideXYZ() {
        // System.out.println("toggling XYZ");
        if (x.getRenderStates().renderingEnabled()) {
            (x.getRenderStates()).disableRendering();
            (y.getRenderStates()).disableRendering();
            (z.getRenderStates()).disableRendering();
        } else {
            (x.getRenderStates()).enableRendering();
            (y.getRenderStates()).enableRendering();
            (z.getRenderStates()).enableRendering();
        }

    }

}
