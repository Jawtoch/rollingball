package fr.ul.rollingball.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

import fr.ul.rollingball.RollingBall;
import fr.ul.rollingball.dataFactories.SoundFactory;

public class GameState {

    private static int AVAILABLE_TIME = 20;

    public enum States {
        ONGOING,
        VICTORY,
        DEFEAT,
        STOP,
    }

    private int score;

    private int reamingTime;

    private int swallowedPastilles;

    private Timer.Task timeCount;

    private States currentState;

    private SoundFactory soundFactory = SoundFactory.soundFactory();

    private Sound alertSound = soundFactory.getAlerteSound();

    private Sound victoireSound = soundFactory.getVictoireSound();

    private Sound perteSound = soundFactory.getPerteSound();

    public GameState() {
        this.timeCount = new Timer.Task() {
            @Override
            public void run() {
                countDown();
                Gdx.app.log("Timer", String.valueOf(reamingTime));
            }
        };

        this.score = 0;
        this.reamingTime = GameState.AVAILABLE_TIME;
        this.swallowedPastilles = 0;

        this.currentState = States.ONGOING;

        Timer.schedule(this.timeCount, 1, 1);
    }

    public int getScore() {
        return score;
    }

    public int getReamingTime() {
        return reamingTime;
    }

    public int getSwallowedPastilles() {
        return swallowedPastilles;
    }

    public States getCurrentState() {
        return currentState;
    }

    public void setState(GameState.States state) {
        this.currentState = state;
        if ((state == States.VICTORY) || (state == States.DEFEAT)) {
            this.timeCount.cancel();
        }

        if (state == States.VICTORY) {
            this.victoireSound.play(RollingBall.SOUND_VOLUME);
            Timer.schedule(this.timeCount, 3, 1);
        }

        if (state == States.DEFEAT) {
            this.perteSound.play(RollingBall.SOUND_VOLUME);
            this.reamingTime = GameState.AVAILABLE_TIME;
            this.score = 0;
            this.swallowedPastilles = 0;
            Timer.schedule(this.timeCount, 3, 1);
        }
    }

    private void countDown() {
        this.reamingTime -= 1;

        if (this.reamingTime < 0) {
            this.setState(States.DEFEAT);
        } else if (this.reamingTime <= 10) {
            this.alertSound.play();
        }
    }

    public void addRemainingTime(int seconds) {
        this.reamingTime += seconds;
    }

    public void addScore() {
        this.score += 1;
    }

    public void addSwallowedPastilles() {
        this.swallowedPastilles += 1;
    }

}
