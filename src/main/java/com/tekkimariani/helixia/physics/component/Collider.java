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
public abstract class Collider extends Component {
	private Vector2f offset = new Vector2f();
	
	protected void setOffset(Vector2f offset) {
		this.offset.set(offset);
	}
	
	public Vector2f getOffset() {
		return this.offset;
	}
}
