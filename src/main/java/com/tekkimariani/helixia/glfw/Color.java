/**
 * 
 */
package com.tekkimariani.helixia.glfw;

import org.joml.Vector4f;

/**
 * @author tekki mariani
 *
 */
public class Color extends Vector4f {
	public static final Color RED = new Color(1,0,0,1);
	public static final Color BLUE = new Color(0,0,1,1);
	public static final Color GREEN = new Color(0,1,0,1);
	public static final Color WHITE = new Color(1,1,1,1);
	public static final Color BLACK = new Color(0,0,0,1);
	public static final Color LIGHT_GRAY = new Color(0.2f,0.2f,0.2f,1);
	public static final Color DARK_GRAY = new Color(0.4f,0.4f,0.4f,1);

	public Color(double r, double g, double b, double a) {
		super((float)r, (float)g, (float)b, (float)a);
	}
	public Color(float r, float g, float b, float a) {
		super(r, g, b, a);
	}
	
	public Color(double r, double g, double b) {
		super((float)r, (float)g, (float)b, 1);
	}
	public Color(float r, float g, float b) {
		super(r, g, b, 1);
	}
	
	public Color() {
		super();
	}
}
