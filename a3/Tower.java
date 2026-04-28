package a3;

import tage.*;
import org.joml.Vector3f;

public class Tower extends GameObject {

    private TowerType towerType;
    private MyGame g;

    public Tower(MyGame myG, TowerType tT) {
        super(GameObject.root(), tT.getModel(), tT.getTexture());
        g = myG;
        towerType = tT;
    }

}
