package com.wwmteam.wwm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.wwmteam.wwm.beans.Map;
import com.wwmteam.wwm.views.MetroMapView;

public class MapActivity extends Activity {
	
	protected MetroMapView mMetroMapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMetroMapView = new MetroMapView(this) {
			@Override
			protected void onClickToStation(int stationId) {
				super.onClickToStation(stationId);
				goToStationInfoScreen(stationId);
			}
		};
		mMetroMapView.setMap(new Map(0));
		final FrameLayout layout = new FrameLayout(this);
		final FrameLayout.LayoutParams COVER_SCREEN_GRAVITY_CENTER = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		layout.setBackgroundColor(getResources()
				.getColor(android.R.color.white));
		layout.addView(mMetroMapView, COVER_SCREEN_GRAVITY_CENTER);
		setContentView(layout);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	protected void goToStationInfoScreen(int stationId) {
		final Intent intent = new Intent(this, StationInfoActivity.class);
		intent.putExtra(StationInfoActivity.TAG_STATION_ID, stationId);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_first_order:
			mMetroMapView.setMap(new Map(1));
			return true;
		case R.id.action_full_map:
			mMetroMapView.setMap(new Map(0));
			return true;
		case R.id.action_ready:
			mMetroMapView.setMap(new Map(2));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
