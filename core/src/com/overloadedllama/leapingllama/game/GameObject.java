package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.overloadedllama.leapingllama.LlamaConstants;

public interface GameObject extends LlamaConstants {

    void draw();

    void setPosition(float x, float y, float d);
    void setPosition(float x, float y);

    void createBody(BodyDef.BodyType type, Shape shape, float density, float friction, float restitution);

    String getName();

    float getX();
    void setX(float x);

    float getY();
    void setY(float y);

    float getW();
    void setW(float w);

    float getH();
    void setH(float h);

    boolean isDestroyable();
    void setDestroyable(boolean destroyable);

    Texture getTexture();
    void setTexture(Texture texture);

    void setWorld(World world);
    World getWorld();

    Body getBody();

}
