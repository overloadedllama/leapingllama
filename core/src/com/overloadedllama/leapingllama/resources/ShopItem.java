package com.overloadedllama.leapingllama.resources;

import com.badlogic.gdx.graphics.Texture;

public class ShopItem {
    String id;
    Texture texture;
    int value;

    public ShopItem(String id, Texture texture, int value) {
        this.id = id;
        this.texture = texture;
        this.value = value;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getValue() {
        return value;
    }

    public String getId() {
        return id;
    }
}
