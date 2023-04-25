/**
 * 
 */
package com.tekkimariani.helixia.component;

import org.joml.Vector4f;

import com.tekkimariani.helixia.renderer.Texture;
import com.tekkimariani.helixia.renderer.TextureCoordinates;

/**
 * @author tekki mariani
 *
 */
public class Sprite {
	
	private float width = 0.0f;
	private float height = 0.0f;

	private Texture texture= null;
	private TextureCoordinates textureCoordinates = 
		new TextureCoordinates(
			0,0,
			0,1,
			1,0,
			1,1
		);
	
	
	
	private void init(Texture texture, TextureCoordinates textureCoordinates) {
		this.setTexture(texture);
		this.setTextureCoordinates(textureCoordinates);
	}

	public Texture getTexture() {
		return texture;
	}

	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	

	public TextureCoordinates getTextureCoordinates() {
		return textureCoordinates;
	}

	
	public void setTextureCoordinates(TextureCoordinates textureCoordinates) {
		this.textureCoordinates = textureCoordinates;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public int getTextureId() {
		return texture == null ? -1 : texture.getId();
	}
	
}
