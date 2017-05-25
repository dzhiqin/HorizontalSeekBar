package com.example.horizontalseekbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/5/24.
 */

public class HorizontalSeekBar extends View {
    private int width;
    private int height;
    private Paint paint;
    private int progressLeft;
    private int progressRight;
    private int progressLeftValue;
    private int progressRightValue;
    private int imgLeftId;
    private int imgRightId;
    private int marginSpace=30;//作为空白间隔
    private int marginProgress=5;//作为固定的左边滑块和右边滑块的间隔，固定的
    private int deltaProgress;//作为右边滑块和左边滑块直接的间隔，可变的
    private boolean isLeftSlider;//判断是左边的滑块滑动还是右边的
    private int max;
    private OnSlideListener listener;
    public HorizontalSeekBar(Context context){
        super(context);
    }
    public HorizontalSeekBar(Context context, AttributeSet attrs){
        super(context,attrs);
        paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(10);
        progressRight=100;
        progressLeft=0;
        deltaProgress=progressRight-progressLeft;
        imgLeftId=R.mipmap.slider_32px;
        imgRightId=R.mipmap.slider_32px;
        max=100;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        progressRightValue=progressRight*width/max;
        if(progressRightValue>width-marginSpace)
            {progressRightValue=width-marginSpace;}
        progressLeftValue=progressLeft*width/max;
        if(progressLeftValue<marginSpace)
            {progressLeftValue=marginSpace;}

        RectF rectF=new RectF(marginSpace,height/2-height/6,width-marginSpace,height/2+height/6);
        RectF progressArea=new RectF(progressLeftValue,height/2-height/6,progressRightValue,height/2+height/6);
        //画水平进度条
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        canvas.drawRoundRect(rectF,20,height/3,paint);
        //画进度块
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(progressArea,20,height/3,paint);
        Bitmap bmLeft= BitmapFactory.decodeResource(getResources(),imgLeftId);
        Bitmap bmRight=BitmapFactory.decodeResource(getResources(),imgRightId);
        //取原图全部用于显示
        Rect srcLeft=new Rect(0,0,bmLeft.getWidth(),bmLeft.getHeight());
        Rect srcRight=new Rect(0,0,bmLeft.getWidth(),bmLeft.getHeight());
        //右边滑动块显示在height*3/4和height为边长的矩形内
        int dstRightEndX=progressRightValue+height*3/8;//if(dstRightEndX>width-marginSpace) dstRightEndX=width-marginSpace;
        int dstRightStartX=progressRightValue-height*3/8;
        RectF dstRight=new RectF(dstRightStartX,0,dstRightEndX,height);
        canvas.drawBitmap(bmRight,srcRight,dstRight,new Paint());
        //左边滑动块
        int dstLeftStartX=progressLeftValue-height*3/8; if(dstLeftStartX<0) dstLeftStartX=0;
        int dstLeftEndX=dstLeftStartX+height*3/4;
        RectF dstLeft=new RectF(dstLeftStartX,0,dstLeftEndX,height);
        canvas.drawBitmap(bmLeft,srcLeft,dstLeft,new Paint());
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int downX=(int)event.getX();
                int progress=downX*max/width;
                //点击屏幕的时候判断是要滑动左边滑动块还是要滑动右边滑动块
                if(progress>(progressLeft+deltaProgress/2)){
                    isLeftSlider=false;
                }else{
                    isLeftSlider=true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX=(int)event.getX();
                if(isLeftSlider){//滑动左边滑动块
                    progressLeft=moveX*max/width;
                    if(progressLeft<0) progressLeft=0;//左边滑动块的行程，0到右边滑动块之间
                    if(progressLeft>progressRight-marginProgress) progressLeft=progressRight-marginProgress;
                }else{
                    progressRight=moveX*max/width;
                    if(progressRight>max) progressRight=max;//右边滑动块的行程，左边滑动块到max
                    if(progressRight<progressLeft+marginProgress) progressRight=progressLeft+marginProgress;
                }
                deltaProgress=progressRight-progressLeft;//deltaProgress是我们需要的，两个滑动块之间的距离
                if(listener!=null){
                    listener.onSlidingProgress(deltaProgress);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                int upX=(int)event.getX();
                if(isLeftSlider){
                    progressLeft=upX*max/width;
                    if(progressLeft<0) progressLeft=0;
                    if(progressLeft>progressRight-marginProgress) progressLeft=progressRight-marginProgress;
                }else{
                    progressRight=upX*max/width;
                    if(progressRight>max) progressRight=max;
                    if(progressRight<progressLeft+marginProgress) progressRight=progressLeft+marginProgress;
                }
                deltaProgress=progressRight-progressLeft;
                if(listener!=null){
                    listener.onSlidProgress(deltaProgress);
                }
                invalidate();
                break;
        }
        return true;
    }
    public void setProgressLeft(int progressLeft){
        if((progressLeft>=0)&&(progressLeft<=progressRight-marginProgress)){
            this.progressLeft=progressLeft;
        }

    }
    public void setProgressRight(int progressRight){
        if ((progressRight <= max) && (progressRight >= progressLeft + marginProgress)) {
            this.progressRight=progressRight;
        }
    }
    public void setOnSlideListener(OnSlideListener listener){
        this.listener=listener;
    }
    public interface OnSlideListener{
        void onSlidingProgress(int progress);
        void onSlidProgress(int progress);
    }
}

