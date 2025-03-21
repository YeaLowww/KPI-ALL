package com.example.holovnia_lab3;

public class myShadersLibrary {
    //--- Vertex Shaders
    public static final String vertexShaderCode1 =
            "#version 300 es\n" +
                    "uniform mat4 uModelMatrix;\n" +
                    "uniform mat4 uViewMatrix;\n" +
                    "uniform mat4 uProjMatrix;\n" +
                    "in vec3 vPosition;\n" +
                    "in vec3 vColor;\n" +
                    "out vec3 outColor;\n" +
                    "void main() {\n" +
                        "gl_Position = uProjMatrix * uViewMatrix * uModelMatrix * vec4(vPosition, 1.0f);\n" +
                        "outColor = vColor;\n" +
                    "}";
    public static final String fragmentShaderCode1 =
            "#version 300 es\n" +
                    "precision mediump float;\n" +
                    "in vec3 outColor;\n" +
                    "out vec4 resultColor;\n" +
                    "void main() {\n" +
                        "resultColor = vec4(outColor, 1.0f);\n" +
                    "}";
}

