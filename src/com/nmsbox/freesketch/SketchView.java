/* FreeSketch - A simple Android app for sketching out ideas etc
 * 
 * Copyright (C) 2012  Nathan Sullivan (nathan.sullivan@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nmsbox.freesketch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class SketchView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "SketchView";
	private static final int BACKGROUND_COLOR = Color.WHITE;
	private static final long STROKE_TIMEOUT_MILLIS = 100;
	private static final int SETTINGS_REQUEST = 0;
	private float mPrevX;
	private float mPrevY;
	private long mPrevTime;
	private Bitmap mBitmap;
	private Paint mPaint;
	private boolean mEnableTripleTouch;
	
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
		mEnableTripleTouch = true;
	}
	
	private void doDraw(Canvas canvas) {
		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}
	}
	
	public void showSettings(View view) {
    	Intent intent = new Intent(this.getContext(), SettingsActivity.class);
    	this.getContext().startActivity(intent);
    }
	
	@Override
	protected void onVisibilityChanged (View changedView, int visibility) {
		if (visibility == View.VISIBLE) {
			mEnableTripleTouch = true;
		}
	}
		
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		final float x = e.getX();
		final float y = e.getY();
		final int historySize = e.getHistorySize();
		final int pointerCount = e.getPointerCount(); // this is essentially the number of fingers/touches
		
		if (pointerCount == 3 && mEnableTripleTouch) {
			mEnableTripleTouch = false;
			showSettings(this);
			return true;
		}
		
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (pointerCount > 1) {
				return true;
			}
			long time = e.getEventTime();
			Path path = new Path();
			boolean firstPoint = true;
			
			// Continue current stroke if last motion event was less than STROKE_TIMEOUT_MILLIS ago
			if (time - mPrevTime < STROKE_TIMEOUT_MILLIS) {
				path.moveTo(mPrevX, mPrevY);
				firstPoint = false;
			}
			
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
            break;
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
