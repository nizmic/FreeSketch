package com.nmsbox.freesketch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SketchView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "SketchView";
	private static final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private static final int BACKGROUND_COLOR = Color.WHITE;
	private float mPrevX;
	private float mPrevY;
	private Bitmap mBitmap;
	private Paint mPaint;
	
	public SketchView(Context context) {
		super(context);
		setFocusable(true);
		getHolder().addCallback(this);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(5.0f);
	}
	
	private void doDraw(Canvas canvas) {
		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			Log.i(TAG, String.format("ACTION_MOVE (x,y)=(%f,%f)", x, y));
			float dx = x - mPrevX;
            float dy = y - mPrevY;

            // reverse direction of rotation above the mid-line
            if (y > getHeight() / 2) {
              dx = dx * -1 ;
            }

            // reverse direction of rotation to left of the mid-line
            if (x < getWidth() / 2) {
              dy = dy * -1 ;
            }

            //mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;
            if (mBitmap != null) {
            	Canvas canvas = new Canvas(mBitmap);
            	canvas.drawPoint(x, y, mPaint);
            	canvas = getHolder().lockCanvas();
            	if (canvas != null) {
            		doDraw(canvas);
            		getHolder().unlockCanvasAndPost(canvas);
            	}
            }
		}
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mBitmap == null) {
			mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(mBitmap);
			Paint paint = new Paint();
			paint.setColor(BACKGROUND_COLOR);
			canvas.drawPaint(paint);
		}
		
		Canvas canvas = holder.lockCanvas();
		if (canvas != null) {
			doDraw(canvas);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}
