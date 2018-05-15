package strategy.gamelogic;

import strategy.graphics.Renderer;

import static org.lwjgl.glfw.Callbacks.*;

public class GameCleanup {
	public static void cleanup (Renderer renderer) {
		glfwFreeCallbacks(renderer.getWindowId());
		renderer.cleanup();
	}
}
