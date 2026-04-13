package a3;

import org.joml.*;

import tage.*;
import tage.shapes.*;

import tage.input.*;
import tage.input.action.*;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;
import org.joml.Matrix4f;
import tage.rml.Vector2f;
import tage.rml.Vector3;
import org.joml.Vector3f;
import org.joml.Vector4f;

import tage.shapes.ImportedModel;

import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import java.util.Arrays;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;
import tage.GhostManager;
import tage.networking.IGameConnection.ProtocolType;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyGame extends VariableFrameRateGame {

    private static Engine engine;

    private double lastFrameTime, currFrameTime, deltaTime;

    private GameObject test;
    private ObjShape testS;
    private TextureImage testT;

    private GameObject ghost;
    private ObjShape ghostS;
    private TextureImage ghostT;

    private Light lightP1;

    private InputManager im;

    private Camera cam;
    private Viewport camVp;

    String dispStr1 = "";
    String dispStr2 = "";

    private GhostManager gm;
    private String serverAddress = "0";
    private int serverPort = 0;
    private ProtocolType serverProtocol = ProtocolType.UDP;
    private ProtocolClient protClient;
    private boolean isClientConnected = false;

    public MyGame(String serverAddress, int serverPort, String protocol) {
        super();
        gm = new GhostManager(this);
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        if (protocol.toUpperCase().compareTo("TCP") == 0) {
            this.serverProtocol = ProtocolType.TCP;
        } else {
            this.serverProtocol = ProtocolType.UDP;
        }
    }

    public MyGame() {
        super();
    }

    public static void main(String[] args) {
        MyGame game = new MyGame();
        // MyGame game = new MyGame(args[0], Integer.parseInt(args[1]), args[2]);
        engine = new Engine(game);
        engine.initializeSystem();
        game.buildGame();
        game.startGame();
        // FindComponents f = new FindComponents();
        // f.listControllers();
    }

    // @Override
    // public void createViewports() {
    //     (engine.getRenderSystem()).addViewport("MAIN", 0, 0, 1f, 1f);
    //     camVp = (engine.getRenderSystem()).getViewport("MAIN");
    //     cam = (engine.getRenderSystem().getViewport("MAIN").getCamera());
    // }
    @Override
    public void loadShapes() {
        testS = new ImportedModel("rat.obj");
        ghostS = new ImportedModel("rat.obj");

    }

    @Override
    public void loadTextures() {
        testT = new TextureImage("rat.png");
        ghostT = new TextureImage("rat.png");
    }

    @Override
    public void buildObjects() {
        Matrix4f initialTranslation, initialScale;
        // build test in the center of the window
        test = new GameObject(GameObject.root(), testS, testT);
        initialTranslation = (new Matrix4f()).translation(0, 2, 0);
        initialScale = (new Matrix4f()).scaling(3.0f);
        test.setLocalTranslation(initialTranslation);
        test.setLocalScale(initialScale);
    }

    @Override
    public void initializeLights() {
        Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);
        lightP1 = new Light();
        lightP1.setLocation(new Vector3f(5f, 4.0f, 2.0f));
        (engine.getSceneGraph()).addLight(lightP1);
    }

    @Override
    public void initializeGame() {
        im = engine.getInputManager();
        setupNetworking();

        lastFrameTime = System.currentTimeMillis();
        currFrameTime = System.currentTimeMillis();
        (engine.getRenderSystem()).setWindowDimensions(1900, 1000);

        // ------------- positioning the camera -------------
        // String gamepadName = (im.getFirstGamepadName());
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setLocation(new Vector3f(0f, 0f, 5f));
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setU(new Vector3f(1f, 0f, 0f));
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setV(new Vector3f(0f, 1f, 0f));
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setN(new Vector3f(0f, 0f, -1f));

        // ---------------- input section ------------------
        // MoveAction moveActionPad = new MoveAction(this, true);
        // MoveAction moveActionKeyF = new MoveAction(this, false, true);
        // MoveAction moveActionKeyB = new MoveAction(this, false, false);
        // im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Y, moveActionPad, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        // im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.W, moveActionKeyF, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        // im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.S, moveActionKeyB, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        //-----------------------------
    }

    private void setupNetworking() {
        isClientConnected = false;
        try {
            protClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (protClient == null) {
            System.out.println("missing protocol host");
        } else {
            //ask client rotocol to send initial join message to server, with a unique identifier for this client
            protClient.sendJoinMessage();
        }

    }

    @Override
    public void update() {

        //---------------update time----------
        lastFrameTime = currFrameTime;
        currFrameTime = System.currentTimeMillis();
        processNetworking((float) currFrameTime);

        // if (!paused) {
        //     deltaTime = (currFrameTime - lastFrameTime) / 1000;
        // } else {
        //     deltaTime = 0.0f;
        // }
        // System.out.println(deltaTime);
        //-----------------update inputs-------------
        im.update((float) deltaTime);

        // overheadController.updateCameraPosition();
        // (engine.getHUDmanager()).setHUD1(dispStr1, hud1Color, 15, 15);
        // int screenBoundsX = (engine.getRenderSystem()).getBounds().width;
        // int screenBoundsY = (engine.getRenderSystem()).getBounds().height;
        // double HUD2Y = (screenBoundsY/10)*0.2;
        // (engine.getHUDmanager()).setHUD2(dispStr2, hud2Color, (int) HUD2X, 15);
        // (engine.getHUDmanager()).setHUD3(dispStr3, hud2Color, (int) HUD3X, (int) HUD3Y);
    }

    private void processNetworking(float currFrameTime) {
        if (protClient != null) {
            protClient.processPackets();
        }
    }

    public GameObject getAvatar() {
        return test;
    }

    public ObjShape getGhostShape() {
        return ghostS;
    }

    public TextureImage getGhostTexture() {
        return ghostT;
    }

    public GhostManager getGhostManager() {
        return gm;
    }

    public Vector3f getPlayerPosition() {
        return test.getWorldLocation();
    }

    public boolean getIsConnected() {
        return isClientConnected;
    }

    public void setIsConnected(boolean isConnected) {
        isClientConnected = isConnected;
    }

    public Engine getEngine() {
        return engine;
    }

    public Camera getCamera() {
        return (engine.getRenderSystem().getViewport("MAIN").getCamera());
    }

    public double getDeltaTime() {
        return deltaTime;
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

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_7:
                protClient.sendMoveMessage(test.getWorldLocation());
                break;
            case KeyEvent.VK_9:
                if (protClient != null && isClientConnected == true) {
                    protClient.sendByeMessage();
                }
        }
        super.keyPressed(e);
    }

}
