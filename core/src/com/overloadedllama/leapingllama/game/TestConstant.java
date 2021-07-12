package com.overloadedllama.leapingllama.game;

public interface TestConstant {

    String LLAMA = "llama";

    String LEVEL_LENGTH = "levelLength";

    // main actor Strings
    String ENEMIES = "enemies";
    String COINS = "coins";
    String AMMO = "ammo";
    String PLATFORM1 = "platformI";
    String PLATFORM2 = "platformII";
    String GROUND = "grounds";
    String OBSTACLES = "obstacles";

    // support element Strings
    String COINS_NUM = "coinsNum";
    String AMMO_NUM = "ammoNum";
    String PLATFORM1_LEN = "platformILength";
    String PLATFORM2_LEN = "platformIILength";
    String GROUND_LEN = "groundsLength";

    // Music names
    String GAME_MUSIC1 = "gameMusic1";
    String MAIN_MENU_MUSIC = "mainMenuMusic";

    // Action (and some Sound) names
    String PUNCH = "punch";
    String JUMP = "jump";
    String SHOT = "shot";
    String CROUCH = "crouch";
    String PLAY = "play";
    String PAUSE = "pause";
    String EXIT = "exit";
    String CASH = "cash";
    String LOAD = "load";

    // Filter categories
    short CATEGORY_LLAMA = 0x001;
    short CATEGORY_ENEMY = 0x002;
    short CATEGORY_GROUND = 0x004;
    short CATEGORY_PLATFORM = 0x008;

    // Filter masks
    short MASK_LLAMA = CATEGORY_ENEMY | CATEGORY_GROUND | CATEGORY_PLATFORM;
    short MASK_GROUND = CATEGORY_LLAMA | CATEGORY_ENEMY;
    short MASK_PLATFORM = CATEGORY_ENEMY | CATEGORY_LLAMA;
    short MASK_ENEMY = CATEGORY_GROUND | CATEGORY_LLAMA | CATEGORY_PLATFORM;

}
