package com.example.holovnia_lab4;

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
    // Джерело світла
    protected float[] lightPosition = {0.2f, 0.2f, 1f};
    protected float[] lightColor = {1.0f, 1.0f, 1.0f}; // Розово-фіолетовий колір
    protected float ambientStrength = 0.5f;
    protected float diffuseStrength = 0.3f;
    // Сила спекулярного освітлення та шерохуватість
    protected float specularStrength = 5.5f;
    protected float shininess = 400.0f; // Для спекулярного освітлення
    private int lightVAO, lightVBO;
    myPyramidRotation(){
        super();
        myCreateScene();
        xCamera = 0f;
        yCamera = -15f;
        zCamera = 9f;
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

        float[] arrayVertex2 = getChessboard(n,50);
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
        // Зберігаємо початкові координати дотику
        xTouchDown = x;
        yTouchDown = y;
        return true;
    }

    @Override
    public boolean onActionMove(float x, float y, int cx, int cy) {
        // Обчислюємо різницю в координатах
        float dx = x - xTouchDown;
        float dy = y - yTouchDown;

        // Оновлюємо координати джерела світла
        lightPosition[0] += dx * 0.01f; // Переміщаємо по осі X
        lightPosition[1] -= dy * 0.01f; // Переміщаємо по осі Y

        // Оновлюємо початкові координати для наступного руху
        xTouchDown = x;
        yTouchDown = y;

        // Переміщаємо джерело світла
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
        // Передаємо нові уніформні значення для спекулярного освітлення
        int specularHandle = GLES32.glGetUniformLocation(gl_Program, "uSpecularStrength");
        GLES32.glUniform1f(specularHandle, specularStrength);

        int shininessHandle = GLES32.glGetUniformLocation(gl_Program, "uShininess");
        GLES32.glUniform1f(shininessHandle, shininess);
        int lightPosHandle = GLES32.glGetUniformLocation(gl_Program, "uLightPos");
        GLES32.glUniform3fv(lightPosHandle, 1, lightPosition, 0);

        int lightColorHandle = GLES32.glGetUniformLocation(gl_Program, "uLightColor");
        GLES32.glUniform3fv(lightColorHandle, 1, lightColor, 0);

        int ambientHandle = GLES32.glGetUniformLocation(gl_Program, "uAmbientStrength");
        GLES32.glUniform1f(ambientHandle, ambientStrength);

        int diffuseHandle = GLES32.glGetUniformLocation(gl_Program, "uDiffuseStrength");
        GLES32.glUniform1f(diffuseHandle, diffuseStrength);
        // Малюємо джерело світла у вигляді куба
        drawLightSourceCube();
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
    private void drawLightSourceCube() {
        float[] lightVertices = {
                // Кожна вершина: (x, y, z) + 3 кольори для кожної вершини
                lightPosition[0] - 0.1f, lightPosition[1] - 0.1f, lightPosition[2] - 0.1f, lightColor[0], lightColor[1], lightColor[2],  // 0
                lightPosition[0] + 0.1f, lightPosition[1] - 0.1f, lightPosition[2] - 0.1f, lightColor[0], lightColor[1], lightColor[2],  // 1
                lightPosition[0] + 0.1f, lightPosition[1] + 0.1f, lightPosition[2] - 0.1f, lightColor[0], lightColor[1], lightColor[2],  // 2
                lightPosition[0] - 0.1f, lightPosition[1] + 0.1f, lightPosition[2] - 0.1f, lightColor[0], lightColor[1], lightColor[2],  // 3
                lightPosition[0] - 0.1f, lightPosition[1] - 0.1f, lightPosition[2] + 0.1f, lightColor[0], lightColor[1], lightColor[2],  // 4
                lightPosition[0] + 0.1f, lightPosition[1] - 0.1f, lightPosition[2] + 0.1f, lightColor[0], lightColor[1], lightColor[2],  // 5
                lightPosition[0] + 0.1f, lightPosition[1] + 0.1f, lightPosition[2] + 0.1f, lightColor[0], lightColor[1], lightColor[2],  // 6
                lightPosition[0] - 0.1f, lightPosition[1] + 0.1f, lightPosition[2] + 0.1f, lightColor[0], lightColor[1], lightColor[2],  // 7
        };

        int[] indices = {
                0, 1, 2, 0, 2, 3,    // Нижня сторона
                4, 5, 6, 4, 6, 7,    // Верхня сторона
                0, 1, 5, 0, 5, 4,    // Передня сторона
                1, 2, 6, 1, 6, 5,    // Бічна права
                2, 3, 7, 2, 7, 6,    // Задня сторона
                3, 0, 4, 3, 4, 7     // Бічна ліва
        };

        int[] buffers = new int[2];
        GLES32.glGenVertexArrays(1, buffers, 0);
        lightVAO = buffers[0];
        GLES32.glGenBuffers(1, buffers, 1);
        lightVBO = buffers[1];

        GLES32.glBindVertexArray(lightVAO);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, lightVBO);
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, lightVertices.length * 4,
                java.nio.ByteBuffer.allocateDirect(lightVertices.length * 4)
                        .order(java.nio.ByteOrder.nativeOrder())
                        .asFloatBuffer().put(lightVertices).position(0),
                GLES32.GL_STATIC_DRAW);

        // Передаємо індекси
        int[] indexBuffer = new int[1];
        GLES32.glGenBuffers(1, indexBuffer, 0);
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0]);
        GLES32.glBufferData(GLES32.GL_ELEMENT_ARRAY_BUFFER, indices.length * 4,
                java.nio.ByteBuffer.allocateDirect(indices.length * 4)
                        .order(java.nio.ByteOrder.nativeOrder())
                        .asIntBuffer().put(indices).position(0),
                GLES32.GL_STATIC_DRAW);

        GLES32.glVertexAttribPointer(0, 3, GLES32.GL_FLOAT, false, 6 * 4, 0);
        GLES32.glEnableVertexAttribArray(0);
        GLES32.glVertexAttribPointer(1, 3, GLES32.GL_FLOAT, false, 6 * 4, 3 * 4);
        GLES32.glEnableVertexAttribArray(1);

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);
        GLES32.glBindVertexArray(0);

        GLES32.glBindVertexArray(lightVAO);
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0]);
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, indices.length, GLES32.GL_UNSIGNED_INT, 0);
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
                    "uniform vec3 uLightPos;  // Позиція джерела світла\n" +
                    "uniform vec3 uLightColor; // Колір джерела світла\n" +
                    "uniform float uAmbientStrength;\n" +
                    "uniform float uDiffuseStrength;\n" +
                    "uniform float uSpecularStrength;  // Сила спекулярного освітлення\n" +
                    "uniform float uShininess;        // Шерохуватість для спекулярного освітлення\n" +
                    "\n" +
                    "varying vec3 fragPos;\n" +
                    "varying vec3 fragColor;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    // Обчислення амбієнтного освітлення\n" +
                    "    vec3 ambient = uAmbientStrength * uLightColor;\n" +
                    "\n" +
                    "    // Розрахунок нормалі для піраміди (якщо вона вже відома)\n" +
                    "    vec3 norm = normalize(vec3(0.0, 0.0, 1.0));\n" +
                    "\n" +
                    "    // Вектор від джерела світла до фрагмента\n" +
                    "    vec3 lightDir = normalize(uLightPos - fragPos);\n" +
                    "\n" +
                    "    // Обчислення дифузного освітлення\n" +
                    "    float diff = max(dot(norm, lightDir), 0.0);\n" +
                    "    vec3 diffuse = uDiffuseStrength * diff * uLightColor;\n" +
                    "\n" +
                    "    // Обчислення спекулярного освітлення\n" +
                    "    vec3 viewDir = normalize(-fragPos); // напрямок до камери\n" +
                    "    vec3 reflectDir = reflect(lightDir, norm); // відбитий вектор світла\n" +
                    "    float spec = pow(max(dot(viewDir, reflectDir), 0.0), uShininess);\n" +
                    "    vec3 specular = uSpecularStrength * spec * uLightColor;\n" +
                    "\n" +
                    "    // Обчислення відстані і зменшення освітлення відповідно до квадрата відстані\n" +
                    "    float distance = length(uLightPos - fragPos);\n" +
                    "    float attenuation = 1.0 / (1.0 + 0.1 * distance * distance);\n" +
                    "\n" +
                    "    // Фінальний результат: комбінуємо всі компоненти освітлення\n" +
                    "    vec3 result = (ambient + diffuse + specular) * attenuation * fragColor;\n" +
                    "\n" +
                    "    // Якщо ми відображаємо шахівницю, то потрібно додати освітлення відбитого світла від піраміди:\n" +
                    "    if (fragPos.y <= 0.0) {\n" +
                    "        // Для шахівниці (плоска поверхня, нормаль вгору)\n" +
                    "        vec3 normalChess = vec3(0.0, 0.0, 1.0);\n" +
                    "        float diffChess = max(dot(normalChess, -lightDir), 0.0);\n" +
                    "        vec3 diffuseChess = uDiffuseStrength * diffChess * uLightColor;\n" +
                    "\n" +
                    "        // Відбитий вектор для шахівниці (обчислюється так само, як для піраміди)\n" +
                    "        vec3 reflectDirChess = reflect(lightDir, normalChess);\n" +
                    "        float specChess = pow(max(dot(viewDir, reflectDirChess), 0.0), uShininess);\n" +
                    "        vec3 specularChess = uSpecularStrength * specChess * uLightColor;\n" +
                    "\n" +
                    "        // Змішування освітлення для шахівниці з відбитого світла\n" +
                    "        result += (diffuseChess + specularChess) * attenuation;\n" +
                    "    }\n" +
                    "\n" +
                    "    // Встановлюємо кольори в фрагмент\n" +
                    "    gl_FragColor = vec4(result, 1.0);\n" +
                    "}\n";
}