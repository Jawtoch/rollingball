package fr.ul.rollingball.models;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import fr.ul.rollingball.RollingBall;
import fr.ul.rollingball.dataFactories.SoundFactory;
import fr.ul.rollingball.dataFactories.TextureFactory;
import fr.ul.rollingball.models.pastilles.Pastille;
import fr.ul.rollingball.views.GameScreen;

public class GameWorld {

    public static int DIM_WIDTH = 80;
    public static int DIM_HEIGHT = 60;

    private ScreenAdapter currentScreen;

    private World world;

    private Ball2D ball2D;

    private Texture backgroundTexture;

    private ArrayList<Pastille> pastilles;

    private Maze maze;

    private Sound collisionSound;

    public GameWorld() {

        this.world = new World(new Vector2( 0, 0), true);

        TextureFactory textureFactory = TextureFactory.textureFactory();
        this.backgroundTexture = textureFactory.getPisteTexture();

        this.pastilles = new ArrayList<>();

        this.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body firstBody = contact.getFixtureA().getBody();
                Body secondBody = contact.getFixtureB().getBody();

                if ((firstBody.getUserData() instanceof Pastille) || (secondBody.getUserData() instanceof Pastille)) {
                    // La collision concerne une pastille

                    Pastille pastille;

                    if (firstBody.getUserData() instanceof Pastille) {
                        pastille = (Pastille) firstBody.getUserData();
                    } else {
                        pastille = (Pastille) secondBody.getUserData();
                    }
                    pastille.setRamassee(true);
                }

                if ((firstBody.getUserData() == "M") || (secondBody.getUserData() == "M")) {
                    collisionSound.play(RollingBall.SOUND_VOLUME);
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        this.maze = new Maze(0, this);
        this.maze.loadLaby(this.pastilles);

        Vector2 initialPosition = this.maze.getInitialPosition();
        this.ball2D = new Ball2D(initialPosition, this.world);

        SoundFactory soundFactory = SoundFactory.soundFactory();
        this.collisionSound = soundFactory.getCollisionSound();

    }

    public Ball2D getBall2D() {
        return this.ball2D;
    }

    public void setCurrentScreen(ScreenAdapter currentScreen) {
        this.currentScreen = currentScreen;
    }

    public ScreenAdapter getCurrentScreen() {
        return currentScreen;
    }

    public void draw(SpriteBatch spriteBatch) {
        //spriteBatch.draw(this.backgroundTexture, 0, 0, GameWorld.DIM_WIDTH, GameWorld.DIM_HEIGHT);

        this.maze.draw(spriteBatch);

        this.ball2D.draw(spriteBatch);

        for(Pastille pastille: this.pastilles) {
            pastille.draw(spriteBatch);
        }
    }

    public World getWorld() {
        return this.world;
    }

    public void update() {

        ArrayList<Pastille> deletedPastilles = new ArrayList<>();

        for(Pastille pastille: this.pastilles) {
            if (pastille.isRamassee()) {
                pastille.effect();
                world.destroyBody(pastille.getBody());
                deletedPastilles.add(pastille);
            }
        }

        this.pastilles.removeAll(deletedPastilles);
    }

    public void dispose() {
        this.backgroundTexture.dispose();
        this.maze.dispose();
    }

    public void changeLaby() {
        for(Pastille pastille: this.pastilles) {
            this.world.destroyBody(pastille.getBody());
        }

        this.pastilles.clear();

        this.maze.nextLaby();

        this.maze.loadLaby(this.pastilles);

        this.ball2D.setPosition(this.maze.getInitialPosition());

        this.ball2D.applyForce(new Vector2(0, 0));

        if (this.currentScreen instanceof GameScreen) {
            GameScreen gameScreen = (GameScreen) this.currentScreen;
            gameScreen.getKeyboardListener().resetKeyboardAcceleration();
        }
    }

    public boolean isVictory() {
        return this.getBall2D().isOut();
    }
}
