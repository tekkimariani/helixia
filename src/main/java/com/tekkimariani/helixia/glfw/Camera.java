/**
 * 
 */
package com.tekkimariani.helixia.glfw;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;


/**
 * @author tekki mariani
 *
 */
public class Camera {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 2000;
	
	//private Vector2f projectionSize = new Vector2f(32.0f * 40.0f, 32.0f * 21.0f);
	private Vector2f projectionSize = new Vector2f(Window.getWidth(), Window.getHeight());
	
	
	// Projection matrix say how big the screen is going to be.
	private Matrix4f projectionMatrix = new Matrix4f();
	// The inverse projection matrix
	private Matrix4f inverseProjectionMatrix = new Matrix4f();
	
	// View Matrix says where the camera is in relation to our world.
	private Matrix4f viewMatrix = new Matrix4f();
	// The inverse view matrix
	private Matrix4f inverseViewMatrix = new Matrix4f();
	
	private Vector2f position;
	private float zoom = 1.0f;
	
	public Camera() {
		this.init(new Vector2f());
	}
	
	public Camera(Vector2f position) {
		this.init(position);
	}
	
	private void init(Vector2f position) {
		this.position = position;
		this.projectionMatrix = new Matrix4f();
		this.inverseProjectionMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.inverseViewMatrix = new Matrix4f();
		this.adjustProjection();
	}
	
	
	public void setPosition(Vector2f position) {
		this.init(position);
	}
	
	
	private Matrix4f createProjectionMatrix() {
		float aspectRatio = (float) Window.getWidth() / (float) Window.getHeight();
		float yScale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
		float xScale = yScale / aspectRatio;
		float frustrumLength = FAR_PLANE - NEAR_PLANE;	
		Matrix4f matrix = new Matrix4f();
		matrix.m00(xScale);
		matrix.m11(yScale);
		matrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustrumLength));
		matrix.m23(-1);
		matrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustrumLength));
		matrix.m33(0);
		return matrix;
	}
	
	public void adjustProjection() {
		projectionMatrix.identity();
		
		// Somehow this defines how many tiles are visible on the screen (40 * 21).
		//projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0, 32.0f * 21.0f, 0, 100);
		projectionMatrix.ortho(
				0.0f, 
				projectionSize.x * zoom, 
				0, 
				projectionSize.y * zoom, 
				0, 
				100
		);
		//projectionMatrix = this.createProjectionMatrix();
		// Save the inverted matrix
		this.projectionMatrix.invert(this.inverseProjectionMatrix);
	}
	
	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}
	
	
	/*
	 * This returns a matrix which tells where the camera is in the worldspace and where it is looking at.
	 */
	public Matrix4f getViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		
		this.viewMatrix.identity();
		this.viewMatrix.lookAt(
				new Vector3f(position.x, position.y, 20.0f), // Is the 20.0f something i want to scroll to zoom in and out?
				cameraFront.add(position.x, position.y, 0.0f),
				cameraUp
		);
		this.viewMatrix.invert(this.inverseViewMatrix);
		return this.viewMatrix;
	}
	
	public Matrix4f getInverseProjection() {
		return new Matrix4f(this.inverseProjectionMatrix);
	}
	
	public Matrix4f getInverseViewMatrix() {
		return new Matrix4f(this.inverseViewMatrix);
	}
	
	public Vector2f getProjectionSize() {
		return this.projectionSize;
	}
	
	public Vector2f getPosition() {
		return this.position;
	}
	
    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        this.adjustProjection();
    }

    public void addZoom(float value) {
        this.zoom += value;
    }
}
