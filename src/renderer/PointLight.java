package renderer;

import utils.Math2;
import utils.Vector4f;

public class PointLight extends Entity implements Light{
	
	private Vector4f m_pos;
	private Vector4f m_color;
	private float maxDistance;
	
	public PointLight(Vector4f pos, Vector4f color, float maxDistance){
		m_pos = new Vector4f(pos);
		m_color = new Vector4f(color);
		this.maxDistance = maxDistance;
	}
	
	@Override
	public Vector4f calcVertexLightContrib(Vector4f worldCoords, Vector4f normals) {
		
		Vector4f rayVector = m_pos.copy().Sub(worldCoords);
		float rayLength = rayVector.Length();
		//float maxDistance2 = maxDistance * maxDistance;
		float strength = (rayLength < maxDistance) ? ((maxDistance - rayLength)/maxDistance) : 0.0f;
		float dot = Math2.clamp(normals.Dot(rayVector), 0f, 1f);
		return m_color.copy().Mul(dot * strength);

	}
	
	public Vector4f getPos(){ return m_pos; };
	public Vector4f getColor(){return m_color;}

	@Override
	public boolean isShadowEnabled() {
		return false;
	}
}
