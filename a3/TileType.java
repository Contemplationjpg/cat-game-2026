package a3;

public class TileType {

    private String name;
    private boolean canHaveTowers;

    public TileType(String n, boolean towerable) {
        name = n;
        canHaveTowers = towerable;
    }

    public String getName() {
        return name;
    }

    public boolean getTowerable() {
        return canHaveTowers;
    }

}
