package com.example.holovnia_lab1gl;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView gLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }

    public class MyGLSurfaceView extends GLSurfaceView {
        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(3);
            setRenderer(new MyGLRenderer());
            setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        }
    }
}

class MyGLRenderer implements GLSurfaceView.Renderer {
    private FloatBuffer triangleBuffer, polygonBuffer, sunRaysBuffer;
    private int program;
    private int positionHandle;
    private static final int COORDS_PER_VERTEX = 3;
    private static final int vertexStride = COORDS_PER_VERTEX * 4;

    private final float[] triangleCoords = {
            0.25f + 0.2f,  -0.25f - 0.3f, 0.0f,  // Правий низ
            -0.25f + 0.2f, -0.25f - 0.3f, 0.0f,  // Лівий низ
            0.0f + 0.2f,  0.05f - 0.3f, 0.0f   // вверх
    };


    private final float[] polygonCoords = generatePolygonCoords(22, 0.0f, 0.2f, 0.2f); // 22-кутник
    private final float[] sunRaysCoords = generateSunRays(22, 0.0f, 0.2f, 0.3f); // Промені
    private final String vertexShaderCode =
            "#version 300 es\n" +
                    "layout(location = 0) in vec4 vPosition;\n" +
                    "void main() {\n" +
                    "  gl_Position = vPosition;\n" +
                    "}";

    private final String fragmentShaderCode =
            "#version 300 es\n" +
                    "precision mediump float;\n" +
                    "out vec4 fragColor;\n" +
                    "void main() {\n" +
                    "  fragColor = vec4(0.0, 0.0, 0.0, 1.0);\n" + // Чорний
                    "}";
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES32.glClearColor(1.0f, 0.8f, 0.9f, 1.0f); // Світло-рожевий фон
        GLES32.glEnable(GLES32.GL_DEPTH_TEST);

        triangleBuffer = createFloatBuffer(triangleCoords);
        polygonBuffer = createFloatBuffer(polygonCoords);
        sunRaysBuffer = createFloatBuffer(sunRaysCoords);

        int vertexShader = loadShader(GLES32.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES32.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES32.glCreateProgram();
        GLES32.glAttachShader(program, vertexShader);
        GLES32.glAttachShader(program, fragmentShader);
        GLES32.glLinkProgram(program);
        GLES32.glUseProgram(program);

        positionHandle = GLES32.glGetAttribLocation(program, "vPosition");
//        if (positionHandle < 0) {
//            Log.e("OpenGL", "Error: vPosition not found in shader!");
//            return;
//        }
    }
    @Override
    public void onDrawFrame(GL10 unused) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);
        GLES32.glUseProgram(program);

        // Малюємо 22-кутник (сонце)
        GLES32.glEnableVertexAttribArray(positionHandle);
        GLES32.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES32.GL_FLOAT, false, vertexStride, polygonBuffer);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, 0, 22);// N = 22
        GLES32.glDisableVertexAttribArray(positionHandle);

        // Малюємо промені сонця
        GLES32.glLineWidth(5.0f); // Товщина
        GLES32.glEnableVertexAttribArray(positionHandle);
        GLES32.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES32.GL_FLOAT, false, vertexStride, sunRaysBuffer);
        GLES32.glDrawArrays(GLES32.GL_LINES, 0, 44); //44 = 22 лінії (промені)
        GLES32.glDisableVertexAttribArray(positionHandle);

        // Малюємо трикутник
        GLES32.glEnableVertexAttribArray(positionHandle);
        GLES32.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES32.GL_FLOAT, false, vertexStride, triangleBuffer);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, 3);
        GLES32.glDisableVertexAttribArray(positionHandle);
    }
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES32.glViewport(0, 0, width, height);
    }
    private FloatBuffer createFloatBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = bb.asFloatBuffer();
        buffer.put(array);
        buffer.position(0);
        return buffer;
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES32.glCreateShader(type);
        GLES32.glShaderSource(shader, shaderCode);
        GLES32.glCompileShader(shader);

        int[] compileStatus = new int[1];
        GLES32.glGetShaderiv(shader, GLES32.GL_COMPILE_STATUS, compileStatus, 0);
//        if (compileStatus[0] == 0) {
//            Log.e("OpenGL", "Shader compilation failed: " + GLES32.glGetShaderInfoLog(shader));
//            GLES32.glDeleteShader(shader);
//            return 0;
//        }
        return shader;
    }

    private float[] generatePolygonCoords(int sides, float centerX, float centerY, float radius) {
        float[] coords = new float[sides * 3];
        double angleStep = 2 * Math.PI / sides;
        for (int i = 0; i < sides; i++) {
            double angle = i * angleStep;
            coords[i * 3] = centerX + (float) Math.cos(angle) * radius;
            coords[i * 3 + 1] = centerY + (float) (Math.sin(angle) * radius * 0.45);//сплюснути по Y
            coords[i * 3 + 2] = 0.0f;
        }
        return coords;
    }

    private float[] generateSunRays(int sides, float centerX, float centerY, float radius) {
        float[] coords = new float[sides * 6]; // 2 координати (стартова та кінцева точка)
        double angleStep = 2 * Math.PI / sides;
        for (int i = 0; i < sides; i++) {
            double angle = i * angleStep;
            coords[i * 6] = centerX; // Початок променя (центр)
            coords[i * 6 + 1] = centerY;
            coords[i * 6 + 2] = 0.0f; // Z-координата для плоскої сцени

            coords[i * 6 + 3] = centerX + (float) Math.cos(angle) * radius; // Кінець променя
            coords[i * 6 + 4] = centerY + (float) (Math.sin(angle) * radius * 0.45); //сплюснути по Y;
            coords[i * 6 + 5] = 0.0f; // Z-координата
        }
        return coords;
    }
}
