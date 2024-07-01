package org.lasmth.graphics.entities;

import org.joml.Vector3f;

import org.lasmth.windowmanager.Window;
import org.lwjgl.glfw.GLFW;

public class Camera {

    private Vector3f position = new Vector3f();
    private float pitch;
    private float yaw;
    private float roll;

    private float moveSpeed = 0.02f;

    Window window = Window.getInstance();

    public void move() {
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            position.z -= moveSpeed;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            position.z += moveSpeed;
        }

        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            position.x -= moveSpeed;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            position.x += moveSpeed;
        }

    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
