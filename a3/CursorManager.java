package a3;

import tage.*;
import org.joml.*;

public class CursorManager {

    private MyGame game;
    private GameObject cursor;
    private int[] cursorPos;

    private int gridWidth;
    private int gridHeight;

    public CursorManager(MyGame g) {
        game = g;
        setup();
        cursorPos = new int[2];
    }

    private void setup() {
        gridWidth = game.getGridWidth();
        gridHeight = game.getGridHeight();
        cursor = game.getCursorObj();
    }

    public void moveCursorVert(float dir) {
        if (dir > 0) {
            if (cursorPos[1] + 1 < gridHeight) {
                cursorPos[1] += 1;
            }
        }
        if (dir < 0) {
            if (cursorPos[1] - 1 >= 0) {
                cursorPos[1] -= 1;
            }
        }

    }

    public void moveCursorHori(float dir) {
        if (dir > 0) {
            if (cursorPos[0] + 1 < gridWidth) {
                cursorPos[0] += 1;
            }
        }
        if (dir < 0) {
            if (cursorPos[0] - 1 >= 0) {
                cursorPos[0] -= 1;
            }
        }

    }

    public int[] getCursorPos() {
        return cursorPos;
    }

    public void updateCursor() {
        Tile[][] grid = game.getGrid();
        Vector3f pos = grid[cursorPos[0]][cursorPos[1]].getPosition();
        float newX = pos.x - (game.getGridWidth() / 3f);
        float newZ = pos.z - (game.getGridHeight() / 3f);
        Matrix4f transform = (new Matrix4f()).translation(newX, 3f, newZ);
        cursor.setLocalTranslation(transform);
    }

}
