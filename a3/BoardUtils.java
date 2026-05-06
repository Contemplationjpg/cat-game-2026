package a3;

import java.io.*;
import java.util.*;

public class BoardUtils {

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

        return out;
    }

}
