/**
 * 
 */
package com.tekkimariani.helixia.component;

import java.util.LinkedList;
import java.util.List;

import com.tekkimariani.helixia.renderer.Texture;
import com.tekkimariani.helixia.renderer.TextureCoordinates;

/**
 * @author tekki mariani
 *
 */
public class Spritesheet {

	private Texture texture;
	private List<Sprite> sprites = new LinkedList<>();
	
	public Spritesheet(
		Texture texture, 
		int spriteWidth, 
		int spriteHeight, 
		int numberSprites
	) {
		this.init(
			texture, 
			spriteWidth, 
			spriteHeight, 
			numberSprites, 
			0, 
			0
		);
	}
	
	public Spritesheet(
		Texture texture,
		int spriteWidth,
		int spriteHeight, 
		int numberSprites,
		int spacing
	) {
		this.init(
			texture, 
			spriteWidth,
			spriteHeight, 
			numberSprites, 
			spacing, 
			spacing
		);
	}
	
	public Spritesheet(
		Texture texture, 
		int spriteWidth, 
		int spriteHeight, 
		int numberSprites, 
		int spacingHorizontal, 
		int spacingVertical
	) {
		this.init(
			texture,
			spriteWidth, 
			spriteHeight, 
			numberSprites, 
			spacingHorizontal, 
			spacingVertical
		);
	}
	
	private void init(
		Texture texture, 
		int spriteWidth, 
		int spriteHeight, 
		int numberSprites, 
		int spacingHorizontal,
		int spacingVertical
	) {
		this.sprites.clear();
		this.texture = texture;
		this.sampling(spriteWidth, spriteHeight, numberSprites, spacingHorizontal, spacingVertical);
	}
	
	private void sampling(			
		int spriteWidth, 
		int spriteHeight, 
		int numberSprites, 
		int spacingHorizontal,
		int spacingVertical
	) {
		int currentX = 0;
		int currentY = texture.getHeight() - spriteHeight;
		
		for(int i = 0; i < numberSprites; i++) {
			
			float topY = (currentY + spriteHeight) / (float)texture.getHeight();
			float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
			float leftX = currentX / (float)texture.getWidth();
			float bottomY = currentY / (float)texture.getHeight();
			
			/*
			System.out.println("topY " + topY);
			System.out.println("rightX " + rightX);
			System.out.println("leftX " + leftX);
			System.out.println("bottomY " + bottomY);
			*/
			/*
			 * 		*							*
			 * 		(leftX, topY)				(rightX, topY)
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 		*							*
			 * 		(leftX, bottomY)			(rightX, bottomY)
			 */
			/*
			TextureCoordinates textureCoordinates = new TextureCoordinates(
					bottomY, leftX, 
					topY, leftX, 
					bottomY, rightX, 
					topY, rightX
			);
			*/
			
			TextureCoordinates textureCoordinates = new TextureCoordinates(
					leftX, bottomY, 
					leftX, topY, 
					rightX, bottomY, 
					rightX, topY
			);
			/*
			TextureCoordinates textureCoordinates = new TextureCoordinates(
					0, 0.5f,
					0,1,
					0.071428575f,0.5f,
					0.071428575f,1
				);*/
			
			Sprite sprite = new Sprite();
			sprite.setTexture(this.texture);
			sprite.setTextureCoordinates(textureCoordinates);
			sprite.setWidth(spriteWidth);
			sprite.setHeight(spriteHeight);
			this.sprites.add(sprite);
			
			currentX += spriteWidth + spacingHorizontal;
			if (currentX  >= texture.getWidth()) {
				currentX = 0;
				currentY -= spriteHeight + spacingVertical;
			}
		}
	}
		
	public Sprite get(int index) {
		return this.sprites.get(index);
	}
	
	public int size() {
		return sprites.size();
	}
	
}
