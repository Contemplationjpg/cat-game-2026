package a3;

import tage.*;
import java.util.*;

public class WaveManager {

    MyGame game;
    ArrayList<Wave> waves = null;

    public WaveManager(MyGame g) {
        game = g;
    }

    public void initializeWaves(ArrayList<Wave> w) {
        waves = w;
    }

    public Boolean updateWave(int currentWave) {
        // System.out.println("Current Wave: " + currentWave);
        if (waves == null) {
            // System.out.println("No Waves: " + currentWave);
            return false;
        }
        if (currentWave < waves.size()) {
            // System.out.println("checking wave " + currentWave);
            return waves.get(currentWave).updateWave(game, game.getDeltaTime());
        }
        System.out.println("wave " + currentWave + " doesn't exist");
        return false;
    }

}
