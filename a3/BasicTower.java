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
    private final double projDelay = 1.5;
    private final double pickupDelay = 5.0;
    private final float projStrength = 100.0f;
    private double attackTimer;
    private double pickupTimer;

    // public BasicTower(String s) {
    //     super(s);
    // }
    // public BasicTower(String n, ObjShape m) {
    //     super(n, m);
    // }
    // public BasicTower(String n, String mStr) {
    //     super(n, mStr);
    // }
    public BasicTower(MyGame g, Tower t, String n, ObjShape m, TextureImage ti) {
        super(g, t, n, m, ti);
        createNewRock();
    }

    // public BasicTower(String n, String mStr, TextureImage t) {
    //     super(n, mStr, t);
    // }
    // public BasicTower(String n, ObjShape m, String tStr) {
    //     super(n, m, tStr);
    // }
    // public BasicTower(String n, String mStr, String tStr) {
    //     super(n, mStr, tStr);
    // }
    public void searchFirstEnemy() {
        // System.out.println("searching for enemy");

        ArrayList<Enemy> es = game.getEnemyManager().getEnemies();
        double closestDist = 10000f;
        Enemy closestEnemy = null;
        for (int i = 0; i < es.size(); i++) {
            double tempDist = MyGame.detectDistance(tower.getWorldLocation(), es.get(i).getWorldLocation());
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

    private void createNewRock() {
        if (projectile == null && tower != null) {
            System.out.println("making projectile");
            projectile = new GameObject(GameObject.root(), game.getRockShape(), game.getRockTexture());
            projectileP = game.createPhysicsRock(projectile);
            Matrix4f initialTranslation = (new Matrix4f()).translation(tower.getLocalLocation());
            Matrix4f initialScale = (new Matrix4f()).scaling(0.5f);
            projectile.setLocalTranslation(initialTranslation);
            projectile.setLocalScale(initialScale);
            game.getTowerManager().addRock(projectileP);
            projectileP.setTransform(tower.getWorldLocation(), projectileP.getRotation());
        }
    }

    @Override
    protected void onRemove() {
        game.getEngine().getSceneGraph().removeGameObject(projectile);
    }

    public void attackTarget() {
        // System.out.println("I have target, I am at " + tower.getWorldLocation());
        if (MyGame.detectDistance(tower.getWorldLocation(), target.getWorldLocation()) > range || target.getHealth() <= 0) {
            target = null;
            return;
        }
        tower.lookAt(target);
        //create projectile if not created yet
        createNewRock();
        //throw projectile on timer (give attack cooldown)
        if (attackTimer <= 0) {
            // System.out.println("throwing");
            projectile.getRenderStates().enableRendering();
            projectileP.disableSleeping();
            Matrix4f initialTranslation = (new Matrix4f()).translation(tower.getLocalLocation());
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
            attackTimer = projDelay;
            pickupTimer = pickupDelay;
        }

    }

    @Override
    public void towerAI(double deltaTime) {
        // System.out.println("I AM BASICTOWER");
        attackTimer -= deltaTime;
        pickupTimer -= deltaTime;
        if (target == null) {
            searchFirstEnemy();
        } else {
            attackTarget();
        }
        if (pickupTimer <= 0) {
            Matrix4f initialTranslation = (new Matrix4f()).translation(tower.getLocalLocation());
            projectile.setLocalTranslation(initialTranslation);
            Vector3f loc = projectile.getWorldLocation();
            projectileP.setTransform(loc, (projectile.getWorldRotation()).getNormalizedRotation(new Quaternionf()));
            //disabling projectile
            projectile.getRenderStates().disableRendering();
            projectileP.enableSleeping();

            // System.out.println("picking up rock");
        } else {
            // System.out.println("not picking up rock");
        }
    }

}
