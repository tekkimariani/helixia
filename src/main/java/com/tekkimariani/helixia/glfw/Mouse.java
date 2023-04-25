/**
 * 
 */
package com.tekkimariani.helixia.glfw;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import com.tekkimariani.helixia.scene.SceneManager;


/**
 * @author tekki mariani
 *
 */
public class Mouse {
	
	public static int MOUSE_BUTTON_1 = GLFW_MOUSE_BUTTON_LEFT;
	public static int MOUSE_BUTTON_2 = GLFW_MOUSE_BUTTON_MIDDLE;
	public static int MOUSE_BUTTON_3 = GLFW_MOUSE_BUTTON_RIGHT;
	public static int MOUSE_BUTTON_LEFT = GLFW_MOUSE_BUTTON_LEFT;
	public static int MOUSE_BUTTON_MIDDLE = GLFW_MOUSE_BUTTON_MIDDLE;
	public static int MOUSE_BUTTON_RIGHT = GLFW_MOUSE_BUTTON_RIGHT;
	public static int BUTTON_LEFT = GLFW_MOUSE_BUTTON_LEFT;
	public static int BUTTON_MIDDLE = GLFW_MOUSE_BUTTON_MIDDLE;
	public static int BUTTON_RIGHT = GLFW_MOUSE_BUTTON_RIGHT;
	
	private static Mouse instance;
	
	
	private double xPos;
	private double yPos;
	private Vector2f pos;
	private double lastX;
	private double lastY;
	private Vector2f lastPos;
	private double worldX;
	private double worldY;
	private Vector2f world;
	private double lastWorldX;
	private double lastWorldY;
	private Vector2f lastWorld;
	
	private double scrollX;
	private double scrollY;
	private Vector2f scroll;
	
	private boolean mouseButtonPressed[] = new boolean[9];
	private int mouseButtonDown = 0;
	
	private boolean isDragging;
	
	private Vector2f viewportPos = new Vector2f();
	private Vector2f viewportSize = new Vector2f();
	
	
	private Mouse() {
		this.xPos = 0.0;
		this.yPos = 0.0;
		this.pos = new Vector2f(0.0f, 0.0f);
		
		this.lastX = 0.0;
		this.lastY = 0.0;
		this.lastPos = new Vector2f(0.0f, 0.0f);
		
		this.worldX = 0.0;
		this.worldY = 0.0;
		this.world = new Vector2f(0.0f, 0.0f);
		
		this.lastWorldX = 0.0;
		this.lastWorldY = 0.0;
		this.lastWorld = new Vector2f(0.0f, 0.0f);
		
		this.scrollX = 0.0;
		this.scrollY = 0.0;
		this.scroll = new Vector2f(0.0f, 0.0f);
		
		this.viewportPos = new Vector2f();
		this.viewportSize = new Vector2f(Window.getWidth(), Window.getHeight());
		
	}
	
	public static Mouse get() {
		if (Mouse.instance == null) {
			Mouse.instance = new Mouse();
		}
		return Mouse.instance;
	}
	
	public static void mousePosCallback(long window, double xpos, double ypos) {
        if (get().mouseButtonDown > 0) {
            get().isDragging = true;
        }
        
		get().lastPos.set(get().pos); // Useless since endFrame() ??
		get().lastX = get().xPos; // Useless since endFrame() ??
		get().lastY = get().yPos; // Useless since endFrame() ??
		
		get().lastPos.set(get().pos);  // Useless since endFrame() ??
        get().lastWorldX = get().worldX;  // Useless since endFrame() ??
        get().lastWorldY = get().worldY;  // Useless since endFrame() ??
		
		get().pos.set(xpos, ypos);
		get().xPos = xpos;// Delete
		get().yPos = ypos;// Delete
		
	    calcOrtho();
	}
	
	public static void mouseButtonCallback(long window, int button, int action, int mods) {
		if(action == GLFW_PRESS) {
			get().mouseButtonDown++;
			if(button < get().mouseButtonPressed.length) {
				get().mouseButtonPressed[button] = true;
			}
		} else if(action == GLFW_RELEASE) {
			get().mouseButtonDown--;
			if(button < get().mouseButtonPressed.length) {
				get().mouseButtonPressed[button] = false;
				get().isDragging =  false;
			}
		}
	}
	
	public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
		get().scroll.set(xOffset, yOffset);
		get().scrollX = xOffset; // Delete
		get().scrollY = yOffset; // Delete
	}
	
	public static void endFrame() {
		get().scroll.set(0, 0);
		get().scrollX = 0.0; // Delete
		get().scrollY = 0.0; // Delete
		
		get().lastPos.set(get().pos);
		get().lastX = get().xPos; // Delete
		get().lastY = get().yPos; // Delete
		
		get().lastWorld.set(get().world);
        get().lastWorldX = get().worldX; // Delete
        get().lastWorldY = get().worldY; // Delete
	}
	
	
	
	
	public static Vector2f getPos() {
		return get().pos;
	}	
	public static float getX() {
		//return (float) get().xPos;
		return get().pos.x;
	}	
	public static float getY() {
		//return (float) get().yPos;
		return get().pos.y;
	}
	
	
	public static Vector2f getDelta() {
		Vector2f delta = new Vector2f();
		return get().lastPos.sub(get().pos, delta);
	}	
	public static float getDx() {
		//return (float) (get().lastX - get().xPos);
		return getDelta().x;
	}
	public static float getDy() {
		//return (float) (get().lastY - get().yPos);
		return getDelta().y;
	}
	
	
	public static Vector2f getScroll() {
		return get().scroll;
	}
	public static float getScrollX() {
		//return (float) get().scrollX;
		return get().scroll.x;
	}
	public static float getScrollY() {
		//return (float) get().scrollY;
		return get().scroll.y;
	}
	
	public static boolean isDragging() {
		return get().isDragging;
	}
	
	

	
	public static boolean buttonDown(int button) {
		if(button < get().mouseButtonPressed.length) {
			return get().mouseButtonPressed[button];
		} else {
			System.out.println("You ask for a mouse button that is not defined!");
			return false;
		}
	}
	
	public static Vector2f getScreen(/*Vector2f viewportPos, Vector2f viewportSize*/) {
		float currentX = getX() - get().viewportPos.x;
		float currentY = getY() - get().viewportPos.y;
		
		currentX = (currentX / get().viewportSize.x) * Window.getWidth();
		currentY = Window.getHeight()-((currentY / get().viewportSize.y) * Window.getHeight());	
		
		return new Vector2f(currentX, currentY);
	}
	
	
	
    public static float getScreenX() {
        float currentX = getX() - get().viewportPos.x;
        currentX = (currentX / get().viewportSize.x) * Window.getWidth();
        return currentX;
    }

    public static float getScreenY() {
        float currentY = getY() - get().viewportPos.y;
        currentY = Window.getHeight() - ((currentY / get().viewportSize.y) * Window.getHeight());
        return currentY;
    }
	
    /**
     * Get the world coordinates of current mouse position.
     * @return
     */
	public static Vector2f getOrtho() {
		/*
		Vector2f viewportPos = new Vector2f(0,0);
		Vector2f viewportSize = new Vector2f(Window.getWidth(), Window.getHeight());
		return getOrthoPos(viewportPos, viewportSize);
		*/
		return get().world;
	}
	
	
	public static Vector2f getOrtho(Vector2f viewportPos, Vector2f viewportSize) {
		float currentX = getX() - viewportPos.x;
		float currentY = getY() - viewportPos.y;
		
		currentX = (currentX / viewportSize.x) * 2.0f - 1.0f;
		currentY = -((currentY / viewportSize.y) * 2.0f - 1.0f);
		
		// vec.w must be 1! (Vector multiplication)
		Vector4f tmp = new Vector4f(currentX, currentY, 0.0f, 1.0f);

		Camera camera = SceneManager.getScene().getCamera();
		Matrix4f viewProjection = new Matrix4f();
		camera.getInverseViewMatrix().mul(camera.getInverseProjection(), viewProjection);
		tmp.mul(viewProjection);
		
		return new Vector2f(tmp.x, tmp.y);
	}

	
	private static void calcOrtho() {
		float currentX = getX() - get().viewportPos.x;
		float currentY = getY() - get().viewportPos.y;
		
		currentX = (currentX / get().viewportSize.x) * 2.0f - 1.0f;
		currentY = -((currentY / get().viewportSize.y) * 2.0f - 1.0f);
		
		// vec.w must be 1! (Vector multiplication)
		Vector4f tmp = new Vector4f(currentX, currentY, 0.0f, 1.0f);

		Camera camera = SceneManager.getScene().getCamera();
		Matrix4f viewProjection = new Matrix4f();
		camera.getInverseViewMatrix().mul(camera.getInverseProjection(), viewProjection);
		tmp.mul(viewProjection);
		
		get().world.set(tmp.x, tmp.y);
	}
	
	public static float getOrthoX() {
		//return getOrthoPos().x;
		return get().world.x;
	}
	
	public static float getOrthoY() {
		//return getOrthoPos().y;
		return get().world.x;
	}
	
	
	public static Vector2f getWorldDelta() {
		Vector2f delta = new Vector2f();
		get().lastWorld.sub(get().world, delta);
		return delta;
	}
	
	public static float getWorldDx() {
		return getWorldDelta().x;
	}
	public static float getWorldDy() {
		return getWorldDelta().y;
	}	


    public static void setViewportPos(Vector2f gameViewportPos) {
        get().viewportPos.set(gameViewportPos);
    }

    public static void setViewportSize(Vector2f gameViewportSize) {
        get().viewportSize.set(gameViewportSize);
    }
	
	
}
