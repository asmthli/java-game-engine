package org.lasmth.models;

public class TexturedModel {

    private RawModel rawModel;
    private int textureId;

    public TexturedModel(RawModel model, int textureId) {
        this.rawModel = model;
        this.textureId = textureId;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public int getTextureId() {
        return textureId;
    }
}
