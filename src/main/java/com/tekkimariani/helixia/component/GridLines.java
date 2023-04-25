/**
 * 
 */
package com.tekkimariani.helixia.component;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.tekkimariani.helixia.glfw.Camera;
import com.tekkimariani.helixia.glfw.Component;
import com.tekkimariani.helixia.renderer.DebugDraw;
import com.tekkimariani.helixia.scene.SceneManager;
import com.tekkimariani.helixia.util.Setting;

/**
 * @author tekki mariani
 *
 */
public class GridLines extends Component {

	@Override
	public void editorUpdate(float deltatime) {	
		
		Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);
		
		Camera camera = SceneManager.getScene().getCamera();
		Vector2f cameraPos = camera.getPosition();
		Vector2f projectionSize = camera.getProjectionSize();
		
		
		int firstX = ((int)(cameraPos.x / Setting.GRID_WIDTH) -1 )* Setting.GRID_WIDTH;
		int firstY = ((int)(cameraPos.y / Setting.GRID_HEIGHT) -1)* Setting.GRID_HEIGHT;
		
		int numVtLines = (int)(projectionSize.x * camera.getZoom() / Setting.GRID_WIDTH) + 2;
		int numHzLines = (int)(projectionSize.y * camera.getZoom() / Setting.GRID_HEIGHT ) + 2;
		
		int height = (int)(projectionSize.y * camera.getZoom()) + Setting.GRID_HEIGHT * 2;
		int width = (int)(projectionSize.x * camera.getZoom()) + Setting.GRID_WIDTH * 2;
		
		int maxLines = Math.max(numVtLines, numHzLines);
		
		for (int i = 0; i < maxLines; i++) {
			int x = firstX + (Setting.GRID_WIDTH * i);
			int y = firstY + (Setting.GRID_WIDTH * i);
			
			if(i < numVtLines) {
				DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY+height), color);
			}
			if(i < numHzLines) {
				DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
			}
		}
	}
}
