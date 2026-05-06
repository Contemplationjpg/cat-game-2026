package a3;

import java.io.*;
import java.util.*;
import org.joml.*;

public class BoardUtils {

    private static ArrayList<ArrayList<String>> parsedBoard;

    public static ArrayList<ArrayList<String>> parseBoard(String filename) throws IOException {

        ArrayList<ArrayList<String>> out = new ArrayList<ArrayList<String>>();

        InputStream input = new FileInputStream(new File(filename));
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = br.readLine()) != null) {
            ArrayList<String> temp = new ArrayList<String>();
            for (String s : (line).split(",")) {
                temp.add(s);
            }
            out.add(temp);
        }

        parsedBoard = out;
        return out;
    }

    public static ArrayList<Vector2i> pathDrawer(MyGame game) {
        //right now, this assumes enemies enter from left and exit on right
        Tile[][] grid = game.getGrid();
        int gridWidth = game.getGridWidth();
        int gridHeight = game.getGridHeight();
        ArrayList<Vector2i> out = new ArrayList<Vector2i>();
        //grid[x][z] | for reference: [0][0] = top left, [9][9] = 10 right, 10 down
        int x = 0; //current x
        int z = 0; //current z
        while (x < gridWidth) {
            // System.out.println(x + ", " + z);
            if (grid[x][z].getTileType().getIsTrail()) {
                System.out.println(x + ", " + z);
                Vector2i tempCoord = new Vector2i(x, z);
                out.add(tempCoord);

                if (x + 1 >= gridWidth) { //if at the far right, end
                    break;
                } else if (grid[x + 1][z].getTileType().getIsTrail()) { //if right is valid, move right
                    x += 1;
                    continue;
                }
                if (z - 1 >= 0) { //if above is within range
                    if (grid[x][z - 1].getTileType().getIsTrail()) { //and above is valid
                        if (!out.get(out.size() - 2).equals(new Vector2i(x, z - 1))) { //and above is not the same as the last thing entered, move up
                            z -= 1;
                            continue;
                        }
                    }
                    if (z + 1 < gridHeight) { //if below is within range
                        if (grid[x][z + 1].getTileType().getIsTrail()) { //and below is valid
                            if (!out.get(out.size() - 2).equals(new Vector2i(x, z + 1))) { //and below is not the same as last, move down
                                z += 1;
                                continue;
                            }
                        } else {
                            return out;
                        }
                    }
                }

            } else {
                if (z + 1 < gridHeight) {
                    z += 1;
                }
            }
        }
        return out;
    }

}
