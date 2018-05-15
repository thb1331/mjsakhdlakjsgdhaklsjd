package strategy.gamelogic.pathing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import strategy.tile_engine.Board;
import strategy.tile_engine.DamageType;
import strategy.tile_engine.Square;
import strategy.tile_engine.Team;
import strategy.tile_engine.Unit;

public class BruteForcePather extends Pather{
	public List<Square> filterDuplicates (List<Square> squares) {
		List<Square> newList = new LinkedList<Square>();
		List<String> doneSquares = new ArrayList<String>();
		for (Square s : squares) {
			if (!doneSquares.contains(s.x + " " + s.y)) {
				newList.add(s);
				doneSquares.add(s.x + " " + s.y);
			}
		}
		return newList;
	}
	public List<Square> getPathableSquares (int x, int y, int move, Board board, Team team) {
		//System.out.println("Pathing");
		// dont use this function
		// must be paired with filterDuplicates
		List<Square> paths = new LinkedList<Square>();
		Square s = new Square();
		s.x = x;
		s.y = y;
		Unit u = board.getUnitAtPosition(x, y);
		if (move < 0) {
			//System.out.println("Finished recursion");
			return paths;
		} else if (u != null) {
			if (u.team != team) {
				//System.out.println("Stuck here");
				return paths;
			}
		} 
		if (u == null) {
			paths.add(s);
		}
		if (x - 1 >= 0) {
			//System.out.println("Getting paths");
			paths.addAll(getPathableSquares(x - 1, y, move - 1, board, team));
		}
		if (x + 1 < board.getWidth()) {
			paths.addAll(getPathableSquares(x + 1, y, move - 1, board, team));
		}
		if (y - 1 >= 0) {
			paths.addAll(getPathableSquares(x, y - 1, move - 1, board, team));
		}
		if (y + 1 < board.getWidth()) {
			paths.addAll(getPathableSquares(x, y + 1, move - 1, board, team));
		}
		return paths;
	}
	public Path pathToSquare (int x, int y, int startX, int startY, int move, Board board, Team team) {
		List<String> doneSquares = new LinkedList<String>();
		Square s = new Square();
		s.x = startX;
		s.y = startY;
		Path path = new Path(move);
		path.addSquare(s);
		List<Path> stack = new LinkedList<Path>();
		stack.add(path);
		doneSquares.add(startX + " " + startY);
		/*System.out.println("Length: " + path.getPath().size());
		System.out.println("X:" + path.lastSquare().x);
		System.out.println("Y:" + path.lastSquare().y);
		System.out.println(stack.size() > 0 && path.lastSquare().x != x && path.lastSquare().y != y);*/
		while (stack.size() > 0 && (path.lastSquare().x != x || path.lastSquare().y != y)) {
			//System.out.println("x:" + path.lastSquare().x + ", y:" + path.lastSquare().y + ", moves:" + path.remainingMove);
			
			if (path.remainingMove > 0 ) {
				int[] lPos = {path.lastSquare().x - 1, path.lastSquare().y};
				int[] rPos = {path.lastSquare().x + 1, path.lastSquare().y};
				int[] uPos = {path.lastSquare().x, path.lastSquare().y + 1};
				int[] dPos = {path.lastSquare().x, path.lastSquare().y - 1};
				if (isValidPath(lPos[0], lPos[1], board, team)
						&& !doneSquares.contains(lPos[0] + " " + lPos[1])) {
					Path lPath = new Path(path.remainingMove - 1);
					lPath.joinPath(path);
					Square sq = new Square();
					sq.x = lPos[0];
					sq.y = lPos[1];
					lPath.addSquare(sq);
					stack.add(lPath);
					doneSquares.add(lPos[0]+ " " + lPos[1]);
				}
				if (isValidPath(rPos[0], rPos[1], board, team)
						&& !doneSquares.contains(rPos[0] + " " + rPos[1])) {
					Path rPath = new Path(path.remainingMove - 1);
					rPath.joinPath(path);
					Square sq = new Square();
					sq.x = rPos[0];
					sq.y = rPos[1];
					rPath.addSquare(sq);
					stack.add(rPath);
					doneSquares.add(rPos[0]+ " " + rPos[1]);
				}
				if (isValidPath(uPos[0], uPos[1], board, team)
						&& !doneSquares.contains(uPos[0] + " " + uPos[1])) {
					Path uPath = new Path(path.remainingMove - 1);
					uPath.joinPath(path);
					Square sq = new Square();
					sq.x = uPos[0];
					sq.y = uPos[1];
					uPath.addSquare(sq);
					stack.add(uPath);
					doneSquares.add(uPos[0]+ " " + uPos[1]);
				}
				if (isValidPath(dPos[0], dPos[1], board, team)
						&& !doneSquares.contains(dPos[0] + " " + dPos[1])) {
					Path dPath = new Path(path.remainingMove - 1);
					dPath.joinPath(path);
					Square sq = new Square();
					sq.x = dPos[0];
					sq.y = dPos[1];
					dPath.addSquare(sq);
					stack.add(dPath);
					doneSquares.add(lPos[0]+ " " + lPos[1]);
				}
			}
			if (stack.size() > 1) {
				stack.remove(0);
				path = stack.get(0);
			} else {
				path = null;
				break;
			}
		}
		return path;
	}
	
	public List<Node> getNeighbours (int x, int y, int width, int height) {
		// gets neighbouring nodes
		List<Node> neighbours = new LinkedList<Node>();
		if (isValidSquare(x - 1, y, width, height)) {
			neighbours.add(new Node(x - 1, y));
		}
		if (isValidSquare(x + 1, y, width, height)) {
			neighbours.add(new Node(x + 1, y));
		}
		if (isValidSquare(x, y - 1, width, height)) {
			neighbours.add(new Node(x, y - 1));
		}
		if (isValidSquare(x, y + 1, width, height)) {
			neighbours.add(new Node(x, y + 1));
		}
		return neighbours;
	}
	public List<Node> nodeGetPathableSquares(int x, int y, int move, Board board, Team team) {
		// Finds the pathable squares from a position
		
		// sets up the lists for breadth first algorithm
		List<Node> openList = new LinkedList<Node>();
		List<Node> closedList = new LinkedList<Node>();
		List<Node> enemyUnits = new LinkedList<Node>();
		// gets width and height so I don't call them every iteration
		int width = board.getWidth();
		int height = board.getHeight();
		for (Unit u : board.getUnits()) {
			// adds enemy units
			if (u.team != team) {
				enemyUnits.add(new Node(u.x, u.y));
			}
		}
		// sets up the first node
		Node first = new Node(x, y);
		first.cost = 0;
		openList.add(first);
		Node current;
		while (openList.size() > 0) {
			// sets current
			current = openList.remove(0);
			closedList.add(current);
			
			// gets neighbouring nodes
			List<Node> neighbours = getNeighbours(current.x, current.y, width, height);
			for (Node n : neighbours) {
				// add slowing cost code
				n.cost = current.cost  + 1;
				if (n.cost  > move) {
					break;
				}
				if (openList.contains(n)) {
					// if it contains n already and it has a higher cost, then
					// this n is better and the old n should be discarded
					// but that realistically shouldn't happen in a
					// breadth first search
					int index = openList.indexOf(n);
					if (openList.get(index).cost > n.cost) {
						// shouldn't happen
						openList.remove(index);
					}
				}
				if (closedList.contains(n)) {
					int index = closedList.indexOf(n);
					if (closedList.get(index).cost > n.cost) {
						// shouldn't happen
						System.out.println("The impossible happened in bruteforcepather");
					}
				}
				// if neither openList or closedList or enemyUnits has the node
				// then the node is new and should be put into openList
				if (!openList.contains(n) && !closedList.contains(n) && !enemyUnits.contains(n)) {
					openList.add(n);
				}
			}
		}
		return closedList;
	}
	public List<Node> nodeGetAttackableSquares(Unit u, Board board, int range) {
		int x = u.x;
		int y = u.y;
		int width = board.getWidth();
		int height = board.getHeight();
		List<Node> positionList = new LinkedList<Node>();
		
		for (int i = 0; i < range * 2 + 1; i ++) {
			int testY = y + range - i;
			if ((i == 0 || i == range * 2) && isValidSquare(x, testY, width, height)) {
				positionList.add(new Node(x, testY));
			}else {
				int deltaX = range - Math.abs((y + range - i) - y);
				if (isValidSquare(x + deltaX, testY, width, height)) {
					positionList.add(new Node(x + deltaX, testY));
				}
				if (isValidSquare(x - deltaX, testY, width, height)) {
					positionList.add(new Node(x - deltaX, testY));
				}
			}
		}
		return positionList;
	}
	public List<Node> nodeGetAttackableUnits (Unit unit, Board board, List<Node> movablePositions) {
		//.System.out.println("Getting attack locations for unit with range " + unit.stats.range);
		List<Node> attackable = new LinkedList<Node>();
		List<Node> friendlyUnitPositions = new LinkedList<Node>();
		for (Unit u : board.getUnits()) {
			if (u.team == unit.team) {
				friendlyUnitPositions.add(new Node(u.x, u.y));
			}
		}
		List<Node> validPositions = movablePositions.stream()
				.filter(node -> !friendlyUnitPositions.contains(node))
				.collect(Collectors.toList());
		for (Unit u: board.getUnits()) {
			if (u.team != unit.team && unit.damageType != DamageType.HEALING) {
				// add mage code
				List<Node> attackablePositions = nodeGetAttackableSquares(u, board, unit.stats.range);
				for (Node n : attackablePositions) {
					if (validPositions.contains(n)) {
						attackable.add(new Node(u.x, u.y));
					}
				}
			}
		}
		return attackable;
	}
}

