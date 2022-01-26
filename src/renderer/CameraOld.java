package renderer;

import utils.Input;
import utils.Matrix4f;
import utils.Quaternion;
import utils.Transform;
import utils.Vertex;
import utils.Vector4f;

public class CameraOld
{

	/*private Transform m_transform;
	private Matrix4f m_projection;
	protected float[] m_zBuffer;
	public Transform getTransform()
	{
		return m_transform;
	}

	public static CameraOld createPerspectiveCamera(float FOVDegrees, int viewportWidth, int viewportHeight, float nearPlane, float farPlane, Input input){
		Matrix4f projection = new Matrix4f().InitPerspective((float) Math.toRadians(FOVDegrees), 
				(float)viewportWidth/(float)viewportHeight, 
				nearPlane, 
				farPlane);
		return new CameraOld(projection);
	}
	
	public static CameraOld createOrthoCamera(float left, float right, float top, float bottom, float nearPlane, float farPlane, Input input){
		Matrix4f projection = new Matrix4f().InitOrthographic(left, right, bottom, top,
				nearPlane, 
				farPlane);
		return new CameraOld(projection);
	}
	public float[] getZBuffer() {
		return m_zBuffer;
	}
	
	public void setZBuffer(int index, float value) {
		m_zBuffer[index] = value;
	}
	
	public void clearDepthBuffer(){
		for (int i = 0; i < m_zBuffer.length; i++){
			m_zBuffer[i] = Float.MAX_VALUE;
		}
	}
	public CameraOld(Matrix4f projection)
	{
		this.m_projection = projection;
		this.m_transform = new Transform();
		m_zBuffer = new float[Display.BUFFER_WIDTH * Display.BUFFER_HEIGHT];
	}

	public Matrix4f getViewProjection()
	{
		Matrix4f cameraRotation = getTransform().GetTransformedRot().copy().Conjugate().ToRotationMatrix();
		Vector4f cameraPos = getTransform().GetTransformedPos().copy().Mul(-1);

		Matrix4f cameraTranslation = new Matrix4f().InitTranslation(cameraPos.GetX(), cameraPos.GetY(), cameraPos.GetZ());

		return m_projection.Mul(cameraRotation.Mul(cameraTranslation));
	}

	public Vertex projectOnScreen(Transform meshInstanceTransform, Vertex point){
		Matrix4f mvp = getViewProjection().Mul(meshInstanceTransform.GetTransformation());
		
		return point.Transform(mvp, meshInstanceTransform.GetTransformation());
	}
	
	public void Move(Vector4f dir, float amt)
	{
		getTransform().GetPos().Add(dir.copy().Mul(amt));
	}

	public void Rotate(Vector4f axis, float angle)
	{
		//GetTransform().Rotate(new Quaternion(axis, angle));
		getTransform().Rotate(new Quaternion(axis, angle));
	}*/
}
