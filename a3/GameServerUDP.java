package a3;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import tage.networking.server.GameConnectionServer;
import tage.networking.server.IClientInfo;

public class GameServerUDP extends GameConnectionServer<UUID> {

    public GameServerUDP(int localPort) throws IOException {
        super(localPort, ProtocolType.UDP);
        System.out.println("UDP Server: " + localPort);
    }

    @Override
    public void processPacket(Object o, InetAddress senderIP, int sndPort) {
        String message = (String) o;
        // System.out.println("PACKET RECEIVED FROM CLIENT: " + message);
        String[] msgTokens = message.split(",");
        if (msgTokens.length > 0) {
            // case where server receives a JOIN message
            // format: join,localid
            if (msgTokens[0].compareTo("join") == 0) {
                try {
                    IClientInfo ci;
                    ci = getServerSocket().createClientInfo(senderIP, sndPort);
                    UUID clientID = UUID.fromString(msgTokens[1]);
                    addClient(ci, clientID);
                    sendJoinedMessage(clientID, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // case where server receives a CREATE message
        // format: create,localid,x,y,z
        if (msgTokens[0].compareTo("create") == 0) {
            System.out.println("RECEIVED CREATE");
            UUID clientID = UUID.fromString(msgTokens[1]);
            String[] pos = {msgTokens[2], msgTokens[3], msgTokens[4]};
            sendCreateMessages(clientID, pos);
            sendWantsDetailsMessages(clientID);
        }

        // case where server receives a BYE message
        // format: bye,localid
        if (msgTokens[0].compareTo("bye") == 0) {
            UUID clientID = UUID.fromString(msgTokens[1]);
            // System.out.println("sendByeMessage("+clientID+")");
            sendByeMessages(clientID);
            removeClient(clientID);
        }

        // case where server receives a DETAILS-FOR message
        if (msgTokens[0].compareTo("dsfr") == 0) {
        }

        // case where server receives a MOVE message
        if (msgTokens[0].compareTo("move") == 0) {
            UUID clientID = UUID.fromString(msgTokens[1]);
            String[] pos = {msgTokens[2], msgTokens[3], msgTokens[4]};
            // System.out.println(pos.toString());
            System.out.println("MOVE RECEIVED FROM " + clientID + ": " + pos[0] + ", " + pos[1] + ", " + pos[2]);
            sendMoveMessage(clientID, pos);
        }

    }

    public void sendJoinedMessage(UUID clientID, boolean success) {
        // format: join, success or join, failure
        System.out.println("sendJoinedMessage("+clientID+", "+success+")");
        try {
            String message = "join,";
            if (success) {
                message += "success";
            } else {
                message += "failure";
            }
            sendPacket(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCreateMessages(UUID clientID, String[] position) {
        // format: create, remoteId, x, y, z
        try {
            String message = "create," + clientID.toString();
            message += "," + position[0];
            message += "," + position[1];
            message += "," + position[2];
            forwardPacketToAll(message, clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sndDetailsMsg(UUID clientID, UUID remoteId, String[] position) {
        System.out.println(clientID + ", " + remoteId + ", " + position);
    }

    public void sendWantsDetailsMessages(UUID clientID) {
        System.out.println(clientID + " reqesting details");
    }

    public void sendMoveMessage(UUID clientID, String[] position) {
        // System.out.println(clientID + ", " + position);
        // System.out.println("SENDING MOVE MESSAGE");
        try {
            sendPacket("move," + position[0] + "," + position[1] + "," + position[2], clientID);
            // forwardPacketToAll("move," + position[0] + "," + position[1] + "," + position[2], clientID);
        }
        catch (IOException e) {
            System.out.print("failed to send move to " + clientID);
            e.printStackTrace();
        }
    }

    public void sendByeMessages(UUID clientID) {
        System.out.println(clientID + " Bye");
    }

}
