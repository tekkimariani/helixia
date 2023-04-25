/**
 * 
 */
package com.tekkimariani.helixia.physics.component;

import com.tekkimariani.helixia.glfw.Component;

/**
 * @author tekki mariani
 *
 */
public class CircleCollider extends Collider {

	private float radius = 1.0f;

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	
}
