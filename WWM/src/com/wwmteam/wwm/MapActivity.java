package com.wwmteam.wwm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.widget.FrameLayout;

import com.wwmteam.wwm.views.MetroMapView;
public class MapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MetroMapView metroMapView = new MetroMapView(this) {
        	/*@Override
        	public boolean onTouchEvent(MotionEvent event) {
        		if (event.getAction() == MotionEvent.ACTION_UP && mode == NONE){
        			goToStationInfoScreen();
        		}
        		return super.onTouchEvent(event);
        	}*/
        };
        final FrameLayout layout = new FrameLayout(this);
        final FrameLayout.LayoutParams COVER_SCREEN_GRAVITY_CENTER = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        layout.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        layout.addView(metroMapView, COVER_SCREEN_GRAVITY_CENTER);
        setContentView(layout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }
    
    protected void goToStationInfoScreen() {
    	final Intent intent = new Intent(this, StationInfoActivity.class);
    	startActivity(intent);
    }
    
}
