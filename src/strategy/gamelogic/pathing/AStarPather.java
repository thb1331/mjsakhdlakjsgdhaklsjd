package strategy.gamelogic.pathing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import strategy.tile_engine.Board;
import strategy.tile_engine.Square;
import strategy.tile_engine.Team;
import strategy.tile_engine.Unit;

public class AStarPather extends Pather{
	protected final double hScale = 1.001;
	public List<Square> getPathableSquares (int x, int y, int move, Board board, Team team) {
		return null;
	}

	public Path pathToSquare(int x, int y, int startX, int startY, 
								int move, Board board, Team team) {
		//System.out.printf("Moving from (%d, %d) to (%d, %d)\n", 
		//						startX, startY, x, y);
		//System.out.println("Pathing");
		// if there is a unit at the position then 
		// the unit can't move there
		if (board.isUnitAtPosition(x, y)) {
			return null;
		}
		
		//sets queues
		List<Node> openQueue = new LinkedList<Node>();
		List<Node> closedQueue = new LinkedList<Node>();
		List<Unit> enemyUnits = board.getUnits();
		List<Node> enemyPositions = new ArrayList<Node>();
		
		// adds enemy units
		// they are the "obstacles" for the A* algorithm
		for (Unit u : enemyUnits) {
			if (u.team != team) {
				enemyPositions.add(new Node(u.x, u.y));
			}
		}
		int boardWidth = board.getWidth();
		int boardHeight = board.getHeight();
		Node current = new Node(startX, startY);
		current.cost = 0;
		Node goal = new Node(x, y);
		while (!current.equals(goal)) {
			closedQueue.add(current);
			//System.out.printf("Testing: (%d, %d)\n", current.x, current.y);
			//System.out.printf("Heuristic cost: %f\n", current.hCost);
			List<Node> neighbours = neighbourNodes(current, boardWidth,
														boardHeight);
			for (Node node : neighbours) {
				// sets values for neighbour
				node.cost = current.cost + 1;
				node.parent = current;
				node.hCost = heuristic(node, goal);
				//System.out.printf("Heuristic cost: %f\n", n.hCost);
				if (enemyPositions.contains(node)) {
					// dont even bother with it
					closedQueue.add(node);
				}
				if (openQueue.contains(node)) {
					// check if known cost is less. If it is, n is
					// better than the one already there so it 
					// should be removed
					int index = openQueue.indexOf(node);
					if (openQueue.get(index).cost > node.cost) {
						openQueue.remove(index);
						openQueue.add(node);
					}
				}else if (!closedQueue.contains(node)) {
					// if in neither queue add it to open queue
					openQueue.add(node);
				}
			}
			// sorts by hcost for A* algorithm to work
			openQueue.sort(Comparator.comparing(Node::getHCost));
			// gets current node
			current = openQueue.remove(0);
		}
		
		// gets a path from the nodes
		List<Square> nodePath = new LinkedList<Square>();
		nodePath.add(current);
		
		// semi-recursively determines the path
		while (current.parent != null) {
			nodePath.add(0, current.parent);
			current = current.parent;
		}
		
		// if the path is too long it can be discarded
		// will need to change this to account for slowing effects
		if (nodePath.size() > move + 1) {
			return null;
		}
		// create a path
		Path path = new Path(move - nodePath.size());
		path.addSquares(nodePath);
		return path;
	}
	public List<Node> neighbourNodes (Node n, int width, int height) {
		// gets neighbouring nodes
		List<Node> nodes = new LinkedList<Node>();
		if (isValidSquare(n.x - 1, n.y, width, height)) {
			Node nd = new Node(n.x - 1, n.y);
			nodes.add(nd);
		}
		if (isValidSquare(n.x + 1, n.y, width, height)) {
			Node nd = new Node(n.x + 1, n.y);
			nodes.add(nd);
		}
		if (isValidSquare(n.x, n.y - 1, width, height)) {
			Node nd = new Node(n.x, n.y - 1);
			nodes.add(nd);
		}
		if (isValidSquare(n.x, n.y + 1, width, height)) {
			Node nd = new Node(n.x, n.y + 1);
			nodes.add(nd);
		}
		return nodes;
	}

	public double heuristic (Node current, Node goal) {
		// blockDistance is basically Manhattan Distance
		// use a scale for blockDistance to tiebreak
		return current.cost + blockDistance(current.x, current.y, goal.x, goal.y) * hScale;
	}
	
}
