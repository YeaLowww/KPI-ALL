package com.example.holovnia_lab2;

import android.opengl.GLES32;
public class myExampleFirst extends myWorkMode {
    protected float UserColor[] = { 0.7f, 0.5f, 0.9f, 1.0f }; // Magenta
    myExampleFirst() {
        super();
        myCreateScene();
    }
    @Override
    protected void myCreateScene() {
        arrayVertex = new float[] { //triangle vertex coords
                0.5f, -0.5f, 0.0f, // Bottom Right
                -0.5f, -0.5f, 0.0f, // Bottom Left
                0.0f, 0.5f, 0.0f}; // Top
        numVertex = 3;
    }
    @Override
    public void myCreateShaderProgram() {
        myCompileAndAttachShaders(
                myShadersLibrary.vertexShaderCode1,
                myShadersLibrary.fragmentShaderCode2);
        myVertexArrayBind(arrayVertex,"vPosition");
    }
    @Override
    public void myUseProgramForDrawing(int width, int height) {
// Set color for drawing object(s)
// get handle to fragment shader's vColor member
        int colorHandle = GLES32.glGetUniformLocation(gl_Program, "vColor");
        GLES32.glUniform4fv(colorHandle, 1, UserColor, 0);
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, numVertex);
        GLES32.glBindVertexArray(0);
    }
}