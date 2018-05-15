package strategy.gamelogic;

import strategy.graphics.Camera;
import strategy.graphics.RenderQueue;
import strategy.graphics.Renderer;
import strategy.graphics.ui.UIManager;
import strategy.tile_engine.Board;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class GameLoop {
	Renderer renderer;
	protected long window;
	Camera camera; // later on put it into separate class
	RenderQueue queue;
	Board board;
	GameLogic gameLogic;
	Game game;
	UIManager uimanager;
	InputManager inputmanager;
	int width = 12;
	double idealFPS = 60;
	double timePerFrame = 1 / idealFPS;
	double previousTime;
	public void attachRenderer (Renderer renderer) {
		this.renderer = renderer;
		this.window = renderer.getWindowId();
	}
	public void loop() {
		// general init stuff - probably put into a different function
		camera = new Camera();
		queue = new RenderQueue();
		//board = new Board(width, width);
		gameLogic = new GameLogic();
		uimanager = new UIManager();
		gameLogic.setUIManager(uimanager);
		camera.scale(1.0f/width); //just for testing
		camera.move(width - 1, width - 1);
		inputmanager = new InputManager(gameLogic, camera, window);
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		System.out.println("At loop");
		gameLogic.newMicroTurn();
		previousTime = glfwGetTime();
		while (!glfwWindowShouldClose(window)) {
			// get rid of commented out code
			// halves length
			double loopStartTime = glfwGetTime();
			double deltaTime = loopStartTime - previousTime;
			gameLogic.logicLoop(renderer.window, deltaTime, loopStartTime);
			renderer.startRender();
			//renderer.renderTest();
			//board = game.getCurrentBoard();
			//board.addTiles(queue);
			//uimanager.addMovableSquaresToQueue(queue);
			//System.out.println("Added tiles");
			//board.addUnits(queue);
			//System.out.println("Added units");
			//queue.render(renderer, camera);
			gameLogic.render(renderer, camera);
			//System.out.println("Rendered");
			renderer.endRender();
			//System.out.println("Ended render");
			glfwPollEvents();
			//camera.moveRelative(0.01f, 0.0f);
			previousTime = loopStartTime;
			//System.out.println(deltaTime);
			sync(loopStartTime);
		}
	}
	protected void sync (double loopStartTime) {
		// sleeps for the required time for 60fps (idealy)
		double endTime = loopStartTime + timePerFrame;
		while (glfwGetTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
