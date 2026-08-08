package me.bestnuts.drive.core.model.entity.component;

import me.bestnuts.drive.api.model.entity.component.InputProvider;

public class PlayerInputProvider implements InputProvider {

    private float sideway;
    private float forward;
    private boolean jump;

    @Override
    public float getSideway() {
        return sideway;
    }

    @Override
    public void setSideway(float value) {
        sideway = value;
    }

    @Override
    public float getForward() {
        return forward;
    }

    @Override
    public void setForward(float value) {
        forward = value;
    }

    @Override
    public boolean isJump() {
        return jump;
    }

    @Override
    public void setJump(boolean value) {
        jump = value;
    }
}
