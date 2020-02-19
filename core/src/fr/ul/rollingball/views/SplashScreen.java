package fr.ul.rollingball.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.ul.rollingball.dataFactories.TextureFactory;

public class SplashScreen extends ScreenAdapter {

    private SpriteBatch spriteBatch;
    private Texture introTexture;

    public SplashScreen() {
        this.spriteBatch = new SpriteBatch();
        TextureFactory textureFactory = TextureFactory.textureFactory();
        this.introTexture = textureFactory.getIntroTexture();
    }

    @Override
    public void render(float delta) {

        Graphics graphics = Gdx.graphics;
        OrthographicCamera orthographicCamera = new OrthographicCamera(graphics.getWidth(), graphics.getHeight());
        orthographicCamera.setToOrtho(false,1024, 720);
        orthographicCamera.update();

        this.spriteBatch.setProjectionMatrix(orthographicCamera.combined);

        this.spriteBatch.begin();
        this.spriteBatch.draw(this.introTexture, 0, 0);
        this.spriteBatch.end();
    }

    @Override
    public void dispose() {
        this.spriteBatch.dispose();
    }
}
