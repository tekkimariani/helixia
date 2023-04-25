/**
 * 
 */
package com.tekkimariani.helixia.util;

/**
 * @author tekki mariani
 * If you run into framerate problems, this could maybe be the problem.
 * You can delete this and get the time with '(float)glfwGetTime()'
 */
public class Time {

	public static long timeStarted = System.nanoTime();
	
	/*
	 * Time in sec the app is running.
	 */
	public static float getTime() {
		return (float) ((System.nanoTime()-timeStarted) * 1E-9);
	}
}
