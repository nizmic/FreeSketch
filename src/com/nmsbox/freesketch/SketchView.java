package com.nmsbox.freesketch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SketchView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "SketchView";
	private static final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private static final int BACKGROUND_COLOR = Color.WHITE;
	private static final long STROKE_TIMEOUT_MILLIS = 200;
	private float mPrevX;
	private float mPrevY;
	private long mPrevTime;
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
		mPaint.setStyle(Paint.Style.STROKE);
		mPrevTime = 0;
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
			long time = e.getEventTime();

			Path path = new Path();
			boolean firstPoint = true;
			
			// Continue current stroke if last motion event was less than STROKE_TIMEOUT_MILLIS ago
			if (time - mPrevTime < STROKE_TIMEOUT_MILLIS) {
				Log.i(TAG, String.format("continuing previous stroke"));
				path.moveTo(mPrevX, mPrevY);
				firstPoint = false;
			}
			
			final int historySize = e.getHistorySize();
			final int pointerCount = e.getPointerCount();
			for (int h = 0; h < historySize; h++) {
				for (int p = 0; p < pointerCount; p++) {
					float histX = e.getHistoricalX(p, h);
					float histY = e.getHistoricalY(p, h);
					if (firstPoint) {
						path.moveTo(histX, histY);
						firstPoint = false;
					} else {
						path.lineTo(histX, histY);
					}
				}
			}

			if (firstPoint) {
				path.moveTo(x, y);
			}
			path.lineTo(x, y);
			
            if (mBitmap != null) {
            	Canvas canvas = new Canvas(mBitmap);
           		canvas.drawPath(path, mPaint);
            	/*Paint p = new Paint();
            	p.setColor(Color.BLACK);
            	p.setStrokeWidth(10.0f);
            	p.setAntiAlias(true);
            	canvas.drawPoint(x, y, p);
            	p.setColor(Color.GREEN);
            	canvas.drawPoint(mPrevX, mPrevY, p);*/
            	canvas = getHolder().lockCanvas();
            	if (canvas != null) {
            		doDraw(canvas);
            		getHolder().unlockCanvasAndPost(canvas);
            	}
            }
            mPrevX = x;
            mPrevY = y;
            mPrevTime = time;
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
