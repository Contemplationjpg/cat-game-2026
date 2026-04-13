package a3;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import tage.*;
import tage.GhostAvatar.*;
import tage.networking.client.GameConnectionClient;
import org.joml.Vector3f;
// import org.joml.Vector3;

public class ProtocolClient extends GameConnectionClient {

    private MyGame game;
    private UUID id;
    private GhostManager ghostManager;

    public ProtocolClient(InetAddress remAddr, int remPort,
            ProtocolType pType, MyGame game) throws IOException {
        super(remAddr, remPort, pType);
        this.game = game;
        this.id = UUID.randomUUID();
        ghostManager = game.getGhostManager();
    }

    @Override
    protected void processPacket(Object msg) {
        String strMessage = (String) msg;
        String[] msgTokens = strMessage.split(",");
        if (msgTokens.length > 0) {
            if (msgTokens[0].compareTo("join") == 0) // receive "join"
            { // format: join, success or join, failure
                if (msgTokens[1].compareTo("success") == 0) {
                    game.setIsConnected(true);
                    sendCreateMessage(game.getPlayerPosition());
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
                Vector3f ghostPosition = new Vector3f(
                        Float.parseFloat(msgTokens[2]),
                        Float.parseFloat(msgTokens[3]),
                        Float.parseFloat(msgTokens[4]));
                try {
                    ghostManager.createGhost(ghostID, ghostPosition);
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

    public void sendCreateMessage(Vector3f pos) { // format: (create, localId, x,y,z)
        try {
            String message = new String("create," + id.toString());
            message += "," + pos.x + "," + pos.y + "," + pos.z;
            sendPacket(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendByeMessage() {

    }

    public void sendDetailsForMessage(UUID remId, Vector3f pos) {

    }

    public void sendMoveMessage(Vector3f pos) {

    }
}
