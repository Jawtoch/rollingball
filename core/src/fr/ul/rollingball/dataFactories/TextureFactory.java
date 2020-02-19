package fr.ul.rollingball.dataFactories;

import com.badlogic.gdx.graphics.Texture;

public class TextureFactory {

    private static String BILLE2D_TEXTURE = "images/Bille2D.png";
    private static String INTRO_TEXTURE = "images/Intro.jpg";
    private static String MURS_TEXTURE = "images/Murs.jpg";
    private static String PASTILLE_NORMALE_TEXTURE = "images/pastilleNormale.bmp";
    private static String PASTILLE_TAILLE_TEXTURE = "images/pastilleTaille.bmp";
    private static String PASTILLE_TEMPS_TEXTURE = "images/pastilleTemps.bmp";
    private static String PISTE_TEXTURE = "images/Piste.jpg";
    private static String VICTOIRE_TEXTURE = "images/Bravo.bmp";
    private static String DEFAITE_TEXTURE = "images/Perte.bmp";

    private Texture bille2DTexture = new Texture(BILLE2D_TEXTURE);
    private Texture introTexture = new Texture(INTRO_TEXTURE);
    private Texture mursTexture = new Texture(MURS_TEXTURE);
    private Texture pastilleNormaleTexture = new Texture(PASTILLE_NORMALE_TEXTURE);
    private Texture pastilleTailleTexture = new Texture(PASTILLE_TAILLE_TEXTURE);
    private Texture pastilleTempsTexture = new Texture(PASTILLE_TEMPS_TEXTURE);
    private Texture pisteTexture = new Texture(PISTE_TEXTURE);
    private Texture victoireTexture = new Texture(VICTOIRE_TEXTURE);
    private Texture defaiteTexture = new Texture(DEFAITE_TEXTURE);

    /**
     * Shared instance
     */
    private static final TextureFactory sharedInstance = new TextureFactory();

    /**
     * Get the shared instance
     * @return TextureFactory' shared instance
     */
    public static TextureFactory textureFactory() {
        return TextureFactory.sharedInstance;
    }

    /**
     * Create instance of TextureFactory
     */
    private TextureFactory() { }

    public Texture getBille2DTexture() {
        return bille2DTexture;
    }

    public Texture getIntroTexture() {
        return introTexture;
    }

    public Texture getMursTexture() {
        return mursTexture;
    }

    public Texture getPastilleNormaleTexture() {
        return pastilleNormaleTexture;
    }

    public Texture getPastilleTailleTexture() {
        return pastilleTailleTexture;
    }

    public Texture getPastilleTempsTexture() {
        return pastilleTempsTexture;
    }

    public Texture getPisteTexture() {
        return pisteTexture;
    }

    public Texture getVictoireTexture() {
        return victoireTexture;
    }

    public Texture getDefaiteTexture() {
        return defaiteTexture;
    }
}
