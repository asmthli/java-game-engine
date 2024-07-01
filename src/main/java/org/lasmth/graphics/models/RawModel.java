package org.lasmth.graphics.models;

/**
 * Representation of a 3D model as vertex data stored in memory. Note that the
 * vertex data itself isn't stored here as a Java object - just a pointer to
 * the data. This class simply describes the data stored in an openGL Vertex
 * Array Object (VAO).
 */
public class RawModel {

    // Pointer to the VAO in memory.
	private final int vaoID;
	private final int vertexCount;

	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

}