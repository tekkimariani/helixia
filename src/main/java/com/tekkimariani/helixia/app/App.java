
package com.tekkimariani.helixia.app;

import com.tekkimariani.helixia.glfw.Window;

/**
 * @author tekki mariani
 *
 */
public class App {

	
	public void run() {
		System.out.println("Boot the engine.");
		Window window = Window.get();
		window.run();
	}
}
