package org.lasmth.graphics;

import org.lasmth.models.RawModel;
import org.lwjgl.opengl.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
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
    private List<Integer> textures = new ArrayList<>();

    private static final String RESOURCES_DIRECTORY_PATH = "src/main/resources/";

    /**
     * Store an array of vertex positions in an openGL Vertex Array Object (VAO).
     * @param positions 3D position coordinates for each vertex.
     * @param textureUVs 2D UV coordinates describing texture mapping for each vertex.
     * @param indices Rendering indices - the order in which to render our stored positions.
     * @return RawModel which stores the VAO information for the model.
     */
    public RawModel loadToVAO(float[] positions, float[] textureUVs, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInBoundVAO(0, 3, positions);
        storeDataInBoundVAO(1, 2, textureUVs);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    /**
     * Load a texture from an image file into memory, registering it with OpenGL.
     * @param fileName Texture image file of format [file_name].[extension]
     * @return ID for texture loaded in memory.
     */
    public int loadTexture(String fileName) {
        String filePath = RESOURCES_DIRECTORY_PATH + fileName;
        // Buffers for holding the named values after being extracted in the decoding process.
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels_in_file = BufferUtils.createIntBuffer(1);

		// Notice the output into the given buffer arguments also.
		// Desired channels in 4. We are expecting RGBA.
        ByteBuffer decodedImageData = STBImage.stbi_load(filePath, width, height, channels_in_file, 4);

        if (decodedImageData == null) {
            System.out.println("Loading of " + filePath + " failed.\nReason: " + STBImage.stbi_failure_reason());
        }

        // Reserve an ID for the texture.
        int textureId = GL11.glGenTextures();

        // Activate a texture unit.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
        // Bind the texture ID to the texture unit.
		GL11.glBindTexture(GL13.GL_TEXTURE_2D, textureId);

        // Minifying and magnification functions for when the texture is of a different size than
        // the shape they are mapping to. These have to be set otherwise the loaded texture was displaying
        // as fully black. Seems like these should have just been set to a default?
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // Change the state of the bound texture unit to contain our texture data (along with
        // how to interpret that data).
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width.get(),
				height.get(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, decodedImageData);

        // Again track our textures in use so we can clean up when we are finished.
		textures.add(textureId);
		return textureId;
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

        for (int textureId:textures) {
            GL11.glDeleteTextures(textureId);
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
     * @param size The dimensions used to represent each vertex (e.g. 3 for 3D position, 2 for UV coordinates).
     * @param data vertex data to be stored as a VBO.
     */
    private void storeDataInBoundVAO(int attributeNumber, int size, float[] data) {
        // Create a VBO.
        int vboId = GL15.glGenBuffers();
        vbos.add(vboId);
        // OpenGL requires that we 'bind' buffers we wish to interact with. Here we bind the VBO.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        // Loading the data into the currently bound VBO.
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        // Loading
        GL30.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
        // Unbind the VBO.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    /**
     * Binds a list of integers as an index buffer. An index buffer defines the order in which vertices are drawn.<p/>
     * As opposed to repeating the entire vertex information to define the render order, using just an index
     * for this can greatly improve memory usage.
     * @param indices Rendering order.
     */
    private void bindIndicesBuffer(int[] indices) {
        int vboId = GL15.glGenBuffers();
        vbos.add(vboId);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        // Flip the buffer from write to read.
        buffer.flip();
        return buffer;
    }
}
