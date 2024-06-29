package org.lasmth.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads raw vertex data into memory as vertex objects (VAOs and VBOs), preparing it
 * to be rendered.
 */
public class Loader {

    // Keep track of the pointers for our vertex objects. These must be manually deleted
    // when application ends.
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();

    /**
     * Store an array of vertex positions in an openGL Vertex Array Object (VAO).
     * @param positions Unstructured array of floats describing triangle vertices in 3D.
     * @return RawModel which stores the VAO information for the model.
     */
    public RawModel loadToVAO(float[] positions) {
        int vaoID = createVAO();
        storeDataInBoundVAO(0, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length/3);
    }

    /**
     * Clears allocated memory which will otherwise not be garbage collected.
     */
    public void cleanUp() {
        for (int vaoId:vaos) {
            GL30.glDeleteVertexArrays(vaoId);
        }

        for (int vboId:vbos) {
            GL15.glDeleteBuffers(vboId);
        }

    }

    /**
     * Creates a Vertex Array Object (VAO) for storing vertex data. Note that it is not a Java object
     * and only a pointer for the memory location is returned.
     * @return Pointer to VAO memory location.
     */
    private int createVAO() {
        int vaoId = GL30.glGenVertexArrays();
        vaos.add(vaoId);
        GL30.glBindVertexArray(vaoId);
        return vaoId;
    }

    /**
     * A VAO comprises multiple slots which can hold different information for each vertex. The data in each
     * slot is stored as a Vertex Buffer Object (VBO).
     *
     * @param attributeNumber The 'slot' for our vertex data.
     * @param data vertex data to be stored as a VBO.
     */
    private void storeDataInBoundVAO(int attributeNumber, float[] data) {
        // Create a VBO.
        int vboId = GL15.glGenBuffers();
        vbos.add(vboId);
        // OpenGL requires that we 'bind' buffers we wish to interact with. Here we bind the VBO.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        // Loading the data into the currently bound VBO.
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        // Loading
        GL30.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
        // Unbind the VBO.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        // Flip the buffer from write to read.
        buffer.flip();
        return buffer;
    }
}
