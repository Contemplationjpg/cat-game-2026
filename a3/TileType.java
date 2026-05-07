package a3;

public class TileType {

    private String name = "default";
    private boolean canHaveTowers = false;
    private boolean isTrail = false;

    public TileType(String n, boolean towerable) {
        name = n;
        canHaveTowers = towerable;
    }

    public TileType(String n, boolean towerable, boolean trail) {
        name = n;
        canHaveTowers = towerable;
        isTrail = trail;
    }

    public String getName() {
        return name;
    }

    public boolean getTowerable() {
        return canHaveTowers;
    }

    public boolean getIsTrail() {
        return isTrail;
    }


    
}
