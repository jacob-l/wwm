package com.wwmteam.wwm;

import com.wwmteam.wwm.R.string;

public class Station {

	public int Id;
	public R.string Name;
	public R.string Address;
	public int[] StationsBefore;
	public int[] StationsAfter;
	
	private Station() {}
	
	private Station(int id, R.string name, R.string address, int[] stationsBefore, int[] stationsAfter) {
		this.Id = id;
		this.Name = name;
		this.Address = address;
		this.StationsBefore = stationsBefore;
		this.StationsAfter = stationsAfter;
	}
	
	/*private static Station[] stations = new Station[] {
		new Station(0, R.string.west, null, new int)
	}*/
}
