#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTextureCoordinates;
layout (location=3) in float aTextureId;

uniform mat4 uProjection;
uniform mat4 uView;


out vec4 fColor;
out vec2 fTextureCoordinates;
out float fTextureId;

void main()
{
    fColor = aColor;
    fTextureCoordinates = aTextureCoordinates;
    fTextureId = aTextureId;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTextureCoordinates;
in float fTextureId;

uniform sampler2D uTextures[8];

out vec4 color;

void main()
{
    if(fTextureId > 0) {
        int id = int(fTextureId);
        color =  fColor * texture(uTextures[id], fTextureCoordinates);
        //color = vec4(fTextureCoordinates, 0, 1);
    } else {
        color = fColor;
    }
}