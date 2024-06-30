package org.lasmth.graphics;

import org.lwjgl.opengl.GL11;
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
    public void render(RawModel model) {
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
