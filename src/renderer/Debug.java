package renderer;

import utils.Vector4f;

public class Debug {

	public static void Log(String message){
		System.out.println(message);
	}
	
	public static void Log(float message){
		System.out.println(message);
	}

	public static void Log(Vector4f light) {
		System.out.println(light.GetX()+", "+light.GetY()+", "+light.GetZ()+", "+light.GetW());
	}
}
