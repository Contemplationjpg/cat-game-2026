package a3;

public class Wave1 extends Wave {

    private final int round1Enemies = 5;
    private final double round1Time = 0;
    private int round1EnemiesToDraw = 0;
    private final int round2Enemies = 5;
    private final double round2Time = 5;
    private int round2EnemiesToDraw = 0;
    private Boolean hasSentRound1 = false;
    private Boolean hasSentRound2 = false;

    private final double spawnDelay = 0.5;
    private double lastSpawnTime;

    @Override
    public void initializeWave() {
        System.out.println("initializing wave1");
        round1EnemiesToDraw = round1Enemies;
        round2EnemiesToDraw = round2Enemies;
        lastSpawnTime = -spawnDelay;
    }

    public Wave1() {
        initializeWave();
    }

    private boolean spawnSmallEnemyOnDelay(MyGame g, float sp) {
        if (lastSpawnTime + spawnDelay < activeTimer) {
            // System.out.println("last spawn time: " + lastSpawnTime + "\ncurrent time: " + activeTimer);
            g.spawnSmallEnemy(sp);
            lastSpawnTime = activeTimer;
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateWave(MyGame g, double deltaTime) {
        activeTimer += deltaTime;
        // System.out.println(activeTimer + " this current round");
        if (activeTimer > round1Time && !hasSentRound1) {
            // System.out.println("round1");
            if (round1EnemiesToDraw > 0) {
                if (spawnSmallEnemyOnDelay(g, 2.0f)) {
                    round1EnemiesToDraw--;
                    System.out.println("enemies left:" + round1EnemiesToDraw);
                }
            } else {
                hasSentRound1 = true;
            }
        }

        if (activeTimer > round2Time && !hasSentRound2) {
            if (round2EnemiesToDraw > 0) {
                // System.out.println("round2");
                if (spawnSmallEnemyOnDelay(g, 3.0f)) {
                    round2EnemiesToDraw--;
                    System.out.println("enemies left:" + round2EnemiesToDraw);
                }
            } else {
                hasSentRound2 = true;
            }
        }

        if (hasSentRound1 && hasSentRound2) {
            // System.out.println("sent all rounds");
            hasSentAllRounds = true;
        }

        return hasSentAllRounds;
    }

}
