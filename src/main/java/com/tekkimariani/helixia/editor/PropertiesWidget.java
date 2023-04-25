///**
// * 
// */
//package com.tekkimariani.helixia.editor;
//
//import org.joml.Vector2f;
//
//import com.tekkimariani.helixia.component.MouseControl;
//import com.tekkimariani.helixia.component.NonPickable;
//import com.tekkimariani.helixia.glfw.Component;
//import com.tekkimariani.helixia.glfw.GameObject;
//import com.tekkimariani.helixia.glfw.Mouse;
//import com.tekkimariani.helixia.glfw.Window;
//import com.tekkimariani.helixia.physics.component.BoxCollider;
//import com.tekkimariani.helixia.physics.component.CircleCollider;
//import com.tekkimariani.helixia.physics.component.Rigidbody;
//import com.tekkimariani.helixia.renderer.PickerTexture;
//import com.tekkimariani.helixia.scene.Scene;
//
//import imgui.ImGui;
//
///**
// * @author tekki mariani
// *
// */
//public class PropertiesWidget extends Component {
//
//	private GameObject activeGameObject = null;
//	
//	private PickerTexture pickerTexture = null;
//	
//	private ViewportWidget viewport = null;
//	
//	private Scene scene;
//	
//	private static float DEBOUNCETIME = 0.1f;
//	private float debouncetimer = DEBOUNCETIME;
//	
//	public PropertiesWidget(Scene scene) {
//		this.scene = scene;
//	}
//	
//	@Override
//	public void start() {
//		viewport = this.getGameObject().getComponent(ViewportWidget.class);
//	}
//	
//	@Override
//	public void editorUpdate(float deltatime/*, Scene scene*/) {
//		debouncetimer -= deltatime;
//		if(Mouse.buttonDown(Mouse.BUTTON_LEFT) && !Mouse.isDragging() && debouncetimer < 0) {
//			Vector2f pos = Mouse.getScreen();//Pos(viewport.getPos(), viewport.getSize());
//			
//			int activeGameObjectId = Window.getPickerTexture().readPixel((int)pos.x, (int)pos.y);
//			
//			GameObject pickedObject = scene.getGameObject(activeGameObjectId);
//			if(pickedObject != null && pickedObject.getComponent(NonPickable.class) == null) {
//				activeGameObject = pickedObject;
//				System.out.println("Active Game Object: " + activeGameObject.getName() + " " + activeGameObject.getUId());
//			} else if(pickedObject == null && !Mouse.isDragging()){
//				activeGameObject = null;
//			}
//			debouncetimer = DEBOUNCETIME;
//		}
//	}
//	
//	@Override
//	public void imgui() {
//		if(activeGameObject != null) {
//			//ImGui.pushStyleColor(ImGuiCol.WindowBg,1.0f, 0.0f, 0.0f, 0.5f);
//			ImGui.begin("Properties");
//			
//			if (ImGui.beginPopupContextWindow("AddComponent")) {
//				if (ImGui.menuItem("Add Rigidbody")) {
//					if (activeGameObject.getComponent(Rigidbody.class) == null) {
//						activeGameObject.addComponent(new Rigidbody());
//					}
//					
//				}
//				if (ImGui.menuItem("Add BoxCollider")) {
//					if (activeGameObject.getComponent(BoxCollider.class) == null) {
//						activeGameObject.addComponent(new BoxCollider());
//					}
//					
//				}
//				if (ImGui.menuItem("Add CircleCollider")) {
//					if (activeGameObject.getComponent(CircleCollider.class) == null) {
//						activeGameObject.addComponent(new CircleCollider());
//					}
//					
//				}
//				ImGui.endPopup();
//			}
//			
//			
//			activeGameObject.imgui();
//			
//			ImGui.end();
//			//ImGui.popStyleColor();
//		}
//	}
//	
//	public GameObject getActiveGameObject() {
//		return this.activeGameObject;
//	}
//	
//	public void setActiveGameObject(GameObject go) {
//		this.activeGameObject = go;
//	}
//	
//	public void lock() {
//		debouncetimer = DEBOUNCETIME*2;
//	}
//}
