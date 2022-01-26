package renderer;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;


import javax.swing.JFrame;

import utils.Input;

@SuppressWarnings("serial")
public class Display extends Canvas{

	private final JFrame m_frame;
	private final BufferedImage m_displayImage;
	private final RenderContext m_frameBuffer;
	private final byte[] m_displayComponents;
	private final BufferStrategy m_bufferStrategy;
	private final Graphics m_graphics;
	private final Input m_input;
	public final static int FRAME_WIDTH = 1280;
	public final static int FRAME_HEIGHT = 720;
	public final static int BUFFER_WIDTH = 960/2;
	public final static int BUFFER_HEIGHT = 540/2;
	
	public Input GetInput() { return m_input; }
	
	public Display(int width, int height, String title){
		Dimension size = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		m_frameBuffer = new RenderContext(BUFFER_WIDTH, BUFFER_HEIGHT);
		
		m_displayImage = new BufferedImage(BUFFER_WIDTH, BUFFER_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		DataBufferByte databuffer = (DataBufferByte) m_displayImage.getRaster().getDataBuffer();
		m_displayComponents = databuffer.getData();
		
		
		//create the frame
		m_frame = new JFrame();
		//add the canvas inside
		m_frame.add(this);
		
		m_frame.setResizable(false);
		m_frame.pack();
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setLocationRelativeTo(null);
		m_frame.setTitle(title);
		m_frame.setVisible(true);
		
		createBufferStrategy(1);
		m_bufferStrategy = getBufferStrategy();
		m_graphics = m_bufferStrategy.getDrawGraphics();
		
		m_input = new Input();
		addKeyListener(m_input);
		addFocusListener(m_input);
		addMouseListener(m_input);
		addMouseMotionListener(m_input);

		setFocusable(true);
		requestFocus();
	}
	
	public void SwapBuffer(){
		m_frameBuffer.CopyToByteArray(m_displayComponents);
		m_graphics.drawImage(m_displayImage, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);
		m_bufferStrategy.show();
	}
	
	public RenderContext getFrameBuffer(){
		return m_frameBuffer;
	}
}
