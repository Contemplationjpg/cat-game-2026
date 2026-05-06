package a3;

// import tage.*;
import org.joml.*;
import tage.ObjShape;
import tage.TextureImage;
import tage.shapes.ImportedModel;

public class BasicTower extends TowerType {

    public BasicTower(String s) {
        super(s);
    }

    public BasicTower(String n, ObjShape m) {
        super(n, m);
    }

    public BasicTower(String n, String mStr) {
        super(n, mStr);
    }

    public BasicTower(String n, ObjShape m, TextureImage t) {
        super(n, m, t);
    }

    public BasicTower(String n, String mStr, TextureImage t) {
        super(n, mStr, t);
    }

    public BasicTower(String n, ObjShape m, String tStr) {
        super(n, m, tStr);
    }

    public BasicTower(String n, String mStr, String tStr) {
        super(n, mStr, tStr);
    }

    @Override
    public void towerAI(Tower t, double deltaTime) {
        System.out.println("I AM BASICTOWER");
    }

}
