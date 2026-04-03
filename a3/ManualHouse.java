package a3;

import tage.*;
import tage.shapes.*;
import org.joml.*;

public class ManualHouse extends ManualObject {

    private Vector3f[] vertices = new Vector3f[6];
    private Vector2f[] texcoords = new Vector2f[8];
    private Vector3f[] normals = new Vector3f[8];
    private int[] indices = new int[]{
        0, 1, 2, 
        1, 2, 3,
        2, 3, 4,
        3, 4, 5
    };

    public ManualHouse() { super();

        vertices[0] = (new Vector3f()).set(-2.0f, -1.0f, 0.0f);
        vertices[1] = (new Vector3f()).set(-2.0f, 1.0f, 0.0f);
        vertices[2] = (new Vector3f()).set(2.0f, -1.0f, 0.0f);
        vertices[3] = (new Vector3f()).set(2.0f, 1.0f, 0.0f);
        vertices[5] = (new Vector3f()).set(2.0f, 1.0f, 1.0f);
        vertices[4] = (new Vector3f()).set(2.0f, -1.0f, 1.0f);
        texcoords[0] = (new Vector2f()).set(0.5f, 0f);
        texcoords[1] = (new Vector2f()).set(0.5f, 0.5f);
        texcoords[2] = (new Vector2f()).set(0f, 0f);
        texcoords[3] = (new Vector2f()).set(0f, 0.5f);
        texcoords[4] = (new Vector2f()).set(0.5f, 0f);
        texcoords[5] = (new Vector2f()).set(0.5f, 0.5f);
        texcoords[6] = (new Vector2f()).set(0f, 0f);
        texcoords[7] = (new Vector2f()).set(0f, 0.5f);
        normals[0] = (new Vector3f()).set(0f, 0f, 1f);
        normals[1] = (new Vector3f()).set(0f, 0f, 1f);
        normals[2] = (new Vector3f()).set(0f, 0f, 1f);
        normals[3] = (new Vector3f()).set(0f, 0f, 1f);
        normals[4] = (new Vector3f()).set(0f, 0f, 1f);
        normals[5] = (new Vector3f()).set(0f, 0f, 1f);
        normals[6] = (new Vector3f()).set(0f, 0f, 1f);
        normals[7] = (new Vector3f()).set(0f, 0f, 1f);
// there are 4 indexed vertices, but the object has 6 vertices
        setNumVertices(12);
        setVerticesIndexed(indices, vertices);
        setTexCoordsIndexed(indices, texcoords);
        setNormalsIndexed(indices, normals);
        // setMatAmb(Utils.goldAmbient());
        // setMatDif(Utils.goldDiffuse());
        // setMatSpe(Utils.goldSpecular());
        // setMatShi(Utils.goldShininess());
    }

}
