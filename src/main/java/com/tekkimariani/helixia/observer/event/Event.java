/**
 * 
 */
package com.tekkimariani.helixia.observer.event;

/**
 * @author tekki mariani
 *
 */
public class Event {
	EventType type;

	public Event(EventType type) {
		this.type = type;
	}
	
	public Event() {
		this.type = EventType.UserEvent;
	}	
	
	public EventType getType() {
		return this.type;
	}
}
