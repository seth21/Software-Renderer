package renderer;

import utils.Vector4f;

public interface Light {

	public abstract Vector4f calcVertexLightContrib(Vector4f woorldCoords, Vector4f normals);
	
	public abstract boolean isShadowEnabled();
	
	
	
}
