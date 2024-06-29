package org.lasmth.windowmanager;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window instance = null;

    private final int WIDTH = 1280;
	private final int HEIGHT = 720;
    private final boolean VSYNC = true;
    private final String TITLE = "Tester Game";

    // Pointer for our window. Passed to wrapped native C functions
    private long windowId;

    // Private constructor used to create singleton
    private Window() {
        createWindowAndContext();
    }

    /**
     * @return Singleton instance
     */
    public static Window getInstance() {
        if (instance == null) {
            instance = new Window();
        }

        return instance;
    }

    /**
     * Set up and configure the window and OpenGL canvas, ready for drawing.
     */
    private void createWindowAndContext() {
        // Set up an error callback. This will print error messages to
        // System.err
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialise GLFW. Most GLFW functions will not work before doing this
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Window 'hints' refer to configurations.
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        // We will manually set visibility true after fully initializing the window
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        // Minimum OpenGL context version to use. Version number of form <major>.<minor>
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);

        // Create the window (and openGL context). Can fail if wrong drivers are
        // installed or openGL version not supported etc.
        windowId = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
        if (windowId == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        setWindowCloseCallback();

        centreWindow();

        // Set the OpenGL drawing context to this window
        glfwMakeContextCurrent(windowId);

        // Enable v-sync
        if (VSYNC) {
            // How many frames to wait before swapping the OpenGL buffers.
        	glfwSwapInterval(1);
        }

        // Make the window visible
        glfwShowWindow(windowId);

        // Set up the openGL drawing capabilities
        GL.createCapabilities();
        
        setClearColour(1.0f, 0.0f, 0.0f, 0.0f);
	}

    private void setWindowCloseCallback() {
        // Provided function set the windowShouldClose flag which can then be checked elsewhere
        // to handle closing the application.
        glfwSetKeyCallback(windowId, (windowId, key, _, action, _) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(windowId, true);
            }
        });
    }

    private void centreWindow() {
        // Get the resolution of the primary monitor
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Centre our window
        assert videoMode != null;
        glfwSetWindowPos(
                windowId,
                (videoMode.width() - WIDTH) / 2,
                (videoMode.height() - HEIGHT) / 2
        );
    }

    /**
     * Set the colour openGL uses to 'clear' the screen.
     * @param r Red value. [0, 1]
     * @param g Green value. [0, 1]
     * @param b Blue value. [0, 1]
     * @param alpha Transparency value. [0, 1]
     */
	public void setClearColour(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
	}

    /**
     * Destroys the window as well as its associated event callbacks.
     */
	public void destroyWindow() {
    	glfwFreeCallbacks(windowId);
        glfwDestroyWindow(windowId);
    }

    /**
     * Stops GLFW. Necessary as we are using native functions which may allocate memory without garbage collection.
     * These should ensure proper cleanup.
     */
	public void stopGLFW() {
    	// Terminate GLFW and release the GLFWerrorfun
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Check whether the given mouse button is currently pressed.
     *
     * @param keyCode Integer code for mouse button.
     * @return True if button is pressed.
     * @see <a href="https://www.glfw.org/docs/3.3/group__keys.html#gaa06a712e6202661fc03da5bdb7b6e545">Button Codes</a>
     */
	public boolean isKeyPressed(int keyCode) {
    	return glfwGetKey(windowId, keyCode) == GLFW_PRESS;
    }

    /**
     * Check whether the given mouse button is currently pressed.
     *
     * @param buttonCode Integer code for keyboard button
     * @return True if button is pressed
     * @see <a href="https://www.glfw.org/docs/3.3/group__buttons.html">Button codes</a>
     */
	public boolean isMouseButtonPressed(int buttonCode) {
		return glfwGetMouseButton(windowId, buttonCode) == GLFW_PRESS;
	}

    /**
     * GLFW windows have an associated flag which we can set (i.e. after some event) which indicates that we  want
     * to close the window.
     * @return Boolean value of the windowShouldClose flag.
     */
	public boolean windowShouldClose() {
    	return glfwWindowShouldClose(windowId);
    }

    /**
     * Swaps the two openGL drawing buffers and polls for window events.
     * <p>
     * OpenGL by default uses double buffering, giving us a 'front' and 'back' buffer. At any one time we use one to
     * display and one to draw on. Every frame we have one buffer on display whilst we draw on the other before
     * swapping them for the next frame.
     */
	public void updateDisplay() {
        // Swaps 'draw' and 'display' buffers
		glfwSwapBuffers(windowId);
        // Poll events e.g. key presses
		glfwPollEvents();
	}
}
