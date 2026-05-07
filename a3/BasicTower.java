package a3;

import tage.*;
import tage.physics.*;
import org.joml.*;
import tage.ObjShape;
import tage.TextureImage;
import tage.shapes.ImportedModel;
import java.util.*;
import org.joml.Quaternionf;

public class BasicTower extends TowerType {

    private Enemy target = null;
    private double range = 20f;
    private GameObject projectile = null;
    private PhysicsObject projectileP = null;
    private final double projDelay = 1.5;
    private final float projStrength = 100.0f;
    private double timer;

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

    public void attackTarget(MyGame g, Tower t) {
        // System.out.println("I have target");
        if (MyGame.detectDistance(t.getWorldLocation(), target.getWorldLocation()) > range || target.getHealth() <= 0) {
            target = null;
            return;
        }
        t.lookAt(target);
        //create projectile if not created yet
        if (projectile == null) {
            projectile = new GameObject(GameObject.root(), g.getRockShape(), g.getRockTexture());
            projectileP = g.createPhysicsRock(projectile);
            Matrix4f initialTranslation = (new Matrix4f()).translation(t.getLocalLocation());
            Matrix4f initialScale = (new Matrix4f()).scaling(0.5f);
            projectile.setLocalTranslation(initialTranslation);
            projectile.setLocalScale(initialScale);
            g.getTowerManager().addRock(projectileP);
        }
        //throw projectile on timer (give attack cooldown)
        if (timer <= 0) {
            // System.out.println("throwing");
            Matrix4f initialTranslation = (new Matrix4f()).translation(t.getLocalLocation());
            projectile.setLocalTranslation(initialTranslation);
            Vector3f loc = projectile.getWorldLocation();
            // float locF[] = {loc.x, loc.y, loc.z};
            projectileP.setTransform(loc, (projectile.getWorldRotation()).getNormalizedRotation(new Quaternionf()));
            // projectileP.setLocation(locF,(projectile.getWorldRotation()).getNormalizedRotation(new Quaternionf()));
            // projectileP.setRotation((projectile.getWorldRotation()).getNormalizedRotation(new Quaternionf()));

            projectile.lookAt(target);
            Vector3f dir = projectile.getLocalForwardVector();
            float throwingVelo[] = {dir.x * projStrength, 0f, dir.z * projStrength};
            // System.out.println(Arrays.toString(throwingVelo));
            projectileP.setLinearVelocity(throwingVelo);
            timer = projDelay;
        }

    }

    @Override
    public void towerAI(MyGame g, Tower t, double deltaTime) {
        // System.out.println("I AM BASICTOWER");
        timer -= deltaTime;
        if (target == null) {
            searchFirstEnemy(t, (g.getEnemyManager()).getEnemies());
        } else {
            attackTarget(g, t);
        }

    }

}
