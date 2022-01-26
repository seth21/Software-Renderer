package renderer;

import utils.Matrix4f;

public abstract class ShadowLight extends Observer implements Light{


	public ShadowLight(Matrix4f projection, int viewportWidth, int viewportHeight) {
		super(projection, viewportWidth, viewportHeight);
	}

	@Override
	public boolean isShadowEnabled() {
		return true;
	}
	
}
