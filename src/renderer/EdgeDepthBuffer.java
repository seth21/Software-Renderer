package renderer;

import utils.Vertex;

public class EdgeDepthBuffer {

	private float m_x;
	private float m_xStep;
	private int m_yStart;
	private int m_yEnd;
	private GradientsDepthBuffer m_grads;
	private float m_oneOverZ;
	private float m_oneOverZStep;
	private float m_depth;
	private float m_depthStep;
	
	
	public GradientsDepthBuffer getGradients(){
		return m_grads;
	}
	
	public float getOneOverZ() { return m_oneOverZ;}
	public float getOneOverZStep() { return m_oneOverZStep;}
	public float getDepth(){ return m_depth; }
	
	public EdgeDepthBuffer(GradientsDepthBuffer grads, Vertex minYVert, Vertex maxYVert, int minYVertIndex){
		m_grads = grads;
		m_yStart = (int)Math.ceil(minYVert.getY());
		m_yEnd = (int)Math.ceil(maxYVert.getY());

		float yDist = maxYVert.getY() - minYVert.getY();
		float xDist = maxYVert.getX() - minYVert.getX();

		float yPrestep = m_yStart - minYVert.getY();
		m_xStep = (float)xDist/(float)yDist;
		m_x = minYVert.getX() + yPrestep * m_xStep;
		
		float xPrestep = m_x - minYVert.getX();
		
		
		m_oneOverZ = grads.getOneOverZ(minYVertIndex) +
				grads.getOneOverZXStep() * xPrestep +
				grads.getOneOverZYStep() * yPrestep;
		m_oneOverZStep = grads.getOneOverZYStep() + grads.getOneOverZXStep() * m_xStep;
		
		m_depth = grads.getDepth(minYVertIndex) +
				grads.getDepthXStep() * xPrestep +
				grads.getDepthYStep() * yPrestep;
		m_depthStep = grads.getDepthYStep() + grads.getDepthXStep() * m_xStep;
		
	}
	
	public void Step(){
		m_x = m_x + m_xStep;
		
		m_oneOverZ += m_oneOverZStep;
		m_depth += m_depthStep;
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

	
}


