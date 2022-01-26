package utils;

public class Color extends Vector4f{

	public final static Vector4f WHITE = new Color(1, 1, 1, 1);
	public final static Vector4f RED = new Color(1, 0, 0, 1);
	public final static Vector4f GREEN = new Color(0, 1, 0, 1);
	public final static Vector4f BLUE = new Color(0, 0, 1, 1);
	

	public Color(float r, float g, float b, float a){
		super(r, g, b, a);
		this.x = Math2.clamp(r, 0f , 1.0f);
		this.y = Math2.clamp(g, 0f , 1.0f);
		this.z = Math2.clamp(b, 0f , 1.0f);
		this.w = Math2.clamp(a, 0f , 1.0f);

	}
	
	public float getR(){ return x; }
	public float getG(){ return y; }
	public float getB(){ return z; }
	public float getA(){ return w; }
}
