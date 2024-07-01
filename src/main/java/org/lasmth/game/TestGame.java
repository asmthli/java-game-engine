package org.lasmth.game;

import org.joml.Vector3f;
import org.lasmth.graphics.Loader;
import org.lasmth.graphics.entities.Camera;
import org.lasmth.graphics.entities.Entity;
import org.lasmth.graphics.models.RawModel;
import org.lasmth.graphics.Renderer;
import org.lasmth.graphics.shaders.staticshader.StaticShader;
import org.lasmth.graphics.models.TexturedModel;
import org.lasmth.windowmanager.Window;

public class TestGame {
    private final Window window = Window.getInstance();
    private final Loader loader = new Loader();
    private final StaticShader staticShader = new StaticShader();
    private final Renderer renderer = new Renderer(staticShader);
    private final Camera camera = new Camera();

    public static void main(String[] args) {
        new TestGame().run();
    }

    private void run() {
         float[] vertices = {
				-0.5f,0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f,

				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,

				0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,

				-0.5f,0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,

				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,

				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f

		};

		float[] textureCoords = {

				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0


		};

		int[] indices = {
				0,1,3,
				3,1,2,
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22

		};

         RawModel rawModel = loader.loadToVAO(vertices, textureCoords, indices);
         int textureId = loader.loadTexture("image.png");
         TexturedModel texturedModel = new TexturedModel(rawModel, textureId);
         Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -1), 0, 0, 0, 1);

        while (!window.windowShouldClose()) {
            camera.move();
            entity.increaseRotation(0.05f, 0.05f, 0.05f);
            renderer.prepare();
            staticShader.start();
            staticShader.loadViewMatrix(camera);
            renderer.render(entity, staticShader);
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
