/**
 * 
 */
package com.tekkimariani.helixia.renderer;

import org.joml.Vector2f;

/**
 * @author tekki mariani
 *
 */
public class TextureCoordinates {
	
	private Vector2f topLeft = new Vector2f();
	private Vector2f topRight = new Vector2f();
	private Vector2f bottomLeft = new Vector2f();
	private Vector2f bottomRight = new Vector2f();
	
	public TextureCoordinates() {
		this.setTopLeft(new Vector2f());
		this.setTopRight(new Vector2f());
		this.setBottomLeft(new Vector2f());
		this.setBottomRight(new Vector2f());		
	}
	
	public TextureCoordinates(Vector2f topLeft, Vector2f topRight, Vector2f bottomLeft, Vector2f bottomRight) {
		this.setTopLeft(topLeft);
		this.setTopRight(topRight);
		this.setBottomLeft(bottomLeft);
		this.setBottomRight(bottomRight);
	}
	
	public TextureCoordinates(
			float bottomLeftX, 
			float bottomLeftY,				
			float topLeftX, 
			float topLeftY,	
			float bottomRightX,
			float bottomRightY,				
			float topRightX, 
			float topRightY		
			
				


	) {
		this.setTopLeft(new Vector2f(topLeftX, topLeftY));
		this.setTopRight(new Vector2f(topRightX, topRightY));
		this.setBottomLeft(new Vector2f(bottomLeftX, bottomLeftY));
		this.setBottomRight(new Vector2f(bottomRightX, bottomRightY));
	}
	
	public Vector2f getTopLeft() {
		return topLeft;
	}
	public void setTopLeft(Vector2f topLeft) {
		this.topLeft = topLeft;
	}
	public Vector2f getTopRight() {
		return topRight;
	}
	public void setTopRight(Vector2f topRight) {
		this.topRight = topRight;
	}
	public Vector2f getBottomLeft() {
		return bottomLeft;
	}
	public void setBottomLeft(Vector2f bottomLeft) {
		this.bottomLeft = bottomLeft;
	}
	public Vector2f getBottomRight() {
		return bottomRight;
	}
	public void setBottomRight(Vector2f bottomRight) {
		this.bottomRight = bottomRight;
	}
	
	

}
