package strategy.gamelogic;

import java.io.IOException;

import strategy.graphics.Renderer;
import strategy.graphics.Window;
import strategy.graphics.shaders.ShaderProgram;

public class GameInit {
	protected static final int WINDOW_X = 800;
	protected static final int WINDOW_Y = 600;
	protected static final String WINDOW_NAME = "Strategy game";
	protected static final String FRAGMENT_PATH = "shaders/fragmentshader.fs";
	protected static final String VERTEX_PATH = "shaders/vertexshader.vs";
	protected static int nextUnitID = 0;
	public static Renderer init () {
		// some game logic stuff
		Renderer renderer = new Renderer (new Window(WINDOW_X, WINDOW_Y, WINDOW_NAME));
		ShaderProgram shaderProgram;
		try {
			shaderProgram = initShaders();
			renderer.shaderProgram = shaderProgram;
		} catch (Exception e) {
			System.err.println("Error initialising shaders");
			e.printStackTrace(System.err);
		}
		renderer = initAssets(renderer);
		renderer.init();
		return renderer;
	}
	private static ShaderProgram initShaders () throws Exception {
		ShaderProgram shaderProgram;
		shaderProgram = new ShaderProgram();
		shaderProgram.createShaders(VERTEX_PATH, FRAGMENT_PATH);
		shaderProgram.link();
		return shaderProgram;
	}
	protected static Renderer initAssets(Renderer r) {
		initTexture(r, "assets/textures/test.png", "test");
		initTexture(r, "assets/textures/test2.png", "test2");
		initTexture(r, "assets/textures/test_unit.png", "testunit");
		initTexture(r, "assets/textures/grass_temp.png", "grass");
		initTexture(r, "assets/textures/tilemovable.png", "squaremovable");
		initTexture(r, "assets/textures/squareattackable.png", "squareattackable");
		initTexture(r, "assets/textures/tempunitsquad.png", "temp unitsquad");
		/*try {
		r.addTexture("assets/textures/test.png", "test");
		r.addTexture("assets/textures/test2.png", "test2");
		r.addTexture("assets/textures/test_unit.png", "testunit");
		r.addTexture("assets/textures/grass_temp.png", "grass");
		r.addTexture("assets/textures/tilemovable.png", "squaremovable");
		r.addTexture("assets/textures/squareattackable.png", "squareattackable");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return r;
	}
	protected static void initTexture (Renderer r, String path, String key) {
		try {
			//System.out.println("Adding texture at " + path);
			r.addTexture(path, key);
		} catch (IOException e) {
			System.err.println("Error occured adding texture at " + path);
		}
	}
	public static int getUnitID () {
		int id = nextUnitID;
		nextUnitID++;
		return id;
	}
}
