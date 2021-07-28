package com.overloadedllama.leapingllama.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.resources.Settings;
import com.overloadedllama.leapingllama.screens.MainMenuScreen;

public class SettingStage extends MyAbstractStage {

    private Table settingTable;
    private Table backButtonTable;

    private final String ON = "on";
    private final String OFF = "off";

    private ImageButton backButton;

    // TextButtons
    private TextButton musicButton;
    private TextButton soundButton;
    private TextButton lxDxButton;
    private TextButton goreButton;

    // Skins
    private Skin textButtonSkin;
    private Skin backButtonSkin;


    public SettingStage(GameApp gameApp) {
        super(gameApp);

        settingTable = new Table();
        backButtonTable = new Table();

        // creation of the Skins
        textButtonSkin = assets.getSkin("bigButton");
        backButtonSkin = assets.getSkin("backButton");

        // creation of TextButtons
        musicButton = new TextButton("MUSIC: " + Settings.getStringSetting(MUSIC), textButtonSkin);
        soundButton = new TextButton("SOUND: " + Settings.getStringSetting(SOUND), textButtonSkin);
        lxDxButton = new TextButton(Settings.getStringSetting(GAME_MODE_), textButtonSkin);
        goreButton = new TextButton("GORE: " + Settings.getStringSetting(GORE), textButtonSkin);
        backButton = new ImageButton(backButtonSkin);
        backButton.setDisabled(false);

        // adding items to settingTable and backButtonTable and them to settingsStage
        float w = 300f, h = 120f, pad = 15f;
        settingTable.add(musicButton).width(w).height(h);
        settingTable.row();
        settingTable.add(soundButton).width(w).height(h).padTop(pad);
        settingTable.row();
        settingTable.add(lxDxButton).width(w).height(h).padTop(pad);
        settingTable.row();
        settingTable.add(goreButton).width(w).height(h).padTop(pad);
        addActor(settingTable);

        backButtonTable.top().left();
        backButtonTable.add(backButton).width(h).height(h).padLeft(pad).padTop(pad);
        addActor(backButtonTable);

        setUpButtons();
    }

    private void setUpButtons() {
        Gdx.input.setInputProcessor(this);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(gameApp));
            }
        });

        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.isMUSIC()) {
                    Settings.setMUSIC(false);
                    Settings.stopMusic(MAIN_MENU_MUSIC);
                    musicButton.getLabel().setText("MUSIC: " + OFF);
                } else {
                    Settings.setMUSIC(true);
                    Settings.playMusic(MAIN_MENU_MUSIC);
                    musicButton.getLabel().setText("MUSIC: " + ON);
                }
            }
        });

        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.isSOUND()) {
                    Settings.setSOUND(false);
                    soundButton.getLabel().setText("SOUND: " + OFF);
                } else {
                    Settings.setSOUND(true);
                    soundButton.getLabel().setText("SOUND: " + ON);
                }
            }
        });

        lxDxButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.getStringSetting(GAME_MODE_).equals("LEFT HANDED")) {
                    Settings.setGameMode();
                    lxDxButton.setText("RIGHT HANDED");
                } else if (Settings.getStringSetting(GAME_MODE_).equals("RIGHT HANDED")){
                    Settings.setGameMode();
                    lxDxButton.setText("GESTURES");
                } else {
                    Settings.setGameMode();
                    lxDxButton.setText("LEFT HANDED");
                }
            }
        });

        goreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (Settings.isGORE()) {
                    Settings.setGORE(false);
                    goreButton.setText("GORE: " + OFF);
                } else {
                    Settings.setGORE(true);
                    goreButton.setText("GORE: " + ON);
                }
            }
        });
    }

    @Override
    public void renderer() {
        super.renderer();

    }

    public void resizer() {
        settingTable.invalidateHierarchy();
        settingTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        backButtonTable.invalidateHierarchy();
        backButtonTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);
    }
}
