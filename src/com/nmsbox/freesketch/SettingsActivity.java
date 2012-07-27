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

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.GridLayout;

public class SettingsActivity extends Activity implements OnClickListener {
	private static final String TAG = "SettingsActivity";
	
	//private GridLayout mButtonGrid;
	private Button mClearButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        mClearButton = (Button)findViewById(R.id.clearButton);
        mClearButton.setOnClickListener(this);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		Log.i(TAG, "onClick");
	}

    
}
