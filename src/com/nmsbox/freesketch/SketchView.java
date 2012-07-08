package com.nmsbox.freesketch;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class SketchView extends GLSurfaceView {
	private static final String TAG = "SketchView";
	private static final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float mPrevX;
	private float mPrevY;
	private SketchRenderer mRenderer;

	public SketchView(Context context) {
		super(context);
		
		setEGLContextClientVersion(2);
		// Render the view only when there is a change in the drawing data
		// commented it out because it was causing application to crash
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		mRenderer = new SketchRenderer();
		setRenderer(mRenderer);
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

            mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;
            requestRender();
		}
		return true;
	}
}
