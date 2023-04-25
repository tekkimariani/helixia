/**
 * 
 */
package com.tekkimariani.helixia.renderer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.tekkimariani.helixia.scene.SceneManager;
import com.tekkimariani.helixia.util.AssetPool;
import com.tekkimariani.helixia.util.JMath;


/**
 * @author tekki mariani
 *
 */
public class DebugDraw {

	private static int MAX_LINES = 500;
	
	private static List<Line2D> lines = new LinkedList<>();
	
	// 6 floats per vertex, 2 vertices per line
	private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
	
	private static Shader shader = AssetPool.getShader("src/main/resources/shader/debugLine2D.glsl");
	
	private static int vaoId;
	private static int vboId;
	
	private static boolean  started = false;
	
	public static void start() {
		//Generate the vao
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		// Create the vbo and buffer some memory
		vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vertexArray.length*Float.BYTES, GL_DYNAMIC_DRAW);
		
		// Enable vertex array attributes
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 6*Float.BYTES, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 6*Float.BYTES, 3*Float.BYTES);
		glEnableVertexAttribArray(0);	
		
		glLineWidth(2.0f);
	}
	
	public static void beginFrame() {
		if (!started) {
			start();
			started = true;
		}
		
		// Remove dead lines
		for (int i = 0; i  < lines.size(); i++) {
			if(lines.get(i).beginFrame() < 0) {
				lines.remove(i);
				i--;
			}
		}
	}
	
	public static void draw() {
		if (lines.size() <= 0) {
			return;
		}
		
		int index = 0;
		for (Line2D line : lines) {
			Vector3f color = line.getColor();
			for(int i = 0; i < 2; i++) {
				Vector2f position = i == 0 ? line.getFrom() : line.getTo();
				
				
				// Load position
				vertexArray[index] = position.x;
				vertexArray[index + 1] = position.y;
				vertexArray[index + 2] = -10.0f;
				
				// Load color
				vertexArray[index + 3] = color.x;
				vertexArray[index + 4] = color.y;
				vertexArray[index + 5] = color.z;			
				
				index += 6;
			}
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, (lines.size()*6*2)));
	
		// Use shader
		shader.use();
		shader.uploadCamera(SceneManager.getScene().getCamera());
		
		// Bind the vao
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		// Draw the batch
		glDrawArrays(GL_LINES, 0, lines.size()*6*2);
		
		// Disable
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		
		shader.detach();
	}
	
	public static void addLine2D(Vector2f from, Vector2f to) {
		addLine2D(from, to, new Vector3f(0,1,0), 1);
	}
	public static void addLine2D(Vector2f from, Vector2f to, Vector3f color) {
		addLine2D(from, to, color, 1);
	}
	public static void addLine2D(Vector2f from, Vector2f to, int lifetime) {
		addLine2D(from, to, new Vector3f(0,1,0), lifetime);
	}
	public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
		if (lines.size() >= MAX_LINES) return;
		DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
	}
	
	/// Boxes
	
	public static void addBox2D(Vector2f center, Vector2f dimensions) {
		addBox2D(center, dimensions, 0.0f, new Vector3f(0,1,0), 1);
	}
	public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation) {
		addBox2D(center, dimensions, rotation, new Vector3f(0,1,0), 1);
	}
	public static void addBox2D(Vector2f center, Vector2f dimensions, Vector3f color) {
		addBox2D(center, dimensions, 0.0f, color, 1);
	}
	public static void addBox2D(Vector2f center, Vector2f dimensions, int lifetime) {
		addBox2D(center, dimensions, 0.0f, new Vector3f(0,1,0), lifetime);
	}
	
	public static void addBox2D(
			Vector2f center, 
			Vector2f dimensions, 
			float rotation, 
			Vector3f color, 
			int lifetime
	) {
		Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
		Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));
		
		Vector2f bottomLeft = new Vector2f(min.x, min.y);
		Vector2f topLeft = new Vector2f(min.x, max.y);
		Vector2f topRight = new Vector2f(max.x, max.y);
		Vector2f bottomRight = new Vector2f(max.x, min.y);
		
		if(rotation != 0.0f) {
			JMath.rotate(bottomLeft, rotation, center);
			JMath.rotate(topLeft, rotation, center);
			JMath.rotate(topRight, rotation, center);
			JMath.rotate(bottomRight, rotation, center);
		}
		
		addLine2D(bottomLeft, topLeft, color, lifetime);
		addLine2D(topLeft, topRight, color, lifetime);
		addLine2D(topRight, bottomRight, color, lifetime);
		addLine2D(bottomRight, bottomLeft, color, lifetime);
	}
	
	
	
	// Circle
	
	public static void addCircle(Vector2f center, float radius) {
		addCircle(center, radius, new Vector3f(0,1,0), 1);
	}
	public static void addCircle(Vector2f center, float radius, Vector3f color) {
		addCircle(center, radius, color, 1);
	}
	public static void addCircle(Vector2f center, float radius, int lifetime) {
		addCircle(center, radius, new Vector3f(0,1,0), lifetime);
	}
	
	public static void addCircle(Vector2f center, float radius, Vector3f color, int lifetime) {
		
		Vector2f[] points = new Vector2f[20];
		float increment = 360 / points.length;
		float currentAngle = 0;
		
		for (int i = 0; i < points.length; i++) {
			Vector2f tmp = new Vector2f(radius, 0);
			JMath.rotate(tmp, currentAngle, new Vector2f());
			points[i] = new Vector2f(tmp).add(center);
		
		
			if (i > 0) {
				addLine2D(points[i-1], points[i], color, lifetime);
			}
			
			currentAngle += increment;
		}
		
		addLine2D(points[points.length-1], points[0], color, lifetime);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
