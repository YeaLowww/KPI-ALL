package com.example.holovnia_lab2;

import android.content.res.Resources;
import android.opengl.GLES32;
import android.os.SystemClock;
import android.util.DisplayMetrics;

public class myColorWeelAnimation extends myColorWeel {

    myColorWeelAnimation() {
        super();
    }

    public void myUseProgramForDrawing(int width, int height) {
        long time = SystemClock.uptimeMillis()% 2000L;
        float vLight;
        if (time <= 1000)
            vLight = time;
        else vLight = 1999 - time;
        int colorHandle = GLES32.glGetUniformLocation(gl_Program, "vLight");
        GLES32.glUniform1f(colorHandle,vLight);
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glUniform1f(colorHandle,1.0f);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, startVertexFAN, numVertexFAN);
        GLES32.glUniform1f(colorHandle,0.001f*(float)vLight);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, startVertexStrip, numVertexStrip);
        GLES32.glBindVertexArray(0);
    }

    @Override
    public void myCreateShaderProgram() {
        myCompileAndAttachShaders(
                myShadersLibrary.vertexShaderCode2,
                myShadersLibrary.fragmentShaderCode4);
        myVertexArrayBind2(arrayVertex, 6,
                "vPosition", 0,
                "vColor", 3 * 4);
    }
}
