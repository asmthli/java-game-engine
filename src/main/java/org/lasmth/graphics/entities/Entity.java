package org.lasmth.graphics.entities;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lasmth.graphics.models.TexturedModel;

import static org.lasmth.tools.maths.createTransformationMatrix;

/**
 * An instance of a textured model with position, rotation etc. Allows us to
 * render the same model with different transformations.
 */
public class Entity {

    private TexturedModel texturedModel;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;

    public Entity(TexturedModel texturedModel, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;

    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public Matrix4f getTransformationMatrix() {
        return createTransformationMatrix(
                this.position,
                this.rotX,
                this.rotY,
                this.rotZ,
                this.scale
        );
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void setModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
