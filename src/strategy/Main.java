package strategy;

import strategy.gamelogic.GameCleanup;
import strategy.gamelogic.GameInit;
import strategy.gamelogic.GameLoop;
import strategy.graphics.Renderer;

public class Main {
	public void start (){
		GameLoop gameloop = new GameLoop();
		Renderer renderer;
		renderer = GameInit.init();
		gameloop.attachRenderer(renderer);
		gameloop.loop();
		GameCleanup.cleanup(renderer);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main main = new Main();
		main.start();
		//Test.test();
	}

}
