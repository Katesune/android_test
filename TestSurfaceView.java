package com.example.balls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    DrawThread thread;
    SurfaceHolder holder;
    private Coloring coloring = new Coloring();
    Rect rect = new Rect();

    class DrawThread extends Thread {

        boolean run=true;

        @Override
        public void run() {
            super.run();
            // задание: реализовать плавную смену цвета
            // палитру выбирайте сами

            while (run) {
                Canvas c = holder.lockCanvas();

                Balls balls = new Balls(c, coloring);
                //balls.koordRight(rect);
                rect.RectStart(c, coloring);

                //balls.CnangeCircleInRect(rect);
                //balls.CnangeIfCircleInBorder();
                rect.RectInBorders();

                rect.drawRect();
                //balls.drawCircles();

                //c.drawCircle(balls.koord.get(0), balls.koord.get(2), 50, coloring.colors[1]);
                //c.drawCircle(balls.koord.get(1), balls.koord.get(3), 50, coloring.colors[2]);
                Paint p = new Paint();
                p.setColor(Color.YELLOW);
                c.drawCircle(balls.koord.get(1), balls.koord.get(3), 50, p);
                //balls.CirclesMoves();


                holder.unlockCanvasAndPost(c);
            }
        }
    }


    public TestSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // запустить поток отрисовки
        holder = surfaceHolder;
        thread = new DrawThread();
        thread.start();
        Log.d("thread", "DrawThread is running");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        holder = surfaceHolder;
        thread.run = false;
        thread = new DrawThread();
        thread.start();
        Log.d("thread", "DrawThread is change");

        // перезапустить поток
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        thread.run = false;
        // остановить поток
    }

//    Canvas c;
//    boolean touch;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        int event_x = (int) event.getX();
//        int event_y = (int) event.getY();
//
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//
//            if (DownInRectKoord(event_x)&&DownInRectKoord(event_y))
//            {
//                c = rect.getCanvas();
//                touch = true;
//            }
//        }
//
//        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            if (touch) {
//                int move_x = c.getWidth() - event_x;
//                int move_y = c.getHeight() - event_y;
//
//                rect.x = rect.x + move_x;
//                rect.y = rect.y + move_y;
//            }
//        }
//
//    return true;
//    }

    private boolean DownInRectKoord(int event_koord) {
        int e_koord = rect.x - event_koord;
        return e_koord < 0 && e_koord > rect.rectSize+1;
    }
}