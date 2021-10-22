package com.overloadedllama.leapingllama.llamautils;


import com.badlogic.gdx.physics.box2d.World;

public class GameplayManager implements Manager {
    private static GameplayManager uniqueInstance;

    private enum GAME_MODE
    {
        LX_DX,
        DX_LX,
        GESTURES
    }
    private GAME_MODE gameMode = GAME_MODE.LX_DX;
    private boolean GORE = true;


    // GamePlay
    private boolean bonusAmmo = false;
    private boolean bonusLife = false;

    // Background
    private float xSky = 0;

    // World
    private World world;



    private GameplayManager() {

    }

    public static GameplayManager getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GameplayManager();
        }
        return uniqueInstance;
    }




    public String getGameMode() {
        if (gameMode == GAME_MODE.LX_DX) {
            return "LEFT HANDED";
        } else if (gameMode == GAME_MODE.DX_LX) {
            return "RIGHT HANDED";
        } else {
            return "GESTURES";
        }
    }

    public void setGameMode() {
        if (gameMode == GAME_MODE.LX_DX) {
            gameMode = GAME_MODE.DX_LX;
        } else if (gameMode == GAME_MODE.DX_LX) {
            gameMode = GAME_MODE.GESTURES;
        } else {
            gameMode = GAME_MODE.LX_DX;
        }
    }

    // GORE
    public boolean isGORE() { return GORE; }
    public void setGORE(boolean GORE) { this.GORE = GORE; }

    // BONUS AMMO
    public boolean hasBonusAmmo() {
        return bonusAmmo;
    }
    public void setBonusAmmo() {
        bonusAmmo = !bonusAmmo;
    }

    // BONUS LIFE
    public boolean hasBonusLife() {
        return bonusLife;
    }
    public void setBonusLife() {
        bonusLife = !bonusLife;
    }

    // BACKGROUND
    public void setXSky(float xSky) {
        this.xSky = xSky;
    }
    public float getXSky() {
        return xSky;
    }

    // WORLD
    public World getWorld() { return world; }
    public void setWorld(World world) { this.world = world; }
}
