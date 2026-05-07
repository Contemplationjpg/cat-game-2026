package a3;

// import tage.*;
import org.joml.*;
import tage.ObjShape;
import tage.TextureImage;
import tage.shapes.ImportedModel;
import java.util.*;

public class BasicTower extends TowerType {

    private Enemy target = null;
    private double range = 20f;

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

    public void searchFirstEnemy(Tower t, ArrayList<Enemy> es) {
        // System.out.println("searching for enemy");
        double closestDist = 10000f;
        Enemy closestEnemy = null;
        for (int i = 0; i < es.size(); i++) {
            double tempDist = MyGame.detectDistance(t.getWorldLocation(), es.get(i).getWorldLocation());
            // System.out.println("enemy " + i + " distance: " + tempDist);
            if (tempDist < closestDist && tempDist <= range) {
                closestEnemy = es.get(i);
                closestDist = tempDist;
                // System.out.println("valid enemy");
            } else {
                // System.out.println("invalid enemy distance from range: " + (tempDist - range));
            }
        }
        if (closestEnemy == null) {
            target = null;
        } else {
            target = closestEnemy;
        }
    }

    public void attackTarget(Tower t) {
        System.out.println("I have target");
        if (MyGame.detectDistance(t.getWorldLocation(), target.getWorldLocation())>range) {
            target = null;
            return;
        }
        t.lookAt(target);
    }

    @Override
    public void towerAI(MyGame g, Tower t, double deltaTime) {
        // System.out.println("I AM BASICTOWER");
        if (target==null) {
            searchFirstEnemy(t, (g.getEnemyManager()).getEnemies());
        }
        else {
            attackTarget(t);
        }
        
    }


}
