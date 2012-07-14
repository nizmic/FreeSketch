package com.nmsbox.freesketch;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.app.NavUtils;

public class SketchActivity extends Activity {
	private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sketch);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mGLView = new SketchView(this);
        mGLView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(mGLView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_sketch, menu);
        return true;
    }

    
}
