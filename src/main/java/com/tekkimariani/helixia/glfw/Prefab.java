/**
 * 
 */
package com.tekkimariani.helixia.glfw;

import org.joml.Vector2f;

import com.tekkimariani.helixia.component.Sprite;
import com.tekkimariani.helixia.component.SpriteRenderer;
import com.tekkimariani.helixia.scene.SceneManager;

/**
 * @author tekki mariani
 *
 */
public class Prefab {

	public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
		GameObject go = SceneManager.getScene().createGameObject();
		go.getTransform().getScale().set(sizeX, sizeY);
		SpriteRenderer renderer = new SpriteRenderer();
		renderer.setSprite(sprite);
		go.addComponent(renderer);
		return go;	
	}
	
	public static GameObject generateSpriteObject(String name, Sprite sprite, float sizeX, float sizeY) {
		GameObject go = SceneManager.getScene().createGameObject(name);
		go.getTransform().getScale().set(sizeX, sizeY);
		SpriteRenderer renderer = new SpriteRenderer();
		renderer.setSprite(sprite);
		go.addComponent(renderer);
		return go;	
	}
}
