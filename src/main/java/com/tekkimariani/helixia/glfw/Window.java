package com.tekkimariani.helixia.glfw;


import static org.lwjgl.glfw.GLFW.*; 

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;


import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
//import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL30.*;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;


import com.tekkimariani.helixia.observer.EventSystem;
import com.tekkimariani.helixia.observer.event.Event;
import com.tekkimariani.helixia.observer.event.EventType;
import com.tekkimariani.helixia.renderer.DebugDraw;
import com.tekkimariani.helixia.renderer.Framebuffer;
import com.tekkimariani.helixia.renderer.PickerTexture;
import com.tekkimariani.helixia.renderer.Renderer;
import com.tekkimariani.helixia.renderer.Shader;
import com.tekkimariani.helixia.scene.SceneManager;
import com.tekkimariani.helixia.util.AssetPool;
import com.tekkimariani.helixia.util.Time;


public class Window {
	
	private int width;
	private int height;
	private int framebufferWidth;
	private int framebufferHeight;
	
	private String title;
	
	private long glfwWindow;
	
	// Stuff that not belongs here
	private ImGuiLayer imGuiLayer;
	private Framebuffer framebuffer;
	private static PickerTexture pickerTexture;
	
	
	private static Window window = null;
	
	private Window() {
		int small = 2;
		this.width = 1920/small;
		this.height = 1080/small;
		this.framebufferWidth = 1920;
		this.framebufferHeight = 1080;
		
		this.title = "Hello World";
	}
	
	public static Window get() {
		if(Window.window == null) {
			Window.window = new Window();
		}
		return Window.window;
	}
	
	public void run() {
		System.out.println("LWJGL " + Version.getVersion());
		
		init();
		
		loop();
		
		free();
	}
	
	public void init() {
		
		// Set error output
		GLFWErrorCallback.createPrint(System.err).set();
		
		
		// Initialize GLFW
		if(!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}
		
		// Configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		
		// Create Window
		System.out.println("Try to make a window " + width + "|" + height);
		float aspectRatio = (float)width/height;
		System.out.println("This is a aspect ratio of " + aspectRatio);
		glfwWindow = glfwCreateWindow(this.width, this.height, this.title, 0, 0);
		if(glfwWindow == 0) {
			throw new IllegalStateException("Failed to create the GLFW window.");
		}
		
		// Get the real size now
		IntBuffer intBufferWidth = BufferUtils.createIntBuffer(1);
		IntBuffer intBufferHeight = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(glfwWindow, intBufferWidth, intBufferHeight);
		Window.setWidth(intBufferWidth.get(0));
		Window.setHeight(intBufferHeight.get(0));
		System.out.println("The real size of your window is " + width + "|" + height);
		aspectRatio = (float)width/height;
		System.out.println("This is a aspect ratio of " + aspectRatio);
		
		// Set callback for mouse input
		glfwSetCursorPosCallback(glfwWindow, Mouse::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, Mouse::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, Mouse::mouseScrollCallback);
		// Set callback for keyboard input
		glfwSetKeyCallback(glfwWindow, Keyboard::keyCallback);
		// Set callback for window size
		glfwSetWindowSizeCallback(glfwWindow, (w, width, height) -> {
			System.out.println("Window was resized: "+width+"|"+height);
			Window.setWidth(width);
			Window.setHeight(height);
			SceneManager.getScene().getCamera().adjustProjection();
			glViewport(0, 0, width, height);
			this.framebuffer = new Framebuffer(width, height);
			pickerTexture = new PickerTexture(width, height);
		});
		
		// OpenGL context
		glfwMakeContextCurrent(glfwWindow);
		
		// Enable v-sync
		glfwSwapInterval(1);
		
		// Show the window
		glfwShowWindow(glfwWindow);
		
		GL.createCapabilities();
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		
		this.imGuiLayer = new ImGuiLayer(this.glfwWindow);
		this.imGuiLayer.initImGui();
		
		// TODO: width and height should be the actual real size of the monitor
		this.framebuffer = new Framebuffer(width, height);
		pickerTexture = new PickerTexture(width, height);
		glViewport(0, 0, width, height); // I guess first time GL does this it self.
		
		// See ow many textures the gc is able to hold.
		IntBuffer max_textures_buffer = BufferUtils.createIntBuffer(1);
		glGetIntegerv(GL_MAX_TEXTURE_IMAGE_UNITS, max_textures_buffer);
		System.out.println("You pc allows up to '" + max_textures_buffer.get(0) + "' texture images.");
		
		SceneManager.init();
	}
	
	public void free() {
		// Free memory and terminate GLFW.
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void loop() {
		float beginTime = Time.getTime();
		float endTime;
		float deltatime = -1.0f;
		
		Shader defaultShader = AssetPool.getShader("src/main/resources/shader/simplestShader.glsl");
		Shader pickerShader =  AssetPool.getShader("src/main/resources/shader/pickerShader.glsl");
		
		while(!glfwWindowShouldClose(glfwWindow)) {
			
			// Poll Events
			glfwPollEvents();
			
			
			////
			//// Render pas 1. render to picking texture
			////
			
			glDisable(GL_BLEND);
			pickerTexture.enableWriting();
			glViewport(0,0,width,height);
			// clear color red must be 0.0 for being not a object in the picker!
			glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Renderer.bindShader(pickerShader);
			SceneManager.getScene().render();
			pickerTexture.disableWriting();
			glEnable(GL_BLEND);
			

			
			////
			//// Render pass 2. the actual game
			////
			
			DebugDraw.beginFrame();
			this.framebuffer.bind();
			// Clear the window
			glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);
			
			
			
			if(deltatime >= 0) {	
				DebugDraw.draw();
				Renderer.bindShader(defaultShader);
				SceneManager.update(deltatime);
				SceneManager.render();
			}
			this.framebuffer.unbind();
			
			this.imGuiLayer.update(deltatime, SceneManager.getScene());
			
			glfwSwapBuffers(glfwWindow);
			Mouse.endFrame();
			
			endTime = Time.getTime();
			deltatime = endTime - beginTime;
			beginTime = endTime;
		}

		//EventSystem.notify(new Event(EventType.SaveLevel));
	}
		
	public static int getWidth() {
		return get().width;
	}
	
	public static int getHeight() {
		return get().height;
	}
	
	public static void setWidth(int width) {
		get().width = width;
	}
	
	public static void setHeight(int height) {
		get().height = height;
	}
	
	public static Framebuffer getFramebuffer() {
		return get().framebuffer;
	}
		
	public static float getTargetAspectRatio() {
		return (float)get().width/get().height; ////16.0f / 9.0f;
	}
	
	public static PickerTexture getPickerTexture() {
		return pickerTexture;
	}
	
	public static ImGuiLayer getImGuiLayer() {
		return get().imGuiLayer;
	}
}