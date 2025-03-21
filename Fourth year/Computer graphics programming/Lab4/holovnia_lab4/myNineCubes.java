package com.example.holovnia_lab4;

import android.opengl.GLES32;
import android.opengl.Matrix;

public class myNineCubes extends myWorkMode{

    protected float[] groundPrimaryColor = {1f, 1f, 1f}, groundSecondaryColor = {0f, 0f, 0f};
    protected float[] cubeColor = {1.0f, 0.0f, 0.0f}, cubeColor1 = {0.0f, 1.0f, 0.0f}, cubeColor2 = {0.0f, 0.0f, 1.0f};
    protected float xTouchDown, yTouchDown, alphaAnglePrev, viewDistancePrev, viewDistance;
    protected float xCamera, yCamera, zCamera;
    protected float rotationFactor = 0.2f, zoomFactor = 0.05f;
    // Джерело світла
    protected float[] lightPosition = {0f, 0f, 2f};
    protected float[] lightColor = {1.0f, 0.0f, 1.0f}; // Розово-фіолетовий колір
    protected float ambientStrength = 0.2f;
    protected float diffuseStrength = 1.0f;
    private int lightVAO, lightVBO;
    myNineCubes(){
        super();
        myCreateScene();
        xCamera = 0f;
        yCamera = -5f;
        zCamera = 5f;
        betaViewAngle = 50;
        viewDistance = getViewDistance();
    }

    @Override
    protected void myCreateScene() {
        int n = 3;
        float n2 = 2;
        float n_square = n2/n;

        float[] arrayVertexXY = getXYTile(n, n_square);
        float[] arrayVertexXZ = getXZTile(n, n_square);
        float[] arrayVertexYZ = getYZTile(n, n_square);
        float[] arrayVertexChess = getChessboard(20, 10);

        int avXY_size = arrayVertexXY.length;
        int avChess_size = arrayVertexChess.length;

        int size = avXY_size*3 + avChess_size;

        arrayVertex = new float[size];

        System.arraycopy(arrayVertexXY,0, arrayVertex, 0, avXY_size);
        System.arraycopy(arrayVertexXZ,0, arrayVertex, avXY_size, avXY_size);
        System.arraycopy(arrayVertexYZ,0, arrayVertex, avXY_size*2, avXY_size);
        System.arraycopy(arrayVertexChess,0, arrayVertex, avXY_size*3, avChess_size);

    }
    private float[] generateTile(int n, float n_square, float startX, float startY, float startZ, float[] color, String transform) {
        int TilesN = 6;
        int verN = 9 * 6 * TilesN;
        numVertex += verN;
        int size = verN * 6;
        float[] arrayVertexRes = new float[size];
        int sizeOneTile = size / 6;

        for (int c = 0; c < TilesN; c++) {
            float[] arrayVertex = getCubesArray(n, startX, startY, startZ + c * n_square, n_square, color);
            if (!transform.equals("XY")) {
                arrayVertex = transformTile(arrayVertex, transform);
            }
            System.arraycopy(arrayVertex, 0, arrayVertexRes, c * sizeOneTile, sizeOneTile);
        }
        return arrayVertexRes;
    }
    public float[] getXYTile(int n, float n_square) {
        return generateTile(n, n_square, -1f, -1f, 0, cubeColor2, "XY");
    }
    public float[] getXZTile(int n, float n_square) {
        return generateTile(n, n_square, -1f, -n_square * 5, -1, cubeColor, "XZ");
    }
    public float[] getYZTile(int n, float n_square) {
        return generateTile(n, n_square, -n_square * 5, -1, -1, cubeColor1, "YZ");
    }
    public float[] transformTile(float[] array, String Tile) {
        float[] transformedArray = new float[array.length];
        for (int i = 0; i < array.length; i += 6) {
            float x = array[i], y = array[i + 1], z = array[i + 2];

            switch (Tile) {
                case "XZ":
                    transformedArray[i] = x;
                    transformedArray[i + 1] = z;
                    transformedArray[i + 2] = -y;
                    break;
                case "YZ":
                    transformedArray[i] = z;
                    transformedArray[i + 1] = y;
                    transformedArray[i + 2] = -x;
                    break;
                default:
                    System.arraycopy(array, i, transformedArray, i, 3);
            }
            System.arraycopy(array, i + 3, transformedArray, i + 3, 3);
        }
        return transformedArray;
    }

    public float[] getCubesArray(int n, float xBegin, float yBegin, float zBegin, float n_square, float[] cubeColor) {
        float[] array = new float[n * n * 6 * 6];
        int i = 0;

        for (float y = yBegin; y < yBegin + 2 * n_square * n; y += 2 * n_square) {
            for (float x = xBegin; x < xBegin + 2 * n_square * n; x += 2 * n_square) {
                i = addSquare(array, i, x, y, zBegin, n_square, cubeColor);
            }
        }

        return array;
    }
    public float[] getChessboard(int n, float n2) {
        numVertex += n * n * 6;
        float[] array = new float[n * n * 6 * 6];
        float n_square = n2 / n;
        boolean firstPallete = true;
        int i = 0;

        for (float y = -n2 / 2; y < n2 / 2; y += n_square) {
            for (float x = -n2 / 2; x < n2 / 2; x += n_square) {
                float[] pallete = firstPallete ? groundSecondaryColor : groundPrimaryColor;
                firstPallete = !firstPallete;
                i = addSquare(array, i, x, y, -0.1f, n_square, pallete);
            }
            if (n % 2 == 0) firstPallete = !firstPallete;
        }

        return array;
    }
    private int addSquare(float[] array, int i, float x, float y, float z, float n_square, float[] color) {
        float[][] vertices = {
                {x, y + n_square, z}, {x, y, z}, {x + n_square, y + n_square, z},
                {x, y, z}, {x + n_square, y, z}, {x + n_square, y + n_square, z}
        };

        for (float[] vertex : vertices) {
            array[i++] = vertex[0];
            array[i++] = vertex[1];
            array[i++] = vertex[2];
            array[i++] = color[0];
            array[i++] = color[1];
            array[i++] = color[2];
        }

        return i;
    }
    private float getViewDistance(){
        return (float) Math.sqrt(xCamera*xCamera+yCamera*yCamera+zCamera*zCamera);
    }
    @Override
    public boolean onActionDown(float x, float y, int cx, int cy) {
        if (x >= cx - cx/6){
            float kb =7f;
            if (y >= cy - cy/6){
                betaViewAngle = betaViewAngle - kb;
            } else if (y <= cy/6) {
                betaViewAngle = betaViewAngle + kb;
            }
        }
        if (y >= cy/2 - cy/6 && y<=cy/2 + cy/6){
            float ka =7f;
            if (x >= cx - cx/6){
                alphaViewAngle = alphaViewAngle - ka;
            } else if (x <= cx/6) {
                alphaViewAngle = alphaViewAngle + ka;
            }
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

        float step = 0.01f *(yTouchDown-y);
        xCamera-=step*(float) Math.sin(0.017453*alphaViewAngle);
        yCamera+=step*(float) Math.cos(0.017453*alphaViewAngle);
        zCamera-=step*(float) Math.sin(0.017453*betaViewAngle);
        yTouchDown=y;
        return true;
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
        // Передача уніформ для освітлення
        int cameraPosHandle = GLES32.glGetUniformLocation(gl_Program, "uCameraPos");
        GLES32.glUniform3fv(cameraPosHandle, 1, new float[]{xCamera, yCamera, zCamera}, 0);

        int lightPosHandle = GLES32.glGetUniformLocation(gl_Program, "uLightPos");
        GLES32.glUniform3fv(lightPosHandle, 1, lightPosition, 0);

        int lightColorHandle = GLES32.glGetUniformLocation(gl_Program, "uLightColor");
        GLES32.glUniform3fv(lightColorHandle, 1, lightColor, 0);

        int ambientHandle = GLES32.glGetUniformLocation(gl_Program, "uAmbientStrength");
        GLES32.glUniform1f(ambientHandle, ambientStrength);

        int diffuseHandle = GLES32.glGetUniformLocation(gl_Program, "uDiffuseStrength");
        GLES32.glUniform1f(diffuseHandle, diffuseStrength);

        // Малюємо шахівницю
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 18, numVertex);

        GLES32.glUniformMatrix4fv(vMatrixHandle, 1, false, modelMatrix, 0);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, arrayVertex.length/6);

        GLES32.glBindVertexArray(0);
    }

    @Override
    public void myCreateShaderProgram() {
        myCompileAndAttachShaders(
                vertexShaderCode,
                fragmentShaderCode);
        myVertexArrayBind2(arrayVertex, 6,
                "vPosition", 0,
                "vColor", 3*4);
    }
    // Вершинний шейдер
    private static final String vertexShaderCode =
            "uniform mat4 uModelMatrix;\n" +
                    "uniform mat4 uViewMatrix;\n" +
                    "uniform mat4 uProjMatrix;\n" +
                    "attribute vec3 vPosition;\n" +
                    "attribute vec3 vColor;\n" +
                    "varying vec3 fragColor;\n" +
                    "varying vec3 fragPos;\n" +
                    "void main() {\n" +
                    "    fragPos = vec3(uModelMatrix * vec4(vPosition, 1.0));\n" +
                    "    fragColor = vColor;\n" +
                    "    gl_Position = uProjMatrix * uViewMatrix * vec4(fragPos, 1.0);\n" +
                    "}";

    // Фрагментний шейдер
    private static final String fragmentShaderCode =
            "precision mediump float;\n" +
                    "\n" +
                    "uniform vec3 uLightPos;\n" +
                    "uniform vec3 uLightColor; // Колір джерела світла\n" +
                    "uniform float uAmbientStrength;\n" +
                    "uniform float uDiffuseStrength;\n" +
                    "uniform vec3 uCameraPos; // Позиція камери\n" +
                    "varying vec3 fragPos;\n" +
                    "varying vec3 fragColor;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    // Розрахунок амбієнтного освітлення\n" +
                    "    vec3 ambient = uAmbientStrength * uLightColor;\n" +
                    "\n" +
                    "    // Оскільки ми працюємо з постійною нормаллю, можливо, потрібно використовувати нормаль для правильного освітлення\n" +
                    "    vec3 norm = normalize(vec3(0.0, 0.0, 1.0)); \n" +
                    "    vec3 lightDir = normalize(uLightPos - fragPos);\n" +
                    "    \n" +
                    "    // Розрахунок дифузного освітлення\n" +
                    "    float diff = max(dot(norm, lightDir), 0.0);\n" +
                    "    vec3 diffuse = uDiffuseStrength * diff * uLightColor;\n" +
                    "\n" +
                    "    // Обчислення відстані для освітлення\n" +
                    "    float distance = length(uLightPos - fragPos);\n" +
                    "    float attenuation = 1.0 / (1.0 + 0.1 * distance * distance);\n" +
                    "\n" +
                    "    // Визначення відстані між камерою і фрагментом\n" +
                    "    float cameraDistance = length(uCameraPos - fragPos);\n" +
                    "    \n" +
                    "    // Масштабування яскравості відповідно до відстані від камери\n" +
                    "    float brightnessFactor = 1.0 / (1.0 + 0.1 * cameraDistance * cameraDistance);\n" +
                    "\n" +
                    "    // Фінальний результат: комбінуємо амбієнтне і дифузне освітлення, з урахуванням відстані до камери\n" +
                    "    vec3 result = (ambient + diffuse * attenuation) * fragColor * brightnessFactor;\n" +
                    "\n" +
                    "    // Встановлюємо кольори в фрагмент\n" +
                    "    gl_FragColor = vec4(result, 1.0);\n" +
                    "}\n";
}