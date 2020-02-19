package fr.ul.rollingball.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import fr.ul.rollingball.dataFactories.TextureFactory;

class Ball2D extends Ball {

    private Texture texture;

    Ball2D(Vector2 position, World world) {
        super(position, world);
        TextureFactory textureFactory = TextureFactory.textureFactory();
        this.texture = textureFactory.getBille2DTexture();
    }

    void draw(SpriteBatch spriteBatch) {

        Vector2 position = this.getPosition();
        float size = this.getRadius() * 2;
        spriteBatch.draw(texture, position.x - size / 2, position.y - size / 2, size, size);
    }
}
