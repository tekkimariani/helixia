/**
 * 
 */
package com.tekkimariani.helixia.renderer;

import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import org.lwjgl.BufferUtils;

import com.tekkimariani.helixia.util.AssetPool;

/**
 * @author tekki mariani
 *
 */
public class Texture {

	private String filepath = null;
	private transient int textureId = -1;;
	private int width = -1;
	private int height = -1;
	
	public Texture() {
		
	}
	
	public Texture(int width, int height) {
		this.filepath = "generated";
		
		textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);
		
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glTexImage2D(
				GL_TEXTURE_2D,
				0, 
				GL_RGB, 
				width, 
				height, 
				0, 
				GL_RGB, 
				GL_UNSIGNED_BYTE, 
				0
		);	
	}
	
	
	public void init(String filepath) {
		
		AssetPool.getShader(filepath);
		
	}
	
	public void build(String filepath) {
		
		this.filepath = filepath;
		
		// Generate texture on GPU
		textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);
		
		// Set texture parameters
		// Repeat image in both directions
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		// When streching the image, pixelate
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		
		// When shrinking an image, pixelate
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		
		// Turn image upside down.
		stbi_set_flip_vertically_on_load(true);
		
		ByteBuffer image = stbi_load(filepath, width, height, channels, 0);
	
		if(image != null) {
			this.width = width.get(0);
			this.height = height.get(0);
			if(channels.get(0) == 3) {
				glTexImage2D(
						GL_TEXTURE_2D,
						0, 
						GL_RGB, 
						width.get(0), 
						height.get(0), 
						0, 
						GL_RGB, 
						GL_UNSIGNED_BYTE, 
						image
				);
			} else if (channels.get(0) == 4) {
				glTexImage2D(
						GL_TEXTURE_2D,
						0, 
						GL_RGBA, 
						width.get(0), 
						height.get(0), 
						0, 
						GL_RGBA, 
						GL_UNSIGNED_BYTE, 
						image
				);				
			} else {
				assert false : "Error: (Texture) Unknown number of channels '" + channels.get(0) + "'";
			}
		} else {
			assert false : "Error: (Texture) Could not load image '" + filepath + "'";
		}
		
		stbi_image_free(image);
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureId);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public String getFilepath() {
		return this.filepath;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getId() {
		return textureId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(filepath, height, textureId, width);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}	
		Texture other = (Texture) obj;
		return Objects.equals(filepath, other.getFilepath());
	}
	
	
	
}
