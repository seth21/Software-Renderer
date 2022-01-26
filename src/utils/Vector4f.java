package utils;

public class Vector4f
{
	protected float x;
	protected float y;
	protected float z;
	protected float w;
	
	public final static Vector4f X = new Vector4f(1, 0, 0);
	public final static Vector4f Y = new Vector4f(0, 1, 0);
	public final static Vector4f Z = new Vector4f(0, 0, 1);
	public final static Vector4f Zero = new Vector4f(0, 0, 0);

	public Vector4f(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f(float x, float y, float z)
	{
		this(x, y, z, 1.0f);
	}
	
	public Vector4f()
	{
		this.w = 1.0f;
	}
	
	public Vector4f (final Vector4f vector) {
		this.set(vector);
	}
	
	public Vector4f (final float[] values) {
		this.set(values[0], values[1], values[2]);
	}

	public Vector4f set (float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}
	
	public Vector4f set (float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vector4f set (final Vector4f vector) {
		return this.set(vector.x, vector.y, vector.z, vector.w);
	}

	public Vector4f set (final float[] values) {
		return this.set(values[0], values[1], values[2]);
	}
	
	public Vector4f copy () {
		return new Vector4f(this);
	}
	
	public Vector4f mulAdd (Vector4f vec, float scalar) {
		this.x += vec.x * scalar;
		this.y += vec.y * scalar;
		this.z += vec.z * scalar;
		this.w += vec.w * scalar;
		return this;
	}

	
	public Vector4f mulAdd (Vector4f vec, Vector4f mulVec) {
		this.x += vec.x * mulVec.x;
		this.y += vec.y * mulVec.y;
		this.z += vec.z * mulVec.z;
		this.w += vec.w * mulVec.w;
		return this;
	}
	
	public float dst (final Vector4f vector) {
		final float a = vector.x - x;
		final float b = vector.y - y;
		final float c = vector.z - z;
		final float d = vector.w - w;
		return (float)Math.sqrt(a * a + b * b + c * c + d * d);
	}
	
	public float dst2 (final Vector4f point) {
		final float a = point.x - x;
		final float b = point.y - y;
		final float c = point.z - z;
		final float d = point.w - w;
		return a * a + b * b + c * c + d * d;
	}

	public float Length()
	{
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public float Length2()
	{
		return x * x + y * y + z * z + w * w;
	}

	public float Max()
	{
		return Math.max(Math.max(x, y), Math.max(z, w));
	}

	public float Dot(Vector4f r)
	{
		return x * r.GetX() + y * r.GetY() + z * r.GetZ() + w * r.GetW();
	}

	public Vector4f Cross(Vector4f r)
	{
		float x_ = y * r.GetZ() - z * r.GetY();
		float y_ = z * r.GetX() - x * r.GetZ();
		float z_ = x * r.GetY() - y * r.GetX();

		return new Vector4f(x_, y_, z_, 0);
	}

	public Vector4f Normalized()
	{
		float length = Length();

		return new Vector4f(x / length, y / length, z / length, w / length);
		
	}

	public Vector4f Rotate(Vector4f axis, float angle)
	{
		float sinAngle = (float)Math.sin(-angle);
		float cosAngle = (float)Math.cos(-angle);

		return this.Cross(axis.copy().Mul(sinAngle)).Add(           //Rotation on local X
				(this.copy().Mul(cosAngle)).Add(                     //Rotation on local Z
						axis.copy().Mul(this.copy().Dot(axis.Mul(1 - cosAngle))))); //Rotation on local Y
	}

	public Vector4f Rotate(Quaternion rotation)
	{
		Quaternion conjugate = rotation.copy().Conjugate();

		Quaternion w = rotation.copy().Mul(this).Mul(conjugate);

		return new Vector4f(w.GetX(), w.GetY(), w.GetZ(), 1.0f);
	}

	public Vector4f Lerp(Vector4f dest, float lerpFactor)
	{
		return dest.copy().Sub(this).Mul(lerpFactor).Add(this);
	}

	public Vector4f Add(Vector4f r)
	{
		//return new Vector4f(x + r.GetX(), y + r.GetY(), z + r.GetZ(), w + r.GetW());
		return this.set(x + r.GetX(), y + r.GetY(), z + r.GetZ(), w + r.GetW());
	}

	public Vector4f Add(float r)
	{
		//return new Vector4f(x + r, y + r, z + r, w + r);
		return this.set(x + r, y + r, z + r, w + r);
	}

	public Vector4f Sub(Vector4f r)
	{
		//return new Vector4f(x - r.GetX(), y - r.GetY(), z - r.GetZ(), w - r.GetW());
		return this.set(x - r.GetX(), y - r.GetY(), z - r.GetZ(), w - r.GetW());
	}

	public Vector4f Sub(float r)
	{
		return this.set(x - r, y - r, z - r, w - r);
	}

	public Vector4f Mul(Vector4f r)
	{
		return this.set(x * r.GetX(), y * r.GetY(), z * r.GetZ(), w * r.GetW());
	}

	public Vector4f Mul(float r)
	{
		return this.set(x * r, y * r, z * r, w * r);
	}

	public Vector4f Div(Vector4f r)
	{
		return this.set(x / r.GetX(), y / r.GetY(), z / r.GetZ(), w / r.GetW());
	}

	public Vector4f Div(float r)
	{
		return this.set(x / r, y / r, z / r, w / r);
	}

	public Vector4f Abs()
	{
		return this.set(Math.abs(x), Math.abs(y), Math.abs(z), Math.abs(w));
	}


	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}

	public float GetX()
	{
		return x;
	}

	public float GetY()
	{
		return y;
	}

	public float GetZ()
	{
		return z;
	}

	public float GetW()
	{
		return w;
	}

	public boolean equals(Vector4f r)
	{
		return x == r.GetX() && y == r.GetY() && z == r.GetZ() && w == r.GetW();
	}
}