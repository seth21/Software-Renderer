package utils;

public class Matrix4f
{
	private float[][] m;
	private static Matrix4f tmpMat = new Matrix4f();

	public Matrix4f()
	{
		m = new float[4][4];
	}

	public Matrix4f InitIdentity()
	{
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f InitScreenSpaceTransform(float halfWidth, float halfHeight)
	{
		m[0][0] = halfWidth;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = halfWidth -0.5f;
		m[1][0] = 0;	m[1][1] = -halfHeight;	m[1][2] = 0;	m[1][3] = halfHeight -0.5f;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f InitTranslation(float x, float y, float z)
	{
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = x;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = y;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = z;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f InitRotation(float x, float y, float z, float angle)
	{
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);

		m[0][0] = cos+x*x*(1-cos); m[0][1] = x*y*(1-cos)-z*sin; m[0][2] = x*z*(1-cos)+y*sin; m[0][3] = 0;
		m[1][0] = y*x*(1-cos)+z*sin; m[1][1] = cos+y*y*(1-cos);	m[1][2] = y*z*(1-cos)-x*sin; m[1][3] = 0;
		m[2][0] = z*x*(1-cos)-y*sin; m[2][1] = z*y*(1-cos)+x*sin; m[2][2] = cos+z*z*(1-cos); m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f InitRotation(float x, float y, float z)
	{
		Matrix4f rx = new Matrix4f();
		Matrix4f ry = new Matrix4f();
		Matrix4f rz = new Matrix4f();

		rz.m[0][0] = (float)Math.cos(z);rz.m[0][1] = -(float)Math.sin(z);rz.m[0][2] = 0;				rz.m[0][3] = 0;
		rz.m[1][0] = (float)Math.sin(z);rz.m[1][1] = (float)Math.cos(z);rz.m[1][2] = 0;					rz.m[1][3] = 0;
		rz.m[2][0] = 0;					rz.m[2][1] = 0;					rz.m[2][2] = 1;					rz.m[2][3] = 0;
		rz.m[3][0] = 0;					rz.m[3][1] = 0;					rz.m[3][2] = 0;					rz.m[3][3] = 1;

		rx.m[0][0] = 1;					rx.m[0][1] = 0;					rx.m[0][2] = 0;					rx.m[0][3] = 0;
		rx.m[1][0] = 0;					rx.m[1][1] = (float)Math.cos(x);rx.m[1][2] = -(float)Math.sin(x);rx.m[1][3] = 0;
		rx.m[2][0] = 0;					rx.m[2][1] = (float)Math.sin(x);rx.m[2][2] = (float)Math.cos(x);rx.m[2][3] = 0;
		rx.m[3][0] = 0;					rx.m[3][1] = 0;					rx.m[3][2] = 0;					rx.m[3][3] = 1;

		ry.m[0][0] = (float)Math.cos(y);ry.m[0][1] = 0;					ry.m[0][2] = -(float)Math.sin(y);ry.m[0][3] = 0;
		ry.m[1][0] = 0;					ry.m[1][1] = 1;					ry.m[1][2] = 0;					ry.m[1][3] = 0;
		ry.m[2][0] = (float)Math.sin(y);ry.m[2][1] = 0;					ry.m[2][2] = (float)Math.cos(y);ry.m[2][3] = 0;
		ry.m[3][0] = 0;					ry.m[3][1] = 0;					ry.m[3][2] = 0;					ry.m[3][3] = 1;

		m = rz.Mul(ry.Mul(rx)).GetM();

		return this;
	}

	public Matrix4f InitScale(float x, float y, float z)
	{
		m[0][0] = x;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = y;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = z;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f InitPerspective(float fov, float aspectRatio, float zNear, float zFar)
	{
		float tanHalfFOV = (float)Math.tan(fov / 2);
		float zRange = zNear - zFar;

		m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);	m[0][1] = 0;					m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;						m[1][1] = 1.0f / tanHalfFOV;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;						m[2][1] = 0;					m[2][2] = (-zNear -zFar)/zRange;	m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;						m[3][1] = 0;					m[3][2] = 1;	m[3][3] = 0;


		return this;
	}

	public Matrix4f InitOrthographic(float left, float right, float bottom, float top, float near, float far)
	{
		float width = right - left;
		float height = top - bottom;
		float depth = far - near;

		m[0][0] = 2/width;m[0][1] = 0;	m[0][2] = 0;	m[0][3] = -(right + left)/width;
		m[1][0] = 0;	m[1][1] = 2/height;m[1][2] = 0;	m[1][3] = -(top + bottom)/height;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = -2/depth;m[2][3] = -(far + near)/depth;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f InitRotation(Vector4f forward, Vector4f up)
	{
		Vector4f f = forward.Normalized();

		Vector4f r = up.Normalized();
		r = r.Cross(f);

		Vector4f u = f.Cross(r);

		return InitRotation(f, u, r);
	}

	public Matrix4f InitRotation(Vector4f forward, Vector4f up, Vector4f right)
	{
		Vector4f f = forward;
		Vector4f r = right;
		Vector4f u = up;

		m[0][0] = r.GetX();	m[0][1] = r.GetY();	m[0][2] = r.GetZ();	m[0][3] = 0;
		m[1][0] = u.GetX();	m[1][1] = u.GetY();	m[1][2] = u.GetZ();	m[1][3] = 0;
		m[2][0] = f.GetX();	m[2][1] = f.GetY();	m[2][2] = f.GetZ();	m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;

		return this;
	}

	public Vector4f Transfor(Vector4f r)
	{
		return new Vector4f(m[0][0] * r.GetX() + m[0][1] * r.GetY() + m[0][2] * r.GetZ() + m[0][3] * r.GetW(),
		                    m[1][0] * r.GetX() + m[1][1] * r.GetY() + m[1][2] * r.GetZ() + m[1][3] * r.GetW(),
		                    m[2][0] * r.GetX() + m[2][1] * r.GetY() + m[2][2] * r.GetZ() + m[2][3] * r.GetW(),
							m[3][0] * r.GetX() + m[3][1] * r.GetY() + m[3][2] * r.GetZ() + m[3][3] * r.GetW());
	}

	public Matrix4f Mul(Matrix4f r)
	{
		Matrix4f res = new Matrix4f();

		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				res.Set(i, j, m[i][0] * r.Get(0, j) +
						m[i][1] * r.Get(1, j) +
						m[i][2] * r.Get(2, j) +
						m[i][3] * r.Get(3, j));
			}
		}

		return res;
	}

	public float[][] GetM()
	{
		float[][] res = new float[4][4];

		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				res[i][j] = m[i][j];

		return res;
	}

	public float Get(int x, int y)
	{
		return m[x][y];
	}

	public void SetM(float[][] m)
	{
		this.m = m;
	}

	public void Set(int xRow, int yColumn, float value)
	{
		m[xRow][yColumn] = value;
	}
	
	 public Matrix4f invert(final Matrix4f store) {
	        Matrix4f result = store;
	        if (result == null) {
	            result = new Matrix4f();
	        }

	        final float dA0 = m[0][0] * m[1][1] - m[0][1] * m[1][0];
	        final float dA1 = m[0][0] * m[1][2] - m[0][2] * m[1][0];
	        final float dA2 = m[0][0] * m[1][3] - m[0][3] * m[1][0];
	        final float dA3 = m[0][1] * m[1][2] - m[0][2] * m[1][1];
	        final float dA4 = m[0][1] * m[1][3] - m[0][3] * m[1][1];
	        final float dA5 = m[0][2] * m[1][3] - m[0][3] * m[1][2];
	        final float dB0 = m[2][0] * m[3][1] - m[2][1] * m[3][0];
	        final float dB1 = m[2][0] * m[3][2] - m[2][2] * m[3][0];
	        final float dB2 = m[2][0] * m[3][3] - m[2][3] * m[3][0];
	        final float dB3 = m[2][1] * m[3][2] - m[2][2] * m[3][1];
	        final float dB4 = m[2][1] * m[3][3] - m[2][3] * m[3][1];
	        final float dB5 = m[2][2] * m[3][3] - m[2][3] * m[3][2];
	        final float det = dA0 * dB5 - dA1 * dB4 + dA2 * dB3 + dA3 * dB2 - dA4 * dB1 + dA5 * dB0;

	        if (Math.abs(det) <= 0.001f) {
	            throw new ArithmeticException("This matrix cannot be inverted");
	        }

	        final float temp00 = +m[1][1] * dB5 - m[1][2] * dB4 + m[1][3] * dB3;
	        final float temp10 = -m[1][0] * dB5 + m[1][2] * dB2 - m[1][3] * dB1;
	        final float temp20 = +m[1][0] * dB4 - m[1][1] * dB2 + m[1][3] * dB0;
	        final float temp30 = -m[1][0] * dB3 + m[1][1] * dB1 - m[1][2] * dB0;
	        final float temp01 = -m[0][1] * dB5 + m[0][2] * dB4 - m[0][3] * dB3;
	        final float temp11 = +m[0][0] * dB5 - m[0][2] * dB2 + m[0][3] * dB1;
	        final float temp21 = -m[0][0] * dB4 + m[0][1] * dB2 - m[0][3] * dB0;
	        final float temp31 = +m[0][0] * dB3 - m[0][1] * dB1 + m[0][2] * dB0;
	        final float temp02 = +m[3][1] * dA5 - m[3][2] * dA4 + m[3][3] * dA3;
	        final float temp12 = -m[3][0] * dA5 + m[3][2] * dA2 - m[3][3] * dA1;
	        final float temp22 = +m[3][0] * dA4 - m[3][1] * dA2 + m[3][3] * dA0;
	        final float temp32 = -m[3][0] * dA3 + m[3][1] * dA1 - m[3][2] * dA0;
	        final float temp03 = -m[2][1] * dA5 + m[2][2] * dA4 - m[2][3] * dA3;
	        final float temp13 = +m[2][0] * dA5 - m[2][2] * dA2 + m[2][3] * dA1;
	        final float temp23 = -m[2][0] * dA4 + m[2][1] * dA2 - m[2][3] * dA0;
	        final float temp33 = +m[2][0] * dA3 - m[2][1] * dA1 + m[2][2] * dA0;
	        
	        result.set(temp00, temp01, temp02, temp03, temp10, temp11, temp12, temp13, temp20, temp21, temp22, temp23,
	                temp30, temp31, temp32, temp33);
	        result.multiplyLocal(1.0f / det);

	        return result;
	    }

	   public Matrix4f multiplyLocal(final float scalar) {
	        for (int i = 0; i < 4; i++) {
	            for (int j = 0; j < 4; j++) {
	                m[i][j] *= scalar;
	            }
	        }
	        return this;
	    }
	   
	   public Matrix4f set(final float m00, final float m01, final float m02, final float m03, final float m10,
	            final float m11, final float m12, final float m13, final float m20, final float m21, final float m22,
	            final float m23, final float m30, final float m31, final float m32, final float m33) {

	        m[0][0] = m00;
	        m[0][1] = m01;
	        m[0][2] = m02;
	        m[0][3] = m03;
	        m[1][0] = m10;
	        m[1][1] = m11;
	        m[1][2] = m12;
	        m[1][3] = m13;
	        m[2][0] = m20;
	        m[2][1] = m21;
	        m[2][2] = m22;
	        m[2][3] = m23;
	        m[3][0] = m30;
	        m[3][1] = m31;
	        m[3][2] = m32;
	        m[3][3] = m33;

	        return this;
	    }


	   public Matrix4f(final float m00, final float m01, final float m02, final float m03, final float m10,
	            final float m11, final float m12, final float m13, final float m20, final float m21, final float m22,
	            final float m23, final float m30, final float m31, final float m32, final float m33) {

	       m[0][0] = m00;
	       m[0][1] = m01;
	       m[0][2] = m02;
	       m[0][3] = m03;
	       m[1][0] = m10;
	       m[1][1] = m11;
	       m[1][2] = m12;
	       m[1][3] = m13;
	       m[2][0] = m20;
	       m[2][1] = m21;
	       m[2][2] = m22;
	       m[2][3] = m23;
	       m[3][0] = m30;
	       m[3][1] = m31;
	       m[3][2] = m32;
	       m[3][3] = m33;
	    }
}