/**
 * 
 */
package com.tekkimariani.helixia.renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import com.tekkimariani.helixia.glfw.Camera;

/**
 * @author tekki mariani
 *
 */
public class Shader {
	
	private int shaderProgramId;
	
	private boolean beingUsed = false;
	
	private String filepath;
	private String vertexSource;
	private String fragmentSource;
	
	public  Shader(String filepath) {
		this.filepath = filepath;
		
		try {
			String source = new String(Files.readAllBytes(Paths.get(filepath)));
			
			String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
			
			// Find the first pattern after #tape 'pattern'
			int index = source.indexOf("#type") + 6;
			int eol = source.indexOf("\n", index);
			String firstPattern = source.substring(index, eol).trim();
			
			// Find the second pattern after #tape 'pattern'
			index = source.indexOf("#type", eol) + 6;
			eol = source.indexOf("\n", index);
			String secondPattern = source.substring(index, eol).trim();
			
			if(firstPattern.equals("vertex")) {
				vertexSource = splitString[1];
			} else if(firstPattern.equals("fragment")) {
				fragmentSource = splitString[1];
			} else {
				throw new IOException("Unexpect token '" + firstPattern + "'");
			}

			
			if(secondPattern.equals("vertex")) {
				vertexSource = splitString[2];
			} else if(secondPattern.equals("fragment")) {
				fragmentSource = splitString[2];
			} else {
				throw new IOException("Unexpect token '" + secondPattern + "' in '" + filepath + "'");
			}
			
			
		} catch(IOException e) {
			e.printStackTrace();
			assert false: "Error: Could not open file for shader: '" + filepath + "'";
		}
		
		this.compile();
		
	}

	
	
	
	public void compile() {
		int vertexId;
		int fragmentId;
		
		// Compile Shaders and link them together.
		
		// Vertex shader
		vertexId = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexId, vertexSource);
		glCompileShader(vertexId);
		int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
		if( success == GL_FALSE) {
			int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
			System.out.println("Error: '" + filepath + "'\n\tVertex shader compilation failed.");
			System.out.println(glGetShaderInfoLog(vertexId, len));
			assert false: "";
		}
		
		// Fragment shader
		fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentId, fragmentSource);
		glCompileShader(fragmentId);
		success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
		if( success == GL_FALSE) {
			int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
			System.out.println("Error: '" + filepath + "'\n\tFragment shader compilation failed.");
			System.out.println(glGetShaderInfoLog(fragmentId, len));
			assert false: "";
		}
				
		// Link it
		shaderProgramId = glCreateProgram();
		glAttachShader(shaderProgramId, vertexId);
		glAttachShader(shaderProgramId, fragmentId);
		glLinkProgram(shaderProgramId);
		success = glGetProgrami(shaderProgramId, GL_LINK_STATUS);
		if( success == GL_FALSE) {
			int len = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
			System.out.println("Error: '" + filepath + "'\n\tShader linking failed.");
			System.out.println(glGetShaderInfoLog(shaderProgramId, len));
			assert false: "";
		}
		
		System.out.println("Shader compiled.");

	}

	
	public void use() {
		if(!beingUsed) {
			// Bind shaderProgram
			glUseProgram(shaderProgramId);
			beingUsed = true;
		}
	}
	
	public void detach() {
		// Detach shaderProgram
		glUseProgram(0);
		beingUsed = false;
	}
	
	private void uploadMat3f(String name, Matrix3f mat3) {
		int location = glGetUniformLocation(shaderProgramId, name);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
		mat3.get(matBuffer);
		
		glUniformMatrix3fv(location, false, matBuffer);
	}
	
	public void uploadMat4f(String name, Matrix4f mat4) {
		int location = glGetUniformLocation(shaderProgramId, name);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);
		
		glUniformMatrix4fv(location, false, matBuffer);
	}
	
	public void uploadProjectionMatrix(Matrix4f matrix) {
		this.uploadMat4f("uProjection", matrix);
	}
	
	public void uploadViewMatrix(Matrix4f matrix) {
		this.uploadMat4f("uView", matrix);
	}
	
	private void uploadVec2f(String name, Vector2f vec2) {
		int location = glGetUniformLocation(shaderProgramId, name);
		use();
		glUniform2f(location, vec2.x, vec2.y);
		
	}
	
	private void uploadVec3f(String name, Vector3f vec3) {
		int location = glGetUniformLocation(shaderProgramId, name);
		use();
		glUniform3f(location, vec3.x, vec3.y, vec3.z);
		
	}
	
	private void uploadVec4f(String name, Vector4f vec4) {
		int location = glGetUniformLocation(shaderProgramId, name);
		use();
		glUniform4f(location, vec4.x, vec4.y, vec4.z, vec4.w);
		
	}
	
	private void uploadFloat(String name, float value) {
		int location = glGetUniformLocation(shaderProgramId, name);
		use();
		glUniform1f(location, value);
	}
	
	public void uploadTime(float time) {
		this.uploadFloat("uTime", time);
	}
	
	private void uploadInt(String name, int value) {
		int location = glGetUniformLocation(shaderProgramId, name);
		use();
		glUniform1i(location, value);
	}
	
	public void uploadTexture(int slot) {
		this.uploadInt("tSampler", slot);
	}
		
	private void uploadIntArray(String name, int[] value) {
		int location = glGetUniformLocation(shaderProgramId, name);
		use();
		glUniform1iv(location, value);
	}
	
	public void uploadTextures(int[] value) {
		this.uploadIntArray("uTextures", value);
	}


	public void uploadCamera(Camera camera) {
		this.uploadProjectionMatrix(camera.getProjectionMatrix());
		this.uploadViewMatrix(camera.getViewMatrix());
	}
}
