package a3;

import tage.*;
import org.joml.*;
import java.util.*;
import tage.physics.PhysicsObject;
import tage.physics.PhysicsObject;

public class TowerManager {

    private MyGame game;

    public TowerManager(MyGame g) {
        game = g;
    }

    private ArrayList<Tower> towers = new ArrayList<Tower>();
    private ArrayList<PhysicsObject> rocks = new ArrayList<PhysicsObject>();

    public void updateAllTowers(double deltaTime) {
        if (towers.isEmpty()) {
            return;
        }
        // System.out.println("TowerManager: I have " + towers.size() + " towers");
        for (int i = 0; i < towers.size(); i++) {
            towers.get(i).updateTowerAI(deltaTime);
            // System.out.println(rocks);
        }
    }

    public void addTower(Tower t) {
        towers.add(t);
    }

    public void removeTower(Tower t) {
        towers.remove(t);
        game.getEngine().getSceneGraph().removeGameObject(t);
    }

    public ArrayList<Tower> getTowers() {
        return towers;
    }

    public void addRock(PhysicsObject r) {
        rocks.add(r);
    }

    public void removeRock(PhysicsObject r) {
        rocks.remove(r);
        game.getEngine().getSceneGraph().removePhysicsObject(r);
    }

    public ArrayList<PhysicsObject> getRocks() {
        return rocks;
    }

    public Boolean checkForRock(PhysicsObject r) {
        return rocks.contains(r);
    }

}
