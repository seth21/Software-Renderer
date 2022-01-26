package renderer;

import utils.Vector4f;
import utils.Vertex;

public class GradientsDepthBuffer {
	
	private float[] m_oneOverZ;
	private float[] m_depth;
	
	private float m_depthXStep;
	private float m_depthYStep;
	private float m_oneOverZXStep;
	private float m_oneOverZYStep;
	
	private final static Vector4f tmpVec = new Vector4f();
	private final static Vector4f tmpVec2 = new Vector4f();
	
	public float getOneOverZ(int loc){ return m_oneOverZ[loc];}
	public float getDepth(int loc){ return m_depth[loc];}
	public float getDepthXStep() { return m_depthXStep;}
	public float getDepthYStep() { return m_depthYStep;}
	public float getOneOverZXStep(){ return m_oneOverZXStep;}
	public float getOneOverZYStep(){ return m_oneOverZYStep;}
	
	public GradientsDepthBuffer(Vertex minYVert, Vertex midYVert, Vertex maxYVert){
		
		m_oneOverZ = new float[3];
		m_depth = new float[3];
		
		// Note that the W component is the perspective Z value;
		// The Z component is the occlusion Z value
		m_oneOverZ[0] = 1.0f/minYVert.getPosition().GetW();
		m_oneOverZ[1] = 1.0f/midYVert.getPosition().GetW();
		m_oneOverZ[2] = 1.0f/maxYVert.getPosition().GetW();
		
		m_depth[0] = minYVert.getPosition().GetZ();
		m_depth[1] = midYVert.getPosition().GetZ();
		m_depth[2] = maxYVert.getPosition().GetZ();
		
		float oneOverdX = 1.0f/
				((midYVert.getX() - maxYVert.getX()) * (minYVert.getY() - maxYVert.getY()) - 
				(minYVert.getX() - maxYVert.getX()) * (midYVert.getY() - maxYVert.getY()));
		
		float oneOverdY = -oneOverdX;
		
		m_depthXStep = CalcXStep(m_depth, minYVert, midYVert, maxYVert, oneOverdX);
		m_depthYStep = CalcYStep(m_depth, minYVert, midYVert, maxYVert, oneOverdY);
		m_oneOverZXStep = CalcXStep(m_oneOverZ, minYVert, midYVert, maxYVert, oneOverdX);
		m_oneOverZYStep = CalcYStep(m_oneOverZ, minYVert, midYVert, maxYVert, oneOverdY);
	}
	

	protected Vector4f CalcXStep(Vector4f[] values, Vertex minYVert, Vertex midYVert,
			Vertex maxYVert, float oneOverdX){

		Vector4f dInterpolantX = (tmpVec.set(values[1]).Sub(values[2]).Mul(minYVert.getY() - maxYVert.getY())).Sub(
				tmpVec2.set(values[0]).Sub(values[2]).Mul(midYVert.getY() - maxYVert.getY()));
		return new Vector4f(dInterpolantX).Mul(oneOverdX);
	}
	
	protected Vector4f CalcYStep(Vector4f[] values, Vertex minYVert, Vertex midYVert,
			Vertex maxYVert, float oneOverdY){
		Vector4f dInterpolantY = (tmpVec.set(values[1]).Sub(values[2]).Mul(minYVert.getX() - maxYVert.getX())).Sub(
				tmpVec2.set(values[0]).Sub(values[2]).Mul(midYVert.getX() - maxYVert.getX()));
		return new Vector4f(dInterpolantY).Mul(oneOverdY);
	}

	private float CalcXStep(float[] values, Vertex minYVert, Vertex midYVert,
			Vertex maxYVert, float oneOverdX)
	{
		return
			(((values[1] - values[2]) *
			(minYVert.getY() - maxYVert.getY())) -
			((values[0] - values[2]) *
			(midYVert.getY() - maxYVert.getY()))) * oneOverdX;
	}

	private float CalcYStep(float[] values, Vertex minYVert, Vertex midYVert,
			Vertex maxYVert, float oneOverdY)
	{
		return
			(((values[1] - values[2]) *
			(minYVert.getX() - maxYVert.getX())) -
			((values[0] - values[2]) *
			(midYVert.getX() - maxYVert.getX()))) * oneOverdY;
	}
}
