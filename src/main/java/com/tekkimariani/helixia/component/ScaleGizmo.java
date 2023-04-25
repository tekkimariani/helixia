/**
 * 
 */
package com.tekkimariani.helixia.component;

import com.tekkimariani.helixia.glfw.Mouse;

/**
 * @author tekki mariani
 *
 */
public class ScaleGizmo extends Gizmo {

	public ScaleGizmo(Sprite sprite) {
		super(sprite);
	}
	
	@Override
	public void editorUpdate(float deltatime) {
		
		/*
		if (this.activeGameObject != null) {
			if (this.xAxisActive) {
				this.activeGameObject.getTransform().getScale().x -= Mouse.getWorldDx();
			} else
			if (this.yAxisActive) {
				this.activeGameObject.getTransform().getScale().y -= Mouse.getWorldDy();
			}
		}
		*/
		//super.editorUpdate(deltatime);
	}
}
