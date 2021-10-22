package com.overloadedllama.leapingllama.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.screens.MainMenuScreen;

import java.util.ArrayList;

public class UserStage extends MyAbstractStage {
    float pad = 15f;
    float defaultButtonWidth = 220f;
    float defaultButtonHeight = 100F;
    boolean setUser = false;

    // Table
    private Table chooseUserTable;
    private Table scrollTable;

    // Scroller
    private ScrollPane scrollPane;

    // ImageButton
    private ImageButton backButton;

    // Label
    private Label userLabel;
    private Label titleUserChooser;

    // TextField
    private TextField userTextField;

    // TextButton
    private TextButton deviceUsers;
    private ArrayList<TextButton> userButtons;

    // Skins
    private Skin backButtonSkin;
    private Skin justTextSkin;
    private Skin textButtonFieldLabelSkin;

    public UserStage(LlamaUtil llamaUtil) {
        super(llamaUtil);

        chooseUserTable = new Table();
        scrollTable = new Table();

        justTextSkin = llamaUtil.getAssetManager().getSkin("justText");
        textButtonFieldLabelSkin = llamaUtil.getAssetManager().getSkin("bigButton");
        backButtonSkin = llamaUtil.getAssetManager().getSkin("backButton");

        backButton = new ImageButton(backButtonSkin);
        userLabel = new Label("CURRENT USER: ", justTextSkin);
        userLabel.setAlignment(Align.center);
        userTextField = new TextField("" + currentUser, textButtonFieldLabelSkin);
        userTextField.setAlignment(Align.center);
        chooseUserTable.top();
        chooseUserTable.add(backButton).padTop(pad).align(Align.left);

        titleUserChooser = new Label("USERS", justTextSkin);
        titleUserChooser.setFontScale(2);
        chooseUserTable.add(titleUserChooser).align(Align.right);

        chooseUserTable.row();
        chooseUserTable.add(userLabel).size(defaultButtonWidth, defaultButtonHeight).padRight(15f).padTop(15f);
        chooseUserTable.add(userTextField).size(defaultButtonWidth, defaultButtonHeight).padTop(15f);

        // Users
        deviceUsers = new TextButton("DEVICE USERS", textButtonFieldLabelSkin);
        String[] users = llamaUtil.getLlamaDbHandler().getAllUsers();
        userButtons = new ArrayList<>();
        for (String user : users) {
            userButtons.add(new TextButton(user, textButtonFieldLabelSkin));
        }

        for (TextButton button : userButtons) {
            scrollTable.add(button).size(defaultButtonWidth, defaultButtonHeight).padTop(pad);
            scrollTable.row();
        }
        scrollPane = new ScrollPane(scrollTable);
        chooseUserTable.row();
        chooseUserTable.add(scrollPane).colspan(2).padTop(pad);
        addActor(chooseUserTable);

        setUpButtons();

    }

    void setUpButtons() {

        Gdx.input.setInputProcessor(this);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                llamaUtil.getLlamaDbHandler().insertNewUser(userTextField.getText());
                llamaUtil.setCurrentUser(userTextField.getText());
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(llamaUtil));
            }
        });

        deviceUsers.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });

        for (final TextButton button : userButtons) {
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    setUser = true;
                    System.out.println("STRING: " + button.getText().toString());
                    llamaUtil.setCurrentUser(button.getText().toString());
                    System.out.println("CURRENT USER: " + llamaUtil.getCurrentUser());

                }
            });
        }
    }

    public void renderer() {
        super.renderer();
        if (setUser) {
            userTextField.setText(llamaUtil.getCurrentUser());
            setUser = false;
        }
    }

    public void resizer() {
        chooseUserTable.invalidateHierarchy();
        chooseUserTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        scrollTable.invalidateHierarchy();
        scrollTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);
    }

}
