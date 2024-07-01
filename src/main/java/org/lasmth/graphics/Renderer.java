package org.lasmth.graphics;

import org.joml.Matrix4f;
import org.lasmth.graphics.entities.Entity;
import org.lasmth.graphics.models.TexturedModel;
import org.lasmth.graphics.shaders.staticshader.StaticShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    public Renderer(StaticShader shader) {
        // Projection matrix will never change unless we decide to allow for window resizing
        // or want to change the view frustrum.
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Clear the screen OpenGL canvas.
     */
    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        // Instructs OpenGL to clear the depth buffer every frame.
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Render a basic 3D model.
     * @param entity to render.
     */
    public void render(Entity entity, StaticShader shader) {
        TexturedModel texturedModel = entity.getTexturedModel();

        GL30.glBindVertexArray(texturedModel.getRawModel().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        Matrix4f transformationMatrix = entity.getTransformationMatrix();
        shader.loadTransformationMatrix(transformationMatrix);

        // Bind our the texture we wish to use.
        // Sampler 2D uniform variable is located in GL_TEXTURE0 by default.
        // So we use this texture bank.
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTextureId());


        GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    private void createProjectionMatrix() {
		float aspectRatio = 1280f / 720f;
		float y_scale = (float) (1f / Math.tan(Math.toRadians((FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.zero();
		projectionMatrix.set(0, 0, x_scale);
		projectionMatrix.set(1, 1, y_scale);
		projectionMatrix.set(2, 2, -((FAR_PLANE + NEAR_PLANE) / frustrum_length));
		projectionMatrix.set(2, 3, -1);
		projectionMatrix.set(3, 2, -((2 * NEAR_PLANE * FAR_PLANE) / frustrum_length));
		projectionMatrix.set(3, 3, 0);
	}
}
