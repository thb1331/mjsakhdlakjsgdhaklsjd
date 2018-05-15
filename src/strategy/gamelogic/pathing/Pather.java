package strategy.gamelogic.pathing;

import java.util.List;

import strategy.tile_engine.Board;
import strategy.tile_engine.Square;
import strategy.tile_engine.Team;
import strategy.tile_engine.Unit;

public abstract class Pather {
	public abstract Path pathToSquare (int x, int y, int startX, int startY, int move, Board board, Team team);
	public abstract List<Square> getPathableSquares (int x, int y, int move, Board board, Team team);
	public boolean isValidPath (int x, int y, Board board, Team team) {
		Unit u = board.getUnitAtPosition(x, y);
		if (x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight()) {
			if (u == null) {
				return true;
			} else if (u.team == team) {
				return true;
			}
		}
		return false;
	}
	public int blockDistance (int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	public boolean isValidSquare (int x, int y, int width, int height) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}
	public String squareToString (Square s) {
		return s.x + " " + s.y;
	}
}
