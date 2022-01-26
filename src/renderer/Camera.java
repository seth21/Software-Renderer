package renderer;

import utils.Input;
import utils.Matrix4f;
import utils.Quaternion;
import utils.Vector4f;

public class Camera extends Observer
{

	public static Camera createPerspectiveCamera(float FOVDegrees, int viewportWidth, int viewportHeight, float nearPlane, float farPlane, Input input){
		Matrix4f projection = new Matrix4f().InitPerspective((float) Math.toRadians(FOVDegrees), 
				(float)viewportWidth/(float)viewportHeight, 
				nearPlane, 
				farPlane);
		return new Camera(projection, viewportWidth, viewportHeight);
	}
	
	public static Camera createOrthoCamera(float left, float right, float top, float bottom, float nearPlane, float farPlane, Input input){
		Matrix4f projection = new Matrix4f().InitOrthographic(left, right, bottom, top,
				nearPlane, 
				farPlane);
		return new Camera(projection, 0, 0);
	}
	
	public Camera(Matrix4f projection, int viewportWidth, int viewportHeight)
	{
		super(projection, viewportWidth, viewportHeight);
		

	}

	public Matrix4f GetViewProjection()
	{
		Matrix4f cameraRotation = getTransform().GetTransformedRot().copy().Conjugate().ToRotationMatrix();
		Vector4f cameraPos = getTransform().GetTransformedPos().copy().Mul(-1);

		Matrix4f cameraTranslation = new Matrix4f().InitTranslation(cameraPos.GetX(), cameraPos.GetY(), cameraPos.GetZ());

		return m_projection.Mul(cameraRotation.Mul(cameraTranslation));
	}

	/*public Vertex projectOnScreen(Transform meshInstanceTransform, Vertex point){
		Matrix4f mvp = GetViewProjection().Mul(meshInstanceTransform.GetTransformation());
		
		return point.Transform(mvp, meshInstanceTransform.GetTransformation());
	}*/
	
	public void Move(Vector4f dir, float amt)
	{
		getTransform().GetPos().Add(dir.copy().Mul(amt));
	}

	public void Rotate(Vector4f axis, float angle)
	{
		//GetTransform().Rotate(new Quaternion(axis, angle));
		getTransform().Rotate(new Quaternion(axis, angle));
	}
}
