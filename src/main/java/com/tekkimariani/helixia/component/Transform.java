/**
 * 
 */
package com.tekkimariani.helixia.component;

import java.util.Objects;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.tekkimariani.helixia.editor.HImGui;
import com.tekkimariani.helixia.glfw.Component;

/**
 * @author tekki mariani
 *
 */
public class Transform  extends Component {

	public Vector2f position;
	public Vector2f scale;
	public float rotation = 0.0f;
	private int zIndex;
	
	private transient Matrix4f transformMatrix;
	
	public Transform() {
		this.init(new Vector2f(),  new Vector2f());
	}
	
	public Transform(Vector2f position) {
		this.init(position, new Vector2f());
	}
	
	public Transform(Vector2f position, Vector2f scale) {
		this.init(position, scale);
	}
	
	public void init(Vector2f position, Vector2f scale) {
		this.position = position;
		this.scale = scale;
		this.zIndex = 0;
	}	

	
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}	
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}	
	
	
	
	
	
	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public float getRotation() {
		return rotation;
	}
	
	
	public int zIndex() {
		return zIndex;
	}

	public void zIndex(int zIndex) {
		this.zIndex = zIndex;
	}



	public Transform copy() {
		return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
	}
	
	public void copy(Transform to) {
		to.position.set(this.position);
		to.scale.set(this.scale);
	}
	
	public Matrix4f getMatrix() {
		transformMatrix = new Matrix4f().identity();
		transformMatrix.translate(
				position.x(), 
				position.y(), 
				0
		);
		transformMatrix.rotate((float)Math.toRadians(rotation), new Vector3f(0,0,1));
		transformMatrix.scale(scale.x(), scale.y(), 1);	
		return this.transformMatrix;
	}
	
	@Override
	public void imgui() {
		
		HImGui.drawVec2Control("Position", position);
		HImGui.drawVec2Control("Scale", scale, 32.0f);
		HImGui.dragFloat("Rotation", rotation);
		HImGui.dragInt("Z-Index", zIndex);
	
	}

	@Override
	public int hashCode() {
		return Objects.hash(position, rotation, scale, zIndex);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transform other = (Transform) obj;
		return Objects.equals(position, other.position)
			&& Float.floatToIntBits(rotation) == Float.floatToIntBits(other.rotation)
			&& Objects.equals(scale, other.scale) 
			&& zIndex == other.zIndex;
	}


	
	
	
}
