/**
 * 
 */
package com.tekkimariani.helixia.component;

import org.joml.Vector2f;

import com.tekkimariani.helixia.editor.PropertiesWindow;
import com.tekkimariani.helixia.glfw.Color;
import com.tekkimariani.helixia.glfw.Component;
import com.tekkimariani.helixia.glfw.GameObject;
import com.tekkimariani.helixia.glfw.Mouse;
import com.tekkimariani.helixia.glfw.Prefab;
import com.tekkimariani.helixia.glfw.Window;
import com.tekkimariani.helixia.scene.SceneManager;

/**
 * @author tekki mariani
 *
 */
public class Gizmo extends Component {

	private Color xAxisColor = new Color(1,0.4,0.4);
	private Color xAxisColorHover = new Color(1,0,0);
	private Color yAxisColor = new Color(0.4,0.4,1);
	private Color yAxisColorHover = new Color(0, 0, 1);
	
	
	private GameObject xAxisObject;
	private GameObject yAxisObject;
	
	private SpriteRenderer xAxisSprite;
	private SpriteRenderer yAxisSprite;
	
	protected GameObject activeGameObject = null;
	
	private Vector2f xAxisOffset = new Vector2f(47.0f, -15.0f);
	private Vector2f yAxisOffset = new Vector2f(1.0f, 46.0f);
	
	private int gizmoHeight = 46;
	private int gizmoWidth = 16;
	
	protected boolean xAxisActive = false;
	protected boolean yAxisActive = false;
	
	private boolean using = false;
	
	//private PropertiesWidget propertiesWidget = null;
	private PropertiesWindow propertiesWindow = null;
	
	
	
	public Gizmo(Sprite arrowSprite ) {
		this.xAxisObject = Prefab.generateSpriteObject("Gizmo " + this.getClass().getSimpleName(), arrowSprite, 16, 46);
		this.yAxisObject = Prefab.generateSpriteObject("Gizmo " + this.getClass().getSimpleName(), arrowSprite, 16, 46);
		
		this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
		this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);
				
		this.xAxisObject.addComponent(new NonPickable());
		this.yAxisObject.addComponent(new NonPickable());
		
		SceneManager.getScene().addGameObjectToScene(xAxisObject);
		SceneManager.getScene().addGameObjectToScene(yAxisObject);
		
	}
	
	@Override
	public void start() {
		
		this.propertiesWindow = Window.getImGuiLayer().getPropertiesWindow();//this.getGameObject().getComponent(PropertiesWidget.class);
		
		this.xAxisObject.getTransform().setRotation(90.0f);
		this.xAxisObject.setNoSerialize();
		
		this.yAxisObject.getTransform().setRotation(180.0f);
		this.yAxisObject.setNoSerialize();
		
		this.xAxisObject.getTransform().zIndex(100);
		this.yAxisObject.getTransform().zIndex(100);
	}
	
	@Override
	public void editorUpdate(float deltatime) {
		/*
		if (!using) {
			return;
		}
		
		this.activeGameObject = this.propertiesWindow.getActiveGameObject();
		if (this.activeGameObject != null) {
			this.setActive();
		} else {
			this.setInactive();
			return;
		}		
		
		boolean xAxisHot = checkXHoverState();
		boolean yAxisHot = checkYHoverState();
		
		if ((xAxisHot || xAxisActive) && Mouse.isDragging() && Mouse.mouseButtonDown(Mouse.BUTTON_LEFT)) {
			xAxisActive = true;
			yAxisActive = false;
		} else
		if ((yAxisHot || yAxisActive) && Mouse.isDragging() && Mouse.mouseButtonDown(Mouse.BUTTON_LEFT)) {
			xAxisActive = false;
			yAxisActive = true;
		}
		else {
			xAxisActive = false;
			yAxisActive = false;
		}
		
		if (this.activeGameObject != null) {
			this.xAxisObject.getTransform().getPosition().set(this.activeGameObject.getTransform().getPosition());
			this.yAxisObject.getTransform().getPosition().set(this.activeGameObject.getTransform().getPosition());
			this.xAxisObject.getTransform().getPosition().add(xAxisOffset);
			this.yAxisObject.getTransform().getPosition().add(yAxisOffset);
		}
		
		*/
		

	}
	
	private void setActive() {
		this.xAxisSprite.setColor(xAxisColor);
		this.yAxisSprite.setColor(yAxisColor);
	}
	
	private void setInactive() {
		this.activeGameObject = null;
		this.xAxisSprite.setColor(new Color(0,0,0,0));
		this.yAxisSprite.setColor(new Color(0,0,0,0));		
	}
	
	private boolean checkXHoverState() {
		Vector2f mousePos = Mouse.getOrtho();
		if (
				   mousePos.x <= xAxisObject.getTransform().getPosition().x
				&& mousePos.x >= xAxisObject.getTransform().getPosition().x - gizmoHeight
				&& mousePos.y >= xAxisObject.getTransform().getPosition().y
				&& mousePos.y <= xAxisObject.getTransform().getPosition().y + gizmoWidth
				
		) {
			this.xAxisSprite.setColor(this.xAxisColorHover);
			return true;
		}
		
		xAxisSprite.setColor(xAxisColor);
		return false;
	}
	
	
	private boolean checkYHoverState() {
		Vector2f mousePos = Mouse.getOrtho();
		if (
				   mousePos.x <= yAxisObject.getTransform().getPosition().x 
				&& mousePos.x >= yAxisObject.getTransform().getPosition().x - gizmoWidth
				&& mousePos.y <= yAxisObject.getTransform().getPosition().y
				&& mousePos.y >= yAxisObject.getTransform().getPosition().y - gizmoHeight
				
		) {
			this.yAxisSprite.setColor(this.yAxisColorHover);
			return true;
		}
		
		yAxisSprite.setColor(yAxisColor);
		return false;
	}
	
	public void setUsing(boolean bool) {
		this.using = bool;
		if(!bool) {
			this.setInactive();
		}
		
	}
}
