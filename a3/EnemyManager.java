package a3;

import tage.*;
import tage.audio.*;
import org.joml.*;
import java.util.*;
import tage.physics.*;

public class EnemyManager {

    private MyGame game;

    public EnemyManager(MyGame g) {
        game = g;
    }

    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private ArrayList<PhysicsObject> enemyPs = new ArrayList<PhysicsObject>();
    private ArrayList<Sound> deathSounds = new ArrayList<Sound>();


    public void updateAllEnemies(double deltaTime) {
        if (enemies.isEmpty()) {
            return;
        }
        for (int i = 0; i < enemies.size(); i++) {
            // System.out.println("updating enemy " + i);
            HashSet<PhysicsObject> colls = enemyPs.get(i).getNewlyCollidedSet();
            // System.out.println("enemy " + i + " collided set:" + colls.toString());
            if (!colls.isEmpty()) {
                for (int j = 0; j < game.getTowerManager().getRocks().size(); j++) {
                    if (colls.contains(game.getTowerManager().getRocks().get(j))) {
                        enemies.get(i).takeDamage(1);
                        // System.out.println("Health: " + enemies.get(i).getHealth());
                    }
                }
                for (int j = 0; j < game.getTowerManager().getBombs().size(); j++) {
                    if (colls.contains(game.getTowerManager().getBombs().get(j))) {
                        enemies.get(i).takeDamage(5);
                        // System.out.println("Health: " + enemies.get(i).getHealth());
                    }
                }
            }
            enemies.get(i).updateEnemyAI(deltaTime);
            Vector3f loc = enemies.get(i).getWorldLocation();
            Quaternionf rot = new Quaternionf();
            Matrix4f rotation = (new Matrix4f()).rotation(0f, 0f, (float) org.joml.Math.PI / 2, 1f);
            ((enemies.get(i).getWorldRotation()).mul(rotation)).getNormalizedRotation(rot);
            enemyPs.get(i).setTransform(loc, rot);
            if (enemies.get(i).getIsAtEndOfPath()) {
                destroyEnemyEnd(i);
            } else if (enemies.get(i).getHealth() <= 0) {
                destroyEnemyEarly(i);
            }
        }
    }

    private void destroyEnemyEarly(int i) {
        game.increaseMoney(enemies.get(i).getReward());
        game.getEngine().getSceneGraph().removeGameObject(enemies.get(i));
        enemies.set(i, null);
        enemies.remove(i);
        game.getEngine().getSceneGraph().removePhysicsObject(enemyPs.get(i));
        enemyPs.set(i, null);
        enemyPs.remove(i);
        deathSounds.get(i).play();
        deathSounds.set(i,null); //memory leak here maybe??
        deathSounds.remove(i);
        System.out.println("Enemy health reduced to zero.");
    }

    private void destroyEnemyEnd(int i) {
        game.reduceHealth(enemies.get(i).getDamageToPlayer());
        game.getEngine().getSceneGraph().removeGameObject(enemies.get(i));
        enemies.set(i, null);
        enemies.remove(i);
        game.getEngine().getSceneGraph().removePhysicsObject(enemyPs.get(i));
        enemyPs.set(i, null);
        enemyPs.remove(i);
        deathSounds.set(i,null); //there might be a memory leak here?? Java data collection hopefully gets to it
        deathSounds.remove(i);
        // System.out.println("Enemy made it to end!");

    }

    private void destroyEnemyEnd(Enemy e) {
        game.getEngine().getSceneGraph().removeGameObject(e);
        enemies.remove(enemies.indexOf(e));
        // System.out.println("Enemy made it to end!");
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void addEnemyP(PhysicsObject e) {
        enemyPs.add(e);
    }

    public ArrayList<PhysicsObject> getEnemyPs() {
        return enemyPs;
    }

    public void addDeathSound(Sound s) {
        deathSounds.add(s);
    }

    public ArrayList<Sound> getDeathSounds() {
        return deathSounds;
    }
}
