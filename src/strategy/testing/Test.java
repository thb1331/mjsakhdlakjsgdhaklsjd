package strategy.testing;

import java.util.List;

import strategy.gamelogic.pathing.AStarPather;
import strategy.gamelogic.pathing.BruteForcePather;
import strategy.gamelogic.pathing.Path;
import strategy.tile_engine.Board;
import strategy.tile_engine.Square;
import strategy.tile_engine.Team;

public class Test {
	public static void test () {
		/*BruteForcePather pather = new BruteForcePather();
		Board board = new Board (20, 20);
		List<Square> squares = pather.filterDuplicates(pather.getPathableSquares(5, 5, 2, board, Team.RED));
		System.out.println("Length: " + squares.size());
		for (Square s : squares) {
			System.out.println("x:" + s.x + ", y:" + s.y);
		}*/
		Board board = new Board(20, 20);
		AStarPather pather = new AStarPather();
		Path path = pather.pathToSquare(2, 2, 4, 5, 7, board, Team.BLUE);
		assert (path.getRemainingMove() == 2);
	}
}
