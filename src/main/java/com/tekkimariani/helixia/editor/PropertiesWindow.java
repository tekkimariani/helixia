/**
 * 
 */
package com.tekkimariani.helixia.editor;

import com.tekkimariani.helixia.component.NonPickable;
import com.tekkimariani.helixia.glfw.GameObject;
import com.tekkimariani.helixia.glfw.Mouse;
import com.tekkimariani.helixia.glfw.Window;
import com.tekkimariani.helixia.physics.component.BoxCollider;
import com.tekkimariani.helixia.physics.component.CircleCollider;
import com.tekkimariani.helixia.physics.component.Rigidbody;
import com.tekkimariani.helixia.renderer.PickerTexture;
import com.tekkimariani.helixia.scene.Scene;

import imgui.ImGui;

/**
 * @author tekki mariani
 *
 */
public class PropertiesWindow {
    private GameObject activeGameObject = null;
    private PickerTexture pickingTexture;

    private float debounce = 0.2f;

    public PropertiesWindow(/*PickerTexture pickingTexture*/) {
        //this.pickingTexture = pickingTexture;
        this.pickingTexture = Window.getPickerTexture();
    }

    public void update(float dt, Scene currentScene) {
    	
        debounce -= dt;

        if (Mouse.buttonDown(Mouse.BUTTON_LEFT) && !Mouse.isDragging() && debounce < 0) {
            int x = (int)Mouse.getScreenX();
            int y = (int)Mouse.getScreenY();
            int gameObjectId = Window.getPickerTexture().readPixel(x, y); //pickingTexture.readPixel(x, y);
            GameObject pickedObj = currentScene.getGameObject(gameObjectId);
            if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null) {
                activeGameObject = pickedObj;
                System.out.println("Active Game Object Id: " + activeGameObject.getUId());
            } else if (pickedObj == null && !Mouse.isDragging()) {
                activeGameObject = null;
                System.out.println("Active Game Object Id: null");
            }
            this.debounce = 0.2f;
            //activeGameObject = null;
        }
        
        
    }

    public void imgui() {
        if (activeGameObject != null) {
            ImGui.begin("Properties");

            if (ImGui.beginPopupContextWindow("ComponentAdder")) {
                if (ImGui.menuItem("Add Rigidbody")) {
                    if (activeGameObject.getComponent(Rigidbody.class) == null) {
                        activeGameObject.addComponent(new Rigidbody());
                    }
                }

                if (ImGui.menuItem("Add Box Collider")) {
                    if (activeGameObject.getComponent(BoxCollider.class) == null &&
                            activeGameObject.getComponent(CircleCollider.class) == null) {
                        activeGameObject.addComponent(new BoxCollider());
                    }
                }

                if (ImGui.menuItem("Add Circle Collider")) {
                    if (activeGameObject.getComponent(CircleCollider.class) == null &&
                            activeGameObject.getComponent(BoxCollider.class) == null) {
                        activeGameObject.addComponent(new CircleCollider());
                    }
                }

                ImGui.endPopup();
            }

            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject() {
    	if(activeGameObject != null) {
    	System.out.println("GET Active Game Object Id: " + activeGameObject.getUId());
    	}
        return this.activeGameObject;
    }

    public void setActiveGameObject(GameObject go) {
        this.activeGameObject = go;
    }
}
