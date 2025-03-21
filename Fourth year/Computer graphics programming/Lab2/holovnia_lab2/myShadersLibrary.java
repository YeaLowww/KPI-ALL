package com.example.holovnia_lab2;

public class myShadersLibrary {
    //--- Vertex Shaders
    public static final String vertexShaderCode1 =
            "#version 300 es\n" +
                    "in vec3 vPosition;\n" +
                    "void main() {\n" +
                    " gl_Position = vec4(vPosition, 1.0f);\n" +
                    "}\n";
    //--- Fragment Shaders
    public static final String fragmentShaderCode2 =
            "#version 300 es\n" +
                    "precision mediump float;\n" +
                    "uniform vec4 vColor;\n" +
                    "out vec4 outColor;\n" +
                    "void main() {\n" +
                    " outColor = vColor;\n" +
                    "}\n";
    public static final String vertexShaderCode2 =
            "#version 300 es\n" +
                    "in vec3 vPosition;\n" +
                    "in vec3 vColor;\n" +
                    "out vec3 outColor;\n" +
                    "void main() {\n" +
                    "gl_Position = vec4(vPosition, 1.0f);\n" +
                    "outColor = vColor;\n" +
                    "}";
    public static final String fragmentShaderCode3 =
            "#version 300 es\n" +
                    "precision mediump float;\n" +
                    "in vec3 outColor;\n" +
                    "out vec4 resultColor;\n" +
                    "void main() {\n" +
                    "resultColor = vec4(outColor, 1.0f);\n" +
                    "}\n";
    public static final String fragmentShaderCode4 =
            "#version 300 es\n" +
                    "precision mediump float;\n" +
                    "uniform float vLight;\n" +
                    "in vec3 outColor;\n" +
                    "out vec4 resultColor;\n" +
                    "void main() {\n" +
                    "resultColor = vec4(vLight*outColor, 1.0f);\n" +
                    "}";

}

