///**
// * 
// */
//package com.tekkimariani.helixia.editor;
//
//import org.joml.Vector2f;
//
//import com.tekkimariani.helixia.glfw.Component;
//import com.tekkimariani.helixia.glfw.Mouse;
//import com.tekkimariani.helixia.glfw.Window;
//import com.tekkimariani.helixia.observer.EventSystem;
//import com.tekkimariani.helixia.observer.event.Event;
//import com.tekkimariani.helixia.observer.event.EventType;
//
//import imgui.ImGui;
//import imgui.ImVec2;
//import imgui.flag.ImGuiStyleVar;
//import imgui.flag.ImGuiWindowFlags;
//import imgui.type.ImBoolean;
//
///**
// * @author tekki mariani
// *
// */
//public class ViewportWidget extends Component {
//	
//	/*
//	private static float leftX;
//	private static float topY;
//	private static float rightX;
//	private static float bottomY;	
//	*/
//	private boolean isPlaying = false;
//	
//	private static Vector2f viewportPos = new Vector2f();
//	private static Vector2f viewportSize = new Vector2f(); 
//
//	public void imgui() {
//		
//
//		
//    	int windowFlags
//  			= ImGuiWindowFlags.NoScrollbar
//  			| ImGuiWindowFlags.NoScrollWithMouse
//  			| ImGuiWindowFlags.MenuBar
//  			;
//    	
//    	// Need to push style for window padding as there is a standard padding.
//    	ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0, 0);
//		ImGui.begin("Viewport", new ImBoolean(true), windowFlags);
//		ImGui.popStyleVar(1);
//		
//	
//		ImGui.beginMenuBar();
//		
//		if (ImGui.menuItem("Play", "", isPlaying, !isPlaying)) {
//			isPlaying= true;
//			EventSystem.notify(new Event(EventType.GameEngineStartPlay));
//		}
//		if (ImGui.menuItem("Stop", "", !isPlaying, isPlaying)) {
//			isPlaying= false;
//			EventSystem.notify(new Event(EventType.GameEngineStopPlay));
//		}		
//		
//		ImGui.endMenuBar();	
//		
//		ImVec2 windowSize = getLargestSizeForViewport();
//		ImVec2 windowPos = getCenteredPositionForViewport(windowSize);
//		
//		ImGui.setCursorPos(windowPos.x, windowPos.y);
//		
//		ImVec2 topLeft = new ImVec2();
//		ImGui.getCursorScreenPos(topLeft);
//		topLeft.x -= ImGui.getScrollX();
//		topLeft.y -= ImGui.getScrollY();
//		
//		int textureId = Window.getFramebuffer().getTextureId();
//		
//		ImGui.image(
//				textureId,  
//				windowSize.x, 
//				windowSize.y, 
//				0, 
//				1, 
//				1, 
//				0);/*, 
//				1, 
//				0, 
//				0, 
//				1.0f);*/
//		
//		
//		viewportPos.set(topLeft.x, topLeft.y);
//		viewportSize.set(windowSize.x, windowSize.y);
//		
//
//        Mouse.setViewportPos(viewportPos);
//        Mouse.setViewportSize(viewportSize);
//	
//		
//		ImGui.end();
//		
//		
//	}
//	
//	private void menuBar() {
//		ImGui.beginMenuBar();
//		
//		if (ImGui.menuItem("Play", "", isPlaying, !isPlaying)) {
//			isPlaying= true;
//			EventSystem.notify(new Event(EventType.GameEngineStartPlay));
//		}
//		if (ImGui.menuItem("Stop", "", !isPlaying, isPlaying)) {
//			isPlaying= false;
//			EventSystem.notify(new Event(EventType.GameEngineStopPlay));
//		}		
//		
//		ImGui.endMenuBar();		
//	}
//	
//	public static boolean getWantCaptureMouse() {
//		return isMouseInsideViewport();
//	}
//	
//	public static boolean getWantCaptureMouseScroll() {
//		return isMouseInsideViewport();
//	}
//	
//	private static boolean isMouseInsideViewport() {
//		return     Mouse.getX() >= viewportPos.x 
//				&& Mouse.getX() <= viewportPos.x+viewportSize.x 
//				&& Mouse.getY() >= viewportPos.y
//				&& Mouse.getY() <= viewportPos.y+viewportSize.y;		
//	}
//	
//	public Vector2f getSize() {
//		return viewportSize;
//	}
//	
//	
//	public Vector2f getPos() {
//		return viewportPos;
//	}
//	
//	
//	private ImVec2 getLargestSizeForViewport() {
//		ImVec2 windowSize = new ImVec2();
//		ImGui.getContentRegionAvail(windowSize);
//		windowSize.x -= ImGui.getScrollX();
//		windowSize.y -= ImGui.getScrollY();
//		
//		float aspectWidth = windowSize.x;
//		float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
//		
//		if(aspectHeight > windowSize.y) {
//			aspectHeight = windowSize.y;
//			aspectWidth = aspectHeight * Window.getTargetAspectRatio();
//		}
//		
//		
//		return new ImVec2(aspectWidth, aspectHeight);
//	}
//	
//
//	
//	private ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
//		ImVec2 windowSize = new ImVec2();
//		ImGui.getContentRegionAvail(windowSize);
//		windowSize.x -= ImGui.getScrollX();
//		windowSize.y -= ImGui.getScrollY();
//		
//		float viewportX = (windowSize.x/2.0f) - (aspectSize.x / 2.0f);
//		float viewportY = (windowSize.y/2.0f) - (aspectSize.y / 2.0f);
//		
//		
//		return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY+ImGui.getCursorPosY());
//	}
//}
