package a3;

import tage.*;
import org.joml.*;
import java.util.*;
import java.lang.Math;

public class Enemy extends GameObject {

    private MyGame game;
    private ArrayList<Vector2i> path;
    private int nextTileOnPath = 1;
    private float speed = 1f;
    private boolean isAtEndOfPath = false;

    public Enemy(GameObject p, ObjShape s, TextureImage t, MyGame g) {
        super(p, s, t);
        game = g;
        path = game.getPath();
        initializeLocation();
    }

    public Enemy(GameObject p, ObjShape s, TextureImage t, MyGame g, float sp) {
        super(p, s, t);
        game = g;
        path = game.getPath();
        speed = sp;
        initializeLocation();
    }

    private void initializeLocation() {
        Vector2i startTile = path.get(0);
        Tile[][] grid = game.getGrid();
        Vector3f initialLoc = new Vector3f(grid[startTile.x][startTile.y].getPosition().x, 1f, grid[startTile.x][startTile.y].getPosition().z);
        setLocalLocation(initialLoc);
    }

    public void updateEnemyAI(double deltaTime) {
        // System.out.println("nextTileOnPath: " + nextTileOnPath + ", pathsize=" + path.size());
        if (path.size() <= nextTileOnPath) {
            isAtEndOfPath = true;
            return;
        }
        Vector3f oldPos = getLocalLocation();
        Vector2i nextTile = path.get(nextTileOnPath);
        Vector3f goalPos = game.getGrid()[nextTile.x][nextTile.y].getPosition();
        // Vector3f direction = goalPos.sub(oldPos).normalize().mul(speed * (float) deltaTime);
        // oldPos.lerp(goalPos, speed * (float) deltaTime);
        setLocalLocation(oldPos.lerp(goalPos, speed * (float) deltaTime));
        lookAt(goalPos);
        // setLocalLocation(oldPos.add(direction.x(), direction.y(), direction.z()));
        if (detectDistance(oldPos, goalPos) < 1f) {
            nextTileOnPath += 1;
        }
    }

    public boolean getIsAtEndOfPath() {
        return isAtEndOfPath;
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

}
