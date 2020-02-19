package fr.ul.rollingball.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class KeyboardListener implements InputProcessor {

    private static float ACCELERATION_MULTIPLIER = 0.4f;

    private boolean wantToQuit;

    private boolean isDebug;

    private Vector2 keyboardAcceleration;

    public KeyboardListener() {
        this.wantToQuit = false;
        this.isDebug = false;
        this.keyboardAcceleration = new Vector2();
    }

    public boolean isWantToQuit() {
        return wantToQuit;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public Vector2 getKeyboardAcceleration() {
        return keyboardAcceleration;
    }

    public void resetKeyboardAcceleration() {
        this.keyboardAcceleration = new Vector2();
    }

    @Override
    public boolean keyDown(int keycode) {

        if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.Q)) {
            return false;
        }

        switch (keycode) {
            case Input.Keys.LEFT:
                this.keyboardAcceleration.x = -1 * KeyboardListener.ACCELERATION_MULTIPLIER;
                break;
            case Input.Keys.RIGHT:
                this.keyboardAcceleration.x = KeyboardListener.ACCELERATION_MULTIPLIER;
                break;
            case Input.Keys.UP:
                this.keyboardAcceleration.y = KeyboardListener.ACCELERATION_MULTIPLIER;
                break;
            case Input.Keys.DOWN:
                this.keyboardAcceleration.y = -1 * KeyboardListener.ACCELERATION_MULTIPLIER;
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.RIGHT:
            case Input.Keys.UP:
            case Input.Keys.DOWN:
                this.resetKeyboardAcceleration();
                break;
            case Input.Keys.D:
                this.isDebug = !this.isDebug;
                break;
            case Input.Keys.A:
            case Input.Keys.ESCAPE:
                this.wantToQuit = true;
                break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
