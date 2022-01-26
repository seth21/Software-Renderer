package renderer;

import utils.Vector4f;
import utils.Vertex;

public class Gradients {
	
	private Vector4f[] m_color;
	private float[] m_texCoordX;
	private float[] m_texCoordY;
	private float[] m_oneOverZ;
	private float[] m_depth;
	private Vector4f[] m_lightAmt;
	
	private Vector4f m_colorXStep;
	private Vector4f m_colorYStep;
	private float m_texCoordXXStep;
	private float m_texCoordXYStep;
	private float m_texCoordYXStep;
	private float m_texCoordYYStep;
	private float m_oneOverZXStep;
	private float m_oneOverZYStep;
	private float m_depthXStep;
	private float m_depthYStep;
	private Vector4f m_lightAmtXStep;
	private Vector4f m_lightAmtYStep;
	
	private final static Vector4f tmpVec = new Vector4f();
	private final static Vector4f tmpVec2 = new Vector4f();
	
	public Vector4f getColor(int loc){ return m_color[loc]; }
	public float getTexCoordX(int loc){ return m_texCoordX[loc];}
	public float getTexCoordY(int loc){ return m_texCoordY[loc];}
	public float getOneOverZ(int loc){ return m_oneOverZ[loc];}
	public float getDepth(int loc){ return m_depth[loc];}
	public Vector4f getLightAmt(int loc){ return m_lightAmt[loc]; }
	
	public Vector4f getColorXStep(){ return m_colorXStep; }
	public Vector4f getColorYStep(){ return m_colorYStep; }
	public float getTexCoordXXStep(){ return m_texCoordXXStep;}
	public float getTexCoordXYStep(){ return m_texCoordXYStep;}
	public float getTexCoordYXStep(){ return m_texCoordYXStep;}
	public float getTexCoordYYStep(){ return m_texCoordYYStep;}
	public float getOneOverZXStep(){ return m_oneOverZXStep;}
	public float getOneOverZYStep(){ return m_oneOverZYStep;}
	public float getDepthXStep() { return m_depthXStep;}
	public float getDepthYStep() { return m_depthYStep;}
	public Vector4f getLightAmtXStep() { return m_lightAmtXStep;}
	public Vector4f getLightAmtYStep() { return m_lightAmtYStep;}
	
	public Gradients(Vertex minYVert, Vertex midYVert, Vertex maxYVert){
		m_color = new Vector4f[3];
		m_color[0] = minYVert.getColor();
		m_color[1] = midYVert.getColor();
		m_color[2] = maxYVert.getColor();
		m_texCoordX = new float[3];
		m_texCoordY = new float[3];
		m_oneOverZ = new float[3];
		m_depth = new float[3];
		m_lightAmt = new Vector4f[3];

		// Note that the W component is the perspective Z value;
		// The Z component is the occlusion Z value
		m_oneOverZ[0] = 1.0f/minYVert.getPosition().GetW();
		m_oneOverZ[1] = 1.0f/midYVert.getPosition().GetW();
		m_oneOverZ[2] = 1.0f/maxYVert.getPosition().GetW();
		
		m_texCoordX[0] = minYVert.getTexCoords().GetX() * m_oneOverZ[0];
		m_texCoordX[1] = midYVert.getTexCoords().GetX() * m_oneOverZ[1];
		m_texCoordX[2] = maxYVert.getTexCoords().GetX() * m_oneOverZ[2];

		m_texCoordY[0] = minYVert.getTexCoords().GetY() * m_oneOverZ[0];
		m_texCoordY[1] = midYVert.getTexCoords().GetY() * m_oneOverZ[1];
		m_texCoordY[2] = maxYVert.getTexCoords().GetY() * m_oneOverZ[2];
		
		m_depth[0] = minYVert.getPosition().GetZ();
		m_depth[1] = midYVert.getPosition().GetZ();
		m_depth[2] = maxYVert.getPosition().GetZ();
		

		m_lightAmt[0] = minYVert.getLight();
		m_lightAmt[1] = midYVert.getLight();
		m_lightAmt[2] = maxYVert.getLight();
		
		float oneOverdX = 1.0f/
				((midYVert.getX() - maxYVert.getX()) * (minYVert.getY() - maxYVert.getY()) - 
				(minYVert.getX() - maxYVert.getX()) * (midYVert.getY() - maxYVert.getY()));
		
		float oneOverdY = -oneOverdX;
		
		Vector4f dColorX = (tmpVec.set(m_color[1]).Sub(m_color[2]).Mul(minYVert.getY() - maxYVert.getY())).Sub(
				tmpVec2.set(m_color[0]).Sub(m_color[2]).Mul(midYVert.getY() - maxYVert.getY()));
		m_colorXStep = new Vector4f(dColorX).Mul(oneOverdX);
		
		Vector4f dColorY = (tmpVec.set(m_color[1]).Sub(m_color[2]).Mul(minYVert.getX() - maxYVert.getX())).Sub(
				tmpVec2.set(m_color[0]).Sub(m_color[2]).Mul(midYVert.getX() - maxYVert.getX()));
		m_colorYStep = new Vector4f(dColorY).Mul(oneOverdY);
		
		m_texCoordXXStep = CalcXStep(m_texCoordX, minYVert, midYVert, maxYVert, oneOverdX);
		m_texCoordXYStep = CalcYStep(m_texCoordX, minYVert, midYVert, maxYVert, oneOverdY);
		m_texCoordYXStep = CalcXStep(m_texCoordY, minYVert, midYVert, maxYVert, oneOverdX);
		m_texCoordYYStep = CalcYStep(m_texCoordY, minYVert, midYVert, maxYVert, oneOverdY);
		m_oneOverZXStep = CalcXStep(m_oneOverZ, minYVert, midYVert, maxYVert, oneOverdX);
		m_oneOverZYStep = CalcYStep(m_oneOverZ, minYVert, midYVert, maxYVert, oneOverdY);
		m_depthXStep = CalcXStep(m_depth, minYVert, midYVert, maxYVert, oneOverdX);
		m_depthYStep = CalcYStep(m_depth, minYVert, midYVert, maxYVert, oneOverdY);
		m_lightAmtXStep = CalcXStep(m_lightAmt, minYVert, midYVert, maxYVert, oneOverdX);
		m_lightAmtYStep = CalcYStep(m_lightAmt, minYVert, midYVert, maxYVert, oneOverdY);
	}
	

	private Vector4f CalcXStep(Vector4f[] values, Vertex minYVert, Vertex midYVert,
			Vertex maxYVert, float oneOverdX){

		Vector4f dInterpolantX = (tmpVec.set(values[1]).Sub(values[2]).Mul(minYVert.getY() - maxYVert.getY())).Sub(
				tmpVec2.set(values[0]).Sub(values[2]).Mul(midYVert.getY() - maxYVert.getY()));
		return new Vector4f(dInterpolantX).Mul(oneOverdX);
	}
	
	private Vector4f CalcYStep(Vector4f[] values, Vertex minYVert, Vertex midYVert,
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
