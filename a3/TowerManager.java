package a3;

import tage.*;
import org.joml.*;
import java.util.*;

public class TowerManager {

    private MyGame game;

    public TowerManager(MyGame g) {
        game = g;
    }

    private ArrayList<Tower> towers = new ArrayList<Tower>();

    public void updateAllTowers(double deltaTime) {
        if (towers.isEmpty()) {
            return;
        }
        for (int i = 0; i < towers.size(); i++) {
            towers.get(i).updateTowerAI(deltaTime);
        }
    }

    public void addTower(Tower t) {
        towers.add(t);
    }

    public ArrayList<Tower> getTowers() {
        return towers;
    }

}
