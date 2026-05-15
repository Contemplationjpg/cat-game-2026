package a3;

public class Wave {

    protected double activeTimer = 0;
    protected Boolean hasSentAllRounds = false;

    public void initializeWave() {
        return;
    }

    public Boolean updateWave(MyGame g, double deltaTime) {
        activeTimer += deltaTime;
        return hasSentAllRounds;
    }

    public void resetWave() {
        initializeWave();
    }

    public Boolean getHasSentAllRounds() {
        return hasSentAllRounds;
    }

}
