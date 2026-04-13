package tage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import tage.*;
import a3.MyGame;
import org.joml.Vector3f;

public class GhostAvatar extends GameObject {

    private UUID id;

    public GhostAvatar(UUID id, ObjShape s, TextureImage t, Vector3f p) {
        super(GameObject.root(), s, t);
        this.id = id;
        setLocalLocation(p);
    }

    public UUID getGhostUUID() {
        return id;
    }

}
