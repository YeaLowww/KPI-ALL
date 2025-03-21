package com.example.holovnia_lab2;


public class myOwnColorForEachVertexMode extends myWorkMode {
    myOwnColorForEachVertexMode() {
        super();
        myCreateScene();
    }
    @Override
    protected void myCreateScene() {
//coords and color for each vertex individually
        arrayVertex = new float[] {
// coords color
                0.5f, -0.5f, 0.0f, 0.0f, 0.8f, 1.0f,
                -0.5f, -0.5f, 0.0f, 0.3f, 1.0f, 0.0f,
                0.0f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f};
        numVertex = 3;
    }
    public void myCreateShaderProgram() {
        myCompileAndAttachShaders(
                myShadersLibrary.vertexShaderCode2,
                myShadersLibrary.fragmentShaderCode3);
        myVertexArrayBind2(arrayVertex, 6,
                "vPosition", 0,
                "vColor", 3*4);
    }
}