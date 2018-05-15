package strategy.gamelogic.pathing;

import java.util.LinkedList;
import java.util.List;

import strategy.tile_engine.Square;

public class Path {
	// defines a path which can be passed around
	// rather than a million List<Square>s
	List<Square> paths;
	int remainingMove;
	public Path (int move) {
		paths = new LinkedList<Square>();
		remainingMove = move;
	} 
	public void addSquare (Square s) {
		paths.add(s);
	}
	
	public void joinPath (Path p) {
		paths.addAll(p.getPath());
	}
	public List<Square> getPath () {
		return paths;
	}
	public Square lastSquare () {
		return paths.get(paths.size() - 1);
	}
	public void addSquares (List<Square> squares) {
		for (Square s : squares) {
			addSquare(s);
		}
	}
	public int getRemainingMove () {
		return remainingMove;
	}
}
