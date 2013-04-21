package com.wwmteam.wwm.utils;

import android.graphics.PointF;
import android.graphics.RectF;

public class WWMUtils {
	
	public static final RectF st_0_0 = new RectF(200, 50, 400, 115);//pixels on initial sizes
	public static final RectF st_0_1 = new RectF(40, 115, 250, 180);
	//...
	//public static final RectF st_0_16 = new RectF(..);
	//public static final RectF st_0_17 = new RectF(..);
	
	//public static final RectF st_1_0 = new RectF(..);
	//public static final RectF st_1_1 = new RectF(..);
	//public static final RectF st_1_2 = new RectF(..);
	//public static final RectF st_1_3 = new RectF(..);
	//public static final RectF st_1_4 = new RectF(..);
	
	//public static final RectF st_2_0 = new RectF(..);
	
	public static boolean isPointIntoRect(PointF point, RectF rect) {
		if (point.x < rect.right && point.x > rect.left && point.y > rect.top
				&& point.y < rect.bottom) {
			return true;
		}
		return false;
	}

}
