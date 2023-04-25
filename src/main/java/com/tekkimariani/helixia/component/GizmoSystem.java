/**
 * 
 */
package com.tekkimariani.helixia.component;

import com.tekkimariani.helixia.glfw.Component;
import com.tekkimariani.helixia.glfw.GameObject;
import com.tekkimariani.helixia.glfw.Keyboard;
import com.tekkimariani.helixia.util.AssetPool;

/**
 * @author tekki mariani
 *
 */
public class GizmoSystem extends Component {
	
	String gizmosPath = "src/main/resources/asset/image/spritesheet/gizmos.png";
	
	private Spritesheet gizmos;
	private int usingGizmo = 0;
	
	private TranslateGizmo transportGizmo = null;
	private ScaleGizmo scaleGizmo = null;
	
	public GizmoSystem() {
		loadResources();
		gizmos = AssetPool.getSpritesheet(gizmosPath);
	}
	
	@Override
	public void start() {
		this.transportGizmo = new TranslateGizmo(gizmos.get(1));
		this.scaleGizmo = new ScaleGizmo(gizmos.get(2));
		this.getGameObject().addComponent(this.transportGizmo);
		this.getGameObject().addComponent(this.scaleGizmo);
	}
	
	@Override
	public void editorUpdate(float deltatime) {
		/*
		if (Keyboard.isPressed(Keyboard.KEY_E)) {
			if (usingGizmo == 0) {
				usingGizmo = 1;
			}
			if (usingGizmo == 1) {
				usingGizmo = 0;
			}
		}	
		*/
		if (Keyboard.isPressed(Keyboard.KEY_E)) {
			
			usingGizmo = 0;
		} else if (Keyboard.isPressed(Keyboard.KEY_R)) {
			usingGizmo = 1;
		}
		
		if (usingGizmo == 0) {
			this.transportGizmo.setUsing(true);
			this.scaleGizmo.setUsing(false);
		} else
		if (usingGizmo == 1) {
			this.transportGizmo.setUsing(false);
			this.scaleGizmo.setUsing(true);			
		}
		

	}
	
	private void loadResources() {
		AssetPool.addSpritesheet(gizmosPath, 
				new Spritesheet(AssetPool.getTexture(gizmosPath),
						24, 48, 3, 0)
		);
	}
	
	
	public void imgui() {
		
	}
}
