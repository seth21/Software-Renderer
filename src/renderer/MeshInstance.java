package renderer;

import utils.Mesh;
import utils.Transform;
import utils.Vector4f;
import utils.Vertex;

public class MeshInstance {

	public Transform transform;
	private Bitmap m_texture;
	private Mesh m_mesh;
	private Environment m_env;
	
	public MeshInstance(Mesh mesh){
		this(mesh, new Transform());
	}
	
	public MeshInstance(Mesh mesh, Vector4f pos){
		this(mesh, new Transform(pos));
	}
	
	public MeshInstance(Mesh mesh, Transform transform){
		m_mesh = mesh;
		this.transform = transform;
	}
	
	public MeshInstance setTexture(Bitmap texture){
		m_texture = texture;
		return this;
	}
	
	public MeshInstance setEnvironment(Environment env){ 
		m_env = env; 
		return this;
	}
	
	public Vertex getMeshVertexByIndex(int index){
		return new Vertex(m_mesh.getVector3f(index));
	}
	
	public Vertex getTransformedVertexByIndex(int index){
		return m_mesh.getVector3f(index).TransformPosNormal(transform.GetTransformation(), transform.GetTransformation());
	}
	
	public Vertex transformVertex(Vertex vertex){
		return vertex.TransformPosNormal(transform.GetTransformation(), transform.GetTransformation());
	}
	
	public Environment getEnvironment(){ return m_env; }
	public Mesh getMesh(){ return m_mesh; }
	public Bitmap getTexture(){ return m_texture; }
}
