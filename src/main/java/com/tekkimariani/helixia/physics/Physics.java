/**
 * 
 */
package com.tekkimariani.helixia.physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

import com.tekkimariani.helixia.component.Transform;
import com.tekkimariani.helixia.glfw.GameObject;
import com.tekkimariani.helixia.physics.component.BoxCollider;
import com.tekkimariani.helixia.physics.component.CircleCollider;
import com.tekkimariani.helixia.physics.component.Rigidbody;

/**
 * @author tekki mariani
 *
 */
public class Physics {

	
	private Vec2 gravity = new Vec2(0.0f, -10.0f);
	private World world = new World(gravity);
	
	private float physicsTime = 0.0f;
	private float physicsTimeStep = 1.0f / 60.0f;
	private int velocityIterations = 8;
	private int positionIterations = 3;
	
	public void add(GameObject go) {
		Rigidbody rb = go.getComponent(Rigidbody.class);
		if (rb != null && rb.getRawBody() == null) {
			Transform transform = go.getTransform();
			
			BodyDef bodyDef = new BodyDef();
			bodyDef.angle = (float)Math.toRadians(transform.getRotation());
			bodyDef.position.set(transform.getPosition().x, transform.getPosition().y);
			bodyDef.angularDamping = rb.getAngularDamping();
			bodyDef.linearDamping = rb.getLinearDamping();
			bodyDef.fixedRotation = rb.isFixedRotation();
			bodyDef.bullet = rb.isContinuousCollision();
			
			
		
			switch(rb.getBodyType()) {
				case Kinematic: bodyDef.type = BodyType.KINEMATIC; break;
				case Static: bodyDef.type = BodyType.STATIC; break;
				case Dynamic: bodyDef.type = BodyType.DYNAMIC; break;
			}
			
			PolygonShape shape = new PolygonShape();
			CircleCollider circleCollider;
			BoxCollider boxCollider;
			
			if ((circleCollider = go.getComponent(CircleCollider.class)) != null) {
				shape.setRadius(circleCollider.getRadius());
				
			} else if ((boxCollider = go.getComponent(BoxCollider.class)) != null) {
				Vector2f halfSize = new Vector2f(boxCollider.getHalfSize()).mul(0.5f);
				Vector2f offset = boxCollider.getOffset();
				Vector2f origin = new Vector2f(boxCollider.getOrigin());
				
				shape.setAsBox(halfSize.x, halfSize.y, new Vec2(origin.x, origin.y), 0);
			
			
				Vec2 pos = bodyDef.position;
				float xPos = pos.x + offset.x;
				float yPos = pos.y + offset.y;
				bodyDef.position.set(xPos, yPos);
			
			
			}
			
			Body body = this.world.createBody(bodyDef);
			rb.setRawBody(body);
			body.createFixture(shape, rb.getMass());
		}
	}
	
	public void destroyGameObject(GameObject go) {
		Rigidbody rb = go.getComponent(Rigidbody.class);
		if (rb != null) {
			if (rb.getRawBody() != null) {
				world.destroyBody(rb.getRawBody());
				rb.setRawBody(null);
			}
		}
	}
	
	public void update(float dt) {
		/*
		this.physicsTime += dt;
		if (physicsTime >= 0.0f) {
			this.physicsTime -= this.physicsTimeStep;
			world.step(physicsTime, velocityIterations, positionIterations);
		}
		*/
	}
}
