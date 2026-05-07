package a3;

import org.joml.Vector3f;

public class Tile {

    private TileType type;
    private Tower tower = null;
    private Vector3f position;
    private MyGame game;

    public Tile(MyGame g, TileType t, Vector3f pos) {
        game = g;
        type = t;
        position = pos;
    }

    public Tile(MyGame g, TileType t, Vector3f pos, Tower tw) {
        game = g;
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
        return (tower != null);
    }

    public Tower getTower() {
        return tower;
    }

    public boolean setTower(Tower tw) {
        if (type.getTowerable() && tower == null) {
            tower = tw;
            (game.getTowerManager()).addTower(tower);
            tower.setLocalLocation(position);
            return true;
        }
        return false;
    }

    public void removeTower() {
        if (tower != null) {
            (game.getTowerManager()).removeTower(tower);
            game.getEngine().getSceneGraph().removeGameObject(tower);
            tower = null;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f pos) {
        position = pos;
    }

    public boolean getTowerable() {

        System.out.println(type.getTowerable() && tower == null);
        return (type.getTowerable() && tower == null);
    }

}
