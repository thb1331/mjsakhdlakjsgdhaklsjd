package strategy.graphics.animations;

public interface Animation {

	public void update (double deltaTime);
	public void start (double startTime, double endTime);
}
