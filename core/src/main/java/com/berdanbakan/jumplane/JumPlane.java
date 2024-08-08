package com.berdanbakan.jumplane;

import com.badlogic.gdx.Game;

public class JumPlane extends Game {
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this)); // Start with the main menu screen
    }


    @Override
    public void dispose() {
        getScreen().dispose();
    }
}
