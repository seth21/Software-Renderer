package renderer;

import java.util.ArrayList;
import java.util.List;

import utils.Math2;
import utils.Vector4f;

public class Environment {

	public Vector4f ambientLightColor;
	private List<Light> lights;
	private List<ShadowLight> shadowLights;

	public Vector4f getAmbientLightColor(){ return ambientLightColor;}
	
	public Environment(){
		ambientLightColor = new Vector4f (0,0,0);
		lights = new ArrayList<Light>();
		shadowLights = new ArrayList<ShadowLight>();
	}
	
	public void add(Light light){
		lights.add(light);
	}
	
	public void remove(Light light){
		lights.remove(light);
	}
	
	public void add(ShadowLight light){
		shadowLights.add(light);
	}
	
	public void remove(ShadowLight light){
		shadowLights.remove(light);
	}
	
	public Vector4f calcVertexLight(Vector4f woorldCoords, Vector4f normals){
		
		Vector4f vertexLight = new Vector4f(ambientLightColor);
		
		for (int i = 0; i < lights.size(); i++){
			vertexLight.Add(lights.get(i).calcVertexLightContrib(woorldCoords, normals));
		}
		Math2.clamp(vertexLight, 0.0f, 1.0f);
		return vertexLight;
	}

	public List<Light> getLights() {
		return lights;
	}
	
	public List<ShadowLight> getShadowLights() {
		return shadowLights;
	}

	public void clearLightDepthBuffers() {
		for (int i = 0; i < shadowLights.size(); i++)
		{
			for (int j = 0; j < shadowLights.get(i).getZBuffer().length; j++){
				shadowLights.get(i).getZBuffer()[j] = Float.MAX_VALUE;
			}
		}
		
	}

}
