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
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.screens.MainMenuScreen;

public class SettingStage extends MyAbstractStage {

    private final Table settingTable;
    private final Table backButtonTable;

    private final String ON = "on";
    private final String OFF = "off";

    private final ImageButton backButton;

    // TextButtons
    private final TextButton musicButton;
    private final TextButton soundButton;
    private final TextButton lxDxButton;
    private final TextButton goreButton;

    // Skins
    private final Skin textButtonSkin;
    private final Skin backButtonSkin;


    public SettingStage(LlamaUtil llamaUtil) {
        super(llamaUtil);

        settingTable = new Table();
        backButtonTable = new Table();

        // creation of the Skins
        textButtonSkin = llamaUtil.getAssetManager().getSkin("bigButton");
        backButtonSkin = llamaUtil.getAssetManager().getSkin("backButton");

        // creation of TextButtons
        musicButton = new TextButton("MUSIC: " + llamaUtil.getStringSetting(MUSIC), textButtonSkin);
        soundButton = new TextButton("SOUND: " + llamaUtil.getStringSetting(SOUND), textButtonSkin);
        lxDxButton = new TextButton(llamaUtil.getStringSetting(GAME_MODE_), textButtonSkin);
        goreButton = new TextButton("GORE: " + llamaUtil.getStringSetting(GORE), textButtonSkin);
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

    void setUpButtons() {
        Gdx.input.setInputProcessor(this);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(llamaUtil));
            }
        });

        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (llamaUtil.getMusicManager().isMUSIC()) {
                    llamaUtil.getMusicManager().setMUSIC(false);
                    llamaUtil.getMusicManager().stopMusic(MAIN_MENU_MUSIC);
                    musicButton.getLabel().setText("MUSIC: " + OFF);
                } else {
                    llamaUtil.getMusicManager().setMUSIC(true);
                    llamaUtil.getMusicManager().playMusic(MAIN_MENU_MUSIC);
                    musicButton.getLabel().setText("MUSIC: " + ON);
                }
            }
        });

        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (llamaUtil.getSoundManager().isSOUND()) {
                    llamaUtil.getSoundManager().setSOUND(false);
                    soundButton.getLabel().setText("SOUND: " + OFF);
                } else {
                    llamaUtil.getSoundManager().setSOUND(true);
                    soundButton.getLabel().setText("SOUND: " + ON);
                }
            }
        });

        lxDxButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (llamaUtil.getStringSetting(GAME_MODE_).equals("LEFT HANDED")) {
                    llamaUtil.getGameplayManager().setGameMode();
                    lxDxButton.setText("RIGHT HANDED");
                } else if (llamaUtil.getStringSetting(GAME_MODE_).equals("RIGHT HANDED")){
                    llamaUtil.getGameplayManager().setGameMode();
                    lxDxButton.setText("GESTURES");
                } else {
                    llamaUtil.getGameplayManager().setGameMode();
                    lxDxButton.setText("LEFT HANDED");
                }
            }
        });

        goreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (llamaUtil.getGameplayManager().isGORE()) {
                    llamaUtil.getGameplayManager().setGORE(false);
                    goreButton.setText("GORE: " + OFF);
                } else {
                    llamaUtil.getGameplayManager().setGORE(true);
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
