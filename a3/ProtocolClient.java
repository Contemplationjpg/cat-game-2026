package a3;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import tage.*;
import tage.GhostAvatar.*;
import tage.networking.client.GameConnectionClient;
import org.joml.*;
// import org.joml.Vector3;

public class ProtocolClient extends GameConnectionClient {

    private MyGame game;
    private UUID id;
    private GhostManager ghostManager;

    public ProtocolClient(InetAddress remAddr, int remPort,
            ProtocolType pType, MyGame g, GhostManager gm) throws IOException {
        super(remAddr, remPort, pType);
        this.game = g;
        this.id = UUID.randomUUID();
        System.out.println("generated random UUID: " + id.toString());
        ghostManager = gm;
    }

    @Override
    protected void processPacket(Object msg) {
        // System.out.println("RECEIVED PACKET: " + msg);
        String strMessage = (String) msg;
        String[] msgTokens = strMessage.split(",");
        System.out.println("RECEIVED PACKET: " + Arrays.toString(msgTokens));
        if (msgTokens.length > 0) {
            if (msgTokens[0].compareTo("join") == 0) // receive "join"
            { // format: join, success or join, failure
                if (msgTokens[1].compareTo("success") == 0) {
                    game.setIsConnected(true);
                    sendCreateMessage(game.getCursorManager().getCursorPos());
                    System.out.println("CONNECTED");

                }
                if (msgTokens[1].compareTo("failure") == 0) {
                    game.setIsConnected(false);
                }
            }
            if (msgTokens[0].compareTo("bye") == 0) // receive "bye"
            { // format: bye, remoteId
                UUID ghostID = UUID.fromString(msgTokens[1]);
                ghostManager.removeGhostAvatar(ghostID);
            }
            if ((msgTokens[0].compareTo("dsfr") == 0) // receive "dsfr"
                    || (msgTokens[0].compareTo("create") == 0)) { // format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z
                UUID ghostID = UUID.fromString(msgTokens[1]);
                int[] ghostPosition = {
                        Integer.parseInt(msgTokens[2]),
                        Integer.parseInt(msgTokens[3])
                    };
                try {
                    if (ghostManager == null) {
                        System.out.println("ghost manager null");
                    }
                    else {
                        ghostManager.createGhost(ghostID, ghostPosition);
                        System.out.println("creating ghost avatar");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("error creating ghost avatar");
                }

            }
            if (msgTokens[0].compareTo("wsds") == 0) // rec. "wants…"
            {
            }
            if (msgTokens[0].compareTo("move") == 0) // rec. "move..."
            {
                // System.out.print("SUCCESSFULLY SENT SERVER POSITION: " + msgTokens[1] + ", " + msgTokens[2] + ", " + msgTokens[3]);
                System.out.println("RECEIVED MOVE FROM " + msgTokens[1]);
                int[] pos = {Integer.parseInt(msgTokens[2]), Integer.parseInt(msgTokens[3])};
                game.getGhostManager().updateGhostAvatar(UUID.fromString(msgTokens[1]), pos);
            }
        }
    }

    public void sendJoinMessage() // format: join, localId
    {
        try {
            sendPacket(new String("join," + id.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCreateMessage(int[] pos) { // format: (create, localId, x,y,z)
        try {
            System.out.println("SEND CREATE");
            String message = new String("create," + id.toString());
            message += "," + pos[0] + "," + pos[1];
            sendPacket(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendByeMessage() {
        try {
            sendPacket(new String("bye," + id.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDetailsForMessage(UUID remId, Vector3f pos) {

    }

    public void sendMoveMessage(int[] pos) { //sends cursor position on grid
        try {
            String message = new String("move," + id.toString());
            message += "," + pos[0] + "," + pos[1];
            sendPacket(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
