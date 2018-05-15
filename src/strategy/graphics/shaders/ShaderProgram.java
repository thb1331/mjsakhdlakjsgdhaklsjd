package strategy.graphics.shaders;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

public class ShaderProgram {
	// handles the shaders
	public final int programId;
	private int vertexId;
	private int fragmentId;
	private Map<String, Integer> uniforms;
	protected FloatBuffer dataBuffer;
	public ShaderProgram () throws Exception {
		programId = glCreateProgram();
		if (programId == 0) {
			throw new Exception ("Could not create shader program");
		}
		uniforms = new HashMap<String, Integer>();
		dataBuffer = MemoryUtil.memAllocFloat(16);
	}
	public void createShaders (String vertexPath, String fragmentPath) {
		vertexId = createShader (vertexPath, GL_VERTEX_SHADER);
		fragmentId = createShader (fragmentPath, GL_FRAGMENT_SHADER);
	}
	public int createShader (String path, int type) {
		int shaderId = ShaderLoader.loadShader(path, type);
		glAttachShader(programId, shaderId);
		return shaderId;
	}
	public void link () {
		glLinkProgram (programId);
		if(glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			System.err.println("Error linking shader code: " + glGetProgramInfoLog(programId, 1024));
		}
		if (vertexId != 0) {
			glDetachShader(programId, vertexId);
		}
		if (fragmentId != 0) {
			glDetachShader(programId, fragmentId);
		}
		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
			System.err.println("Warning validating shader code: " + glGetProgramInfoLog(programId, 1024));
		}
	}
	public void bind () {
		glUseProgram(programId);
	}
	public void unbind() {
		glUseProgram(0);
	}
	public void cleanup() {
		unbind();
		if (programId != 0) {
			glDeleteProgram(programId);
		}
		MemoryUtil.memFree(dataBuffer);
	}
	public void addUniform (String key) {
		int uniformid = glGetUniformLocation(programId, key);
		uniforms.put(key, uniformid);
	}
	public void setUniform (String key, int value) {
		if (!uniforms.containsKey(key)) {
			return;
		}
		glUniform1i(uniforms.get(key), value);
	}
	public void setUniform (String key, Matrix4f value) {
		if (!uniforms.containsKey(key)) {
			return;
		}
		value.get(dataBuffer);
		glUniformMatrix4fv(uniforms.get(key), false, dataBuffer);
		dataBuffer.clear();
	}
}
