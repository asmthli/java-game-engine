package org.lasmth.graphics;

import org.lasmth.models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

    /**
     * Clear the screen OpenGL canvas.
     */
    public void prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Render a basic 3D model.
     * @param model to render.
     */
    public void render(TexturedModel model) {
        GL30.glBindVertexArray(model.getRawModel().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        // Bind our the texture we wish to use.
        // Sampler 2D uniform variable is located in GL_TEXTURE0 by default.
        // So we use this texture bank.
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTextureId());


        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }
}
