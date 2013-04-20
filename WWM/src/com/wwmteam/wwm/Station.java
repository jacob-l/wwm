package com.wwmteam.wwm;

public class Station {

	public int Id;
	public int Name;
	public int Address;
	public int[] StationsBefore;
	public int[] StationsAfter;
	
	private Station() {}
	
	private Station(int id, int name, int address, int[] stationsBefore, int[] stationsAfter) {
		this.Id = id;
		this.Name = name;
		this.Address = address;
		this.StationsBefore = stationsBefore;
		this.StationsAfter = stationsAfter;
	}
	
	private static Station[] stations = new Station[] {
		new Station(0, R.string.west, -1, new int[0], new int[0]),
		new Station(1, R.string.sunny, -1, new int[0], new int[0]),
		new Station(2, R.string.young, -1, new int[0], new int[0]),
		new Station(3, R.string.rokossovskogo, -1, new int[0], new int[0]),
		new Station(4, R.string.depot_1, -1, new int[0], new int[0]),
		new Station(5, R.string.cathedral, -1, new int[0], new int[]{ 6, 7, 8 }),
		new Station(6, R.string.crystal, -1, new int[]{ 5 }, new int[]{ 7, 8 }),
		new Station(7, R.string.after_river, -1, new int[]{ 5, 6 }, new int[]{ 8 }),
		new Station(8, R.string.library, -1, new int[]{ 5, 6, 7 }, new int[0]),
		new Station(9, R.string.shop_center, -1, new int[0], new int[0]),
		new Station(10, R.string.Jukov, -1, new int[0], new int[0]),
		new Station(11, R.string.lermontovskaya, -1, new int[0], new int[0]),
		new Station(12, R.string.park, -1, new int[0], new int[0]),
		new Station(13, R.string.tupolev, -1, new int[0], new int[0]),
		new Station(14, R.string.works, -1, new int[0], new int[0]),
		new Station(15, R.string.depot_2, -1, new int[0], new int[0]),
		new Station(16, R.string.moscow, -1, new int[0], new int[0]),
		new Station(17, R.string.sibirian_road, -1, new int[0], new int[0])
	};
	
	public static Station GetStationById(int id) {
		return stations[id];
	}
}
