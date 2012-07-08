package com.nmsbox.freesketch;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class SketchRenderer implements Renderer {
	// mAngle must be volatile because the renderer runs in a separate thread from the UI
	public volatile float mAngle;

	@Override
	public void onDrawFrame(GL10 gl) {
		// Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background frame color
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);		
	}

}
