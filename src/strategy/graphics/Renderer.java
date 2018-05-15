package strategy.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;

import strategy.assets.Texture;
import strategy.graphics.shaders.ShaderProgram;

import static org.lwjgl.glfw.GLFW.*;

public class Renderer {
	public Window window;
	protected int vertexArrayId;
	float[] triangles  = {
		-1.0f, -1.0f, 0.0f,
		1.0f, -1.0f, 0.0f,
		0.0f, 1.0f, 0.0f
	};
	public Map<String, Texture> textures;
	public ShaderProgram shaderProgram;
	protected Matrix4f position;
	protected int vertexBufferId;
	protected final String TEXTURE_UNIFORM_KEY = "textureSampler";
	protected final String SCALING_UNIFORM_KEY = "scaling";
	protected final String TRANSLATION_UNIFORM_KEY = "translation";
	protected final String POSITION_UNIFORM_KEY = "position";
	public Renderer (Window window) {
		this.window = window;
		vertexArrayId = glGenVertexArrays();
		glBindVertexArray(vertexArrayId);
		vertexBufferId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId);
		glBufferData(GL_ARRAY_BUFFER, triangles, GL_STATIC_DRAW);
		textures = new HashMap<String, Texture>();
		position = new Matrix4f();
		position.identity();
	}
	public void init () {
		// adding uniforms to shaderProgram
		shaderProgram.addUniform(TEXTURE_UNIFORM_KEY);
		shaderProgram.addUniform(SCALING_UNIFORM_KEY);
		shaderProgram.addUniform(TRANSLATION_UNIFORM_KEY);
		shaderProgram.addUniform(POSITION_UNIFORM_KEY);
	}
	public long getWindowId () {
		return window.window;
	}
	public void startRender () {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		shaderProgram.bind();
	}
	public void endRender () {
		// IMPORTANT for some reason
		glFlush();
		glfwSwapBuffers(window.window);
		//System.out.println("Swapped buffers");
		glfwPollEvents();
		//shaderProgram.bind();
	}
	public void renderTest () {
		draw(vertexBufferId, 3);
	}
	public void cleanup () {
		window.cleanup();
	}
	public void draw (int vertexbuffer, int vertices) {
		// test draw function
		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, vertexbuffer);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glDrawArrays(GL_TRIANGLES, 0, vertices);
		glDisableVertexAttribArray(0);
	}
	public void draw (String key, Camera camera, float x, float y) {
		if (!textures.containsKey(key)) {
			System.err.println("No image with key '" + key + "' found");
			return;
		}
		// turns image position to matrix
		position.identity();
		position.translate(x, y, 0.0f);
		
		// sets uniform values in the vertex shader
		shaderProgram.setUniform(POSITION_UNIFORM_KEY, position);
		shaderProgram.setUniform(SCALING_UNIFORM_KEY, camera.getScaleMatrix());
		shaderProgram.setUniform(TRANSLATION_UNIFORM_KEY, camera.getCameraMatrix());
		
		//System.out.println("Drawing texture!");
		// bind texture
		glActiveTexture(GL_TEXTURE0);
		//System.out.println("");
		glBindTexture(GL_TEXTURE_2D, textures.get(key).textureId);
		shaderProgram.setUniform(TEXTURE_UNIFORM_KEY, 0);
		
		//System.out.println("Binding vertices!");
		// bind vertices
		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, textures.get(key).vertexId);
		//System.out.println(textures.get(key).textureId);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		//System.out.println("Binding UVs!");
		// bind uvs
		glEnableVertexAttribArray(1);
		glBindBuffer(GL_ARRAY_BUFFER, textures.get(key).uvId);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		//System.out.println("Drawing triangles!");
		// draw triangles
		glDrawArrays(GL_TRIANGLES, 0, 2 * 3);
		
		//System.out.println("Disabling vertex attrib arrays!");
		// disable VAAs
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
	public void addTexture (String path, String key) throws IOException {
		// loads textures into the map so they can be called
		Texture texture = new Texture();
		texture.load(path);
		textures.put(key, texture);
	}
	public void draw (String texture, float x, float y, float scaleX, float scaleY) {
		
	}
}
