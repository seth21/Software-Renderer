package renderer;

import utils.Math2;
import utils.MathUtils;
import utils.Matrix4f;
import utils.Quaternion;
import utils.Vector4f;

public class DirectionalLight extends ShadowLight{

	public Vector4f m_direction;
	public Vector4f m_color;

	
	@Override
	public Vector4f calcVertexLightContrib(Vector4f worldCoords, Vector4f normals) {
		return m_color.copy().Mul(Math2.clamp(normals.Dot(m_direction), 0f, 1f));
	}


	public DirectionalLight(Vector4f direction, Vector4f color){
		super(new Matrix4f().InitPerspective((float) Math.toRadians(120), 
				(float)Display.BUFFER_WIDTH/(float)Display.BUFFER_HEIGHT, 
				0.1f, 60.0f), Display.BUFFER_WIDTH, Display.BUFFER_HEIGHT);
		m_direction = direction;
		m_color = color;

		m_transform.SetPos(new Vector4f(0, 6, -7));
		m_transform.Rotate(new Quaternion(new Vector4f(1, 0, 0), MathUtils.degreesToRadians * 45));
	}
	
	public DirectionalLight(float x, float y, float z, float r, float g, float b){
		this (new Vector4f(x,y,z), new Vector4f(r, g, b));
	}


	
}
