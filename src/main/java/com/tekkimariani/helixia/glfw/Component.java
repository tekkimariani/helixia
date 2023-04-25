/**
 * 
 */
package com.tekkimariani.helixia.glfw;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.tekkimariani.helixia.editor.HImGui;

import imgui.ImGui;
import imgui.type.ImInt;

/**
 * @author tekki mariani
 *
 */
public abstract class Component {
	
	private static int idCounter = 0;
	private int uid = -1;
	
	private transient GameObject gameObject = null;


	
	public void start() {
		
	}
	
	public void update(float deltatime) {
		
	}
	
	public void editorUpdate(float deltatime) {

	}
	
	public void imgui() {
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field field : fields) {
				boolean isTransient = Modifier.isTransient(field.getModifiers());
				if (isTransient) {
					continue;
				}
				boolean isPrivate = Modifier.isPrivate(field.getModifiers());
				if (isPrivate) {
					field.setAccessible(true);
				}
				Class<?> type = field.getType();
				Object value = field.get(this);
				String name = field.getName();
				
				if (type == boolean.class) {
					boolean val = (boolean)value;
					if (ImGui.checkbox(name, val)) {
						field.set(this, !val);
					}					
				} else
				if (type == int.class) {
					int val = (int)value;
					field.set(this, HImGui.dragInt(name, val));
				} else 
					
				if (type == float.class) {
					float val = (float)value;
					field.set(this, HImGui.dragFloat(name, val));				
				} else
				
				if (type == Vector2f.class) {
					Vector2f val = (Vector2f)value;
					HImGui.drawVec2Control(name, val);
				} else
					
				if (type == Vector3f.class) {
					Vector3f val = (Vector3f)value;
					float[]imVec = {val.x, val.y, val.z};
					if (ImGui.dragFloat3(name, imVec)) {
						val.set(imVec[0], imVec[1], imVec[2]);
					}
				} else
				if (type == Vector4f.class) {
					Vector4f val = (Vector4f)value;
					float[]imVec = {val.x, val.y, val.z, val.w};
					if (ImGui.dragFloat4(name, imVec)) {
						val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
					}
				}
				if (type == Color.class) {
					Color val = (Color)value;
					float[]imVec = {val.x, val.y, val.z, val.w};
					if (ImGui.dragFloat4(name, imVec)) {
						val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
					}
				} /*else 
				if (type.isEnum()) {
                String[] enumValues = getEnumValues(type);
                String enumType = ((Enum)value).name();
                ImInt index = new ImInt(indexOf(enumType, enumValues));
                if (ImGui.combo("Enum", index, enumValues, enumValues.length)) {
                    field.set(this, type.getEnumConstants()[index.get()]);
                }
				
				*/
				
				
				
				
				if (isPrivate) {
					field.setAccessible(false);
				}				
			}
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		}
	}
		
	/*
	 * 
	 
	public boolean hasGameObject() {
		return gameObject != null;
	}*/
	
	public GameObject getGameObject() {
		return gameObject;
	}
	
	
	public void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}
	
	
	
	public void generateId() {
		if (this.uid == -1) {
			this.uid = idCounter++;
		}
	}
	
    public void generateId(boolean forceToGenerate) {
        if (forceToGenerate) {
            this.uid = -1;
            this.generateId();
        } else {
            this.generateId();
        }
    }
	
	public int getUId() {
		return this.uid;
		
	}
	
	public static void init(int maxId) {
		idCounter = maxId;
	}
	
    private <T extends Enum<T>> String[] getEnumValues(Class<T> enumType) {
        String[] enumValues = new String[enumType.getEnumConstants().length];
        int i = 0;
        for (T c : enumType.getEnumConstants()) {
            enumValues[i] = c.name();
            i++;
        }
        return enumValues;
    }

    private int indexOf(String obj, String[] arr) {
        for (int i=0; i < arr.length; i++) {
            if (obj.equals(arr[i])) {
                return i;
            }
        }

        return -1;
    }
    
    public void destroy() {
    	
    }
}
