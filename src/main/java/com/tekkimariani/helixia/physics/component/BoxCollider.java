/**
 * 
 */
package com.tekkimariani.helixia.physics.component;

import org.joml.Vector2f;

import com.tekkimariani.helixia.glfw.Component;

/**
 * @author tekki mariani
 *
 */
public class BoxCollider extends Collider {

	private Vector2f halfSize = new Vector2f(1,1);
	
	private Vector2f origin = new Vector2f();

	public Vector2f getHalfSize() {
		return halfSize;
	}

	public void setHalfSize(Vector2f halfSize) {
		this.halfSize = halfSize;
	}

	public Vector2f getOrigin() {
		return origin;
	}

	public void setOrigin(Vector2f origin) {
		this.origin = origin;
	}
	
	
	
	
}
