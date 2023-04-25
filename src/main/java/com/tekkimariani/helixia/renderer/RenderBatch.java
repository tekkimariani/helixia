/**
 * 
 */
package com.tekkimariani.helixia.renderer;


import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.LinkedList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.tekkimariani.helixia.component.SpriteRenderer;
import com.tekkimariani.helixia.glfw.GameObject;
import com.tekkimariani.helixia.scene.SceneManager;


public class RenderBatch implements Comparable<RenderBatch> {

	// Vectex
	// ======
	// Pos               Color                           tex coords        tex id
	// float, float,     float, float, float, float,     float, float,     float,
	
	private final int POS_SIZE = 2;
	private final int COLOR_SIZE = 4;
	private final int TEX_COORDS_SIZE = 2;
	private final int TEX_ID_SIZE = 1;
	private final int ENTITY_ID_SIZE = 1;
	
	private final int POS_OFFSET = 0;
	private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
	private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
	private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
	private final int ENTITY_ID_OFFSET = TEX_ID_OFFSET + TEX_ID_SIZE * Float.BYTES;
			
	private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE + ENTITY_ID_SIZE;
	private final int VERTEX_SIZE_IN_BYTES = VERTEX_SIZE * Float.BYTES;
	
	private SpriteRenderer[] sprites;
	private int numSprites = 0;
	private boolean hasRoom = true;
	private float[] vertices;
	
	
	private int[] texSlots = {0,1,2,3,4,5,6,7,8};
	
	private List<Texture> textures = new LinkedList<>();
	
	private int vaoId;
	private int vboId;
	private int maxBatchSize;
	//private Shader shader;
	private int zIndex;
	
	public RenderBatch(int maxBatchSize, int zIndex) {
		this.maxBatchSize = maxBatchSize;
		this.zIndex = zIndex;
		//shader = AssetPool.getShader("src/main/resources/shader/simplestShader.glsl");
		this.sprites = new SpriteRenderer[maxBatchSize];
		
		// 4 vertices quads
		vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

		//this.start();
	}
	
	public void start() {
		
		// Generate and bind a Vertex Array Object (VAO)
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		
		// Allocate space for vertices (Vertices Buffer Object)
		vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
		
		// Create and upload indices buffer (Element Buffer Object)
		int eboId = glGenBuffers();
		int[] indices = generateIndices();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		// Enable the buffer attribute pointers
		glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_IN_BYTES, POS_OFFSET);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_IN_BYTES, COLOR_OFFSET);
		glEnableVertexAttribArray(1);	
		
		glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_IN_BYTES, TEX_COORDS_OFFSET);
		glEnableVertexAttribArray(2);
		
		glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_IN_BYTES, TEX_ID_OFFSET);
		glEnableVertexAttribArray(3);
		
		glVertexAttribPointer(4, ENTITY_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_IN_BYTES, ENTITY_ID_OFFSET);
		glEnableVertexAttribArray(4);
		
	}
	
	public void addSprite(SpriteRenderer sprite) {
		// Get index and add renderObject;
		int index = this.numSprites;
		this.sprites[index] = sprite;
		this.numSprites++;
		
		if (sprite.getTexture() != null) {
			if (!textures.contains(sprite.getTexture())) {
				textures.add(sprite.getTexture());
			}
		}
		
		// Add properties to local vertices array
		loadVertexProperties(index);
		
		if(numSprites >= this.maxBatchSize) {
			this.hasRoom = false;
		}
	}
	
	public void render() {
		
		
		
		
		// Test if sprites have change at all and if load new data.
		boolean rebufferData = false;
		for (int i = 0; i < this.numSprites; i++) {
			if (sprites[i].isDirty()) {
				loadVertexProperties(i);
				sprites[i].setClean();
				rebufferData = true;
			}
		}
		
		// Use Shader
		Shader shader = Renderer.getBoundShader();
		shader.uploadCamera(SceneManager.getScene().getCamera());		
		
		// Upload data to GPU if there are changes.
		if (rebufferData) { 
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		}

		
		
		// Bind textures
		for (int i = 0; i < textures.size(); i++) {
			glActiveTexture(GL_TEXTURE0 + i + 1);
			textures.get(i).bind();
		}
		shader.uploadTextures(texSlots);

		// Bind VAO
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		// Draw to screen
		glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);
		
		// Detach VAO
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glBindVertexArray(0);
		
		// Detach textures
		for (int i = 0; i < textures.size(); i++) {
			textures.get(i).unbind();
		}
		
		// Detach shader
		shader.detach();
	}
	
	public boolean destroyIfExists(GameObject go) {
		SpriteRenderer sprite = go.getComponent(SpriteRenderer.class);
		for (int i = 0; i < numSprites; i++) {
			if (sprites[i] == sprite) {
				for (int j = i; j < numSprites -1; j++) {
					sprites[j] = sprites[j + 1];
					sprites[j].setDirty();
				}
				numSprites--;
				return true;
			}
		}
		
		return false;
	}
	
	
	public void loadVertexProperties(int index) {
		SpriteRenderer sprite = this.sprites[index];
		
		//Find offset within array (4 vertices per sprite)
		int offset = index * 4 * VERTEX_SIZE;
		
		Vector4f color = sprite.getColor();
		
		int texId = -1;
		// 
		if (sprite.getTexture() != null) {
			for (int i = 0; i < textures.size(); i++) {
				if(textures.get(i).equals(sprite.getTexture())) { 
					texId = i + 1;
					//System.out.println("Benutzte Texture Slots: "+texId);
					break;
				}
			}
		}
		
		boolean isRotated = sprite.getGameObject().getTransform().getRotation() != 0.0f;
		/*
		Matrix4f transformMatrix = new Matrix4f().identity();
		//if (isRotated) {
			transformMatrix.translate(
					sprite.getGameObject().getTransform().getPosition().x(), 
					sprite.getGameObject().getTransform().getPosition().y(), 
					0
			);
			transformMatrix.rotate((float)Math.toRadians(sprite.getGameObject().getTransform().getRotation()), new Vector3f(0,0,1));
			transformMatrix.scale(sprite.getGameObject().getTransform().getScale().x(), sprite.getGameObject().getTransform().getScale().y(), 1);
		//}
		 * 
		 */
		
		// Add vertices with the appropriate properties
		float xAdd = 1.0f;
		float yAdd = 1.0f;
		for (int i=0; i < 4; i++) {
			if(i == 1) {
				yAdd = 0.0f;
			} else if (i == 2) {
				xAdd = 0.0f;
			} else if (i == 3) {
				yAdd = 1.0f;
			}
			
			Vector4f pos = new Vector4f(xAdd, yAdd, 0, 1).mul(sprite.getGameObject().getTransform().getMatrix());
			
			
/*
			// Load position
			vertices[offset] = sprite.getGameObject().getTransform().getPosition().x() + (xAdd * sprite.getGameObject().getTransform().getScale().x());
			vertices[offset + 1] = sprite.getGameObject().getTransform().getPosition().y() + (yAdd * sprite.getGameObject().getTransform().getScale().y());
*/
			// Load position
			vertices[offset] = pos.x;
			vertices[offset + 1] = pos.y;
	  
			
			
			// Load color
			vertices[offset + 2] = color.x;
			vertices[offset + 3] = color.y;
			vertices[offset + 4] = color.z;
			vertices[offset + 5] = color.w;
			
			// Load texture coordinates
			switch(i) {
				case 3: //Oben Link 3
					vertices[offset + 6] = sprite.getTextureCoordinates().getTopLeft().x();
					vertices[offset + 7] = sprite.getTextureCoordinates().getTopLeft().y();
					break;
				case 0: // oben rechts 0
					vertices[offset + 6] = sprite.getTextureCoordinates().getTopRight().x();
					vertices[offset + 7] = sprite.getTextureCoordinates().getTopRight().y();
					break;
				case 1: // Unten rechts 1
					vertices[offset + 6] = sprite.getTextureCoordinates().getBottomRight().x();
					vertices[offset + 7] = sprite.getTextureCoordinates().getBottomRight().y();

					break;
				case 2: // Unten links 2
					
					vertices[offset + 6] = sprite.getTextureCoordinates().getBottomLeft().x();
					vertices[offset + 7] = sprite.getTextureCoordinates().getBottomLeft().y();

					break;
			}

			
			// Load texture id
			vertices[offset + 8] = texId;
			
			//Load Entity id
			vertices[offset + 9] = sprite.getGameObject().getUId()+1;
			
		
			offset += VERTEX_SIZE;
		}
	}	
	
	private int[] generateIndices() {
		// 6 indices per quad (3 per triangle)
		int[] elements = new int[6 * maxBatchSize];
		for (int i=0; i < maxBatchSize; i++) {
			loadElementIndices(elements, i);
		}
		return elements;
	}
	
	private void loadElementIndices(int[] elements, int index) {
		int offsetArrayIndex = 6 * index;
		int offset = 4 * index;
		
		
		// 3,2,0, 0,2,1          7,6,4,  4,6,5
		// Triangle 1
		elements[offsetArrayIndex] = offset + 3;
		elements[offsetArrayIndex + 1] = offset + 2;
		elements[offsetArrayIndex + 2] = offset + 0;
		
		// Triangle 2
		elements[offsetArrayIndex + 3] = offset + 0;
		elements[offsetArrayIndex + 4] = offset + 2;
		elements[offsetArrayIndex + 5] = offset + 1;
	}
	
	
	public boolean hasRoom() {
		return this.hasRoom;
	}
	
	
	public boolean hasTextureRoom() {
		return this.textures.size() < 8;
	}
	
	public boolean hasTexture(Texture texture) {
		return this.textures.contains(texture);
	}
	
	public int zIndex() {
		return this.zIndex;
	}

	@Override
	public int compareTo(RenderBatch o) {
		return Integer.compare(this.zIndex, o.zIndex());
	}


	
}


	
	
	
	
	

