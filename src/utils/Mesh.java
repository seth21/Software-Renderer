package utils;

import java.util.ArrayList;
import java.util.List;


public class Mesh {

	private List<Vertex> m_vertices;
	private List<Integer> m_indices;
	
	public Vertex getVector3f(int i){ return m_vertices.get(i); }
	public int getIndex(int i){ return m_indices.get(i); }
	public int getNumIndices() { return m_indices.size(); }
	
	public Mesh(String filename) throws Exception{
		IndexedModel model = new OBJModel(filename).ToIndexedModel();
		
		m_vertices = new ArrayList<Vertex>();
		for(int i = 0; i < model.GetPositions().size(); i++){
			m_vertices.add(new Vertex(model.GetPositions().get(i),
					Color.WHITE,
					model.GetTexCoords().get(i),
					model.GetNormals().get(i), 
					new Vector4f(0,0,0)));
		}
		
		m_indices = model.GetIndices();

	}
	
	public Mesh(){
		
	}
	
	public void setIndices(List<Integer> indices){
		m_indices = indices;
	}
	
	public void setVertices(List<Vertex> vertices){
		m_vertices = vertices;
	}
}
