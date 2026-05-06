package a3;

import tage.*;
import org.joml.*;
import java.util.*;

public class EnemyManager {

    private MyGame game;

    public EnemyManager(MyGame g) {
        game = g;
    }

    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    public void updateAllEnemies(double deltaTime) {
        if (enemies.isEmpty()) {
            return;
        }
        for (int i = 0; i < enemies.size(); i++) {
            // System.out.println("updating enemy " + i);
            enemies.get(i).updateEnemyAI(deltaTime);
            if (enemies.get(i).getIsAtEndOfPath()) {
                destroyEnemyEnd(i);
            }
        }
    }

    private void destroyEnemyEnd(int i) {
        game.getEngine().getSceneGraph().removeGameObject(enemies.get(i));
        enemies.set(i, null);
        enemies.remove(i);
        System.out.println("Enemy made it to end!");
    }

    private void destroyEnemyEnd(Enemy e) {
        game.getEngine().getSceneGraph().removeGameObject(e);
        enemies.remove(enemies.indexOf(e));
        System.out.println("Enemy made it to end!");
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}
