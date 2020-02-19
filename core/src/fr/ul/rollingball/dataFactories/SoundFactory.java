package fr.ul.rollingball.dataFactories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundFactory {

    private static String ALERTE_SOUND = "sounds/alerte.mp3";
    private static String COLLISION_SOUND = "sounds/collision.wav";
    private static String PASTILLE_SOUND = "sounds/pastille.wav";
    private static String PERTE_SOUND = "sounds/perte.mp3";
    private static String PTAILLE_SOUND = "sounds/ptaille.wav";
    private static String VICTOIRE_SOUND = "sounds/victoire.mp3";

    private Sound alerteSound = Gdx.audio.newSound(Gdx.files.internal(ALERTE_SOUND));
    private Sound collisionSound = Gdx.audio.newSound(Gdx.files.internal(COLLISION_SOUND));
    private Sound pastilleSound = Gdx.audio.newSound(Gdx.files.internal(PASTILLE_SOUND));
    private Sound perteSound = Gdx.audio.newSound(Gdx.files.internal(PERTE_SOUND));
    private Sound pTailleSound = Gdx.audio.newSound(Gdx.files.internal(PTAILLE_SOUND));
    private Sound victoireSound = Gdx.audio.newSound(Gdx.files.internal(VICTOIRE_SOUND));

    private static final SoundFactory sharedInstance = new SoundFactory();

    public static SoundFactory soundFactory() {
        return SoundFactory.sharedInstance;
    }

    private SoundFactory() {
    }

    public Sound getAlerteSound() {
        return alerteSound;
    }

    public Sound getCollisionSound() {
        return collisionSound;
    }

    public Sound getPastilleSound() {
        return pastilleSound;
    }

    public Sound getPerteSound() {
        return perteSound;
    }

    public Sound getpTailleSound() {
        return pTailleSound;
    }

    public Sound getVictoireSound() {
        return victoireSound;
    }
}
