package com.wwmteam.wwm;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import java.util.*;

public class StationInfoActivity extends Activity {

	private static final int OPEN_YEAR = 2016;
	private static final int OPEN_MONTH = 7;
	private static final int OPEN_DAY = 6;
	private static final int OPEN_HOUR = 12;
	private static final int OPEN_MINUTE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_info);
		
		TextView t=(TextView)findViewById(R.id.tillNextTrain);
		
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
	    
	    t.setText(days + " " + hours);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.station_info, menu);
		return true;
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

}
