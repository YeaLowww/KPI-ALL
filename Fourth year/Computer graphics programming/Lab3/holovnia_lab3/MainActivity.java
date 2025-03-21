package com.example.holovnia_lab3;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.opengl.GLSurfaceView;
import android.opengl.GLES32;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
public class MainActivity extends AppCompatActivity {
    private GLSurfaceView gLView;
    private myWorkMode wmRef = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Pyramid rotation");
        menu.add(0, 2, 0, "Nine Cubes");
        //... add other menuitems
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setTitle(item.getTitle());
        switch (item.getItemId()) {
            case 1:
                myModeStart(new myPyramidRotation(),
                        GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                // GLSurfaceView.RENDERMODE_WHEN_DIRTY or GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                return true;
            case 2:
                myModeStart(new myNineCubes(),
                        GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            // ... responding to selection of other menu items
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void myModeStart(myWorkMode wmode, int rendermode) {
        wmRef = wmode;
        gLView.setRenderMode(rendermode);
        gLView.requestRender();
    }
    public class MyGLSurfaceView extends GLSurfaceView {
        public MyGLSurfaceView(Context context){
            super(context);
            // Create an OpenGL context
            setEGLContextClientVersion(2); // or (3)
            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(new MyGLRenderer());
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); //default
        }
        @Override
        public boolean onTouchEvent(MotionEvent e) {
            if (wmRef == null) return false; //workmode object is not created
            if (wmRef.onTouchNotUsed()) return false;
            int cx = this.getWidth();
            int cy = this.getHeight();
            float xtouch = e.getX();
            float ytouch = e.getY();
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (wmRef.onActionDown(xtouch, ytouch, cx, cy))
                        requestRender();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (wmRef.onActionMove(xtouch, ytouch, cx, cy))
                        requestRender();
                    break;
                default:break;
            }
            return true;
        }
    }
    public class MyGLRenderer implements GLSurfaceView.Renderer {
        private int myRenderHeight = 1, myRenderWidth = 1;
        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            // Set the background frame color (dark blue for example)
            GLES32.glEnable(GLES32.GL_DEPTH_TEST);
            GLES32.glClearColor(0, 0, 0.3f, 1.0f);
        }
        public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES32.glViewport(0, 0, width, height);
            myRenderWidth = width;
            myRenderHeight = height;
        }
        public void onDrawFrame(GL10 unused) {
            GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT |
                    GLES32.GL_DEPTH_BUFFER_BIT);
            if (wmRef == null) return;
            if (wmRef.getProgramId() < 0) return; //previous call error
            if (wmRef.getProgramId() == 0) //for the first onDrawFrame call
                wmRef.myCreateShaderProgram();
            if (wmRef.getProgramId() <= 0) return; //an error
            GLES32.glUseProgram(wmRef.getProgramId());
            wmRef.myUseProgramForDrawing(myRenderWidth, myRenderHeight);
        }
    }
}