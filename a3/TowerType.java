package a3;

import tage.*;
import tage.physics.*;

public class TowerType {

    protected String name;
    protected ObjShape model;
    protected TextureImage texture;
    protected MyGame game;
    protected Tower tower;
    protected GameObject projectile = null;
    protected PhysicsObject projectileP = null;

    // public TowerType(String n) {
    //     name = n;
    //     String objFileStr = name + ".obj";
    //     try {
    //         model = new ImportedModel(objFileStr);
    //     } catch (Exception e) {
    //         System.err.print("unable to find model for " + n);
    //     }
    //     String texFileStr = name + ".jpg";
    //     try {
    //         texture = new TextureImage(texFileStr);
    //     } catch (Exception e) {
    //         System.err.print("unable to find texture " + texFileStr);
    //     }
    // }
    // public TowerType(String n, ObjShape m) {
    //     name = n;
    //     model = m;
    // }
    // public TowerType(String n, String mStr) {
    //     name = n;
    //     String objFileStr = mStr + ".obj";
    //     try {
    //         model = new ImportedModel(objFileStr);
    //     } catch (Exception e) {
    //         System.err.print("unable to find model for " + mStr);
    //     }
    // }
    public TowerType(MyGame g, Tower t, String n, ObjShape m, TextureImage ti) {
        game = g;
        tower = t;
        name = n;
        model = m;
        texture = ti;
    }

    // public TowerType(String n, String mStr, TextureImage t) {
    //     name = n;
    //     texture = t;
    //     String objFileStr = mStr + ".obj";
    //     try {
    //         model = new ImportedModel(objFileStr);
    //     } catch (Exception e) {
    //         System.err.print("unable to find model for " + mStr);
    //     }
    // }
    // public TowerType(String n, ObjShape m, String tStr) {
    //     name = n;
    //     model = m;
    //     String texFileStr = tStr + ".jpg";
    //     try {
    //         texture = new TextureImage(texFileStr);
    //     } catch (Exception e) {
    //         System.err.print("unable to find texture " + texFileStr);
    //     }
    // }
    // public TowerType(String n, String mStr, String tStr) {
    //     name = n;
    //     String texFileStr = tStr + ".jpg";
    //     try {
    //         texture = new TextureImage(texFileStr);
    //     } catch (Exception e) {
    //         System.err.print("unable to find texture " + texFileStr);
    //     }
    //     String objFileStr = mStr + ".obj";
    //     try {
    //         model = new ImportedModel(objFileStr);
    //     } catch (Exception e) {
    //         System.err.print("unable to find model " + objFileStr);
    //     }
    // }
    public void towerAI(double deltaTime) {
        return;
    }

    protected void onRemove() {
        return;
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public void setModel(ObjShape m) {
        model = m;
    }

    public ObjShape getModel() {
        return model;
    }

    public void setTexture(TextureImage t) {
        texture = t;
    }

    public TextureImage getTexture() {
        return texture;
    }

    public GameObject getProjectile() {
        return projectile;
    }

    public PhysicsObject getProjectileP() {
        return projectileP;
    }

}
