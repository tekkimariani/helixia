/**
 * 
 */
package com.tekkimariani.helixia.component;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.tekkimariani.helixia.editor.HImGui;
import com.tekkimariani.helixia.glfw.Component;
import com.tekkimariani.helixia.renderer.Texture;
import com.tekkimariani.helixia.renderer.TextureCoordinates;

import imgui.ImGui;

/**
 * @author tekki mariani
 *
 */
public class SpriteRenderer extends Component {
	
	
	
	private Vector4f color = new Vector4f(1f,1f,1f,1f);
	private Sprite sprite = new Sprite();;
	
	private transient Transform lastTransform = new Transform();
	private transient boolean isDirty = true;
	
	
//	public SpriteRenderer() {
//		this.init(new Vector4f(1f,1f,1f,1f), null);
//	}
//	
//	public SpriteRenderer(Vector4f color) {
//		this.init(color, new Sprite());
//	}
//	
//	public SpriteRenderer(Sprite sprite) {
//		this.init(new Vector4f(1f,1f,1f,1f), sprite);
//	}
//	
//	public SpriteRenderer(Texture texture) {
//		this.init(new Vector4f(1f,1f,1f,1f), new Sprite(texture));
//	}
	
	/**
	 * Init runs in constructors.
	 * There is no way this Class has a reference to GameObject by now.
	 * If you need the GameObject you have to go to the start() function.
	 * @param color
	 * @param sprite
	 */
	private void init(Vector4f color, Sprite sprite) {
		setColor(color);
		setSprite(sprite);
		this.isDirty = true;
	}
	
	/**
	 * After the GameObject is set up and the Scene and the GameObject is ready this function is called.
	 */
	@Override
	public void start() {
		this.getGameObject().getTransform().copy(this.lastTransform);
	}
	
	/**
	 * This method runs every time the GameObject is being updated by the GameLoop.
	 */
	@Override
	public void update(float deltatime) {
		if (!isDirty) {
			if (!this.lastTransform.equals(this.getGameObject().getTransform())) {
				this.getGameObject().getTransform().copy(this.lastTransform);
				this.isDirty = true;
			}
		}
	}
	
	@Override
	public void editorUpdate(float deltatime) {
		if (!isDirty) {
			if (!this.lastTransform.equals(this.getGameObject().getTransform())) {
				this.getGameObject().getTransform().copy(this.lastTransform);
				this.isDirty = true;
			}
		}
	}
	
	
	@Override
	public void imgui() {
		if (HImGui.colorPicker4("Color Picker", color)) {
			this.isDirty = true;
		}
		
	}
	
	
	

	
	
	
	public Vector4f getColor() {
		return color;
	}

	public void setColor(Vector4f color) {
		if (!this.color.equals(color)) {
			this.color.set(color);
			this.isDirty = true;
		}
	}
	
	



	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		this.isDirty = true;
	}

	public Texture getTexture() {
		if(this.sprite == null) {
			return null;
		}
		return this.sprite.getTexture();
	}

	
	public void setTexture(Texture texture) {
		this.sprite.setTexture(texture);
		this.isDirty = true;
	}
	
	
	

	public TextureCoordinates getTextureCoordinates() {
		return this.sprite.getTextureCoordinates();
	}

	/*
	public void setTextureCoordinates(TextureCoordinates textureCoordinates) {
		this.sprite.setTextureCoordinates(textureCoordinates);
	}
	*/

	public boolean isDirty() {
		return this.isDirty;
	}
	
	public void setDirty() {
		this.isDirty = true;
	}
	
	public void setClean() {
		this.isDirty = false;
	}

	
	

}
