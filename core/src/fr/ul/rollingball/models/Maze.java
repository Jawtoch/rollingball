package fr.ul.rollingball.models;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import fr.ul.rollingball.dataFactories.TextureFactory;
import fr.ul.rollingball.models.pastilles.Pastille;
import fr.ul.rollingball.models.pastilles.ScorePastille;
import fr.ul.rollingball.models.pastilles.TaillePastille;
import fr.ul.rollingball.models.pastilles.TimePastille;

public class Maze {

    private static float WALL_SIZE = 0.09f;

    private GameWorld gameWorld;

    private int mazeId;

    private Pixmap pixmap;

    private Texture texture;

    private Vector2 initialPosition;

    private ArrayList<Body> bodies;

    Maze(int mazeId, GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.mazeId = mazeId;
        if ((mazeId < 0) || (mazeId > 5)) throw new AssertionError("MazeID invalide");
        this.bodies = new ArrayList<>();
    }

    public Texture getTexture() {
        return this.texture;
    }

    Vector2 getInitialPosition() {
        return this.initialPosition;
    }

    void draw(SpriteBatch spriteBatch) {

        /*for(Body body: this.bodies) {
            Vector2 position = body.getPosition();
            spriteBatch.draw(texture, position.x - Maze.WALL_SIZE / 2, position.y - Maze.WALL_SIZE / 2, Maze.WALL_SIZE, Maze.WALL_SIZE);
        }*/

        spriteBatch.draw(this.texture, 0, 0, 80, 60);

    }

    void dispose() {
        this.pixmap.dispose();
        this.texture.dispose();
    }

    void loadLaby(ArrayList<Pastille> pastilles) {
        World world = this.gameWorld.getWorld();

        for(Body mur: this.bodies) {
            world.destroyBody(mur);
        }

        this.bodies.clear();

        readMasque();

        readObjects(pastilles);

        buildTexLaby();

    }

    private void readMasque() {
        this.pixmap = new Pixmap(Gdx.files.internal("images/Laby" + this.mazeId + ".png"));
    }

    private int getPixel(int colonne, int ligne) {
        return pixmap.getPixel(colonne, ligne) + 256;
    }

    private void readObjects(ArrayList<Pastille> pastilles) {

        // (0, 0)   (1, 0)  (2, 0)
        // (0, 1)   (1, 1)  (2, 1)
        // (0, 2)   (1, 2)  (2, 2)
        // X, Y
        // COLONNE, LIGNE
        // WIDTH, HEIGHT

        Pixmap pixmap = this.pixmap;

        for (int colonne = 0; colonne < pixmap.getWidth(); colonne++) {

            for (int ligne = 0; ligne < pixmap.getHeight(); ligne++) {

                int pixel = getPixel(colonne, ligne);

                switch (pixel) {

                    case 0:
                        // Murs
                        mur(colonne, ligne);
                        break;
                    case 100:
                        // Position initiale de la bille
                        positionInitiale(colonne, ligne);
                        break;
                    case 128:
                    case 200:
                    case 225:
                        pastille(colonne, ligne, pixel, pastilles);
                        break;
                    case 255:
                    default:
                        // Zone vide
                        break;

                }
            }
        }

    }

    private void mur(int colonne, int ligne) {
        if (testMur(colonne, ligne)) {
            // C'est un mur intéressant
            addMur(colonne, ligne);
        }
    }

    private boolean testMur(int colonne, int ligne) {

        boolean result = false;
        int pixel;

        if (colonne < pixmap.getHeight()) {
            pixel = pixmap.getPixel(colonne + 1, ligne) + 256;
            result = pixel == 255;
        }

        if (colonne > 0) {
            pixel = pixmap.getPixel(colonne - 1, ligne) + 256;
            result |= pixel == 255;
        }

        if (ligne < pixmap.getWidth()) {
            pixel = pixmap.getPixel(colonne, ligne + 1) + 256;
            result |= pixel == 255;
        }

        if (ligne > 0) {
            pixel = pixmap.getPixel(colonne, ligne - 1) + 256;
            result |= pixel == 255;
        }

        return result;
    }

    private void addMur(int colonne, int ligne) {

        World world = this.gameWorld.getWorld();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(toWorldCoords(colonne, ligne));

        Body body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(Maze.WALL_SIZE);

        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.restitution = 0.25f;
        fixtureDef.friction = 0;

        body.createFixture(fixtureDef);

        body.setUserData("M");

        this.bodies.add(body);
    }

    private void pastille(int colonne, int ligne, int pixel, ArrayList<Pastille> pastilles) {
        Vector2 position = toWorldCoords(colonne + 5, ligne + 1);

        Pastille pastille;
        switch (pixel) {
            case 128:
                pastille = new ScorePastille(position, this.gameWorld);
                break;
            case 200:
                pastille = new TaillePastille(position, this.gameWorld);
                break;
            case 225:
            default:
                pastille = new TimePastille(position, this.gameWorld);
                break;
        }

        pastilles.add(pastille);
        correctPixmap(colonne, ligne);
    }

    private void positionInitiale(int colonne, int ligne) {
        Vector2 position = toWorldCoords(colonne + 5, ligne + 1);
        setInitialPosition(position);
        correctPixmap(colonne, ligne);
    }

    private Vector2 toWorldCoords(int colonne, int ligne) {
        Vector2 vector2 = new Vector2();
        vector2.x = (float) colonne * (float) GameWorld.DIM_WIDTH / (float) this.pixmap.getWidth();
        vector2.y = ((float) this.pixmap.getHeight() - (float) ligne) * (float) GameWorld.DIM_HEIGHT / (float) this.pixmap.getHeight();

        return vector2;
    }

    private void correctPixmap(int colonne, int ligne) {
        // (0, 0)   (1, 0)  (2, 0)
        // (0, 1)   (1, 1)  (2, 1)
        // (0, 2)   (1, 2)  (2, 2)
        // X, Y
        // COLONNE, LIGNE
        // WIDTH, HEIGHT

        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(colonne + 5, ligne + 1, 5);
    }

    private void setInitialPosition(Vector2 initialPosition) {
        this.initialPosition = initialPosition;
    }

    private void buildTexLaby() {
        TextureFactory textureFactory = TextureFactory.textureFactory();

        Texture decorTexture = textureFactory.getMursTexture();
        Texture pisteTexture = textureFactory.getPisteTexture();

        TextureData decorTextureData = decorTexture.getTextureData();
        TextureData pisteTextureData = pisteTexture.getTextureData();

        decorTextureData.prepare();
        pisteTextureData.prepare();

        Pixmap decorPixmap = decorTexture.getTextureData().consumePixmap();
        Pixmap pistePixmap = pisteTexture.getTextureData().consumePixmap();

        for (int colonne = 0; colonne < pixmap.getWidth(); colonne++) {

            for (int ligne = 0; ligne < pixmap.getHeight(); ligne++) {
                int pixel = getPixel(colonne, ligne);

                if (pixel != 0) {
                    // Ce n'est pas un mur
                    Color color = new Color();
                    Color.rgba8888ToColor(color, pistePixmap.getPixel(colonne, ligne));
                    color.a = 1f;
                    color.r /= 4f;
                    color.g /= 4f;
                    color.b /= 4f;

                    decorPixmap.setColor(color);
                    decorPixmap.drawPixel(colonne, ligne);
                }
            }
        }

        this.texture = new Texture(decorPixmap);
        decorPixmap.dispose();
        pistePixmap.dispose();
    }

    void nextLaby() {
        this.mazeId += 1;
        if (mazeId > 5) {
            mazeId = 0;
        }

    }
}
