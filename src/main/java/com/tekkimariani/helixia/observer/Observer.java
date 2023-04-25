/**
 * 
 */
package com.tekkimariani.helixia.observer;

import com.tekkimariani.helixia.glfw.GameObject;
import com.tekkimariani.helixia.observer.event.Event;
import com.tekkimariani.helixia.observer.event.EventType;

/**
 * @author tekki mariani
 *
 */
public interface Observer {

	void onNotify(Event event, GameObject object);
	
}
