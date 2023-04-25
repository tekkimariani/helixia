/**
 * 
 */
package com.tekkimariani.helixia.scene;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.joml.Vector2f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tekkimariani.helixia.component.Transform;
import com.tekkimariani.helixia.glfw.Camera;
import com.tekkimariani.helixia.glfw.Component;
import com.tekkimariani.helixia.glfw.GameObject;
import com.tekkimariani.helixia.gson.ComponentSerializer;
import com.tekkimariani.helixia.gson.GameObjectSerializer;
import com.tekkimariani.helixia.physics.Physics;
import com.tekkimariani.helixia.renderer.Renderer;

import imgui.ImGui;

/**
 * @author tekki mariani
 *
 */
public class Scene {
	
	private Renderer renderer = new Renderer();
	private Camera camera = new Camera();
	//private Physics physics = new Physics();
	private List<GameObject> gameObjects = new LinkedList<>();		
	
	private SceneInitializer sceneInitializer;
	
	private boolean isRunning = false;

	public Scene(SceneInitializer sceneInitializer) {
		this.sceneInitializer = sceneInitializer;
	}
	
	public void init() {
		this.camera = new Camera(new Vector2f(0, 0));
		this.sceneInitializer.loadResources(this);
		this.sceneInitializer.init(this);
	}
	
	public void start() {
		System.out.println("Scene.start()");
		GameObject go;
		for (int i = 0; i < gameObjects.size(); i++) {
			
			go = gameObjects.get(i);
			System.out.println("Scene: Start GO " + go.getUId());
			go.start();
			this.renderer.add(go);
			//this.physics.add(go);
		}
		isRunning = true;
	}
	
	public void addGameObjectToScene(GameObject go) {
		if(!isRunning) {
			gameObjects.add(go);
		} else {
			gameObjects.add(go);
			go.start();
			this.renderer.add(go);
			//this.physics.add(go);
		}
	}
	
	public void destroy() {
		for (int i = 0; i < this.gameObjects.size(); i++) {
			this.gameObjects.get(i).destroy();
		}
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public void editorUpdate(float deltatime) {
		this.camera.adjustProjection();
		GameObject go;
		for (int i = 0; i < this.gameObjects.size(); i++) {
			go = this.gameObjects.get(i);
			go.editorUpdate(deltatime);
			
			if(go.isDead()) {
				this.gameObjects.remove(go);
				this.renderer.destroyGameObject(go);
				//this.physics.destroyGameObject(go);
				i--;
			}
		}	
	}
	
	
	public void update(float deltatime) {
		this.camera.adjustProjection();
		GameObject go;
		for (int i = 0; i < this.gameObjects.size(); i++) {
			go = this.gameObjects.get(i);
			go.update(deltatime);
			
			if(go.isDead()) {
				this.gameObjects.remove(go);
				this.renderer.destroyGameObject(go);
				//this.physics.destroyGameObject(go);
				i--;
			}
		}
	}
	
	public void render() {
		this.renderer.render();
	}
	


	
	public void imgui() {
		this.sceneInitializer.imgui();
	}
	
	
	
	public GameObject createGameObject() {
		String name = "Generated game object";
		return this.createGameObject(name);
	}
	
	public GameObject createGameObject(String name) {
		
		GameObject go = new GameObject(name);
		
		// Create and add reference to gameObject:
		Transform transform = new Transform();
		go.addComponent(transform);
		go.setTransform(transform);
		System.out.println("Scene: GameObject "+ go.getUId() +" created");
		return go;
	}
	
	
	public void save() {
		System.out.println("Level saving...");
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Component.class, new ComponentSerializer())
				.registerTypeAdapter(GameObject.class, new GameObjectSerializer())
				.create();
		
		try {
			FileWriter writer = new FileWriter("level.txt");
			
			List<GameObject> objsToSerialize = new LinkedList<>();
			System.out.println("There are " + this.gameObjects.size() + " GameObjects.");
			for (GameObject go : this.gameObjects) {
				System.out.println("Handle GO: " + go.getName() + " " + go.getUId());
				if (go.doSerialization()) {
					objsToSerialize.add(go);
				}
			}
			System.out.println("There are " + objsToSerialize.size() + " GameObjects to be saved.");
			writer.write(gson.toJson(objsToSerialize));
			writer.close();
			System.out.println("Level saved.");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		File file = new File("level.txt");
		System.out.println("Level loading...");
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Component.class, new ComponentSerializer())
				.registerTypeAdapter(GameObject.class, new GameObjectSerializer())
				.create();
		
		String inFile = "";
		try {
			if (file.exists()) {
				inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		if (!inFile.equals("")) {
			
			int maxGoId = -1;
			int maxCompId = -1;
			
			GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
			System.out.println("There are " + objs.length + " GameObjects to load.");
			for (int i = 0; i < objs.length; i++) {
				this.addGameObjectToScene(objs[i]);
				
				for(Component c : objs[i].getComponents()) {
					if (c.getUId() > maxCompId) {
						maxCompId= c.getUId();
					}
				}
				if (objs[i].getUId() > maxGoId) {
					maxGoId = objs[i].getUId();
				}
				System.out.println("Scene: GameObject " + objs[i].getUId() + " loaded.");
			}
			
			GameObject.init(++maxGoId);
			Component.init(++maxCompId);
			System.out.println("Level loaded.");
		}
	}

	
	public List<GameObject> getGameObjects() {
		return this.gameObjects;
	}
	/**
	 * @param activeGameObjectId
	 * @return
	 */
	public GameObject getGameObject(int gameObjectId) {
		Optional<GameObject> result= this.gameObjects.stream()
				.filter(gameObject -> gameObject.getUId() == gameObjectId)
				.findFirst();
		return result.orElse(null);
	}
	
}
