package fr.ul.rollingball.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Ball {

    public static float NORMAL_RADIUS = (float) GameWorld.DIM_WIDTH / 50;
    public static float SMALL_RADIUS = (float) GameWorld.DIM_WIDTH / 100;

    private float currentRadius;

    private Body body;

    public Ball(Vector2 position, World world) {
        // Définition du rayon
        this.currentRadius = Ball.NORMAL_RADIUS;

        // Construction du body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        this.body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(this.currentRadius);

        fixtureDef.shape = circleShape;
        fixtureDef.density = 1;
        fixtureDef.restitution = 0.25f;
        fixtureDef.friction = 0;

        this.body.createFixture(fixtureDef);

        this.body.setUserData("B");
    }

    public float getRadius() {
        return this.currentRadius;
    }

    public void setRadius(float radius) {
        this.currentRadius = radius;
        this.body.getFixtureList().first().getShape().setRadius(radius);
    }

    Vector2 getPosition() {
        return this.body.getPosition();
    }

    void setPosition(Vector2 position) {
        this.body.setTransform(position, 0);
    }

    public void applyForce(Vector2 force) {
        //this.body.applyForce(force, this.body.getPosition(), true);
        this.body.applyForceToCenter(force, true);
    }

    boolean isOut() {
        Vector2 position = this.getPosition();
        position.x += this.currentRadius / 2f;
        position.y += this.currentRadius / 2f;
        return (position.x > GameWorld.DIM_WIDTH) || (position.x < 0) || (position.y > GameWorld.DIM_HEIGHT) || (position.y < 0);
    }

}
