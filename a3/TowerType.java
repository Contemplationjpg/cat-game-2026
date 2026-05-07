package a3;

import tage.*;
import tage.physics.*;
import tage.shapes.ImportedModel;

public class TowerType {

    private String name;
    private ObjShape model;
    private TextureImage texture;
    private GameObject projectile = null;
    private PhysicsObject projectileP = null;

    public TowerType(String n) {
        name = n;
        String objFileStr = name + ".obj";
        try {
            model = new ImportedModel(objFileStr);
        } catch (Exception e) {
            System.err.print("unable to find model for " + n);
        }
        String texFileStr = name + ".jpg";
        try {
            texture = new TextureImage(texFileStr);
        } catch (Exception e) {
            System.err.print("unable to find texture " + texFileStr);
        }
    }

    public TowerType(String n, ObjShape m) {
        name = n;
        model = m;
    }

    public TowerType(String n, String mStr) {
        name = n;
        String objFileStr = mStr + ".obj";
        try {
            model = new ImportedModel(objFileStr);
        } catch (Exception e) {
            System.err.print("unable to find model for " + mStr);
        }
    }

    public TowerType(String n, ObjShape m, TextureImage t) {
        name = n;
        model = m;
        texture = t;
    }

    public TowerType(String n, String mStr, TextureImage t) {
        name = n;
        texture = t;
        String objFileStr = mStr + ".obj";
        try {
            model = new ImportedModel(objFileStr);
        } catch (Exception e) {
            System.err.print("unable to find model for " + mStr);
        }
    }

    public TowerType(String n, ObjShape m, String tStr) {
        name = n;
        model = m;
        String texFileStr = tStr + ".jpg";
        try {
            texture = new TextureImage(texFileStr);
        } catch (Exception e) {
            System.err.print("unable to find texture " + texFileStr);
        }
    }

    public TowerType(String n, String mStr, String tStr) {
        name = n;
        String texFileStr = tStr + ".jpg";
        try {
            texture = new TextureImage(texFileStr);
        } catch (Exception e) {
            System.err.print("unable to find texture " + texFileStr);
        }

        String objFileStr = mStr + ".obj";
        try {
            model = new ImportedModel(objFileStr);
        } catch (Exception e) {
            System.err.print("unable to find model " + objFileStr);
        }
    }

    public void towerAI(MyGame g, Tower t, double deltaTime) {
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

    public PhysicsObject getProjectileP() {
        return projectileP;
    }
}
