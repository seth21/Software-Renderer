/**
 * 
 */
package renderer;

import utils.Color;
import utils.MathUtils;
import utils.Mesh;
import utils.MeshBuilder;
import utils.Quaternion;
import utils.Vertex;
import utils.Vector4f;

/**
 * @author Seth
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.init();
	}
	
	public void init() throws Exception{
		Debug.Log("test");
		Display display = new Display(Display.FRAME_WIDTH, Display.FRAME_HEIGHT, "Hello World!");
		RenderContext target = display.getFrameBuffer();
		

		long previousTime = System.nanoTime();
		float fpsUpdate = 0;
		int fpsCounter = 0;
		
		
		Bitmap texture = Bitmap.createBWNoise(64, 64);
		Bitmap colortexture = Bitmap.createColorNoise(32, 32);
		
		//Environment
		Environment env = new Environment();
		env.ambientLightColor = new Vector4f(0.04f, 0.04f, 0.04f);
		env.add(new DirectionalLight(0, .2f, .7f, 0.7f, 0.7f, 0.7f));
		//PointLight light1 = new PointLight(new Vector4f(0.0f, 2f, -3f), new Vector4f(0.9f, 0.7f, 0f), 5);
		//PointLight light2 = new PointLight(new Vector4f(-4.0f, 2f, 3f), new Vector4f(0.9f, 0.2f, 0.9f), 5);
		//PointLight light3 = new PointLight(new Vector4f(5f, 1f, 5f), new Vector4f(0.1f, 0.7f, 0f), 5);
		//env.add(light1);
		//env.add(light2);
		//env.add(light3);
		
		//mesh
		Mesh meshMonkey = new Mesh("./res/smoothMonkey0.obj");
		MeshInstance monkey = new MeshInstance(meshMonkey, new Vector4f(0,2,3.0f)).setTexture(colortexture).setEnvironment(env);
		MeshInstance monkey2 = new MeshInstance(meshMonkey, new Vector4f(0,2,-3.0f)).setTexture(colortexture).setEnvironment(env);
		//mesh
		Mesh meshTerrain = new Mesh("./res/terrain.obj");
		MeshInstance terrain = new MeshInstance(meshTerrain, new Vector4f(0,0,0.0f)).setTexture(texture).setEnvironment(env);
		
		Mesh plane = MeshBuilder.createPlane(3, Color.WHITE);
		MeshInstance planeInstance = new MeshInstance(plane, new Vector4f(4,2,0)).setEnvironment(env);
		planeInstance.transform.Rotate(new Quaternion(Vector4f.Z, MathUtils.degRad*90));
		//camera
		Camera camera = Camera.createPerspectiveCamera(60, Display.BUFFER_WIDTH, Display.BUFFER_HEIGHT, 0.1f, 60.0f, display.GetInput());
		camera.getTransform().SetPos(new Vector4f(0, 6, -7));
		camera.getTransform().Rotate(new Quaternion(new Vector4f(1, 0, 0), MathUtils.degreesToRadians * 45));
		CameraInputController camController = new CameraInputController(camera, display.GetInput());
		float a = 0;
		while(true){
			long currentTime = System.nanoTime();
			float delta = (float)((currentTime - previousTime) / 1000000000.0f);
			previousTime = currentTime;
			fpsUpdate += delta;
			fpsCounter++;
			a += 0.02f;
			if (fpsUpdate >= 1f){
				fpsUpdate -= 1f;
				Debug.Log("FPS: "+fpsCounter);
				fpsCounter = 0;
			}
			//light1.getPos().set(0.0f, 4f, (float) (Math.sin(a)*-3f));
			camController.update(delta);
			
			monkey.transform.Rotate(new Quaternion(new Vector4f(0, 1, 0), delta));
			monkey2.transform.Rotate(new Quaternion(new Vector4f(0, 1, 0), -delta));
			//stars.UpdateAndRender(target, delta);
			target.clearScreen((byte) 0x00);
			camera.clearDepthBuffer();
			env.clearLightDepthBuffers();
			target.DrawMesh2(planeInstance, camera);
			target.DrawMesh2(monkey, camera);
			target.DrawMesh2(monkey2, camera);
			target.DrawMesh2(terrain, camera);

			display.SwapBuffer();
			
		}
	}

}
