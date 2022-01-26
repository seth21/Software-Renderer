package renderer;

import java.awt.MouseInfo;
import java.awt.event.KeyEvent;

import utils.Input;
import utils.Vector4f;

public class CameraInputController {

	private final Camera m_camera;
	private int mouseLastX, mouseLastY;
	private Input input;
	private static final Vector4f Y_AXIS = new Vector4f(0,1,0);
	
	public CameraInputController(Camera camera, Input input){
		m_camera = camera;
		this.input = input;
		mouseLastX = MouseInfo.getPointerInfo().getLocation().x;
		mouseLastY = MouseInfo.getPointerInfo().getLocation().y;
	}
	
	public Camera getCamera(){
		return m_camera;
	}
	
	public void update(float delta)
	{
		int mouseDX = MouseInfo.getPointerInfo().getLocation().x - mouseLastX;
		int mouseDY = MouseInfo.getPointerInfo().getLocation().y - mouseLastY;
		mouseLastX = MouseInfo.getPointerInfo().getLocation().x;
		mouseLastY = MouseInfo.getPointerInfo().getLocation().y;
		// Speed and rotation amounts are hardcoded here.
		// In a more general system, you might want to have them as variables.
		final float sensitivityX = 1.6f * delta;
		final float sensitivityY = 0.6f * delta;
		final float movAmt = 5.0f * delta;

		// Similarly, input keys are hardcoded here.
		// As before, in a more general system, you might want to have these as variables.
		if(input.GetKey(KeyEvent.VK_W))
			m_camera.Move(m_camera.getTransform().GetRot().GetForward(), movAmt);
		if(input.GetKey(KeyEvent.VK_S))
			m_camera.Move(m_camera.getTransform().GetRot().GetForward(), -movAmt);
		if(input.GetKey(KeyEvent.VK_Q))
			m_camera.Move(m_camera.getTransform().GetRot().GetLeft(), movAmt);
		if(input.GetKey(KeyEvent.VK_E))
			m_camera.Move(m_camera.getTransform().GetRot().GetRight(), movAmt);
		if(input.GetKey(KeyEvent.VK_F))
			m_camera.Move(m_camera.getTransform().GetRot().GetUp(), -movAmt);
		if(input.GetKey(KeyEvent.VK_R))
			m_camera.Move(m_camera.getTransform().GetRot().GetUp(), movAmt);
		
		if(input.GetKey(KeyEvent.VK_D))
			m_camera.Rotate(Y_AXIS, sensitivityX);
		if(input.GetKey(KeyEvent.VK_A))
			m_camera.Rotate(Y_AXIS, -sensitivityX);
		if(input.GetKey(KeyEvent.VK_UP))
			m_camera.Rotate(m_camera.getTransform().GetRot().GetRight(), sensitivityY*3);
		if(input.GetKey(KeyEvent.VK_DOWN))
			m_camera.Rotate(m_camera.getTransform().GetRot().GetRight(), -sensitivityY*3);
		if(mouseDX != 0)
			m_camera.Rotate(Y_AXIS, 500* sensitivityX * mouseDX/Display.FRAME_WIDTH);
		if(mouseDY != 0)
			m_camera.Rotate(m_camera.getTransform().GetRot().GetRight(), 500*sensitivityY * mouseDY/Display.FRAME_HEIGHT);
	}
}
