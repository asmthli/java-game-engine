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
    private int location_projectionMatrix;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(location_projectionMatrix, projection);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
}
