package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Tube {
    Sprite tube, llama;
    float x, wTube, wLlama;
    float y = 5;
    int direction = -1;
    boolean animationFinished;

    public Tube(float x, float wTube, float hLlama) {
        tube = new Sprite(new Texture(Gdx.files.internal("llama/tube.png")));
        llama = new Sprite(new Texture(Gdx.files.internal("llama/llamaStanding.png")));

        animationFinished = false;
        this.x = x;
        this.wTube = wTube * 1.1f;
        this.wLlama = hLlama/llama.getTexture().getHeight()*llama.getTexture().getWidth();

        tube.setSize(this.wTube, this.wTube /tube.getTexture().getWidth()*tube.getTexture().getHeight());
        llama.setSize(wLlama, hLlama);


        tube.setPosition(x- wTube /2, y);
        llama.setPosition(x-wLlama/2, y);



    }

    public void draw(Batch batch){
        llama.draw(batch, 1);
        tube.draw(batch, 1);
    }

    public void update(){
        if (y<0.35){
            direction=1;
        }

        if (y>5){
            animationFinished = true;
        }

        y+=direction*0.1;

        tube.setPosition(x- wTube /2, y);

        if(direction<0){
            llama.setPosition(x- wLlama/2, y);
        }

    }

    public boolean isAnimationFinished() {
        return animationFinished;
    }
}
