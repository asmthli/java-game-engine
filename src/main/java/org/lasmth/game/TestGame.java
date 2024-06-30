package org.lasmth.game;

import org.lasmth.graphics.Loader;
import org.lasmth.models.RawModel;
import org.lasmth.graphics.Renderer;
import org.lasmth.graphics.shaders.staticshader.StaticShader;
import org.lasmth.models.TexturedModel;
import org.lasmth.windowmanager.Window;

public class TestGame {
    private final Window window = Window.getInstance();
    private final Loader loader = new Loader();
    private final Renderer renderer = new Renderer();
    private final StaticShader staticShader = new StaticShader();

    public static void main(String[] args) {
        new TestGame().run();
    }

    private void run() {
         float[] vertices = {
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f,
        };

         int[] indices = {
                 0,1,3,
                 3,1,2
         };

         float[] textureCoords = {
                 0,0,
                 0,1,
                 1,1,
                 1,0
         };

         RawModel rawModel = loader.loadToVAO(vertices, textureCoords, indices);
         int textureId = loader.loadTexture("image.png");
         TexturedModel texturedModel = new TexturedModel(rawModel, textureId);

        while (!window.windowShouldClose()) {
            renderer.prepare();
            staticShader.start();
            renderer.render(texturedModel);
            staticShader.stop();
            iterateGameLoop();
        }

        finalization();
    }

    private void finalization() {
        staticShader.cleanUp();
        window.destroyWindow();
        loader.cleanUp();
        window.stopGLFW();
    }

    private void iterateGameLoop() {
        window.updateDisplay();
    }
}
