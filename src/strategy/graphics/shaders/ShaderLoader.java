package strategy.graphics.shaders;

import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;

import strategy.Utils;

public class ShaderLoader {
	public static int loadShader (String path, int type) {
		int shaderid = glCreateShader(type);
		if (shaderid == 0) {
			System.err.println("Error creating shader of type: " + type);
		}
		String code;
		try {
			code = Utils.loadResource(path);
		} catch (IOException e) {
			System.out.println("Unable to open file: " + path);
			e.printStackTrace();
			return 0;
		}
		
		System.out.println("Compiling shader at: " + path);
		glShaderSource(shaderid, code);
		glCompileShader(shaderid);
		if (glGetShaderi(shaderid, GL_COMPILE_STATUS) == 0) {
			System.err.println("Error compiling shader code: " + glGetShaderInfoLog(shaderid, 1024));
		}
		return shaderid;
	}
}
