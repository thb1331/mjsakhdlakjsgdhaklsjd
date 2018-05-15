package strategy.graphics;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

public class Camera {
	private Matrix4f cam;
	private Matrix4f scale;
	public Camera () {
		cam = new Matrix4f();
		scale = new Matrix4f();
		cam.identity();
		scale.identity();
	}
	public void move (float x, float y) {
		// moves the camera to specified coords
		cam.identity();
		cam.translate(-x, -y, 0.0f);
	}
	public Matrix4f getCameraMatrix () {
		return cam;
	}
	public void scale (float xFactor, float yFactor) {
		// scales by a certain factor
		scale.scale(xFactor, yFactor, 1.0f);
	}
	public void scale (float factor) {
		scale(factor, factor);
	}
	public void resetScale () {
		scale.identity();
	}
	public Matrix4f getScaleMatrix () {
		return scale;
	}
	public void test() {
		Matrix4f oldCam = cam;
		cam.identity();
		cam.translate(4.0f, 1.0f, 0.0f);
		FloatBuffer fb = MemoryUtil.memAllocFloat(16);
		cam.get(fb);
		for (int i = 0; i < 4; i ++) {
			System.out.printf("%f %f %f %f\n", fb.get(i), fb.get(i + 4), fb.get(i + 8), fb.get(i + 12));
		}
		cam = oldCam;
	}
	public void moveRelative (float x, float y) {
		// moves the camera relative to its current position
		cam.translate(-x, -y, 0.0f);
	}
	public void moveRelativeSafe (float x, float y, int boardWidth, int boardHeight) {
		// moves camera so no ugly fill colour is displayed on screen
		float oldX = cam.m30();
		float oldY = cam.m31();
		System.out.printf("Old position: (%g, %g)\n", oldX, oldY);
		System.out.println("Board width: " + boardWidth + ", Board height: " + boardHeight);
		float widthX = 1 / scale.m00();
		float widthY = 1 / scale.m11();
		// 0 < x < max
		// width - 1 < x < (max - (width - 1))
		// width - 1 > x > (max - (width - 1)) where you times everything by -1
		float newX = Math.max(Math.min(oldX - x, -1 * (widthX - 1)), 
				-1 * (boardWidth * 2.0f - (widthX + 1)));
		float newY = Math.max(Math.min(oldY + y, -1 * (widthY - 1)), 
				-1 * (boardHeight * 2.0f - (widthY + 1)));
		System.out.printf("Resulting position: (%g, %g)\n", newX, newY);
		move(-newX, -newY);
	}
}
