package com.wwmteam.wwm.beans;

import com.wwmteam.wwm.utils.WWMUtils;

import android.graphics.RectF;

public class Map {

	public int id;
	public int offset;
	public RectF[] clickAreas;

	public Map(int id) {
		this.id = id;
		switch (id) {
		case 0:
		default:
			clickAreas = WWMUtils.full_map_areas;
			offset = 0;
			break;
		case 1:
			clickAreas = WWMUtils.first_order_areas;
			offset = 4;
			break;
		case 2:
			clickAreas = WWMUtils.ready_areas;
			offset = 8;
			break;
		}
	}
}
