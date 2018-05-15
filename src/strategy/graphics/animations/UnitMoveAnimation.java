package strategy.graphics.animations;

import java.util.List;

import strategy.gamelogic.pathing.AStarPather;
import strategy.gamelogic.pathing.BruteForcePather;
import strategy.gamelogic.pathing.Path;
import strategy.gamelogic.pathing.Pather;
import strategy.tile_engine.Board;
import strategy.tile_engine.Square;
import strategy.tile_engine.Unit;

public class UnitMoveAnimation implements Animation {
	double startTime;
	public boolean inAnimation = false;
	double accumulativeTime;
	double endTime;
	double timePerSquare;
	int lastSquare = -1;
	Unit unit;
	List<Square> pathing;
	public boolean canAnimate = true;
	public UnitMoveAnimation (Unit u, int x, int y, Board board) {
		unit = u;
		Pather pather = new AStarPather();
		// finds the path to the square using implemented A*
		Path path = pather.pathToSquare(x, y, u.x, u.y, u.stats.movespeed, board, u.team);
		//System.out.println("Got path");
		// if no valid path pathToSquare returns null
		if (path == null) {
			canAnimate = false;
			System.err.printf("No path found from (%d, %d) to (%d, %d) with %d movespeed\n",
							  u.x, u.y, x, y, u.stats.movespeed);
		} else {
			pathing = path.getPath();
		}
	}
	public UnitMoveAnimation () {
		canAnimate = false;
	}
	public void update (double deltaTime) {
		if (!inAnimation) {
			return;
		}
		//System.out.println("Updating animation!");
		//System.out.println("Delta time:" + deltaTime);
		
		accumulativeTime += deltaTime;
		//System.out.println("Accumulative time:" + accumulativeTime);
		// calculates the #square the animation is on
		int square = Math.min((int) Math.floor((accumulativeTime - startTime) / timePerSquare), 
				pathing.size() - 1);
		if (square > lastSquare) {
			//System.out.println(square);
			// if for some reason it lags or something
			lastSquare = square;
		}
		// gets the integer part of the position
		int integerX = pathing.get(square).x;
		int integerY = pathing.get(square).y;
		unit.move(integerX, integerY);
		if (square == pathing.size() - 1) {
			// need to reset float values
			inAnimation = false;
			unit.partialX = 0.0f;
			unit.partialY = 0.0f;
		} else {
			// do orientation code here
			
			// calculates the proportion of timePerSquare it is
			double negDelta = (accumulativeTime - startTime) - (timePerSquare * (square + 1));
			double delta = timePerSquare + negDelta; // getting actual delta
			//System.out.println(delta);
			
			// calcultes partial (float) part of position
			float deltaX = pathing.get(square + 1).x - pathing.get(square).x;
			float deltaY = pathing.get(square + 1).y - pathing.get(square).y;
			unit.movePartial((float)(deltaX * (delta / timePerSquare)), 
							 (float)(deltaY * (delta / timePerSquare)));
		}
	}
	public void start (double startTime, double endTime) {
		if (!canAnimate) {
			return;
		}
		// starts animation
		this.startTime = startTime;
		this.endTime = endTime;
		timePerSquare = (endTime - startTime) / (pathing.size() - 1);
		accumulativeTime = startTime;
		inAnimation = true;
		//System.out.println("Started animation!");
		//System.out.println("Time between squares: " + timePerSquare + "s");
		//System.out.println("Path:");
		/*for (Square s : pathing) {
			System.out.printf("(%s, %s)\n", s.x, s.y);
		}*/
	}

}
