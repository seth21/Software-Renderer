package renderer;

import utils.Vertex;
import utils.Vector4f;

public class Edge {

	private float m_x;
	private float m_xStep;
	private int m_yStart;
	private int m_yEnd;
	private Vector4f m_color, m_colorStep;
	private Gradients m_grads;
	private float m_texCoordX;
	private float m_texCoordXStep;
	private float m_texCoordY;
	private float m_texCoordYStep;
	private float m_oneOverZ;
	private float m_oneOverZStep;
	private float m_depth;
	private float m_depthStep;
	private Vector4f m_lightAmt, m_lightAmtStep;
	
	private final static Vector4f tmpVec = new Vector4f();
	private final static Vector4f tmpVec2 = new Vector4f();
	
	public Gradients getGradients(){
		return m_grads;
	}
	public float getTexCoordX(){ return m_texCoordX; }
	public float getTexCoordY(){ return m_texCoordY; }
	public float getTexCoordXStep(){ return m_texCoordXStep; }
	public float getTexCoordYStep(){ return m_texCoordYStep; }
	public float getOneOverZ() { return m_oneOverZ;}
	public float getOneOverZStep() { return m_oneOverZStep;}
	public float getDepth(){ return m_depth; }
	public Vector4f getLightAmt(){ return m_lightAmt; }
	
	public Edge(Gradients grads, Vertex minYVert, Vertex maxYVert, int minYVertIndex){
		m_grads = grads;
		m_yStart = (int)Math.ceil(minYVert.getY());
		m_yEnd = (int)Math.ceil(maxYVert.getY());

		float yDist = maxYVert.getY() - minYVert.getY();
		float xDist = maxYVert.getX() - minYVert.getX();

		float yPrestep = m_yStart - minYVert.getY();
		m_xStep = (float)xDist/(float)yDist;
		m_x = minYVert.getX() + yPrestep * m_xStep;
		
		float xPrestep = m_x - minYVert.getX();
		
		m_color = new Vector4f(grads.getColor(minYVertIndex)).Add(
				tmpVec.set(grads.getColorYStep()).Mul(yPrestep)).Add(
				tmpVec2.set(grads.getColorXStep()).Mul(xPrestep));
		m_colorStep = grads.getColorYStep().copy().Add(tmpVec2.set(grads.getColorXStep()).Mul(m_xStep));
		
		m_texCoordX = grads.getTexCoordX(minYVertIndex) +
			grads.getTexCoordXXStep() * xPrestep +
			grads.getTexCoordXYStep() * yPrestep;
		m_texCoordXStep = grads.getTexCoordXYStep() + grads.getTexCoordXXStep() * m_xStep;

		m_texCoordY = grads.getTexCoordY(minYVertIndex) +
			grads.getTexCoordYXStep() * xPrestep +
			grads.getTexCoordYYStep() * yPrestep;
		m_texCoordYStep = grads.getTexCoordYYStep() + grads.getTexCoordYXStep() * m_xStep;
		
		
		m_oneOverZ = grads.getOneOverZ(minYVertIndex) +
				grads.getOneOverZXStep() * xPrestep +
				grads.getOneOverZYStep() * yPrestep;
		m_oneOverZStep = grads.getOneOverZYStep() + grads.getOneOverZXStep() * m_xStep;
		
		m_depth = grads.getDepth(minYVertIndex) +
				grads.getDepthXStep() * xPrestep +
				grads.getDepthYStep() * yPrestep;
		m_depthStep = grads.getDepthYStep() + grads.getDepthXStep() * m_xStep;
		
		m_lightAmt = new Vector4f(grads.getLightAmt(minYVertIndex)).Add(
				tmpVec.set(grads.getLightAmtXStep()).Mul(xPrestep)).Add(
				tmpVec2.set(grads.getLightAmtYStep()).Mul(yPrestep));
		m_lightAmtStep = grads.getLightAmtYStep().copy().Add(tmpVec2.set(grads.getLightAmtXStep()).Mul(m_xStep));
	}
	
	public void Step(){
		m_x = m_x + m_xStep;
		m_color.Add(m_colorStep);
		m_texCoordX += m_texCoordXStep;
		m_texCoordY += m_texCoordYStep;
		m_oneOverZ += m_oneOverZStep;
		m_depth += m_depthStep;
		m_lightAmt.Add(m_lightAmtStep);
	}

	public int getYStart() {
		return m_yStart;
	}

	public int getYEnd() {
		return m_yEnd;
	}

	public float getX() {
		return m_x;
	}

	public Vector4f getColor(){ return m_color; };
	
}


