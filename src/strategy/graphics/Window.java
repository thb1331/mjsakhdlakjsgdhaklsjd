package strategy.graphics;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
	public long window;
	public Window (int x, int y, String name) {
		this.init(x, y, name);
	}
	protected void init (int x, int y, String name) {
		System.out.println("LWJGL version: " + Version.getVersion());
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialise GLFW");
		}
		initWindowHints();
		createWindow(x, y, name);
		GL.createCapabilities();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		setCallbacks();
	}
	protected void initWindowHints () {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
	}
	protected void createWindow (int x, int y, String name) {
		window = glfwCreateWindow(x, y, name, NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create GLFW window");
		}
		// sets window position
		try (MemoryStack stack = stackPush()){
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, 
					(vidmode.height() - pHeight.get(0)) / 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		glfwMakeContextCurrent(window);
		glfwSwapInterval(2);
		glfwShowWindow(window);
	}
	public void cleanup () {
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}public void setCallbacks () {
		glfwSetWindowSizeCallback(window, (long window, int width, int height) -> {
			glViewport(0, 0, width, height);
			//System.out.println("Window resize callback");
		});
	}public boolean isKeyPressed (int keycode) {
		// not to be used - use InputManager
		return glfwGetKey(window, keycode) == GLFW_PRESS;
	}
}
