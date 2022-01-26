package utils;

public class Transform
{
	private Vector4f   m_pos;
	private Quaternion m_rot;
	private Vector4f   m_scale;

	public Transform()
	{
		this(new Vector4f(0,0,0,0));
	}

	public Transform(Vector4f pos)
	{
		this(pos, new Quaternion(0,0,0,1), new Vector4f(1,1,1,1));
	}

	public Transform(Vector4f pos, Quaternion rot, Vector4f scale)
	{
		m_pos = pos;
		m_rot = rot;
		m_scale = scale;
	}
	
	public void Translate(Vector4f dir)
	{
		m_pos.Add(dir);
	}

	public Transform SetPos(Vector4f pos)
	{
		m_pos.set(pos);
		return this;
	}

	public Transform Rotate(Quaternion rotation)
	{
		m_rot = rotation.copy().Mul(m_rot).Normalize();
		return this;
	}

	public Transform LookAt(Vector4f point, Vector4f up)
	{
		return Rotate(GetLookAtRotation(point, up));
	}

	public Quaternion GetLookAtRotation(Vector4f point, Vector4f up)
	{
		return new Quaternion(new Matrix4f().InitRotation(point.copy().Sub(m_pos).Normalized(), up));
	}

	public Matrix4f GetTransformation()
	{
		Matrix4f translationMatrix = new Matrix4f().InitTranslation(m_pos.GetX(), m_pos.GetY(), m_pos.GetZ());
		Matrix4f rotationMatrix = m_rot.ToRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().InitScale(m_scale.GetX(), m_scale.GetY(), m_scale.GetZ());

		return translationMatrix.Mul(rotationMatrix.Mul(scaleMatrix));
	}

	public Vector4f GetTransformedPos()
	{
		return m_pos;
	}

	public Quaternion GetTransformedRot()
	{
		return m_rot;
	}

	public Vector4f GetPos()
	{
		return m_pos;
	}

	public Quaternion GetRot()
	{
		return m_rot;
	}

	public Vector4f GetScale()
	{
		return m_scale;
	}
}