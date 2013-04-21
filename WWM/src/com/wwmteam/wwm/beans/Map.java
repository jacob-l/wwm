package com.wwmteam.wwm.beans;

import java.util.ArrayList;

import android.graphics.RectF;

public class Map {

	public int id;
	public ArrayList<Station> stations;
	public ArrayList<RectF> clickAreas;

	public Map(int id) {
		this.id = id;
		switch (id) {
		case 0:
		default:
			// clickAreas = WWMUtils.
			break;
		case 1:
			break;
		case 2:
			break;
		}
	}
}
