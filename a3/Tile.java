package a3;

import org.joml.Vector3f;

public class Tile {

    private TileType type;
    private Tower tower;
    private Vector3f position;

    public Tile(TileType t, Vector3f pos) {
        type = t;
        position = pos;
    }

    public Tile(TileType t, Vector3f pos, Tower tw) {
        type = t;
        position = pos;
        tower = tw;
    }

    public TileType getTileType() {
        return type;
    }

    public void setTileType(TileType t) {
        type = t;
    }

    public boolean hasTower() {
        if (tower == null) {
            return false;
        }
        return true;
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tw) {
        tower = tw;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f pos) {
        position = pos;
    }

}
