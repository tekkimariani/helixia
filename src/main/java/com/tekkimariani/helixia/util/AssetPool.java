/**
 * 
 */
package com.tekkimariani.helixia.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.tekkimariani.helixia.component.Spritesheet;
import com.tekkimariani.helixia.renderer.Shader;
import com.tekkimariani.helixia.renderer.Texture;

/**
 * @author tekki mariani
 *
 */
public class AssetPool {
	
	/*
	 * 
	 */

	private static Map<String, Shader> shaders = new HashMap<>();
	private static Map<String, Texture> textures = new HashMap<>();
	private static Map<String, Spritesheet> spritesheets = new HashMap<>();
	
	public static Shader getShader(String name) {
		File file = new File(name);
		if (!shaders.containsKey(file.getAbsolutePath())) {
			Shader shader = new Shader(file.getAbsolutePath());
			shaders.put(file.getAbsolutePath(), shader);			
		}
		return shaders.get(file.getAbsolutePath());
	}
	
	public static Texture getTexture(String name) {
		File file = new File(name);
		if (!shaders.containsKey(file.getAbsolutePath())) {
			Texture texture = new Texture();
			texture.build(file.getAbsolutePath());
			textures.put(file.getAbsolutePath(), texture);			
		}
		return textures.get(file.getAbsolutePath());
	}
	
	public static void addSpritesheet(String name, Spritesheet spritesheet) {
		File file = new File(name);
		if(!spritesheets.containsKey(file.getAbsolutePath())) {
			spritesheets.put(file.getAbsolutePath(), spritesheet);
		}
	}
	
	public static Spritesheet getSpritesheet(String name) {
		File file = new File(name);
		if(!spritesheets.containsKey(file.getAbsolutePath())) {
			assert false : "Error: Tried to acces spritesheet '" + name + "' and is has never been added to AssetPool.";
		} 
		return spritesheets.getOrDefault(file.getAbsolutePath(), null);
	}
}
