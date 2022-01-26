package renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import utils.Vector4f;

public class Bitmap {

	private final int m_width;
	private final int m_height;
	private final byte m_components[];
	
	public Bitmap(int width, int height){
		m_width = width;
		m_height = height;
		m_components = new byte[width * height * 4]; //RGBA per pixel

	}

	
	public static Bitmap createColorNoise(int width, int height){
		Bitmap noise = new Bitmap(width, height);
		for (int j = 0; j < width; j ++){
			for (int i = 0; i < height; i ++){
				noise.DrawPixel(i, j, (float)Math.random(), 
						(float)Math.random(), 
						(float)Math.random(), 
						(float)Math.random());
			}
		}
		return noise;
	}
	
	public static Bitmap createBWNoise(int width, int height){
		Bitmap noise = new Bitmap(width, height);
		for (int j = 0; j < width; j ++){
			for (int i = 0; i < height; i ++){
				float val = (float)Math.random();
				noise.DrawPixel(i, j, val,
						val, 
						val, 
						val);
			}
		}
		return noise;
	}
	
	public Bitmap(String fileName) throws IOException
	{
		int width = 0;
		int height = 0;
		byte[] components = null;

		BufferedImage image = ImageIO.read(new File(fileName));

		width = image.getWidth();
		height = image.getHeight();

		int imgPixels[] = new int[width * height];
		image.getRGB(0, 0, width, height, imgPixels, 0, width);
		components = new byte[width * height * 4];

		for(int i = 0; i < width * height; i++)
		{
			int pixel = imgPixels[i];

			components[i * 4]     = (byte)((pixel >> 24) & 0xFF); // A
			components[i * 4 + 1] = (byte)((pixel      ) & 0xFF); // B
			components[i * 4 + 2] = (byte)((pixel >> 8 ) & 0xFF); // G
			components[i * 4 + 3] = (byte)((pixel >> 16) & 0xFF); // R
		}

		m_width = width;
		m_height = height;
		m_components = components;
	}
	
	public void clearScreen(byte shade){
		Arrays.fill(m_components, shade);
	}
	
	public byte getComponent(int index){
		return m_components[index];
	}
	
	
	public void DrawPixel(int x, int y, float a, float b, float g, float r){
		if (x < 0 || y < 0 || x > m_width - 1 || y > this.m_height - 1) return;
		
		int pixelIndex = (y*m_width + x) * 4;

		m_components[pixelIndex] = (byte) (a * 255 - 128);
		m_components[pixelIndex + 1] = (byte) (b * 255 - 128);
		m_components[pixelIndex + 2] = (byte) (g * 255 - 128);
		m_components[pixelIndex + 3] = (byte) (r * 255 - 128);

	}
	
	public void CopyPixel(int destX, int destY, int srcX, int srcY, Bitmap src, float a, float b, float g, float r, Vector4f lightAmt){
		//if (destX < 0 || destY < 0 || destX > m_width - 1 || destY > m_height - 1) return;
		//if (srcX < 0 || srcY < 0 || srcX > src.m_width - 1 || srcY > src.m_height - 1) return;
		
		int destIndex = (destY * m_width + destX) * 4;
		
		byte aa, bb, gg, rr;

		if (src != null){
			int srcIndex = (srcY * src.getWidth() + srcX) * 4;
			
			aa = (byte) ((src.getComponent(srcIndex) & 0xFF) * a * lightAmt.GetW());
			bb = (byte) ((src.getComponent(srcIndex + 1) & 0xFF) * b * lightAmt.GetZ());
			gg = (byte) ((src.getComponent(srcIndex + 2) & 0xFF) * g * lightAmt.GetY());
			rr = (byte) ((src.getComponent(srcIndex + 3) & 0xFF) * r * lightAmt.GetX());
		}
		else{
			aa = (byte) (255 * a * lightAmt.GetW());
			bb = (byte) (255 * b * lightAmt.GetZ());
			gg = (byte) (255 * g * lightAmt.GetY());
			rr = (byte) (255 * r * lightAmt.GetX());
		}
		
		
		m_components[destIndex] = aa;
		m_components[destIndex + 1] = bb;
		m_components[destIndex + 2] = gg;
		m_components[destIndex + 3] = rr;
		
	}
	
	public void CopyPixel(int destX, int destY, int srcX, int srcY, Bitmap src, Vector4f color, Vector4f lightAmt){
		CopyPixel(destX, destY, srcX, srcY, src, color.GetW(), color.GetZ(), color.GetY(), color.GetX(), lightAmt);
	}
	
	public void DrawPixel(int x, int y, Vector4f color){
		DrawPixel(x, y, color.GetW(), color.GetZ(), color.GetY(), color.GetX());
	}
	
	public void CopyToByteArray(byte[] dest){
	for (int i = 0; i < m_width * m_height; i++){

		//discard A
		dest[i * 3] = m_components[i*4 + 1];
		dest[i * 3 + 1] = m_components[i*4 + 2];
		dest[i * 3 + 2] = m_components[i*4 + 3];
	}
}

	public int getWidth() {
		return m_width;
	}
	public int getHeight() {
		return m_height;
	}
	
	/*public void CopyToIntArray(int[] dest){
		for (int i = 0; i < m_width * m_height; i++){
			int a = ((int)m_components[i*4]) << 24;
			int r = ((int)m_components[i*4 + 1]) << 16;
			int g = ((int)m_components[i*4 + 2]) << 8;
			int b = ((int)m_components[i*4 + 3]);
			
			dest[i] = a | r | g | b;
		}
	}*/
}
