package com.berdanbakan.jumplane;

import com.badlogic.gdx.Game;

public class JumPlane extends Game {
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        getScreen().dispose();
    }
}
