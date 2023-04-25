/**
 * 
 */
package com.tekkimariani.helixia.observer;

import java.util.LinkedList;
import java.util.List;

import com.tekkimariani.helixia.glfw.GameObject;
import com.tekkimariani.helixia.observer.event.Event;
import com.tekkimariani.helixia.observer.event.EventType;

/**
 * @author tekki mariani
 *
 */
public class EventSystem {

	private static List<Observer> observers = new LinkedList<>();
	
	public static void addObserver(Observer observer) {
		observers.add(observer);
	}
	
	public static void removeObserver(Observer oberver) {
		observers.remove(oberver);
	}
	
	public static void notify(Event event) {
		notify(event, null);
	}
	
	public static void notify(Event event, GameObject object) {
		for (Observer observer : observers) {
			observer.onNotify(event, object);
		}
	}
}
