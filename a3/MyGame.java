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
import java.util.*;
import java.util.Vector;
import java.util.Arrays;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;
import tage.nodeControllers.RotationController;
import tage.nodeControllers.ScaleController;
import tage.networking.IGameConnection.ProtocolType;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyGame extends VariableFrameRateGame {

    private static Engine engine;

    private boolean paused = false;
    private int counter = 0;
    private double lastFrameTime, currFrameTime, deltaTime;

    private boolean isOrbitMode = false;

    private CameraOrbitController orbitController;
    private OverheadCameraController overheadController;

    private GameObject dol;
    private ObjShape dolS;
    private TextureImage doltx;

    private GameObject ghost;
    private ObjShape ghostS;
    private TextureImage ghostT;

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

    // private GameObject sphere;
    private ObjShape tableS;
    private GameObject testTables[][];

    private GameObject cursor;
    private ObjShape cursorS;

    private Light lightP1, lightP2, lightP3, lightH;

    private InputManager im;
    private CursorManager cm;
    private EnemyManager em;

    private float[] turnDirY; //Y axis turn is left and right
    private final float turnSpeedY = 0.08f;

    private float[] turnDirX; //X axis turn is up and down
    private final float turnSpeedX = 0.08f;

    private Vector3f loc, fwd, up, right, newLocation;
    private Camera cam, overheadCam;
    private Viewport camVp, overheadVp;

    String dispStr1 = "";
    String dispStr2 = "";

    private GhostManager gm;
    private String serverAddress = "localhost";
    private int serverPort = 1050;
    private ProtocolType serverProtocol = ProtocolType.UDP;
    private ProtocolClient protClient;
    private boolean isClientConnected = false;

    private final Vector3f homePos = new Vector3f(0, 0, 5);

    private boolean gameWon;
    private boolean gameLost;

    private Tile[][] grid; //grid[x][y]
    private final int gridWidth = 10;
    private final int gridHeight = 6;
    private final float tileWidth = 10.0f;
    private final float tileHeight = 10.0f;
    private ArrayList<Vector2i> path;

    private int mountainsBox; //skybox

    private GameObject terr;
    private ObjShape terrS;
    private TextureImage grass;
    public TextureImage grassHM;

    public MyGame(String serverAddress, int serverPort, String protocol) {
        super();
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

    @Override
    public void shutdown() {
        System.out.println("shutting down");
        protClient.sendByeMessage();
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

    @Override
    public void createViewports() {
        (engine.getRenderSystem()).addViewport("MAIN", 0, 0, 1f, 1f);
        // (engine.getRenderSystem()).addViewport("OVERHEAD", 0.65f, 0.0f, 0.35f, 0.35f);

        camVp = (engine.getRenderSystem()).getViewport("MAIN");
        cam = (engine.getRenderSystem().getViewport("MAIN").getCamera());
//USE FOR overhead cam
        // overheadVp = (engine.getRenderSystem()).getViewport("OVERHEAD");
        // overheadCam = (engine.getRenderSystem().getViewport("OVERHEAD").getCamera());
//--------------------

        // overheadVp.setHasBorder(true);
        // overheadVp.setBorderWidth(3);
        // overheadVp.setBorderColor(0f, 1f, 0f);
        // overheadCam.setLocation(new Vector3f(0, 15, 0));
        // overheadCam.setU(new Vector3f(1, 0, 0));
        // overheadCam.setV(new Vector3f(0, 0, -1));
        // overheadCam.setN(new Vector3f(0, -1, 0));
    }

    @Override
    public void loadShapes() {
        // dolS = new ImportedModel("dolphinHighPoly.obj");
        dolS = new ImportedModel("rat.obj");
        tableS = new ImportedModel("table.obj");

        planeS = new Plane();

        terrS = new TerrainPlane(1000); //pixes per axis = 1000x1000

    }

    @Override
    public void loadTextures() {
        // doltx = new TextureImage("Dolphin_HighPolyUV.jpg");
        doltx = new TextureImage("rat.png");
        brick = new TextureImage("brick1.jpg");
        gas = new TextureImage("gaseous.jpg");
        bluebrick = new TextureImage("bluebrick.jpg");
        purplebrick = new TextureImage("purplebrick.jpg");
        grass = new TextureImage("grass.png");
        grassHM = new TextureImage("grassHM.png");
    }

    @Override
    public void loadSkyBoxes() {
        mountainsBox = (engine.getSceneGraph()).loadCubeMap("mountains");
        (engine.getSceneGraph()).setActiveSkyBoxTexture(mountainsBox);
        (engine.getSceneGraph()).setSkyBoxEnabled(true);
    }

    @Override
    public void buildObjects() {
        Matrix4f initialTranslation, initialScale;

        // build dolphin in the center of the window
        // dol = new GameObject(GameObject.root(), dolS, doltx);
        dol = new GameObject(GameObject.root(), dolS, null);
        initialTranslation = (new Matrix4f()).translation(0, -15, 0);
        initialScale = (new Matrix4f()).scaling(3.0f);
        dol.setLocalTranslation(initialTranslation);
        dol.setLocalScale(initialScale);

        // plane = new GameObject(GameObject.root(), planeS, gas);
        // Matrix4f initScalePlane = (new Matrix4f()).scale(50f);
        // Matrix4f initTransPlane = (new Matrix4f()).translation(0, -2, 0);
        // plane.setLocalScale(initScalePlane);
        // build terrain object
        terr = new GameObject(GameObject.root(), terrS, grass);
        initialTranslation = (new Matrix4f()).translation(0f, -4f, 0f);
        terr.setLocalTranslation(initialTranslation);
        initialScale = (new Matrix4f()).scaling(50.0f, 1.0f, 50.0f);
        terr.setLocalScale(initialScale);
        terr.setHeightMap(grassHM);
        terr.getRenderStates().setTiling(10);
        terr.getRenderStates().setTileFactor(1);

        cursor = new GameObject(GameObject.root(), tableS, gas);
        initialTranslation = (new Matrix4f()).translation(0f, 0f, 0f);
        cursor.setLocalTranslation(initialTranslation);

        //------------------- setting up grid of tiles ----------
        //read board
        ArrayList<ArrayList<String>> board = null;
        try {
            board = BoardUtils.parseBoard("a3/board.txt");
            System.out.println(board); //testing board
        } catch (Exception e) {
            System.err.println(e);
        }

        //build grid
        grid = new Tile[gridWidth][gridHeight];

        //center at world position 0,0,0 and start for loop at [0][0] on grid
        //note: only works with even length width and heights for now 
        float tempX = (-gridWidth / 2) * tileWidth; //starting X
        float tempZ = (-gridHeight / 2) * tileHeight; //starting Z

        TileType placableTile = new TileType("placable", true);
        TileType trailTile = new TileType("trail", false, true);
        TileType unplacableTile = new TileType("unplacable", false);

        for (int x = 0; x < gridWidth; x++) { //creating grid
            tempZ = (-gridHeight / 2) * tileHeight; //starting Z
            for (int z = 0; z < gridHeight; z++) {
                Vector3f tempPos = new Vector3f(tempX + (tileWidth / 2), 0f, tempZ + (tileHeight / 2));
                Tile tempTile = new Tile(placableTile, tempPos);
                if (board != null) {
                    try {
                        if (board.get(z).get(x).equals("p")) {
                            tempTile = new Tile(placableTile, tempPos);
                        } else if (board.get(z).get(x).equals("t")) {
                            tempTile = new Tile(trailTile, tempPos);
                        } else if (board.get(z).get(x).equals("u")) {
                            tempTile = new Tile(unplacableTile, tempPos);
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
                grid[x][z] = tempTile;
                tempZ += tileHeight;
            }
            tempX += tileWidth;
        }

        //build path
        path = BoardUtils.pathDrawer(this);
        System.out.print(path.toString());

        //making test tables
        testTables = new GameObject[gridWidth][gridHeight];
        for (int x = 0; x < gridWidth; x++) {
            for (int z = 0; z < gridHeight; z++) {
                Matrix4f initTrans = (new Matrix4f()).translation(grid[x][z].getPosition().x, 0f, grid[x][z].getPosition().z);
                GameObject table = new GameObject(GameObject.root(), tableS, brick);
                if ((grid[x][z].getTileType().getName()).equals("trail")) {
                    table.setTextureImage(purplebrick);
                } else if (grid[x][z].getTileType().getName().equals("unplacable")) {
                    table.setShape(dolS);
                }
                table.setLocalTranslation(initTrans);
                // grid[x][z].setTower((Tower) table);

                testTables[x][z] = table;
            }
        }

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
        gm = new GhostManager(this);
        im = engine.getInputManager();
        cm = new CursorManager(this);
        em = new EnemyManager(this);
        setupNetworking();

        lastFrameTime = System.currentTimeMillis();
        currFrameTime = System.currentTimeMillis();
        (engine.getRenderSystem()).setWindowDimensions(1900, 1000);

        // ------------- positioning the camera -------------
        String gamepadName = (im.getFirstGamepadName());

        orbitController = new CameraOrbitController(cam, this, gamepadName, engine); //FOR ORBIT CONTROLLER
        overheadController = new OverheadCameraController(cam, gamepadName, engine); //FOR OVERHEAD CONTROLLER

        turnDirY = new float[2];
        turnDirX = new float[2];
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setLocation(new Vector3f(0f, 20f, 0f));
        // (engine.getRenderSystem().getViewport("MAIN").getCamera()).setU(new Vector3f(1f, 0f, 0f));
        // (engine.getRenderSystem().getViewport("MAIN").getCamera()).setV(new Vector3f(0f, 1f, 0f));
        // (engine.getRenderSystem().getViewport("MAIN").getCamera()).setN(new Vector3f(0f, 0f, -1f));
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setU(new Vector3f(1f, 0f, 0f));
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setV(new Vector3f(0f, 0f, -1f));
        (engine.getRenderSystem().getViewport("MAIN").getCamera()).setN(new Vector3f(0f, -1f, 0f));

        // ---------------- input section ------------------
        CursorMoveActionVert moveCursorActionVertPad = new CursorMoveActionVert(this, true);
        CursorMoveActionVert moveCursorActionVertKeyF = new CursorMoveActionVert(this, false, true);
        CursorMoveActionVert moveCursorActionVertKeyB = new CursorMoveActionVert(this, false, false);

        CursorMoveActionHori moveCursorActionHoriPad = new CursorMoveActionHori(this, true);
        CursorMoveActionHori moveCursorActionHoriKeyF = new CursorMoveActionHori(this, false, true);
        CursorMoveActionHori moveCursorActionHoriKeyB = new CursorMoveActionHori(this, false, false);

        // TurnAction turnActionPad = new TurnAction(this, true);
        // TurnAction turnActionKeyR = new TurnAction(this, false, true);
        // TurnAction turnActionKeyL = new TurnAction(this, false, false);
        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Y, moveCursorActionVertPad, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.W, moveCursorActionVertKeyF, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.S, moveCursorActionVertKeyB, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

        im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.X, moveCursorActionHoriPad, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.D, moveCursorActionHoriKeyF, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.A, moveCursorActionHoriKeyB, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

        // im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.X, turnActionPad, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        // im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.D, turnActionKeyR, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        // im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.A, turnActionKeyL, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    }

    private void setupNetworking() {
        isClientConnected = false;
        try {
            protClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this, gm);
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
        if (!paused) {
            deltaTime = (currFrameTime - lastFrameTime) / 1000;
        } else {
            deltaTime = 0.0f;
        }

        cm.updateCursor();
        em.updateAllEnemies(deltaTime);

        // System.out.println(deltaTime);
        //-----------------update inputs-------------
        im.update((float) deltaTime);

        //-----------camera lock on dolphin------------
        if (isOrbitMode) {
            orbitController.updateCameraPosition(); //FOR ORBIT CONTROLLER
        } else {
            overheadController.updateCameraPosition(); //FOR OVERHEAD CONTROLLER
        }
        // System.out.println(cam.getLocation());
        //---------------------HUD-------------------------
        String dispStr3 = "";

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

        processNetworking((float) currFrameTime);
        protClient.sendMoveMessage(new Vector3f(dolPos));

    }

    private void processNetworking(float currFrameTime) {
        if (protClient != null) {
            protClient.processPackets();
        }
    }

    public ProtocolClient getProtClient() {
        return protClient;
    }

    public Tile[][] getGrid() {
        return grid;
    }

    public ArrayList<Vector2i> getPath() {
        return path;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public float getTileWidth() {
        return tileWidth;
    }

    public float getTileHeight() {
        return tileHeight;
    }

    public GameObject getCursorObj() {
        return cursor;
    }

    public CursorManager getCursorManager() {
        return cm;
    }

    public GameObject getAvatar() {
        return dol;
    }

    public ObjShape getGhostShape() {// return ghostS;
        return dolS;
    }

    public TextureImage getGhostTexture() {// return ghostT;
        return doltx;
    }

    public GhostManager getGhostManager() {
        return gm;
    }

    public Vector3f getPlayerPosition() {
        return dol.getWorldLocation();
    }

    public boolean getIsConnected() {
        return isClientConnected;
    }

    public void setIsConnected(boolean isConnected) {
        isClientConnected = isConnected;
    }

    public boolean getIsOrbitMode() {
        return isOrbitMode;
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
                protClient.sendMoveMessage(dol.getWorldLocation());
                break;
            case KeyEvent.VK_9:
                if (protClient != null && isClientConnected == true) {
                    protClient.sendByeMessage();
                }
                break;
            case KeyEvent.VK_0:
                isOrbitMode = !isOrbitMode;
                break;
            case KeyEvent.VK_2:
                spawnEnemy();
                break;
        }
        super.keyPressed(e);
    }

    private void spawnEnemy() {
        Enemy testEnemy = new Enemy(GameObject.root(), dolS, doltx, this, 3.0f);
        em.addEnemy(testEnemy);
    }

}
