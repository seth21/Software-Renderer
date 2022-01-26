package renderer;

import utils.Matrix4f;
import utils.Transform;
import utils.Vector4f;
import utils.Vertex;

public abstract class Observer extends Entity{

	protected Matrix4f m_projection;
	protected int m_viewportWidth, m_viewportHeight;
	protected float[] m_zBuffer;
	
	public Observer(Matrix4f projection, int viewportWidth, int viewportHeight)
	{
		this.m_projection = projection;
		m_zBuffer = new float[viewportWidth * viewportHeight];
		m_viewportHeight = viewportHeight;
		m_viewportWidth = viewportWidth;
	}

	public Matrix4f getViewProjection()
	{
		Matrix4f cameraRotation = m_transform.GetTransformedRot().copy().Conjugate().ToRotationMatrix();
		Vector4f cameraPos = m_transform.GetTransformedPos().copy().Mul(-1);

		Matrix4f cameraTranslation = new Matrix4f().InitTranslation(cameraPos.GetX(), cameraPos.GetY(), cameraPos.GetZ());

		return m_projection.Mul(cameraRotation.Mul(cameraTranslation));
	}

	public Vertex transformAndProjectOnScreen(Transform meshInstanceTransform, Vertex point){
		Matrix4f mvp = getViewProjection().Mul(meshInstanceTransform.GetTransformation());
		return point.TransformPosNormal(mvp, meshInstanceTransform.GetTransformation());
	}
	
	public Vertex projectOnScreen(Vertex point){
		Matrix4f mvp = getViewProjection();
		return point.TransformPosOnly(mvp);
	}
	
	private final Matrix4f screenSpaceTransform = new Matrix4f().InitScreenSpaceTransform(Display.BUFFER_WIDTH/2, Display.BUFFER_HEIGHT/2);
	private final Matrix4f inverseScreenSpaceTransform = screenSpaceTransform.invert(null);
	private final Matrix4f identity = new Matrix4f().InitIdentity();
	
	public Vertex unprojectFromScreen(Vertex point){
		//Matrix4f screenSpaceTransform = new Matrix4f().InitScreenSpaceTransform(Display.BUFFER_WIDTH/2, Display.BUFFER_HEIGHT/2);

		point = point.PerspectiveMul().TransformPosNormal(inverseScreenSpaceTransform, identity);

		Matrix4f mvp = getViewProjection().invert(null);
		return point.TransformPosOnly(mvp);
	}
	
	public float[] getZBuffer() {
		return m_zBuffer;
	}

	public int getViewportWidth() {
		return m_viewportWidth;
	}

	public void setViewportWidth(int viewportWidth) {
		m_viewportWidth = viewportWidth;
	}

	public int getViewportHeight() {
		return m_viewportHeight;
	}

	public void setViewportHeight(int m_viewportHeight) {
		this.m_viewportHeight = m_viewportHeight;
	}
	
	public void clearDepthBuffer(){
		for (int i = 0; i < m_zBuffer.length; i++){
			m_zBuffer[i] = Float.MAX_VALUE;
		}
	}
}
