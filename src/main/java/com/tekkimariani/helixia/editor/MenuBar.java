/**
 * 
 */
package com.tekkimariani.helixia.editor;

import com.tekkimariani.helixia.observer.EventSystem;
import com.tekkimariani.helixia.observer.event.Event;
import com.tekkimariani.helixia.observer.event.EventType;

import imgui.ImGui;

/**
 * @author tekki mariani
 *
 */
public class MenuBar {

    public void imgui() {
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Save", "Ctrl+S")) {
                EventSystem.notify(new Event(EventType.SaveLevel));
            }

            if (ImGui.menuItem("Load", "Ctrl+O")) {
                EventSystem.notify(new Event(EventType.LoadLevel));
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }
}
