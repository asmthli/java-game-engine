package org.lasmth.graphics.shaders.staticshader;

import org.lasmth.graphics.shaders.ShaderProgram;

/**
 * Static shader. No shadows or lighting, just basic colour interpolation.
 */
public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = "staticshader/vertex.txt";
    private static final String FRAGMENT_FILE = "staticshader/fragment.txt";

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
