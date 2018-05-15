package strategy.assets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Texture implements Asset{
	public int textureId;
	public int uvId;
	public int vertexId;
	public Texture () {
		// gen buffers
		textureId = glGenTextures();
		uvId = glGenBuffers();
		vertexId = glGenBuffers();
	}
	public void load(String path) throws IOException {
		// gets the pixel data
		InputStream in = new FileInputStream(path);
		PNGDecoder decoder = new PNGDecoder (in);
		//in.close();
		int width = decoder.getWidth();
		int height = decoder.getHeight();
		ByteBuffer buf = MemoryUtil.memAlloc(width*height*4);
		decoder.decode(buf, width*4, Format.RGBA);
		in.close();
		buf.flip();
		
		// gives data to OpenGL
		textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		MemoryUtil.memFree(buf);
		//glGenerateMipmap(GL_TEXTURE_2D);
		float[] defaultUV = {
			0.0f, 1.0f,
			1.0f, 1.f,
			0.0f, 0.0f,
			1.0f, 0.0f, 
			1.0f, 1.0f,
			0.0f, 0.0f
		};
		float[] defaultVertices = {
			-1.0f, -1.0f, 0.0f,
			1.0f, -1.0f, 0.0f,
			-1.0f, 1.0f, 0.0f,
			1.0f, 1.0f, 0.0f,
			1.0f, -1.0f, 0.0f,
			-1.0f, 1.0f, 0.0f
		};
		// sets vertices and uvs to default, which can be changed if you call it
		setUV(defaultUV);
		setVertices(defaultVertices);
	}
	public void setUV (float[] uv) {
		// loads uv values into opengl
		if (uv.length != 6 * 2) {
			System.err.println("Incorrect amount of UVs");
			return;
		}
		glBindBuffer(GL_ARRAY_BUFFER, uvId);
		glBufferData(GL_ARRAY_BUFFER, uv, GL_STATIC_DRAW);
	}
	public void setVertices (float[] vertices) {
		// loads vertices into opengl
		if (vertices.length != 18) {
			System.err.println("Incorrect number of vertices");
			return;
		}
		//System.out.println("set vertices!");
		glBindBuffer(GL_ARRAY_BUFFER, vertexId);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	public int getTextureId () {
		return textureId;
	}
}
