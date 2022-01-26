package renderer;

public class Stars3D {

	private final float m_spread, m_speed;
	private final float m_starX[], m_starY[], m_starZ[];
	
	
	public Stars3D(int numStars, float spread, float speed){
		m_spread = spread;
		m_speed = speed;
		
		m_starX = new float[numStars];
		m_starY = new float[numStars];
		m_starZ = new float[numStars];
		
		for (int i = 0; i< m_starX.length; i++){
			InitStar(i);
		}
	}
	
	private void InitStar(int index){
		m_starX[index] = 2 * (float) (Math.random() - 0.5f) * m_spread;
		m_starY[index] = 2 * (float) (Math.random() - 0.5f) * m_spread;
		m_starZ[index] = (float) (Math.random() + 0.0001f) * m_spread;
	}
	
	public void UpdateAndRender(Bitmap target, float delta){
		target.clearScreen((byte) 0x00);
		
		final float tanHalfFOV = (float) Math.tan(Math.toRadians(70.0f / 2.0f));
		float halfWidth = target.getWidth()/2f;
		float halfHeight = target.getHeight()/2f;
		
		for (int i = 0; i< m_starX.length; i++){
			m_starZ[i] -= delta * m_speed;
			
			if (m_starZ[i] <= 0){
				InitStar(i);
			}
			
			int x = (int) ((m_starX[i]/(m_starZ[i] * tanHalfFOV)) * halfWidth + halfWidth);
			int y = (int) ((m_starY[i]/(m_starZ[i] * tanHalfFOV)) * halfHeight + halfHeight);
			
			if (x < 0 || x >= target.getWidth() ||
					y < 0 || y >= target.getHeight()){
				InitStar(i);
			}
			else target.DrawPixel(x, y, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF);
		}
	}
	
}
