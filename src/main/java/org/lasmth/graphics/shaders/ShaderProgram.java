package org.lasmth.graphics.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.nio.FloatBuffer;

/**
 * Abstract class which creates an OpenGL shader program and stores it in memory. Allows us to
 * bind the shader for rendering.
 */
public abstract class ShaderProgram {

    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    private String SHADERS_DIRECTORY_PATH = "src/main/java/org/lasmth/graphics/shaders";

    // Used when loading matrix uniform variables.
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    /**
     * Registers a shader program with OpenGL. The given vertex and fragment shaders are linked together
     * to form a shader program. The shader program is then registered with OpenGL, so it is ready to use.
     * @param vertexFile Relative path to the GLSL vertex shader. [shader package]/[shader file]
     * @param fragmentFile Relative path to the GLSL fragment shader. [shader package]/[shader file]
     */
    public ShaderProgram(String vertexFile, String fragmentFile) {
        vertexShaderId = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);

        bindAttributes();

        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);

        getAllUniformLocations();
    }

    /**
     * Should be used to retrieve and store all uniform memory locations
     * for a shader program.
     */
    protected abstract void getAllUniformLocations();

    /**
     * Gets the memory location of a uniform variable in our shader program so we
     * can modify the variable.
     * @param uniformName Name of the variable exactly as it appears in shader program.
     * @return Memory location of the variable.
     */
    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programId, uniformName);
    }

    /**
     * Commands OpenGL to use this shader program for subsequent rendering.
     */
    public void start() {
        GL20.glUseProgram(programId);
    }

    /**
     * Commands OpenGL to disable this shader program for subsequent rendering.
     */
    public void stop() {
        GL20.glUseProgram(0);
    }

    /**
     * Memory cleanup for the shader. Should be called on finalization.
     */
    public void cleanUp() {
        stop();
        GL20.glDetachShader(programId, vertexShaderId);
        GL20.glDetachShader(programId, fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
        GL20.glDeleteProgram(programId);
    }

    /**
     * Used to link attribute slots in a VAO to the input variables of the vertex shader.
     */
    protected abstract void bindAttributes();

    /**
     * Links the attribute index of a VAO to an input variable name in our vertex shader.
     * @param attribute Attribute index in VAO.
     * @param variableName Variable name in the vertex shader.
     */
    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programId, attribute, variableName);
    }

    protected void loadFloat(int uniformLocation, float value) {
        GL20.glUniform1f(uniformLocation, value);
    }

    protected void loadVector(int uniformLocation, Vector3f vector) {
        GL20.glUniform3f(uniformLocation, vector.x, vector.y, vector.z);
    }

    protected void loadBoolean(int uniformLocation, boolean value) {
        float toLoad = 0;
        if (value) {
            toLoad = 1;
        }
        GL20.glUniform1f(uniformLocation, toLoad);
    }

    protected void loadMatrix(int uniformLocation, Matrix4f matrix) {
        // Stores the matrix in the matrixBuffer.
        matrix.get(matrixBuffer);
        GL20.glUniformMatrix4fv(uniformLocation, false, matrixBuffer);
    }

    /**
     * Loads a shader file from disk into memory.
     * @param fileName Filename of form [filename].txt
     * @param shaderType Integer either of value GL_VERTEX_SHADER or GL_FRAGMENT_SHADER.
     * @return programId for the shader program now in memory.
     */
    private int loadShader(String fileName, int shaderType) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            String relativeFilePath = SHADERS_DIRECTORY_PATH + "/" + fileName;
            File file = new File(relativeFilePath);
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
        }catch (IOException e) {
            System.err.println("Could not read file!");
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderId = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);
        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderId, 500));
            System.err.println("Could not compile shader.");
            System.exit(-1);
        }
        return shaderId;
    }

}
