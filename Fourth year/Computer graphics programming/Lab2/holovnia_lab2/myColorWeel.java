package com.example.holovnia_lab2;

import android.content.res.Resources;
import android.opengl.GLES32;
import android.util.DisplayMetrics;

public class myColorWeel extends myWorkMode {
    private int screenWidth;
    private int screenHeight;
    private float aspectRatio;
    int startVertexFAN, numVertexFAN, numVertexStrip, startVertexStrip;
    private float[] aVF, aVS, arrayVertexColorFan, getArrayVertexColorStrip;

    myColorWeel() {
        super();
        myCreateScene();
    }

    @Override
    protected void myCreateScene() {

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        aspectRatio = (float) screenWidth / screenHeight;


        arrayVertex = new float[1000];
        aVF = new float[9 * 3];
        float xc = 0;
        float yc = 0;
        float r = 0.7f;
        int tmp = 0, pos = 0;
        aVF[tmp++] = xc;
        aVF[tmp++] = aspectRatio * yc;
        aVF[tmp++] = 0;
        for (int i = 0; i < 8; i++) {
            float a = (float) Math.PI / 2 - 2 * (float) Math.PI * (float) i / 7;
            float x = xc + r * (float) Math.cos(a);
            float y = yc + r * (float) Math.sin(a);
            aVF[tmp++] = x;
            aVF[tmp++] = aspectRatio * y;
            aVF[tmp++] = 0;
        }
        tmp = 0;

        arrayVertexColorFan = new float[]{
                aVF[tmp++], aVF[tmp++], aVF[tmp++], 1.0f, 1.0f, 1.0f,//1
                aVF[tmp++], aVF[tmp++], aVF[tmp++], 1.0f, 0.0f, 0.0f,
                aVF[tmp++], aVF[tmp++], aVF[tmp++], 1.0f, 0.5f, 0.0f,
                aVF[tmp++], aVF[tmp++], aVF[tmp++], 1.0f, 1.5f, 0.0f,
                aVF[tmp++], aVF[tmp++], aVF[tmp++], 0.0f, 1.0f, 0.0f,
                aVF[tmp++], aVF[tmp++], aVF[tmp++], 0.0f, 1.0f, 1.0f,
                aVF[tmp++], aVF[tmp++], aVF[tmp++], 0.0f, 0.0f, 1.0f,
                aVF[tmp++], aVF[tmp++], aVF[tmp++], 1.0f, 0.0f, 1.0f,
                aVF[tmp++], aVF[tmp++], aVF[tmp++], 1.0f, 0.0f, 0.0f,//9
        };
        startVertexFAN = pos / 6;
        pos = myAddTriangleFan(pos, arrayVertexColorFan);
        numVertexFAN = pos / 6 - startVertexFAN;

        tmp=0;
        aVS = new float[14 * 3];
        for (int i = 0; i < 7; i++) {
            float x1 = (float) (-0.9 + 0.3 * i);
            float y1 = -0.9f;
            float x2 = (float) (-0.9 + 0.3 * i);
            float y2 = -1.2f;
            aVS[tmp++] = x1;
            aVS[tmp++] = aspectRatio * y1;
            aVS[tmp++] = 0;
            aVS[tmp++] = x2;
            aVS[tmp++] = aspectRatio * y2;
            aVS[tmp++] = 0;
        }
        tmp = 0;
        getArrayVertexColorStrip = new float[]{
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 1.0f, 0.0f, 0.0f,//1
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 1.0f, 0.0f, 0.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 1.0f, 0.5f, 0.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 1.0f, 0.5f, 0.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 1.0f, 1.0f, 0.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 1.0f, 1.0f, 0.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 0.0f, 1.0f, 0.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 0.0f, 1.0f, 0.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 0.0f, 1.0f, 1.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 0.0f, 1.0f, 1.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 0.0f, 0.0f, 1.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 0.0f, 0.0f, 1.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 1.0f, 0.0f, 1.0f,
                aVS[tmp++], aVS[tmp++], aVS[tmp++], 1.0f, 0.0f, 1.0f,//14
        };
        startVertexStrip = pos / 6;
        pos = myAddTriangleStrip(pos, getArrayVertexColorStrip);
        numVertexStrip = pos / 6 - startVertexStrip;

    }

    private int myAddTriangleStrip(int pos, float[] Vertexs) {
        for (int i = 0; i < Vertexs.length; i++) {
            arrayVertex[pos++] = Vertexs[i];
        }
        return pos;
    }

    private int myAddTriangleFan(int pos1, float[] Vertexs) {
        for (int i = 0; i < Vertexs.length; i++) {
            arrayVertex[pos1++] = Vertexs[i];
        }
        return pos1;
    }

    public void myCreateShaderProgram() {
        myCompileAndAttachShaders(
                myShadersLibrary.vertexShaderCode2,
                myShadersLibrary.fragmentShaderCode3);
        myVertexArrayBind2(arrayVertex, 6,
                "vPosition", 0,
                "vColor", 3 * 4);
    }

    @Override
    public void myUseProgramForDrawing(int width, int height) {
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, startVertexFAN, numVertexFAN);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, startVertexStrip, numVertexStrip);
        GLES32.glBindVertexArray(0);
    }
}