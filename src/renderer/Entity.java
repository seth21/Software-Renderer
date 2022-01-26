package renderer;

import utils.Transform;

public class Entity {

	protected Transform m_transform;
	
	public Entity(){
		this.m_transform = new Transform();
	}
	
	public Transform getTransform()
	{
		return m_transform;
	}
	
}
