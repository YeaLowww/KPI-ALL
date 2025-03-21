package com.example.holovnia_lab4;

import android.opengl.GLES32;
import static android.opengl.GLES32.GL_COMPILE_STATUS;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
//The base class for various concrete OpenGL ES examples
public class myWorkMode {
    protected int gl_Program = 0;
    protected int VAO_id = 0; //Vertex Array Object
    protected int VBO_id = 0; //Vertex Buffer Object
    protected float[] arrayVertex = null;
    protected int numVertex = 0;
    protected float alphaViewAngle = 0;
    protected float betaViewAngle = 0;
    protected float[] modelMatrix; //matrix for objects transformations
    protected float[] viewMatrix; //matrix for camera view
    protected float[] projectionMatrix; //matrix for projection
    myWorkMode() {
        gl_Program = 0;
        VAO_id = VBO_id = 0;
        modelMatrix = new float[16];
        viewMatrix = new float[16];
        projectionMatrix = new float[16];
    }
    public int getProgramId() {
        return gl_Program;
    }
    protected int myCompileShader(int shadertype, String shadercode) {
        int shader_id = GLES32.glCreateShader(shadertype);
        GLES32.glShaderSource(shader_id, shadercode);
        GLES32.glCompileShader(shader_id);
        //check shader compiling errors
        int[] res = new int[1];
        GLES32.glGetShaderiv(shader_id, GL_COMPILE_STATUS, res, 0);
        if (res[0] != 1) return 0;
        return shader_id;
    }
    protected void myCompileAndAttachShaders(String vsh, String fsh) {
        gl_Program = -1; //shader compiling error by default
        int vertex_shader_id = myCompileShader(GLES32.GL_VERTEX_SHADER, vsh);
        if (vertex_shader_id == 0) return; //shader compiling error
        int fragment_shader_id = myCompileShader(GLES32.GL_FRAGMENT_SHADER, fsh);
        if (fragment_shader_id == 0) return; //shader compiling error
        gl_Program = GLES32.glCreateProgram();
        GLES32.glAttachShader(gl_Program, vertex_shader_id);
        GLES32.glAttachShader(gl_Program, fragment_shader_id);
        GLES32.glLinkProgram(gl_Program);
        GLES32.glDeleteShader(vertex_shader_id); //no longer needed
        GLES32.glDeleteShader(fragment_shader_id);
    }
    protected void getId_VAO_VBO() {
        int[] tmp = new int[2];
        GLES32.glGenVertexArrays(1, tmp, 0);
        VAO_id = tmp[0]; //Vertex Array Object id
        GLES32.glGenBuffers(1, tmp, 0);
        VBO_id = tmp[0]; //Vertex Buffer Object id
    }
    protected void myVertexArrayBind(float[] src, String atrib) {
        if (gl_Program <= 0) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(src.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(src);
        vertexBuffer.position(0);
        getId_VAO_VBO();
        // Bind the Vertex Array Object first
        GLES32.glBindVertexArray(VAO_id);
        //then bind and set vertex buffer(s) and attribute pointer(s)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, VBO_id);
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,src.length*4, vertexBuffer,
                GLES32.GL_STATIC_DRAW);
        int handle = GLES32.glGetAttribLocation(gl_Program, atrib);
        GLES32.glEnableVertexAttribArray(handle);
        GLES32.glVertexAttribPointer(handle,3, GLES32.GL_FLOAT,false,3*4,0);
        GLES32.glEnableVertexAttribArray(0);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);
        GLES32.glBindVertexArray(0);
    }
    protected void myVertexArrayBind2(float[] src, int stride,
                                      String atrib1, int offset1,
                                      String atrib2, int offset2) {
        if (gl_Program <= 0) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(src.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(src);
        vertexBuffer.position(0);
        getId_VAO_VBO();
        // Bind the Vertex Array Object first
        GLES32.glBindVertexArray(VAO_id);
        //then bind and set vertex buffer
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, VBO_id);
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,src.length*4, vertexBuffer,
                GLES32.GL_STATIC_DRAW);
        //then define attribute pointers
        int handle = GLES32.glGetAttribLocation(gl_Program, atrib1);
        GLES32.glEnableVertexAttribArray(handle);
        GLES32.glVertexAttribPointer(handle, 3, GLES32.GL_FLOAT, false,
                stride*4, offset1);
        handle = GLES32.glGetAttribLocation(gl_Program, atrib2);
        GLES32.glEnableVertexAttribArray(handle);
        GLES32.glVertexAttribPointer(handle, 3, GLES32.GL_FLOAT, false,
                stride*4, offset2);
        GLES32.glEnableVertexAttribArray(0);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);
        GLES32.glBindVertexArray(0);
    }
    public void myCreateShaderProgram() { }
    protected void myCreateScene() { }
    public void myUseProgramForDrawing(int width, int height) {
        //Default implementation for simply triangulated scenes
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0,numVertex);
        GLES32.glBindVertexArray(0);
    }
    public boolean onTouchNotUsed() { return false; }
    public boolean onActionDown(float x, float y, int cx, int cy) { return false; }
    public boolean onActionMove(float x, float y, int cx, int cy) { return false; }


}