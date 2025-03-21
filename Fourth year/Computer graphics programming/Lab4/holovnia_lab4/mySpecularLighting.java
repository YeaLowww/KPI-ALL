package com.example.holovnia_lab4;

import android.opengl.GLES32;
import android.opengl.Matrix;

public class mySpecularLighting extends myWorkMode{

    protected float xTouchDown, yTouchDown, viewDistance;
    protected float xCamera, yCamera, zCamera;
    protected int numVertex;
    protected float[] groundPrimaryColor = {0.5f, 0.5f, 0.5f};  // Сірий
    protected float[] groundSecondaryColor = {0.5f, 0.5f, 0.5f};  // Сірий
    // Джерело світла
    protected float[] lightPosition = {0f, 0f, 2f};
    protected float[] lightColor = {1.0f, 0.0f, 1.0f}; // Розово-фіолетовий колір
    protected float ambientStrength = 0.4f;
    protected float specularStrength = 2.0f;
    protected float shininess = 32.0f;
    private int lightVAO, lightVBO;
    mySpecularLighting(){
        super();
        myCreateScene();
        xCamera = 0f;
        yCamera = -10f;
        zCamera = 7f;
        betaViewAngle = 60;
        viewDistance = calculateViewDistance();
    }

    @Override
    protected void myCreateScene() {
        int n = 5;
        numVertex = n*n*6;

        float[] arrayVertex2 = getChessboard(n,15);
        int av2_size = arrayVertex2.length;

        arrayVertex = new float[av2_size];

        System.arraycopy(arrayVertex2,0, arrayVertex, 0, av2_size);
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
        int lightPosHandle = GLES32.glGetUniformLocation(gl_Program, "uLightPos");
        GLES32.glUniform3fv(lightPosHandle, 1, lightPosition, 0);

        int lightColorHandle = GLES32.glGetUniformLocation(gl_Program, "uLightColor");
        GLES32.glUniform3fv(lightColorHandle, 1, lightColor, 0);

        int ambientHandle = GLES32.glGetUniformLocation(gl_Program, "uAmbientStrength");
        GLES32.glUniform1f(ambientHandle, ambientStrength);

        int specularHandle = GLES32.glGetUniformLocation(gl_Program, "uSpecularStrength");
        GLES32.glUniform1f(specularHandle, specularStrength);

        int shininessHandle = GLES32.glGetUniformLocation(gl_Program, "uShininess");
        GLES32.glUniform1f(shininessHandle, shininess);

        // Малюємо джерело світла
        drawLightSourceCube();

        // Малюємо шахівницю
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, numVertex);

        GLES32.glBindVertexArray(0);
    }
    // Функція для малювання джерела світла у вигляді куба
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
        myCompileAndAttachShaders(vertexShaderCode, fragmentShaderCode);
        myVertexArrayBind2(arrayVertex, 6, "vPosition", 0, "vColor", 3 * 4);
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
                    "uniform vec3 uLightPos; // Позиція джерела світла\n" +
                    "uniform vec3 uLightColor; // Колір джерела світла\n" +
                    "uniform float uAmbientStrength; // Сила амбієнтного освітлення\n" +
                    "uniform float uSpecularStrength; // Сила спекулярного освітлення\n" +
                    "uniform float uDiffuseStrength; // Сила дифузного освітлення\n" +
                    "uniform float uShininess; // Гладкість поверхні\n" +
                    "uniform vec3 uCameraPos; // Позиція камери\n" +
                    "varying vec3 fragPos; // Позиція фрагмента\n" +
                    "varying vec3 fragColor; // Колір фрагмента\n" +
                    "\n" +
                    "void main() {\n" +
                    "    // Розрахунок амбієнтного освітлення\n" +
                    "    vec3 ambient = uAmbientStrength * uLightColor;\n" +
                    "\n" +
                    "    // Нормаль до поверхні (постійна нормаль для цього прикладу)\n" +
                    "    vec3 norm = normalize(vec3(0.0, 0.0, 1.0)); \n" +
                    "    vec3 lightDir = normalize(uLightPos - fragPos); // Напрямок на джерело світла\n" +
                    "\n" +
                    "    // Розрахунок спекулярного освітлення\n" +
                    "    vec3 viewDir = normalize(uCameraPos - fragPos); // Напрямок до камери\n" +
                    "    vec3 reflectDir = reflect(-lightDir, norm); // Відбитий напрямок\n" +
                    "    float spec = pow(max(dot(viewDir, reflectDir), 0.0), uShininess); // Спекулярний компонент\n" +
                    "    vec3 specular = uSpecularStrength * spec * uLightColor; // Спекулярний світловий компонент\n" +
                    "\n" +
                    "    // Обчислення відстані для освітлення\n" +
                    "    float distance = length(uLightPos - fragPos);\n" +
                    "    float attenuation = 1.0 / (1.0 + 0.01 * distance * distance); // Освітлення, що зменшується з відстанню\n" +
                    "\n" +
                    "    // Фінальний результат: комбінуємо амбієнтне, дифузне та спекулярне освітлення\n" +
                    "    vec3 result = (ambient + specular * attenuation)* fragColor;\n" +
                    "\n" +
                    "    // Встановлюємо кольори в фрагмент\n" +
                    "    gl_FragColor = vec4(result, 1.0);\n" +
                    "}";


}