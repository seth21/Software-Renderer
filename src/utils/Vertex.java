package utils;

public class Vertex {

	private Vector4f m_pos;
	private Vector4f m_color;
	private Vector4f m_texCoords;
	private Vector4f m_normal;
	public  Vector4f m_light;

	public Vector4f getColor(){ return m_color;}
	public Vector4f getTexCoords(){ return m_texCoords;}
	public Vector4f getNormal() { return m_normal; }
	public Vector4f getLight(){ return m_light;}

	public Vertex set(Vector4f pos, Vector4f color, Vector4f texCoords, Vector4f normal, Vector4f light){
		m_pos = pos;
		m_color = color;
		m_texCoords = texCoords;
		m_normal = normal;
		m_light = light;
		return this;
	}
	
	public Vertex set(Vertex vertex){
		m_pos = new Vector4f(vertex.m_pos);
		m_color = new Vector4f(vertex.m_color);
		m_texCoords = new Vector4f(vertex.m_texCoords);
		m_normal = new Vector4f(vertex.m_normal);
		m_light = new Vector4f(vertex.m_light);
		return this;
	}
	
	public Vertex(Vertex other){
		set(other);
	}

	public Vertex(Vector4f pos, Vector4f color, Vector4f texCoords, Vector4f normal, Vector4f light){
		m_pos = pos;
		m_color = color;
		m_texCoords = texCoords;
		m_normal = normal;
		m_light = light;
	}
	/*public Vector3f Transform(Matrix4f transform){
		return new Vector3f(transform.Transform(m_pos), m_color, m_texCoords, m_normal);
	}*/
	
	public Vertex TransformPosNormal(Matrix4f transform, Matrix4f normalTransform)
	{
		// The normalized here is important if you're doing scaling.
		return new Vertex(transform.Transfor(m_pos), m_color, m_texCoords, 
				normalTransform.Transfor(m_normal).Normalized(), m_light);
	}

	
	public Vertex TransformPosOnly(Matrix4f transform)
	{
		// The normalized here is important if you're doing scaling.
		return new Vertex(transform.Transfor(m_pos), m_color, m_texCoords, 
				m_normal, m_light);
	}

	public float TriangleAreaTimesTwo(Vertex b, Vertex c){
		float x1 = b.getX() - getX();
		float y1 = b.getY() - getY();
		
		float x2 = c.getX() - getX();
		float y2 = c.getY() - getY();
		
		return (x1 * y2 - x2 * y1);
	}

	public Vertex PerspectiveDivide(){
		return new Vertex(new Vector4f(m_pos.GetX()/m_pos.GetW(), 
				m_pos.GetY()/m_pos.GetW(), m_pos.GetZ()/m_pos.GetW(), 
				m_pos.GetW()), 
				m_color, m_texCoords, m_normal, m_light);
	}
	
	public Vertex PerspectiveMul(){
		return new Vertex(new Vector4f(m_pos.GetX()*m_pos.GetW(), 
				m_pos.GetY()*m_pos.GetW(), m_pos.GetZ()*m_pos.GetW(), 
				m_pos.GetW()), 
				m_color, m_texCoords, m_normal, m_light);
	}
	
	public float getX() {
		return m_pos.GetX();
	}

	public void setX(float x) {
		//m_pos.se = x;
	}

	public float getY() {
		return m_pos.GetY();
	}

	public void setY(float y) {
		//this.y = y;
	}

	public float getZ() {
		return m_pos.GetZ();
	}

	public void setZ(float z) {
		//this.z = z;
	}
	
	public Vector4f getPosition(){
		return m_pos;
	}
	
	public Vertex lerp(Vertex other, float lerpAmt){
		return new Vertex(m_pos.Lerp(other.getPosition(), lerpAmt),
				m_color.Lerp(other.getColor(), lerpAmt),
				m_texCoords.Lerp(other.getTexCoords(), lerpAmt), 
				m_normal.Lerp(other.getNormal(), lerpAmt), 
				m_light.Lerp(other.getLight(), lerpAmt));
	}
	
	public float get(int index){
		switch(index){
		case 0:
			return m_pos.GetX();
		case 1:
			return m_pos.GetY();
		case 2:
			return m_pos.GetZ();
		case 3:
			return m_pos.GetW();
		default:
			throw new IndexOutOfBoundsException();
		}
	}
	
	public boolean isInsideViewFrustum(){
		return Math.abs(m_pos.GetX()) <= Math.abs(m_pos.GetW()) &&
				Math.abs(m_pos.GetY()) <= Math.abs(m_pos.GetW()) &&
				Math.abs(m_pos.GetZ()) <= Math.abs(m_pos.GetW());
	}
}
