/**
 * 
 */
package com.tekkimariani.helixia.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tekkimariani.helixia.component.Transform;
import com.tekkimariani.helixia.glfw.Component;
import com.tekkimariani.helixia.glfw.GameObject;

/**
 * @author tekki mariani
 *
 */

public class GameObjectSerializer implements JsonDeserializer<GameObject> {
	
	@Override
	public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		JsonObject jsonObject = json.getAsJsonObject();
		
		String name = jsonObject.get("name").getAsString();
		JsonArray components = jsonObject.getAsJsonArray("components");

		
		GameObject go = new GameObject(name);
		
		for (JsonElement e : components) {
			Component c = context.deserialize(e, Component.class);
			go.addComponent(c);
		}
		
		go.setTransform(go.getComponent(Transform.class));
		
		return go;
	}



}

