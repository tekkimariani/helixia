/**
 * 
 */
package com.tekkimariani.helixia.renderer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.opengl.GL30.*;


/**
 * @author tekki mariani
 *
 */
public class PickerTexture {

	private int pickingTextureId;
	private int fboId;
	private int depthTextureId;
	
	public PickerTexture(int width, int height) {
		if (!init(width, height)) {
			assert false : "Error initializing picking texture";
		}
	}
	
	public boolean init(int width, int height) {
		// Generate framebuffer
		fboId = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fboId);
		
		
		// Create the texture to render the data to, and attach it to our framebuffer;
		this.pickingTextureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, pickingTextureId);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, 
				width, height, 0, GL_RGB, GL_FLOAT, 0);
		
		
		glFramebufferTexture2D(
				GL_FRAMEBUFFER, 
				GL_COLOR_ATTACHMENT0, 
				GL_TEXTURE_2D, 
				this.pickingTextureId, 
				0
		);
		
		// Create the texture object for the depth buffer
		glEnable(GL_TEXTURE_2D);
		depthTextureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, this.depthTextureId);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, 
				width, height,0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D,
				this.depthTextureId, 0);
		
		
		/*
		// Create renderbuffer store the depth info
		int rboId = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, rboId);
		glRenderbufferStorage(
				GL_RENDERBUFFER, 
				GL_DEPTH_COMPONENT32,
				width, 
				height
		);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboId);
		*/
		// Disable the reading
		glReadBuffer(GL_NONE);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		
		
		if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			assert false : "Error: Framebuffer is not complete";
			return false;
		}
		
		// Unbind the texture and framebuffer
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		return true;
	}
	
	public void enableWriting() {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fboId);
	}
	
	public void disableWriting() {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
	}
	
	public int readPixel(int x, int y) {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, fboId);
		glReadBuffer(GL_COLOR_ATTACHMENT0);
		
		float pixels[] = new float[3];
		glReadPixels(x, y, 1, 1, GL_RGB, GL_FLOAT, pixels);
		System.out.println("pixels: ["+pixels[0]+"]");

		//this.disableWriting();
		return (int)(pixels[0]) - 1;
		
	}
}
