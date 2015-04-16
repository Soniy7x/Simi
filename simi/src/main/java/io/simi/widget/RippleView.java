package io.simi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class RippleView extends View{

    private static final int INVALIDATE_DURATION = 60;
    private static final int TAP_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final Paint PAINT = new Paint();

    private boolean isPressed = false;
    private long pressTime = 0;
    private int increment = 10;
    private int maxRadius;
    private int shaderRadius;
    private int eventX;
    private int eventY;
    protected int width;
    protected int height;

    static {
        PAINT.setColor(0x1B000000);
    }

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (pressTime == 0) {
                    pressTime = SystemClock.elapsedRealtime();
                }
                eventX = (int)event.getX();
                eventY = (int)event.getY();
                isPressed = true;
                if(width > height) {
                    if (eventX < width / 2) {
                        maxRadius = width - eventX;
                    }else {
                        maxRadius = width / 2 + eventX;
                    }
                }else {
                    if (eventY < height / 2) {
                        maxRadius = height - eventY;
                    }else {
                        maxRadius = height / 2 + eventY;
                    }
                }
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(SystemClock.elapsedRealtime() - pressTime < TAP_TIMEOUT){
                    increment = 15;
                    postInvalidate();
                }else{
                    resetStatus();
                }
                break;
        }
        return true;
    }

    private void resetStatus(){
        isPressed = false;
        increment = 10;
        pressTime = 0;
        shaderRadius = 0;
        postInvalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(!isPressed || !isEnabled() || !isClickable()) {
            return;
        }
        canvas.drawRect(0, 0, width, height, PAINT);
        canvas.save();
        canvas.clipRect(0, 0, width, height);
        canvas.drawCircle(eventX, eventY, shaderRadius, PAINT);
        canvas.restore();
        if(shaderRadius < maxRadius){
            postInvalidateDelayed(INVALIDATE_DURATION, 0, 0, width, height);
            shaderRadius += increment;
        }else{
            resetStatus();
        }
    }

}
