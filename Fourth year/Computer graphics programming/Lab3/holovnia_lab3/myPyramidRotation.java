package com.example.holovnia_lab3;

import android.opengl.GLES32;
import android.opengl.Matrix;
import android.os.SystemClock;

public class myPyramidRotation extends myWorkMode{

    protected float xTouchDown, yTouchDown, alphaAnglePrev, viewDistancePrev, viewDistance;
    protected float xCamera, yCamera, zCamera;

    protected float baseSize = 0.5f;
    protected float rotationFactor = 0.2f, zoomFactor = 0.05f;
    protected int numVertex;
    protected float[] arrayVertex1;
    protected float[] groundPrimaryColor = {1f, 1f, 1f};  // Білий
    protected float[] groundSecondaryColor = {0f, 0f, 0f};  // Чорний

    protected float[] pyramidPrimaryColor = {1.0f, 0.5f, 0.0f};
    protected float[] pyramidSecondaryColor = {0.5f, 1.0f, 0.5f};

    myPyramidRotation(){
        super();
        myCreateScene();
        xCamera = 0f;
        yCamera = -5f;
        zCamera = 5f;
        betaViewAngle = 50;
        viewDistance = calculateViewDistance();
    }

    @Override
    protected void myCreateScene() {
        int n = 5;
        numVertex = 6*3 + n*n*6;

        arrayVertex1 = new float[] {

                -baseSize, -baseSize, 0, pyramidPrimaryColor[0], pyramidPrimaryColor[1], pyramidPrimaryColor[2],
                baseSize, -baseSize, 0, 1, 0, 0,
                0, 0, baseSize, pyramidSecondaryColor[0], pyramidSecondaryColor[1], pyramidSecondaryColor[2],

                baseSize, -baseSize, 0, pyramidPrimaryColor[0], pyramidPrimaryColor[1], pyramidPrimaryColor[2],
                baseSize, baseSize, 0, pyramidPrimaryColor[0], pyramidPrimaryColor[1], pyramidPrimaryColor[2],
                0, 0, baseSize, pyramidSecondaryColor[0], pyramidSecondaryColor[1], pyramidSecondaryColor[2],

                baseSize, baseSize, 0, pyramidPrimaryColor[0], pyramidPrimaryColor[1], pyramidPrimaryColor[2],
                -baseSize, baseSize, 0, pyramidPrimaryColor[0], pyramidPrimaryColor[1], pyramidPrimaryColor[2],
                0, 0, baseSize, pyramidSecondaryColor[0], pyramidSecondaryColor[1], pyramidSecondaryColor[2],

                -baseSize, baseSize, 0, pyramidPrimaryColor[0], pyramidPrimaryColor[1], pyramidPrimaryColor[2],
                -baseSize, -baseSize, 0, pyramidPrimaryColor[0], pyramidPrimaryColor[1], pyramidPrimaryColor[2],
                0, 0, baseSize, pyramidSecondaryColor[0], pyramidSecondaryColor[1], pyramidSecondaryColor[2],

                -baseSize, -baseSize, 0, pyramidSecondaryColor[0], pyramidSecondaryColor[1], pyramidSecondaryColor[2],
                baseSize, -baseSize, 0, pyramidSecondaryColor[0], pyramidSecondaryColor[1], pyramidSecondaryColor[2],
                baseSize, baseSize, 0, pyramidSecondaryColor[0], pyramidSecondaryColor[1], pyramidSecondaryColor[2],

                baseSize, baseSize, 0, pyramidSecondaryColor[0], pyramidSecondaryColor[1], pyramidSecondaryColor[2],
                -baseSize, baseSize, 0, pyramidSecondaryColor[0], pyramidSecondaryColor[1], pyramidSecondaryColor[2],
                -baseSize, -baseSize, 0, pyramidSecondaryColor[0], pyramidSecondaryColor[1], pyramidSecondaryColor[2]
        };

        float[] arrayVertex2 = getChessboard(n,2);
        int av1_size = arrayVertex1.length;
        int av2_size = arrayVertex2.length;

        int size = av1_size + av2_size;

        arrayVertex = new float[size];

        System.arraycopy(arrayVertex1,0, arrayVertex, 0, av1_size);
        System.arraycopy(arrayVertex2,0, arrayVertex, av1_size, av2_size);
    }
    public float[] getChessboard(int n, float n2) {
        float n_square = n2 / n;
        float[] array = new float[n * n * 6 * 6]; // 6 вершин * (3 координати + 3 кольори)

        boolean firstPalette = true;
        int i = 0;

        for (float y = -n2 / 2; y < n2 / 2; y += n_square) {
            for (float x = -n2 / 2; x < n2 / 2; x += n_square) {
                float[] palette = firstPalette ? groundSecondaryColor : groundPrimaryColor;
                firstPalette = !firstPalette;

                i = addSquare(array, i, x, y, -0.1f, n_square, palette);
            }
            if (n % 2 == 0) firstPalette = !firstPalette;
        }
        return array;
    }
    private int addSquare(float[] array, int i, float x, float y, float z, float n_square, float[] color) {
        float[][] vertices = {
                {x, y + n_square, z}, {x, y, z}, {x + n_square, y, z}, {x + n_square, y, z}, {x + n_square, y + n_square, z}, {x, y + n_square, z}
        };
        for (float[] v : vertices) {
            array[i++] = v[0];
            array[i++] = v[1];
            array[i++] = v[2];
            array[i++] = color[0];
            array[i++] = color[1];
            array[i++] = color[2];
        }
        return i;
    }
    private float calculateViewDistance(){
        return (float) Math.sqrt(xCamera*xCamera+yCamera*yCamera+zCamera*zCamera);
    }
    @Override
    public boolean onActionDown(float x, float y, int cx, int cy) {
        if (x >= cx - cx/6){
            float kb =7f;
            if (y >= cy - cy/6){
                betaViewAngle = betaViewAngle + kb;
            } else if (y <= cy/6) {
                betaViewAngle = betaViewAngle - kb;
            }
            updatePosition();
        }

        xTouchDown = x;
        yTouchDown = y;
        alphaAnglePrev = alphaViewAngle;
        viewDistancePrev = viewDistance;

        return true;
    }
    @Override
    public boolean onActionMove(float x, float y, int cx, int cy) {
        float dx = x - xTouchDown;
        float dy = y - yTouchDown;
        alphaViewAngle = (alphaAnglePrev + rotationFactor * dx);
        viewDistance = Math.max(2.0f, viewDistancePrev - dy * zoomFactor);

        updatePosition();

        return true;
    }
    private void updatePosition(){
        float radAlpha = (float) Math.toRadians(alphaViewAngle);
        float radBeta = (float) Math.toRadians(betaViewAngle);

        xCamera = (float) (viewDistance * Math.sin(radBeta)*Math.sin(radAlpha));
        yCamera = (float) (-viewDistance * Math.sin(radBeta)*Math.cos(radAlpha));
        zCamera = (float) (viewDistance * Math.cos(radBeta));
    }
    @Override
    public void myUseProgramForDrawing(int width, int height) {
        super.myUseProgramForDrawing(width, height);
        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.rotateM(viewMatrix, 0, -betaViewAngle, 1, 0, 0); //rotation around X
        Matrix.rotateM(viewMatrix, 0, -alphaViewAngle, 0, 0, 1); //rotation around Z
        Matrix.translateM(viewMatrix, 0, -xCamera, -yCamera, -zCamera);

        //projection matrix definition
        float aspect = (float) width / height;
        Matrix.perspectiveM(projectionMatrix, 0, 45, aspect, 0.1f, 30);

        // Передаємо уніформні матриці
        int vMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "uViewMatrix");
        GLES32.glUniformMatrix4fv(vMatrixHandle, 1, false, viewMatrix, 0);
        vMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "uProjMatrix");
        GLES32.glUniformMatrix4fv(vMatrixHandle, 1, false, projectionMatrix, 0);
        vMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "uModelMatrix");
        GLES32.glUniformMatrix4fv(vMatrixHandle, 1, false, modelMatrix, 0);

        // Малюємо шахівницю
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 18, numVertex);

        // Малюємо піраміду (ОБЕРТАЄТЬСЯ)
        long time = SystemClock.uptimeMillis();
        long rotationAngle = (time / 10) % 360L;

        Matrix.rotateM(modelMatrix, 0, rotationAngle, 0, 0, 1);
        GLES32.glUniformMatrix4fv(vMatrixHandle, 1, false, modelMatrix, 0);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, 18);

        GLES32.glBindVertexArray(0);
    }

    @Override
    public void myCreateShaderProgram() {
        myCompileAndAttachShaders(
                myShadersLibrary.vertexShaderCode1,
                myShadersLibrary.fragmentShaderCode1);
        myVertexArrayBind2(arrayVertex, 6,
                "vPosition", 0,
                "vColor", 3*4);
    }
}