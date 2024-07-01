package org.lasmth.graphics.shaders.staticshader;

import org.joml.Matrix4f;
import org.lasmth.graphics.entities.Camera;
import org.lasmth.graphics.shaders.ShaderProgram;
import org.lasmth.tools.maths;

/**
 * Static shader. No shadows or lighting, just basic colour interpolation.
 */
public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = "staticshader/vertex.txt";
    private static final String FRAGMENT_FILE = "staticshader/fragment.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Allows for 3D transformations of vertices.
     * @param matrix Transformation matrix.
     */
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    /**
     * Allows for 3D transformations which mimic the view from a movable camera.
     * @param camera Camera with 3D position and roll, pitch and yaw.
     */
    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    /**
     * Allows for 3D projection transformations. Makes objects which are more distant appear smaller.
     * @param projection Calculated projection matrix.
     */
    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(location_projectionMatrix, projection);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
}
