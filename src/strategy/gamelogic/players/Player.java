package strategy.gamelogic.players;

public interface Player {
	public void startTurn ();
	public void doAction ();
	public void endTurn ();
	public void selectSquare(int x, int y);
}
