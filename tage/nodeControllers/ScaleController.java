package tage.nodeControllers;
import tage.*;
import org.joml.*;


public class ScaleController extends NodeController {
    private float scaleRate = .0003f;
    private float cycleTime = 2000.0f;
    private float totalTime = 0.0f;
    private float direction = 1.0f;
    private Matrix4f curScale, newScale, curRot, newRot;
    private Engine engine;


	/** Creates a scale controller with scaleRate .0003 that when enabled repeats: grows for (float) ctime duration then shrinks for ctime duration*/
    public ScaleController(Engine e, float ctime) {
        super();
        cycleTime = ctime;
        engine = e;
        newScale = new Matrix4f();
    }
    /** Creates a scale controlelr with specified (float) scaleRate that when enabled repeats: grows for (float) ctime duration then shrinks for ctime duration*/
    public ScaleController(Engine e, float ctime, float srate) {
        super();
        cycleTime = ctime;
        scaleRate = srate;
        engine = e;
        newScale = new Matrix4f();
    }


	/** This is called automatically by the RenderSystem (via SceneGraph) once per frame
	*   during display().  It is for engine use and should not be called by the application.
	*/
    public void apply(GameObject go) {
        float deltaTime = super.getElapsedTime();
        totalTime += deltaTime/1000.0f;
        if (totalTime > cycleTime) {
            direction = -direction;
            totalTime = 0.0f;
        }
        // curScale = go.getLocalScale();
        // float scaleAmt = 1.0f + direction * scaleRate * deltaTime;
        // newScale.scaling(curScale.m00()*scaleAmt, curScale.m11(), curScale.m22());
        // go.setLocalScale(newScale);

        curScale = go.getLocalScale();
        float scaleAmt = 0.5f + direction * scaleRate * deltaTime;
        newScale.scaling(curScale.m00()*scaleAmt, curScale.m11()*scaleAmt, curScale.m22()*scaleAmt);
        go.setLocalScale(newScale);

        // curRot = go.getWorldRotation();

        // curRot = go.getWorldRotation();
        // float rotAmt = 1.0f + direction * scaleRate * deltaTime;
        // newScale.rotate(rotAmt, curRot.m00(), curRot.m11(), curRot.m22());
        // go.setWorldRotation(newRot);
    }


	/** sets the scale rate when the controller is enabled */
	public void setScaleRate(float s) { scaleRate = s; }




}