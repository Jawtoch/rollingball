package fr.ul.rollingball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

import fr.ul.rollingball.dataFactories.SoundFactory;
import fr.ul.rollingball.views.GameScreen;
import fr.ul.rollingball.views.SplashScreen;

public class RollingBall extends Game {

	public static float SOUND_VOLUME = 0.8f;

	private SplashScreen splashScreen;
	private GameScreen gameScreen;

	@Override
	public void create () {
		this.splashScreen = new SplashScreen();
		Gdx.graphics.setContinuousRendering(false);
		this.setScreen(this.splashScreen);

		Sound victoireSound = SoundFactory.soundFactory().getVictoireSound();
		victoireSound.play(RollingBall.SOUND_VOLUME);

		Timer timer = new Timer();
		Timer.Task task = new Timer.Task() {
			@Override
			public void run() {
				Gdx.graphics.setContinuousRendering(true);
				gameScreen = new GameScreen();
				setScreen(gameScreen);
			}
		};

		timer.scheduleTask(task, 3);
		timer.start();
	}

	@Override
	public void dispose () {
		this.splashScreen.dispose();
		this.gameScreen.dispose();
	}
}
