package fr.ul.rollingball.models.pastilles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import fr.ul.rollingball.models.GameWorld;

public abstract class Pastille {

    private static float NORMAL_RADIUS = (float) GameWorld.DIM_WIDTH / 128;

    GameWorld gameWorld;

    private Body body;

    private boolean ramassee;

    public Pastille(Vector2 position, GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.ramassee = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);

        World world = this.gameWorld.getWorld();
        this.body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(Pastille.NORMAL_RADIUS);

        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;

        this.body.createFixture(fixtureDef);

        this.body.setUserData(this);
    }

    public Body getBody() {
        return this.body;
    }

    Vector2 getPosition() {
        return this.getBody().getPosition();
    }

    public float getSize() {
        return Pastille.NORMAL_RADIUS;
    }

    public boolean isRamassee() {
        return this.ramassee;
    }

    public void setRamassee(boolean ramassee) {
        this.ramassee = ramassee;
    }

    public abstract void draw(SpriteBatch spriteBatch);

    public abstract void effect();
}
