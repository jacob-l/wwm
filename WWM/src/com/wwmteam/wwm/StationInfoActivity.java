package com.wwmteam.wwm;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;

import com.wwmteam.wwm.beans.Station;
import com.wwmteam.wwm.utils.ShakeListener;

public class StationInfoActivity extends Activity {

	public static final String TAG_STATION_ID = "tag_station_id";

	private static final int OPEN_YEAR = 2016;
	private static final int OPEN_MONTH = 7;
	private static final int OPEN_DAY = 6;
	private static final int OPEN_HOUR = 12;
	private static final int OPEN_MINUTE = 0;
	
	private ShakeListener mShaker;
	private MediaPlayer mPlayer = new MediaPlayer();;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_info);
		
		initializeShaker();
		
		TextView tillNextTrain = (TextView)findViewById(R.id.tillNextTrain);
		
		Calendar openDate = Calendar.getInstance();
	    Calendar now = Calendar.getInstance();
	    openDate.set(OPEN_YEAR, OPEN_MONTH, OPEN_DAY, OPEN_HOUR, OPEN_MINUTE, 0);
	    now.setTime(new Date());
	    long diff = openDate.getTimeInMillis() - now.getTimeInMillis();
	    long diffHours = diff / (60 * 60 * 1000);
	    long diffDays = diff / (24 * 60 * 60 * 1000);
	    long restHours = diffHours - diffDays * 24;

	    String days = diffDays + " " + getPluralNumber(diffDays,
	    		this.getResources().getString(R.string.days_part_1),
	    		this.getResources().getString(R.string.days_part_2),
	    		this.getResources().getString(R.string.days_part_3),
	    		this.getResources().getString(R.string.days_part_4));
	    
	    String hours = restHours + " " + getPluralNumber(restHours,
	    		this.getResources().getString(R.string.hours_part_1),
	    		"",
	    		this.getResources().getString(R.string.hours_part_2),
	    		this.getResources().getString(R.string.hours_part_3));
	    
	    tillNextTrain.setText(days + " " + hours);
	    
	    
	    final int id = getIntent().getIntExtra(TAG_STATION_ID, 8);
	    
	    Station station = Station.GetStationById(id);
	    ((TextView)findViewById(R.id.stationName)).append(this.getResources().getString(station.Name));
	    
	    if (station.IsFirstOrder() || station.IaReady()) {
	    	//station in first order
	    	findViewById(R.id.predictionArriving).setVisibility(1);
	    	
	    	if (station.StationsBefore.length != 0) {
		    	TextView firstDirection = (TextView)findViewById(R.id.firstDirection);
		    	firstDirection.setVisibility(1);
		    	
		    	TextView firstDirectionStations = (TextView)findViewById(R.id.firstDirectionStations);
		    	for(int i = 0; i < station.StationsBefore.length; i++) {
		    		Station beforeStatition = Station.GetStationById(station.StationsBefore[i]);
		    		firstDirectionStations.append(this.getResources().getString(beforeStatition.Name));
		    		firstDirectionStations.append("\r\n");
		    	}
		    }
		    
		    if (station.StationsAfter.length != 0) {
		    	TextView secondDirection = (TextView)findViewById(R.id.secondDirection);
		    	secondDirection.setVisibility(1);
		    	
		    	TextView secondDirectionStations = (TextView)findViewById(R.id.secondDirectionStations);
		    	for(int i = 0; i < station.StationsAfter.length; i++) {
		    		Station afterStatition = Station.GetStationById(station.StationsAfter[i]);
		    		secondDirectionStations.append(this.getResources().getString(afterStatition.Name));
		    		secondDirectionStations.append("\r\n");
		    	}
		    }
		    
	    }
	    
	    if (station.IsSecondOrder()) {
	    	findViewById(R.id.stationInSecondOrder).setVisibility(1);
	    }
	    
	    if (station.IsDepot()) {
	    	findViewById(R.id.depot).setVisibility(1);
	    }
	    
	    if (station.HasAddress()) {
	    	findViewById(R.id.position_wrapper).setVisibility(1);
	    	((TextView)findViewById(R.id.position)).append(this.getResources().getString(station.Address));
	    }
	}

	public String getPluralNumber(long count, String arg1, String arg2, String arg3, String arg4) {
	    String result = arg1;
	    long last_digit = count % 10;
	    long last_two_digits = count % 100;
	    if (last_digit == 1 && last_two_digits != 11) result += arg2;
	    else if ((last_digit == 2 && last_two_digits != 12)
	    || (last_digit == 3 && last_two_digits != 13)
	    || (last_digit == 4 && last_two_digits != 14))
	        result += arg3;
	    else
	        result += arg4;
	    return result;
	}
	
	protected void initializeShaker() {
		final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
			public void onShake() {
				vibe.vibrate(100);
				try {
					AssetFileDescriptor afd;
					afd = getAssets().openFd("metro_prib.mp3");
					mPlayer.setDataSource(afd.getFileDescriptor(),
							afd.getStartOffset(), afd.getLength());
					mPlayer.prepare();
					mPlayer.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		mShaker.pause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.stop();	
		}
	}
}
