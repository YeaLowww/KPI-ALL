package com.example.holovnia_lab1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CustomView(this));
    }

    public class CustomView extends View {
        private Paint paint;
        public CustomView(Context context) {
            super(context);
            init();
        }
        private void init() {
            paint = new Paint();
            paint.setAntiAlias(true);
            setBackgroundColor(Color.rgb(255, 182, 193)); // Світло-рожевий
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int width = getWidth();
            int height = getHeight();
            int centerX = width / 2;
            int centerY = height / 2;

            drawPolygon(canvas, centerX, centerY - 200, 100, 22);
            drawSunRays(canvas, centerX, centerY - 200, 50, 150, 22);
            drawTriangle(canvas, centerX + 200, centerY + 200, 100);
        }

        private void drawPolygon(Canvas canvas, int x, int y, int radius, int numSides) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);

            Path path = new Path();
            for (int i = 0; i < numSides; i++) {
                double angle = 2 * Math.PI * i / numSides;
                int px = x + (int) (radius * Math.cos(angle));
                int py = y + (int) (radius * Math.sin(angle));
                if (i == 0) {
                    path.moveTo(px, py);
                } else {
                    path.lineTo(px, py);
                }
            }
            path.close();

            canvas.drawPath(path, paint);
        }

        private void drawSunRays(Canvas canvas, int x, int y, int innerRadius, int outerRadius, int numRays) {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);

            for (int i = 0; i < numRays; i++) {
                double angle = 2 * Math.PI * i / numRays;
                int innerX = x + (int) (innerRadius * Math.cos(angle));
                int innerY = y + (int) (innerRadius * Math.sin(angle));
                int outerX = x + (int) (outerRadius * Math.cos(angle));
                int outerY = y + (int) (outerRadius * Math.sin(angle));
                canvas.drawLine(innerX, innerY, outerX, outerY, paint);
            }
        }

        private void drawTriangle(Canvas canvas, int x, int y, int size) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);

            Path path = new Path();
            path.moveTo(x, y - size);
            path.lineTo(x - size, y + size);
            path.lineTo(x + size, y + size);
            path.close();

            canvas.drawPath(path, paint);
        }
    }
}

