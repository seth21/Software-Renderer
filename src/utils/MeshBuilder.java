package utils;

import java.util.ArrayList;
import java.util.List;

public class MeshBuilder {

	public static Mesh createPlane(float width, Vector4f color){
		
		Mesh plane = new Mesh();
		
		List<Integer> indices = new ArrayList<Integer>();
		List<Vertex> vertices = new ArrayList<Vertex>();
		
		vertices.add(new Vertex(new Vector4f(- width/2, 0, - width/2), color, new Vector4f(0, 0, 0), new Vector4f(Vector4f.Y), new Vector4f()));
		vertices.add(new Vertex(new Vector4f(+ width/2, 0, - width/2), color, new Vector4f(1, 0, 0), new Vector4f(Vector4f.Y), new Vector4f()));
		vertices.add(new Vertex(new Vector4f(+ width/2, 0, + width/2), color, new Vector4f(1, 1, 0), new Vector4f(Vector4f.Y), new Vector4f()));
		vertices.add(new Vertex(new Vector4f(- width/2, 0, + width/2), color, new Vector4f(0, 1, 0), new Vector4f(Vector4f.Y), new Vector4f()));
		
		indices.add(0);
		indices.add(3);
		indices.add(2);
		indices.add(0);
		indices.add(2);
		indices.add(1);
		
		plane.setVertices(vertices);
		plane.setIndices(indices);
		return plane;
	}
	
}
