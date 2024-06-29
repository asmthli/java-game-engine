package org.lasmth.game;

import org.lasmth.windowmanager.Window;

public class TestGame {
    private final Window window = Window.getInstance();

    public static void main(String[] args) {
        new TestGame().run();
    }

    private void run() {
        while (!window.windowShouldClose()) {
            iterateGameLoop();
        }

        window.destroyWindow();
        window.stopGLFW();
    }

    private void iterateGameLoop() {
        window.updateDisplay();
    }
}
