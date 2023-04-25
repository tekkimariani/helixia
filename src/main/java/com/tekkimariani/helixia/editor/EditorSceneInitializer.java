/**
 * 
 */
package com.tekkimariani.helixia.editor;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tekkimariani.helixia.component.GizmoSystem;
import com.tekkimariani.helixia.component.GridLines;
import com.tekkimariani.helixia.component.MouseControl;
import com.tekkimariani.helixia.component.ScaleGizmo;
import com.tekkimariani.helixia.component.Sprite;
import com.tekkimariani.helixia.component.SpriteRenderer;
import com.tekkimariani.helixia.component.Spritesheet;
import com.tekkimariani.helixia.component.Transform;
import com.tekkimariani.helixia.component.TranslateGizmo;
import com.tekkimariani.helixia.glfw.Camera;
import com.tekkimariani.helixia.glfw.Component;
import com.tekkimariani.helixia.glfw.GameObject;
import com.tekkimariani.helixia.glfw.Mouse;
import com.tekkimariani.helixia.glfw.Prefab;
import com.tekkimariani.helixia.gson.ComponentSerializer;
import com.tekkimariani.helixia.gson.GameObjectSerializer;
import com.tekkimariani.helixia.renderer.DebugDraw;
import com.tekkimariani.helixia.renderer.TextureCoordinates;
import com.tekkimariani.helixia.scene.Scene;
import com.tekkimariani.helixia.scene.SceneInitializer;
import com.tekkimariani.helixia.util.AssetPool;
import com.tekkimariani.helixia.util.Setting;

import imgui.ImGui;
import imgui.ImVec2;

/**
 * @author tekki mariani
 */
public class EditorSceneInitializer extends SceneInitializer {
	 
	private Spritesheet sprites;
	
	private String gizmosPath = "src/main/resources/asset/image/spritesheet/gizmos.png";
	private String decorationsAndBlocks = "src/main/resources/asset/image/spritesheet/decorationsAndBlocks.png";
	
	private GameObject levelEditorStuff = null;
	
	public EditorSceneInitializer() {
		
	}
	
	@Override
	public void init(Scene scene) {
		
		
		sprites = AssetPool.getSpritesheet(decorationsAndBlocks);
		Spritesheet gizmos = AssetPool.getSpritesheet(gizmosPath);
		
		levelEditorStuff = scene.createGameObject("Level Editor Stuff");
		levelEditorStuff.setNoSerialize();
		
		
		levelEditorStuff.addComponent(new MouseControl());
		levelEditorStuff.addComponent(new GridLines());
		levelEditorStuff.addComponent(new EditorCamera(scene.getCamera()));
		//levelEditorStuff.addComponent(new GizmoSystem());
		////levelEditorStuff.addComponent(new ViewportWidget());
		////levelEditorStuff.addComponent(new PropertiesWidget(scene));
		scene.addGameObjectToScene(levelEditorStuff);	

	}
	
	@Override
	public void loadResources(Scene scene) {
		AssetPool.getShader("src/main/resources/shader/simplestShader.glsl");

		AssetPool.addSpritesheet(decorationsAndBlocks, 
				new Spritesheet(AssetPool.getTexture(decorationsAndBlocks),
						16, 16, 81, 0)
		);
		
		AssetPool.addSpritesheet(gizmosPath, 
				new Spritesheet(AssetPool.getTexture(gizmosPath),
						24, 48, 3, 0)
		);
		
		AssetPool.getTexture("src/main/resources/asset/blendImage2.png");
		
		// This cleans up multiple textures.
		// But it is also an bugfix for multiple textures with the same filepath
		// that should not exist in the first place when loading from file. ;)
		
		for(GameObject go : scene.getGameObjects()) {
			if (go.getComponent(SpriteRenderer.class) != null) {
				SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
				if (spr.getTexture() != null) {
					spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
				}
			}
		}
	}

	
	//@Override
	public void update(float deltatime) {
		this.levelEditorStuff.update(deltatime);
	}
	
	
	
	@Override
	public void imgui() {
		ImGui.begin("LevelEditorStuff");
		levelEditorStuff.imgui();
		ImGui.end();
		
		
		
		// This creates a 'window'-widget inside the actual window of the program!
		// This called window by gabe and also by ImGui, but maybe
		// it should be called something like widget.
		
		ImGui.begin("Textures");

		// Get the position of the window?
		ImVec2 windowPos = new ImVec2();
		ImGui.getWindowPos(windowPos);
		
		// Get the size o the window.
		ImVec2 windowSize = new ImVec2();
		ImGui.getWindowSize(windowSize);
		
		// Get the item spacing (defined in the styles of ImGui)
		ImVec2 itemSpacing = new ImVec2();
		ImGui.getStyle().getItemSpacing(itemSpacing);
		
		// Calculate where the window ends in screen coordinates
		float windowX2 = windowPos.x + windowSize.x;
		
		for (int i = 0; i < this.sprites.size(); i++) {
			Sprite sprite = this.sprites.get(i);
			float spriteWidth = sprite.getWidth() * 3;
			float spriteHeight = sprite.getHeight() * 3;
			int id = sprite.getTextureId();
			TextureCoordinates textureCoordinates = sprite.getTextureCoordinates();
			
			ImGui.pushID(i);
			if (ImGui.imageButton(
					id, 
					spriteWidth, 
					spriteHeight, 
					textureCoordinates.getBottomLeft().x, 
					textureCoordinates.getTopRight().y,
					textureCoordinates.getTopRight().x, 
					textureCoordinates.getBottomLeft().y
					/*, 
					,i, 
					i, 
					spriteWidth, 
					spriteHeight, 
					id
					*/
			)) {
				// If Button get clicked
				System.out.println("Button "+i+" clicked.");
				
				GameObject go = Prefab.generateSpriteObject(sprite, Setting.GRID_WIDTH, Setting.GRID_HEIGHT);
				
				// Attach this to the MouseCurser
				MouseControl mouseControl = levelEditorStuff.getComponent(MouseControl.class);
				if (mouseControl != null) {
					mouseControl.pickupObject(go);
				}
			}
			ImGui.popID();
			
			
			// Get the position of button just created
			ImVec2 lastButtonPos = new ImVec2();
			ImGui.getItemRectMax(lastButtonPos);
			float lastButtonX2 = lastButtonPos.x;
			float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
			
			// Do we have space for an other button?
			if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
				ImGui.sameLine();
			}
			
			
		}

		ImGui.end();
	}


}
