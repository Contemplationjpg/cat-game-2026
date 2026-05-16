package tage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import java.util.Iterator;
import a3.MyGame;
import tage.GhostAvatar;
import tage.*;
// import tage.GameObject;
import tage.VariableFrameRateGame;
import java.util.Vector;
// import java.util.Vector;
// import tage.rml.Vector3;
// import tage.rml.Vector3f;
// import org.joml.Vector3D;
import org.joml.*;
import tage.ObjShape;

public class GhostManager {

    private MyGame game;
    private Vector<GhostAvatar> ghostAvs = new Vector<GhostAvatar>();

    public GhostManager(VariableFrameRateGame vfrg) {
        game = (MyGame) vfrg;
    }

    public void createGhost(UUID id, int[] p) throws IOException {
        Vector3f pos = new Vector3f(game.getGrid()[p[0]][p[1]].getPosition());
        pos.x += (game.getTileWidth()/3f);
        pos.y = 3f;
        pos.z -= (game.getTileHeight()/3);
        ObjShape s = game.getGhostShape();
        TextureImage t = game.getGhostTexture();
        GhostAvatar newAvatar = new GhostAvatar(id, s, t, pos);
        Matrix4f initialScale = (new Matrix4f()).scaling(0.5f);
        newAvatar.setLocalScale(initialScale);
        newAvatar.yaw(-90);
        ghostAvs.add(newAvatar);
    }

    public void removeGhostAvatar(UUID id) {
        GhostAvatar ghostAv = findAvatar(id);
        if (ghostAv != null) {
            game.getEngine().getSceneGraph().removeGameObject(ghostAv);
            ghostAvs.remove(ghostAv);
        } else {
            System.out.println("unable to find ghost in list");
        }
    }

    private GhostAvatar findAvatar(UUID id) {
        GhostAvatar ghostAvatar;
        Iterator<GhostAvatar> it = ghostAvs.iterator();
        while (it.hasNext()) {
            ghostAvatar = it.next();
            if (ghostAvatar.getGhostUUID().compareTo(id) == 0) {
                return ghostAvatar;
            }
        }
        return null;
    }

    public void updateGhostAvatar(UUID id, int[] p) {
        GhostAvatar ghostAvatar = findAvatar(id);
        if (ghostAvatar != null) {
            Vector3f pos = new Vector3f(game.getGrid()[p[0]][p[1]].getPosition());
            pos.x += (game.getTileWidth()/3f);
            pos.y = 3f;
            pos.z -= (game.getTileHeight()/3);
            ghostAvatar.setLocalLocation(pos);
        } else {
            System.out.println("unable to find ghost in list");
        }
    }

}
