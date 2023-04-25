/**
 * 
 */
package com.tekkimariani.helixia.scene;

import com.tekkimariani.helixia.editor.EditorSceneInitializer;
import com.tekkimariani.helixia.glfw.GameObject;
import com.tekkimariani.helixia.glfw.Window;
import com.tekkimariani.helixia.observer.EventSystem;
import com.tekkimariani.helixia.observer.Observer;
import com.tekkimariani.helixia.observer.event.Event;
import com.tekkimariani.helixia.observer.event.EventType;

/**
 * @author tekki mariani
 *
 */
public class SceneManager implements Observer {
	
	private static SceneManager sceneManager;
	
	private static SceneManager get() {
		if(SceneManager.sceneManager == null) {
			SceneManager.sceneManager = new SceneManager();
		}
		return SceneManager.sceneManager;
	}
	
	private static Scene scene;
	private static boolean runtimePlaying = false;
	
	public static void update(float deltatime) {
		if (runtimePlaying) {
			scene.update(deltatime);
		} else {
			scene.editorUpdate(deltatime);
		}
	}
	
	public static void render() {
		scene.render();
	}
	
	public static void init() {
		EventSystem.addObserver(get());
		changeScene(new EditorSceneInitializer());
	}

	public static void changeScene(SceneInitializer initializer) {
		if (scene != null) {
			scene.destroy();
		}
		// TODO: Here the activeGameObject of the PropertiesWidget should be set to null.
		// getImGuiLayer().getPropertiesWindow().setActiveGameObject(null);
		// Since i am not handling this via the Window i need an other way and let it as it is for now.
		// Think since PropertiesWidget should test for null this is not necessary at all.
		scene = new Scene(initializer);
		scene.load();
		scene.init();
		scene.start();
	}
	
	public static Scene getScene() {
		return scene;
	}

	@Override
	public void onNotify(Event event, GameObject object) {

		switch(event.getType()) {
			case GameEngineStartPlay:
				System.out.println("SceneManager: Play level.");
				scene.save();
				//changeScene(new EditorSceneInitializer());
				runtimePlaying = true;
				break;
			case GameEngineStopPlay:
				System.out.println("SceneManager: Stop level.");
				changeScene(new EditorSceneInitializer());
				runtimePlaying = false;
				break;
			case LoadLevel:
				System.out.println("SceneManager: Load level.");
				changeScene(new EditorSceneInitializer());
				break;
			case SaveLevel:
				System.out.println("SceneManager: Save level.");
				scene.save();
				break;
			case UserEvent:
				break;
		}
		
	}
}
