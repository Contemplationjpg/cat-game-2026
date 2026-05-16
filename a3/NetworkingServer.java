package a3;

import java.io.IOException;

public class NetworkingServer {

    private GameServerUDP thisUDPServer;
    // private GameServerTCP thisTCPServer;

    public NetworkingServer(int serverPort) {
        try {
                thisUDPServer = new GameServerUDP(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println(Integer.parseInt(args[0]) + ", " + args[1]);
            NetworkingServer app
                    = new NetworkingServer(Integer.parseInt(args[0]));
        }
    }
}
