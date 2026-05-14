package a3;

import org.joml.Vector3f;
import tage.physics.PhysicsObject;

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
            System.out.println("I am Tile: setting tower");
            tower = tw;
            (game.getTowerManager()).addTower(tower);
            tower.setLocalLocation(position);
            return true;
        }
        return false;
    }

    public void removeTower() {
        if (hasTower()) {
            // PhysicsObject rock = (game.getTowerManager()).getRocks().get((game.getTowerManager().getTowers()).indexOf(tower));
            PhysicsObject rock = tower.getTowerType().getProjectileP();
            if (rock != null) {
                // System.out.println("rock");
                if (game.getTowerManager().checkForRock(rock)) {
                    // System.out.println("there IS a rock");
                }
            } else {
                // System.out.println("no rock");
            }

            (game.getTowerManager()).removeRock(rock);
            (game.getTowerManager()).removeTower(tower);
            tower.getTowerType().onRemove();
            game.getEngine().getSceneGraph().removeGameObject(tower);
            game.getEngine().getSceneGraph().removePhysicsObject(rock);
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
