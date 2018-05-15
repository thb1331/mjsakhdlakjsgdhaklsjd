package strategy.assets;

import java.io.IOException;

public interface Asset {
	public void load (String path) throws IOException;
	public void cleanup();
}
