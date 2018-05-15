#version 330 core
layout(location = 0) in vec3 vertexPosition_modelSpace;
layout(location = 1) in vec2 vertexUV;

uniform mat4 scaling;
uniform mat4 translation;
uniform mat4 position;

out vec2 UV;

void main () {
	vec4 absPos = vec4(vertexPosition_modelSpace, 1);
	gl_Position = scaling * translation * position * absPos;
	//gl_Position = absPos2;
	UV = vertexUV;
}
