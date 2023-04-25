/**
 * 
 */
package com.tekkimariani.helixia.glfw;

import java.util.LinkedList;
import java.util.List;

import org.joml.Vector2f;

import com.tekkimariani.helixia.component.Transform;


import imgui.ImGui;

/**
 * @author tekki mariani
 *
 */
public class GameObject {
	
	private static int idCounter = 0;
	private int uid = -1;

	private String name;
	private List<Component> components = new LinkedList<>();
	private transient Transform transform = new Transform();
	
	
	private boolean doSerialization = true;
	private boolean isDead = false;
	
	public GameObject(String name) {
		this.init(name);
	}
	
	public void destroy() {
		this.isDead = true;
		for (int i = 0; i < this.components.size(); i++) {
			this.components.get(i).destroy();
		}
	}
	
	public boolean isDead() {
		return this.isDead;
	}
	
	public void init(String name) {
		this.name = name;
		
		this.uid = idCounter++;
	}
	

	
	public <T extends Component> T getComponent(Class<T> componentClass) {
		for (Component c : components) {
			if(componentClass.isAssignableFrom(c.getClass())) {
				try {
					return componentClass.cast(c);
				} catch (ClassCastException e) {
					e.printStackTrace();
					assert false: "Error: Casting component.";
				}
			}
		}
		
		return null;
	}
	
	public <T extends Component> void removeComponent(Class<T> componentClass) {
		for(int i = 0; i < components.size(); i++) {
			Component c = components.get(i);
			if(componentClass.isAssignableFrom(c.getClass())) {
				components.remove(i);
				return;
			}
		}
	}
	
	public void addComponent(Component c) {
		c.generateId();
		this.components.add(c);
		c.setGameObject(this);
	}
	
	public void update(float deltatime) {
		for(int i=0; i < components.size(); i++) {
			components.get(i).update(deltatime);
		}
	}
	
	public void editorUpdate(float deltatime) {
		for(int i=0; i < components.size(); i++) {
			components.get(i).editorUpdate(deltatime);
		}	
	}
	
	public void start() {
		for(int i=0; i < components.size(); i++) {
			components.get(i).start();
		}
	}
	
	public void imgui() {
		for (Component c: components) {
			if (ImGui.collapsingHeader(c.getClass().getSimpleName())) {
				c.imgui();
			}
		}
	}

	/**
	 * @return the transform
	 */
	public Transform getTransform() {
		return transform;
	}

	/**
	 * @param transform the transform to set
	 */
	
	public void setTransform(Transform transform) {
		this.transform = transform;
	}
	



	public String getName() {
		return name;
	}
	
	
	public void generateId() {
		if (this.uid == -1) {
			this.uid = idCounter++;
		}
	}
	
	public int getUId() {
		return this.uid;
		
	}
	
	public static void init(int maxId) {
		idCounter = maxId;
	}
	
	
	public List<Component> getComponents() {
		return this.components;
	}

	/**
	 * 
	 */
	public void setNoSerialize() {
		this.doSerialization = false;
		
	}
	
	public boolean doSerialization() {
		return this.doSerialization;
	}
	
}
