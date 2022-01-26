package renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utils.Color;
import utils.Math2;
import utils.Matrix4f;
import utils.Mesh;
import utils.Transform;
import utils.Vertex;
import utils.Vector4f;

public class RenderContext extends Bitmap{

	//private float[] m_zBuffer;
	
	public RenderContext(int width, int height) {
		super(width, height);
		//m_zBuffer = new float[width * height];
	}
	
	public void DrawMesh(MeshInstance meshInstance, Camera camera){
		Mesh mesh = meshInstance.getMesh();
		
		
		for (int i = 0; i < mesh.getNumIndices(); i += 3){
			Vertex screenV1 = camera.transformAndProjectOnScreen(meshInstance.transform, mesh.getVector3f(mesh.getIndex(i)));
			Vertex screenV2 = camera.transformAndProjectOnScreen(meshInstance.transform, mesh.getVector3f(mesh.getIndex(i+1)));
			Vertex screenV3 = camera.transformAndProjectOnScreen(meshInstance.transform, mesh.getVector3f(mesh.getIndex(i+2)));
			
			Environment env = meshInstance.getEnvironment();
			/*screenV1.m_light = env.calcVertexLight(mesh.getVector3f(mesh.getIndex(i)).getPosition(), 
					meshInstance.transform.GetTransformation().Transform(mesh.getVector3f(mesh.getIndex(i)).getNormal()));
			screenV2.m_light = env.calcVertexLight(mesh.getVector3f(mesh.getIndex(i+1)).getPosition(), 
					meshInstance.transform.GetTransformation().Transform(mesh.getVector3f(mesh.getIndex(i+1)).getNormal()));
			screenV3.m_light = env.calcVertexLight(mesh.getVector3f(mesh.getIndex(i+2)).getPosition(), 
					meshInstance.transform.GetTransformation().Transform(mesh.getVector3f(mesh.getIndex(i+2)).getNormal()));*/
			screenV1.m_light = env.calcVertexLight(mesh.getVector3f(mesh.getIndex(i)).getPosition(), screenV1.getNormal());
			screenV2.m_light = env.calcVertexLight(mesh.getVector3f(mesh.getIndex(i+1)).getPosition(), screenV2.getNormal());
			screenV3.m_light = env.calcVertexLight(mesh.getVector3f(mesh.getIndex(i+2)).getPosition(), screenV3.getNormal());
			
			DrawTriangle(screenV1, screenV2, screenV3,
				meshInstance.getTexture(), camera);
			
		}
	}
	
	public void DrawMesh2(MeshInstance meshInstance, Camera camera){
		Mesh mesh = meshInstance.getMesh();
		Environment env = meshInstance.getEnvironment();
		
		
		for (int i = 0; i < mesh.getNumIndices(); i += 3)
		{

			Vertex transformedVert1 = meshInstance.getTransformedVertexByIndex(mesh.getIndex(i));
			Vertex transformedVert2 = meshInstance.getTransformedVertexByIndex(mesh.getIndex(i+1));
			Vertex transformedVert3 = meshInstance.getTransformedVertexByIndex(mesh.getIndex(i+2));
			//Debug.Log(transformedVert1.getPosition());
			transformedVert1.m_light = env.calcVertexLight(transformedVert1.getPosition(), transformedVert1.getNormal());
			transformedVert2.m_light = env.calcVertexLight(transformedVert2.getPosition(), transformedVert2.getNormal());
			transformedVert3.m_light = env.calcVertexLight(transformedVert3.getPosition(), transformedVert3.getNormal());
			
			//draw mesh
			List<Vertex> screenTriangles = new ArrayList<>();
			calculateScreenTriangle(camera, 
					transformedVert1,
					transformedVert2,
					transformedVert3, screenTriangles);
			//if triangle is totally outside of the screen skip it
			if (screenTriangles != null){
				//clipping might result to more than one triangles
				for (int k = 0; k < screenTriangles.size(); k += 3)
				{
					ScanTriangle(screenTriangles.get(k), screenTriangles.get(k+1), screenTriangles.get(k+2), 
							screenTriangles.get(k).TriangleAreaTimesTwo(screenTriangles.get(k+2), screenTriangles.get(k+1)) >= 0, 
							meshInstance.getTexture(), camera);
				}
				screenTriangles.clear();
			}

			//Light zBuffers
			/*List<ShadowLight> lights = env.getShadowLights();
			List<Vertex> lightTriangles = new ArrayList<>();
			
			
			for(ShadowLight light : lights)
			{
				calculateScreenTriangle(light, 
						transformedVert1,
						transformedVert2,
						transformedVert3, lightTriangles);
				//if triangle is totally outside of the light skip it
				if (lightTriangles != null){
					//clipping might result to more than one triangles
					for (int k = 0; k < lightTriangles.size(); k += 3)
					{
						calculateScreenTriangle(camera, 
								light.unprojectFromScreen(lightTriangles.get(k)),
								light.unprojectFromScreen(lightTriangles.get(k+1)),
										light.unprojectFromScreen(lightTriangles.get(k+2)), screenTriangles);
						//Debug.Log(lightTriangles.get(k).getPosition()+" grgerg "+light.unprojectFromScreen(lightTriangles.get(k)).getPosition());
						if (screenTriangles != null){
							//clipping might result to more than one triangles
							for (int a = 0; a < screenTriangles.size(); a += 3)
							{
								ScanTriangle(screenTriangles.get(a), screenTriangles.get(a+1), screenTriangles.get(a+2), 
										screenTriangles.get(a).TriangleAreaTimesTwo(screenTriangles.get(a+2), screenTriangles.get(a+1)) >= 0, 
										meshInstance.getTexture(), camera);
							}
							screenTriangles.clear();
						}
						//DrawDepthBufferTriangle(lightTriangles.get(k), lightTriangles.get(k+1), lightTriangles.get(k+2), 
						//		lightTriangles.get(k).TriangleAreaTimesTwo(lightTriangles.get(k+2), lightTriangles.get(k+1)) >= 0, 
						//		light);
					}
					lightTriangles.clear();
				}
				
			}*/
					//DrawDepthBufferTriangle(minYVert, midYVert, maxYVert, 
						//	minYVert.TriangleAreaTimesTwo(maxYVert, midYVert) >= 0, light);
		}
			
							
	}
	
	private void DrawDepthBufferTriangle(Vertex minYVert, Vertex midYVert, Vertex maxYVert, boolean handedness, Observer observer){
		GradientsDepthBuffer grads = new GradientsDepthBuffer(minYVert, midYVert, maxYVert);
		
		EdgeDepthBuffer topToBottom = new EdgeDepthBuffer(grads, minYVert, maxYVert, 0);
		EdgeDepthBuffer topToMiddle = new EdgeDepthBuffer(grads, minYVert, midYVert, 0);
		EdgeDepthBuffer middleToBottom = new EdgeDepthBuffer(grads, midYVert, maxYVert, 1);

		EdgeDepthBuffer left = topToBottom;
		EdgeDepthBuffer right = topToMiddle;
		
		int yStart, yEnd;
		if (handedness){
			EdgeDepthBuffer temp = left;
			left = right;
			right = temp;
		}
		
		yStart = topToMiddle.getYStart();
		yEnd = topToMiddle.getYEnd();
		
		for (int j = yStart; j < yEnd; j++){
			DrawDepthScanLine(left, right, j, observer);
			left.Step();
			right.Step();
		}
		
		left = topToBottom;
		right = middleToBottom;
		
		if (handedness){
			EdgeDepthBuffer temp = left;
			left = right;
			right = temp;
		}
		
		yStart = middleToBottom.getYStart();
		yEnd = middleToBottom.getYEnd();
		
		for (int j = yStart; j < yEnd; j++){
			DrawDepthScanLine(left, right, j, observer);
			left.Step();
			right.Step();
		}
	}
	
	private void DrawDepthScanLine(EdgeDepthBuffer left, EdgeDepthBuffer right, int j, Observer observer){

		int xMin = (int) Math.ceil(left.getX());
		int xMax = (int) Math.ceil(right.getX());
		float xPrestep = xMin - left.getX();
		
		float xDist = right.getX() - left.getX();
		float depthXStep = (right.getDepth() - left.getDepth())/xDist;

		float depth = left.getDepth() + depthXStep * xPrestep;

		for (int i = xMin; i < xMax; i++){
			
			int index = i + j * getWidth();
			//Depth Buffer - if closer than current draw it
			if (depth < observer.getZBuffer()[index])
			{
				observer.getZBuffer()[index] = depth;
				//Debug.Log(depth);
				//Debug.Log(m_zBuffer[index]);
				DrawPixel(i, j, Color.WHITE.copy().Mul(120f*(Math2.clamp((depth-0.90f)/10, 0, 1))));
			}
			depth += depthXStep;
		}

	}

	private boolean calculateScreenTriangle(Observer observer, Vertex v1, Vertex v2, Vertex v3, List<Vertex> result){
		Vertex screenV1 = observer.projectOnScreen(v1);
		Vertex screenV2 = observer.projectOnScreen(v2);
		Vertex screenV3 = observer.projectOnScreen(v3);

		List<Vertex> clippedVerts = clipTriangle(screenV1, screenV2, screenV3);
		if (clippedVerts != null)
		{
			Vertex initialVertex = clippedVerts.get(0);
			
			for (int j=1; j < clippedVerts.size() - 1; j++){

				Matrix4f screenSpaceTransform = new Matrix4f().InitScreenSpaceTransform(getWidth()/2, getHeight()/2);
				Matrix4f identity = new Matrix4f().InitIdentity();
				
				Vertex minYVert = initialVertex.TransformPosNormal(screenSpaceTransform, identity).PerspectiveDivide(), 
						midYVert = clippedVerts.get(j).TransformPosNormal(screenSpaceTransform, identity).PerspectiveDivide(), 
						maxYVert = clippedVerts.get(j+1).TransformPosNormal(screenSpaceTransform, identity).PerspectiveDivide();
				
				//backface culling
				if (minYVert.TriangleAreaTimesTwo(maxYVert, midYVert) >= 0) continue;
				
				//sort the vertices
				if (maxYVert.getY() < midYVert.getY()){
					Vertex temp = maxYVert;
					maxYVert = midYVert;
					midYVert = temp;
				}
				if (midYVert.getY() < minYVert.getY()){
					Vertex temp = midYVert;
					midYVert = minYVert;
					minYVert = temp;
				}
				if (maxYVert.getY() < midYVert.getY()){
					Vertex temp = maxYVert;
					maxYVert = midYVert;
					midYVert = temp;
				}
			
				result.add(minYVert);
				result.add(midYVert);
				result.add(maxYVert);
				//ScanTriangle(minYVert, midYVert, maxYVert, 
				//	minYVert.TriangleAreaTimesTwo(maxYVert, midYVert) >= 0, bitmap, observer);
			}
	
			return true;
		}
		else return false;
	}
	
	/*public void clearDepthBuffer(){
		for (int i = 0; i < m_zBuffer.length; i++){
			m_zBuffer[i] = Float.MAX_VALUE;
		}
	}*/
	public List<Vertex> clipTriangle(Vertex v1, Vertex v2, Vertex v3){
		
		List<Vertex> verts = new ArrayList<>();
		verts.add(v1);
		verts.add(v2);
		verts.add(v3);
		//if the whole triangle is visible skip clipping
		if (v1.isInsideViewFrustum() && v2.isInsideViewFrustum() && v3.isInsideViewFrustum()){
			return verts;
		}

		List<Vertex> auxillaryList = new ArrayList<>();

		//check for clipping on all axes
		if (ClipPolygonAxis(verts, auxillaryList, 0) &&
				ClipPolygonAxis(verts, auxillaryList, 1) &&
				ClipPolygonAxis(verts, auxillaryList, 2))
		{
			return verts;
			//result might be more than 3 vertices
			//create triangle fan ABC, ACD, ADE etc
		}
		else return null;
	}
	private boolean ClipPolygonAxis(List<Vertex> verts, List<Vertex> auxillaryList, int componentIndex){
		ClipPolygonComponent(verts, componentIndex, 1.0f, auxillaryList);
		verts.clear();
		
		if (auxillaryList.isEmpty()){
			return false;
		}
		
		ClipPolygonComponent(auxillaryList, componentIndex, -1.0f, verts);
		auxillaryList.clear();
		
		return !verts.isEmpty();
	}
	
	private void ClipPolygonComponent(List<Vertex> vertices, int componentIndex, float componentFactor, List<Vertex> result){
		Vertex previousVertex = vertices.get(vertices.size() - 1);
		float previousComponent = previousVertex.get(componentIndex) * componentFactor;
		boolean previousInside = previousComponent <= previousVertex.getPosition().GetW();
		
		Iterator<Vertex> it = vertices.iterator();
		while(it.hasNext()){
			Vertex currentVertex = it.next();
			float currentComponent = currentVertex.get(componentIndex) * componentFactor;
			boolean currentInside = currentComponent <= currentVertex.getPosition().GetW();
			
			if (currentInside ^ previousInside){
				float lerpAmt = (previousVertex.getPosition().GetW() - previousComponent) /
						((previousVertex.getPosition().GetW() - previousComponent) -
								(currentVertex.getPosition().GetW() - currentComponent));
				result.add(previousVertex.lerp(currentVertex, lerpAmt));
							
			}
			
			if (currentInside){
				result.add(currentVertex);
			}
			
			previousVertex = currentVertex;
			previousComponent = currentComponent;
			previousInside = currentInside;
		}
	}


	
	private void ScanTriangle(Vertex minYVert, Vertex midYVert, Vertex maxYVert, boolean handedness, Bitmap texture, Observer observer){
		Gradients grads = new Gradients(minYVert, midYVert, maxYVert);
		
		Edge topToBottom = new Edge(grads, minYVert, maxYVert, 0);
		Edge topToMiddle = new Edge(grads, minYVert, midYVert, 0);
		Edge middleToBottom = new Edge(grads, midYVert, maxYVert, 1);

		Edge left = topToBottom;
		Edge right = topToMiddle;
		
		int yStart, yEnd;
		if (handedness){
			Edge temp = left;
			left = right;
			right = temp;
		}
		
		yStart = topToMiddle.getYStart();
		yEnd = topToMiddle.getYEnd();
		
		for (int j = yStart; j < yEnd; j++){
			DrawScanLine(left, right, j, texture, observer);
			left.Step();
			right.Step();
		}
		
		left = topToBottom;
		right = middleToBottom;
		
		if (handedness){
			Edge temp = left;
			left = right;
			right = temp;
		}
		
		yStart = middleToBottom.getYStart();
		yEnd = middleToBottom.getYEnd();
		
		for (int j = yStart; j < yEnd; j++){
			DrawScanLine(left, right, j, texture, observer);
			left.Step();
			right.Step();
		}
	}
	
	private void DrawScanLine(Edge left, Edge right, int j, Bitmap texture, Observer observer){


		int xMin = (int) Math.ceil(left.getX());
		int xMax = (int) Math.ceil(right.getX());
		float xPrestep = xMin - left.getX();
		
		Vector4f minColor = left.getColor().copy().Add(left.getGradients().getColorXStep().copy().Mul(xPrestep));
		Vector4f maxColor = right.getColor().copy().Add(left.getGradients().getColorXStep().copy().Mul(xPrestep));
		
		float xDist = right.getX() - left.getX();
		float texCoordXXStep = (right.getTexCoordX() - left.getTexCoordX())/xDist;
		float texCoordYXStep = (right.getTexCoordY() - left.getTexCoordY())/xDist;
		float oneOverZXStep = (right.getOneOverZ() - left.getOneOverZ())/xDist;
		float depthXStep = (right.getDepth() - left.getDepth())/xDist;
		//float lightAmtXStep = (right.getLightAmt() - left.getLightAmt())/xDist;

		float texCoordX = left.getTexCoordX() + texCoordXXStep * xPrestep;
		float texCoordY = left.getTexCoordY() + texCoordYXStep * xPrestep;
		float oneOverZ = left.getOneOverZ() + oneOverZXStep * xPrestep;
		float depth = left.getDepth() + depthXStep * xPrestep;
		//float lightAmt = left.getLightAmt() + lightAmtXStep * xPrestep;
		//Vector4f lightAmt = left.getLightAmt().Add(left.getGradients().getLightAmtXStep().Mul(xPrestep));
		Vector4f minLight = left.getLightAmt().copy().Add(left.getGradients().getLightAmtXStep().copy().Mul(xPrestep));
		Vector4f maxLight = right.getLightAmt().copy().Add(left.getGradients().getLightAmtXStep().copy().Mul(xPrestep));
		
		float lerpAmt = 0f;
		float lerpStep = 1.0f/(float)(xMax-xMin);
		for (int i = xMin; i < xMax; i++){
			
			int index = i + j * getWidth();
			
			//Depth Buffer - if closer than current draw it
			if (depth < observer.getZBuffer()[index])
			{
				observer.getZBuffer()[index] = depth;
				float z = 1.0f/oneOverZ;
				//color
				Vector4f color = minColor.Lerp(maxColor, lerpAmt);
				Vector4f light = minLight.Lerp(maxLight, lerpAmt);
				//UV
				int srcX = 0, srcY = 0;
				if (texture != null){
					srcX = (int)(texCoordX * z * (float)(texture.getWidth() - 1) + 0.5f);
					srcY = (int)(texCoordY * z * (float)(texture.getHeight() - 1) + 0.5f);
				}
				//Debug.Log(m_zBuffer[index]);
				CopyPixel(i, j, srcX, srcY, texture, color, Color.WHITE);
				//Vector4f fog = Math2.clamp(Color.WHITE.copy().Mul(m_zBuffer[index]).Mul(30).Add(3).Sub(25).Div(8), 0.03f, 0.97f);
				//CopyPixel(i, j, srcX, srcY, null, fog, Color.WHITE);
				//CopyPixel(i, j, srcX, srcY, texture, fog.Add(color), light);
			}

			texCoordX += texCoordXXStep;
			texCoordY += texCoordYXStep;
			oneOverZ += oneOverZXStep;
			lerpAmt+= lerpStep;
			depth += depthXStep;
			//lightAmt.Add(left.getGradients().getLightAmtXStep());
		}

	}
	
	public void DrawTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture, Observer observer){
		
		//if the whole triangle is visible skip clipping
		if (v1.isInsideViewFrustum() && v2.isInsideViewFrustum() && v3.isInsideViewFrustum()){
			FillTriangle(v1, v2, v3, texture, observer);
			return;
		}
		
		List<Vertex> verts = new ArrayList<>();
		List<Vertex> auxillaryList = new ArrayList<>();
		verts.add(v1);
		verts.add(v2);
		verts.add(v3);
		
		//check for clipping on all axes
		if (ClipPolygonAxis(verts, auxillaryList, 0) &&
				ClipPolygonAxis(verts, auxillaryList, 1) &&
				ClipPolygonAxis(verts, auxillaryList, 2))
		{
			//result might be more than 3 vertices
			//create triangle fan ABC, ACD, ADE etc
			Vertex initialVertex = verts.get(0);
			
			for (int i=1; i < verts.size() - 1; i++){
				FillTriangle(initialVertex, verts.get(i), verts.get(i+1), texture, observer);
			}
		}
	}
	
	private void FillTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture, Observer observer){
		Matrix4f screenSpaceTransform = new Matrix4f().InitScreenSpaceTransform(getWidth()/2, getHeight()/2);
		
		Matrix4f identity = new Matrix4f().InitIdentity();
		Vertex minYVert = v1.TransformPosNormal(screenSpaceTransform, identity).PerspectiveDivide(), 
				midYVert = v2.TransformPosNormal(screenSpaceTransform, identity).PerspectiveDivide(), 
				maxYVert = v3.TransformPosNormal(screenSpaceTransform, identity).PerspectiveDivide();
		
		//backface culling
		if (minYVert.TriangleAreaTimesTwo(maxYVert, midYVert) >= 0) return;
		
		//sort the vertices
		if (maxYVert.getY() < midYVert.getY()){
			Vertex temp = maxYVert;
			maxYVert = midYVert;
			midYVert = temp;
		}
		if (midYVert.getY() < minYVert.getY()){
			Vertex temp = midYVert;
			midYVert = minYVert;
			minYVert = temp;
		}
		if (maxYVert.getY() < midYVert.getY()){
			Vertex temp = maxYVert;
			maxYVert = midYVert;
			midYVert = temp;
		}
		
		ScanTriangle(minYVert, midYVert, maxYVert, 
				minYVert.TriangleAreaTimesTwo(maxYVert, midYVert) >= 0, texture, observer);
		
	}
	
	public void drawRect(int x, int y, int width, int height){
		for (int yy = y; yy < y + height; yy++){

			for (int xx = x; xx < x + width; xx++){

				DrawPixel(xx, yy, Color.WHITE);
			}
		}
	}
	
}
