package fr.ul.rollingball.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

import fr.ul.rollingball.controllers.KeyboardListener;
import fr.ul.rollingball.dataFactories.TextureFactory;
import fr.ul.rollingball.models.Ball;
import fr.ul.rollingball.models.GameState;
import fr.ul.rollingball.models.GameWorld;

public class GameScreen extends ScreenAdapter {

    private SpriteBatch spriteBatch;

    private GameWorld gameWorld;

    private Box2DDebugRenderer box2DDebugRenderer;

    private KeyboardListener keyboardListener;

    private GameState gameState;

    private SpriteBatch textsSpriteBatch;

    private BitmapFont font;

    private Timer.Task nextLabyrintheTask;

    public GameScreen() {
        super();

        this.gameWorld = new GameWorld();
        this.gameWorld.setCurrentScreen(this);

        OrthographicCamera orthographicCamera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        orthographicCamera.setToOrtho(false, GameWorld.DIM_WIDTH, GameWorld.DIM_HEIGHT);
        orthographicCamera.update();

        this.spriteBatch = new SpriteBatch();
        this.spriteBatch.setProjectionMatrix(orthographicCamera.combined);

        this.box2DDebugRenderer = new Box2DDebugRenderer();

        this.keyboardListener = new KeyboardListener();
        Gdx.input.setInputProcessor(keyboardListener);

        this.gameState = new GameState();

        this.textsSpriteBatch = new SpriteBatch();

        this.nextLabyrintheTask = new Timer.Task() {
            @Override
            public void run() {
                if ((gameState.getCurrentState() == GameState.States.DEFEAT) || (gameState.getCurrentState() == GameState.States.VICTORY)) {
                    gameWorld.changeLaby();
                    gameState.setState(GameState.States.ONGOING);
                }
            }
        };

    }

    @Override
    public void render(float delta) {

        update();

        GameState.States currentState = getCurrentState();

        if (currentState != GameState.States.ONGOING) {

            this.textsSpriteBatch.begin();

            if (currentState == GameState.States.VICTORY) {
                this.victoire(textsSpriteBatch);
            } else {
                this.defaite(textsSpriteBatch);
            }

            this.textsSpriteBatch.end();
        } else {

            this.spriteBatch.begin();
            this.gameWorld.draw(this.spriteBatch);
            this.spriteBatch.end();

            this.textsSpriteBatch.begin();

            this.font.draw(this.textsSpriteBatch, "Temps restant: " + this.gameState.getReamingTime(), 0, Gdx.graphics.getHeight() - 23);
            this.font.draw(this.textsSpriteBatch, "Score: " + this.gameState.getScore(), 0, Gdx.graphics.getHeight() - 23 - 60);

            if (this.keyboardListener.isDebug()) {
                this.box2DDebugRenderer.render(this.gameWorld.getWorld(), spriteBatch.getProjectionMatrix());
            }

            this.textsSpriteBatch.end();
        }
    }

    @Override
    public void dispose() {
        this.spriteBatch.dispose();
        this.textsSpriteBatch.dispose();
        this.font.dispose();
        this.gameWorld.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        Camera textsCamera = new OrthographicCamera(width, height);
        textsCamera.position.set(width / 2, height / 2 - 23, 1);
        textsCamera.update();
        this.textsSpriteBatch.setProjectionMatrix(textsCamera.combined);

        FileHandle fontFile = Gdx.files.internal("fonts/Comic_Sans_MS_Bold.ttf");
        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(fontFile);

        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.size = (int) ((40f / 1024f) * (float) width);

        Color color = Color.YELLOW;
        color.a = 0.75f;
        freeTypeFontParameter.color = color;

        freeTypeFontParameter.borderColor = Color.BLACK;
        freeTypeFontParameter.borderWidth = (int) ((3f / 1024f) * (float) width);

        this.font = freeTypeFontGenerator.generateFont(freeTypeFontParameter);
        freeTypeFontGenerator.dispose();

    }

    private void update() {
        World world = this.gameWorld.getWorld();
        world.step(1f, 6, 2);

        this.gameWorld.update();

        float factor = 0.06f;

        Vector2 vector = new Vector2(
                factor * Gdx.input.getAccelerometerY(),
                -1 * factor * Gdx.input.getAccelerometerX()
        );

        Ball ball = this.gameWorld.getBall2D();
        ball.applyForce(vector);

        ball.applyForce(this.keyboardListener.getKeyboardAcceleration());


        // States
        GameState.States currentState = getCurrentState();

        if (this.gameWorld.isVictory()) {
            if (currentState != GameState.States.VICTORY) {
                this.gameState.setState(GameState.States.VICTORY);
            }
        }

        if(getKeyboardListener().isWantToQuit()) {
            this.gameState.setState(GameState.States.STOP);
        }

        if (currentState == GameState.States.STOP) {
            Gdx.app.exit();
        }

        if (currentState != GameState.States.ONGOING) {
            if(!this.nextLabyrintheTask.isScheduled()) {
                Timer.schedule(this.nextLabyrintheTask, 3);
            }
        }

    }

    public KeyboardListener getKeyboardListener() {
        return keyboardListener;
    }

    private GameState.States getCurrentState() {
        return this.gameState.getCurrentState();
    }

    public void addRemainingTime(int seconds) {
        this.gameState.addRemainingTime(seconds);
    }

    public void addScore() {
        this.gameState.addScore();
    }

    public void addSwallowedPastilles() {
        this.gameState.addSwallowedPastilles();
    }

    @Override
    public void show() {
        super.show();
        this.gameState.setState(GameState.States.ONGOING);
    }

    private void victoire(SpriteBatch spriteBatch) {

        if (this.gameState.getCurrentState() == GameState.States.VICTORY) {
            TextureFactory textureFactory = TextureFactory.textureFactory();
            Texture texture = textureFactory.getVictoireTexture();
            Graphics graphics = Gdx.graphics;
            int width = graphics.getWidth();
            int height = graphics.getHeight();
            spriteBatch.draw(texture,  (width - texture.getWidth()) / 2,(height - texture.getHeight()) / 2, width / 2, height / 2);
        }
    }

    private void defaite(SpriteBatch spriteBatch) {

        if (this.gameState.getCurrentState() == GameState.States.DEFEAT) {
            TextureFactory textureFactory = TextureFactory.textureFactory();
            Texture texture = textureFactory.getDefaiteTexture();
            Graphics graphics = Gdx.graphics;
            int width = graphics.getWidth();
            int height = graphics.getHeight();
            spriteBatch.draw(texture,  (width - texture.getWidth()) / 2,(height - texture.getHeight()) / 2, width / 2, height / 2);
        }
    }
}
