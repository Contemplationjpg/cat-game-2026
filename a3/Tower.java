package a3;

import tage.*;
import org.joml.Vector3f;

public class Tower extends GameObject {

    private TowerType towerType;
    private MyGame game;
    private int[] home = new int[2];

    public Tower(MyGame g, TowerType tT) {
        super(GameObject.root(), tT.getModel(), tT.getTexture());
        game = g;
        towerType = tT;
    }

    public Tower(MyGame g, TowerType tT, int[] h) {
        super(GameObject.root(), tT.getModel(), tT.getTexture());
        game = g;
        towerType = tT;
        home = h;
    }

    public void updateTowerAI(double deltaTime) {
        towerType.towerAI(game, this, deltaTime);
    }

    public void setHome(int[] h) {
        home = h;
    }

    public int[] getHome() {
        return home;
    }

    public TowerType getTowerType() {
        return towerType;
    }

}
