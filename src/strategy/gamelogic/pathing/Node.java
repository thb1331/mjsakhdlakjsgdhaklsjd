package strategy.gamelogic.pathing;

import strategy.tile_engine.Square;

public class Node extends Square {
	// another representation for a square
	// comes with features useful for pathing algorithms
	// like overriding .equals() and having a constructor
	// that sets x and y values as well as inbuilt cost
	// and heuristic(total for A*) cost
	public Node parent;
	public int cost;
	public double hCost;
	public Node (int x, int y) {
		this.x = x;
		this.y = y;
		this.parent = null;
	}
	@Override
	public boolean equals (Object o) {
		if (o instanceof Node) {
			Node n = (Node) o;
			if (this.x == n.x && this.y == n.y) {
				return true;
			}
		}
		return false;
	}
	public double getHCost () {
		return hCost;
	}
}
