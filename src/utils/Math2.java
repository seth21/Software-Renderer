package utils;

public class Math2 {

	public static float getDistance(Vertex v1, Vertex v2){
		float dist2 =  getDistanceSquared(v1, v2);
		return (float) Math.sqrt(dist2);
	}
	
	public static float getDistanceSquared(Vertex v1, Vertex v2){
		return (v2.getX() - v1.getX())*(v2.getX() - v1.getX()) + (v2.getY() - v1.getY())*(v2.getY() - v1.getY()) + (v2.getZ() - v1.getZ())*(v2.getZ() - v1.getZ());
	}
	
	public static int clamp(int value, int min, int max){
		if (value > max) value = max;
		else if (value < min) value = min;
		return value;
	}

	public static float clamp(float value, float min, float max) {
		if (value > max) return max;
		else if (value < min) return min;
		return value;
	}

	public static Vector4f clamp(Vector4f vec, float min, float max) {
		vec.x = clamp(vec.x, min, max);
		vec.y = clamp(vec.y, min, max);
		vec.z = clamp(vec.z, min, max);
		vec.w = clamp(vec.w, min, max);
		return vec;
	}
}
