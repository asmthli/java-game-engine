package org.lasmth.graphics.shaders.staticshader;

import org.joml.Matrix4f;
import org.lasmth.graphics.shaders.ShaderProgram;

/**
 * Static shader. No shadows or lighting, just basic colour interpolation.
 */
public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = "staticshader/vertex.txt";
    private static final String FRAGMENT_FILE = "staticshader/fragment.txt";

    private int location_transformationMatrix;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
}
