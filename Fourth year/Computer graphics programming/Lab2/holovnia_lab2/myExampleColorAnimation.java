package com.example.holovnia_lab2;

import android.opengl.GLES32;
import android.os.SystemClock;
public class myExampleColorAnimation extends myExampleFirst {
    myExampleColorAnimation() {
        super();
    }
    @Override
    public void myUseProgramForDrawing(int width, int height) {
//using time for color varying
        long time = SystemClock.uptimeMillis() % 2000L;
        long clrvar;
        if (time <= 1000)
            clrvar = time;
        else clrvar = 1999 - time;
        UserColor[0] = 0.001f*(float)clrvar;
        int colorHandle = GLES32.glGetUniformLocation(gl_Program, "vColor");
        GLES32.glUniform4fv(colorHandle, 1, UserColor, 0);
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, numVertex);
        GLES32.glBindVertexArray(0);
    }
}