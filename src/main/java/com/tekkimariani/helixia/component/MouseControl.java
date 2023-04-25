/**
 * 
 */
package com.tekkimariani.helixia.component;

import com.tekkimariani.helixia.glfw.Component;
import com.tekkimariani.helixia.glfw.GameObject;
import com.tekkimariani.helixia.glfw.Mouse;
import com.tekkimariani.helixia.scene.SceneManager;
import com.tekkimariani.helixia.util.Setting;

/**
 * @author tekki mariani
 *
 */
public class MouseControl extends Component {

	private GameObject holdingObject = null;
	
	public void pickupObject(GameObject go) {
		this.holdingObject = go;
		SceneManager.getScene().addGameObjectToScene(go);
	}
	
	public void place() {
		System.out.println("Should place.");
		this.holdingObject = null;
	}

	
	@Override
	public void editorUpdate(float deltatime) {
		if (this.holdingObject != null) {
			this.holdingObject.getTransform().position = Mouse.getOrtho();
			
			// Grab the grid
			int x = 64;//(int)(holdingObject.getTransform().getPosition().x()/Setting.GRID_WIDTH) * Setting.GRID_WIDTH;
			int y  = 64;//(int)(holdingObject.getTransform().getPosition().y()/Setting.GRID_HEIGHT) * Setting.GRID_HEIGHT;
			
			this.holdingObject.getTransform().getPosition().set(x, y);
			
			if (Mouse.buttonDown(Mouse.BUTTON_LEFT)) {
				place();
			}
		}
	}

}
