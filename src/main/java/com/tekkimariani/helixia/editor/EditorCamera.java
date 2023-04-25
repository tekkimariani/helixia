/**
 * 
 */
package com.tekkimariani.helixia.editor;

import org.joml.Vector2f;

import com.tekkimariani.helixia.glfw.Camera;
import com.tekkimariani.helixia.glfw.Component;
import com.tekkimariani.helixia.glfw.Keyboard;
import com.tekkimariani.helixia.glfw.Mouse;

/**
 * @author tekki mariani
 *
 */
public class EditorCamera extends Component {
	
	private float dragDebounce = 0.032f;
	private float dragSensitivity = 30.0f;
	private float lerpTime = 0.0f;
	private float scrollSensitivity = 0.1f;
	private boolean reset = false;

	private Camera camera;
	private Vector2f clickOrigin = new Vector2f();
	
	public EditorCamera(Camera camera) {
		this.camera = camera;
		
	}
	
	@Override
	public void editorUpdate(float deltatime) {
        if (Mouse.buttonDown(Mouse.BUTTON_MIDDLE) && dragDebounce > 0) {
            this.clickOrigin = new Vector2f(Mouse.getOrtho());
            dragDebounce -= deltatime;
            return;
        } else if (Mouse.buttonDown(Mouse.BUTTON_MIDDLE)) {
            Vector2f mousePos = new Vector2f(Mouse.getOrtho());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);
            camera.getPosition().sub(delta.mul(deltatime).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, deltatime);
        }
        
        if (dragDebounce <= 0.0f && !Mouse.buttonDown(Mouse.BUTTON_MIDDLE)) {
            dragDebounce = 0.1f;
        }

        if (Mouse.getScrollY() != 0.0f) {
            float addValue = (float)Math.pow(Math.abs(Mouse.getScrollY() * scrollSensitivity),
                    1 / camera.getZoom());
            addValue *= -Math.signum(Mouse.getScrollY());
            camera.addZoom(addValue);
        }

        if (Keyboard.isKeyPressed(Keyboard.KEY_PERIOD)) {
            reset = true;
        }

        if (reset) {
            camera.getPosition().lerp(new Vector2f(), lerpTime);
            camera.setZoom(this.camera.getZoom() +
                    ((1.0f - camera.getZoom()) * lerpTime));
            this.lerpTime += 0.1f * deltatime;
            if (Math.abs(camera.getPosition().x) <= 5.0f &&
                    Math.abs(camera.getPosition().y) <= 5.0f) {
                this.lerpTime = 0.0f;
                camera.getPosition().set(0f, 0f);
                this.camera.setZoom(1.0f);
                reset = false;
            }
        }
	}
	
	/*
	public void imgui() {
		
	}
	*/
}
