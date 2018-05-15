package strategy.gamelogic;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import strategy.gamelogic.players.HumanPlayer;
import strategy.gamelogic.players.Player;
import strategy.graphics.Camera;
import strategy.tile_engine.Board;
import strategy.tile_engine.tiles.VillageTile;

public class InputManager {
	GameLogic game;
	Camera camera;
	long windowHandle;
	public InputManager(GameLogic g, Camera c, long windowHandle) {
		//  init stuff
		this.game = g;
		this.camera = c;
		this.windowHandle = windowHandle;
		glfwSetMouseButtonCallback(windowHandle, (long window, int button, int action, int mods) -> {
			if (action == GLFW_RELEASE) {
				mouseButtonRelease(button, mods);
			}
		});
		glfwSetKeyCallback(windowHandle, (long window, int key, int scancode, int action, int mods) -> {
			if (action == GLFW_RELEASE) {
				keyboardRelease (key, mods);
			}
		});
	}
	public void mouseButtonRelease (int button, int mods) {
		if (button == GLFW_MOUSE_BUTTON_LEFT) {
			int[] pos = getBoardPosAtMousePos();
			// passes the values to game
			Player p = game.getCurrentPlayer();
			if (p instanceof HumanPlayer && 
					game.game.state == GameState.INPUT) {
				p.selectSquare(pos[0], pos[1]);
			}
		}
	}
	public void keyboardRelease (int key, int mods) {
		if (key == GLFW_KEY_LEFT || key == GLFW_KEY_RIGHT || key == GLFW_KEY_UP || key == GLFW_KEY_DOWN) {
			int deltaX = 0;
			int deltaY = 0;
			if (key < GLFW_KEY_DOWN) {
				deltaX = (key - GLFW_KEY_RIGHT) * -2 + 1;
			} else {
				deltaY = (key - GLFW_KEY_DOWN) * -2 + 1; // up and down are switched
			}
			Board current = game.getCurrentBoard();
			int width = current.getWidth();
			int height = current.getHeight();
			camera.moveRelativeSafe(deltaX * 2.0f, deltaY * 2.0f, width, height);
		}
		if (key == GLFW_KEY_Q) {
			int[] pos = getBoardPosAtMousePos();
			game.queryUnit(pos[0], pos[1]);
		} else if (key == GLFW_KEY_R) {
			int[] pos = getBoardPosAtMousePos();
			VillageTile v = game.getCurrentBoard()
								.getTile(pos[0], pos[1])
								.getVillageTile();
			if (v != null) {
				game.recruitUnit(pos[0], pos[1]);
			}
		}
	}
	public int[] getBoardPosAtMousePos () {
		// get the board position at the mouse position
		// get absolute mouse position
		DoubleBuffer xPos = MemoryUtil.memAllocDouble(1);
		DoubleBuffer yPos = MemoryUtil.memAllocDouble(1);
		IntBuffer windowWidth = MemoryUtil.memAllocInt(1);
		IntBuffer windowHeight = MemoryUtil.memAllocInt(1);
		glfwGetWindowSize(windowHandle, windowWidth, windowHeight);
		glfwGetCursorPos(windowHandle, xPos, yPos);
		
		// get window coordinates on the scale of -1 to 1
		// same as opengl
		double windowX = xPos.get() / windowWidth.get() * 2 - 1;
		// lowest y is up in cursorPos rather than down so need to reverse 
		double windowY = (yPos.get() / windowHeight.get() * 2 - 1) * -1;
		
		//System.out.printf("Actual cursor location: (%f, %f)\n", windowX, windowY);
		
		// x and y values are times by 2 first and then moved by the camera
		// and then scaled, so this function needs to do them in reverse
		
		// account for scaling of tiles - gets scaled position
		Matrix4f scaleMatrix = camera.getScaleMatrix();
		double relativeX = windowX / scaleMatrix.m00();
		double relativeY = windowY / scaleMatrix.m11();
		
		// gets board position
		Matrix4f cameraMatrix = camera.getCameraMatrix();
		int x = (int) Math.ceil((relativeX - cameraMatrix.m30())) / 2;
		int y = (int) Math.ceil((relativeY - cameraMatrix.m31())) / 2;
		//System.out.printf("selected (%d, %d)\n", x, y);
		
		// frees buffers (probably not necessary but good to be safe)
		MemoryUtil.memFree(xPos);
		MemoryUtil.memFree(yPos);
		MemoryUtil.memFree(windowHeight);
		MemoryUtil.memFree(windowWidth);
		int[] position = {x, y};
		return position;
	}
}
