package fr.ul.rollingball.models.pastilles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import fr.ul.rollingball.RollingBall;
import fr.ul.rollingball.dataFactories.SoundFactory;
import fr.ul.rollingball.dataFactories.TextureFactory;
import fr.ul.rollingball.models.GameWorld;
import fr.ul.rollingball.views.GameScreen;

public class TimePastille extends Pastille {

    private Texture texture;

    public TimePastille(Vector2 position, GameWorld gameWorld) {
        super(position, gameWorld);

        TextureFactory textureFactory = TextureFactory.textureFactory();
        this.texture = textureFactory.getPastilleTempsTexture();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if (!isRamassee()) {
            Vector2 position = getPosition();
            float size = getSize() * 2;
            spriteBatch.draw(this.texture, position.x - size / 2, position.y - size / 2, size, size);
        }
    }

    @Override
    public void effect() {
        SoundFactory soundFactory = SoundFactory.soundFactory();
        Sound pastilleSound = soundFactory.getPastilleSound();
        pastilleSound.play(RollingBall.SOUND_VOLUME);

        if (this.gameWorld.getCurrentScreen() instanceof GameScreen) {
            GameScreen gameScreen = (GameScreen) this.gameWorld.getCurrentScreen();
            gameScreen.addRemainingTime(10);
            gameScreen.addSwallowedPastilles();
        }
    }
}
