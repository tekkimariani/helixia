/**
 * 
 */
package com.tekkimariani.helixia.renderer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.tekkimariani.helixia.component.SpriteRenderer;
import com.tekkimariani.helixia.glfw.GameObject;

/**
 * @author tekki mariani
 *
 */
public class Renderer {
	
	private final int MAX_BATCH_SIZE = 1000;
	private List<RenderBatch> batches = new LinkedList<>();
	private static Shader currentShader;
	
	public Renderer() {
		
	}
	
	public void add(GameObject go) {
		SpriteRenderer sprite = go.getComponent(SpriteRenderer.class);
		if (sprite != null) {
			add(sprite);
		}
	}
	
	private void add(SpriteRenderer sprite) {
		boolean added = false;
		for (RenderBatch batch : batches) {
			if(batch.hasRoom() && batch.zIndex() == sprite.getGameObject().getTransform().zIndex()) {
				if (sprite.getTexture() == null || (batch.hasTexture(sprite.getTexture()) || batch.hasTextureRoom()) ) {
					batch.addSprite(sprite);
					added = true;
					break;
				}
			}
		}
		
		if (!added) {
			RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE, sprite.getGameObject().getTransform().zIndex());
			batch.start();
			batches.add(batch);
			batch.addSprite(sprite);
			Collections.sort(batches);
		}
	}
	
	
	public void destroyGameObject(GameObject go) {
		if(go.getComponent(SpriteRenderer.class) == null) { return; }
		
		for (RenderBatch batch : this.batches) {
			if (batch.destroyIfExists(go)) {
				return;
			}
		}
	}
	

	
	public static void bindShader(Shader shader) {
		currentShader = shader;
	}
	
	public static Shader getBoundShader() {
		return currentShader;
	}	
	
	public void render() {
		currentShader.use();
		int i = 0;
		for(RenderBatch batch : batches) {
			//System.out.println("Batch no: " + i);
			batch.render();
			i++;
		}
	}
}
