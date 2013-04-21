package com.wwmteam.wwm.utils;

import android.graphics.PointF;
import android.graphics.RectF;

public class WWMUtils {
	
	public static final RectF st_0_0 = new RectF(200, 50, 400, 115);//pixels on initial sizes
	public static final RectF st_0_1 = new RectF(40, 115, 250, 180);
	public static final RectF st_0_2 = new RectF(200, 180, 440, 255);
	public static final RectF st_0_3 = new RectF(0, 255, 250, 330);
	public static final RectF st_0_4 = new RectF(270, 340, 498, 430);
	public static final RectF st_0_5 = new RectF(40, 390, 250, 460);
	public static final RectF st_0_6 = new RectF(200, 460, 400, 530);
	public static final RectF st_0_7 = new RectF(50, 530, 250, 590);
	public static final RectF st_0_8 = new RectF(200, 660, 450, 740);
	public static final RectF st_0_9 = new RectF(40, 740, 310, 810);
	public static final RectF st_0_10 = new RectF(200, 870, 400, 950);
	public static final RectF st_0_11 = new RectF(0, 950, 300, 1020);
	public static final RectF st_0_12 = new RectF(200, 1020, 400, 1090);
	public static final RectF st_0_13 = new RectF(0, 1090, 250, 1150);
	public static final RectF st_0_14 = new RectF(200, 1150, 390, 1230);
	public static final RectF st_0_15 = new RectF(0, 1230, 170, 1350);
	public static final RectF st_0_16 = new RectF(200, 1300, 450, 1350);
	public static final RectF st_0_17 = new RectF(20, 1350, 250, 1440);
	
	public static final RectF st_1_0 = new RectF(270, 340, 498, 430);
	public static final RectF st_1_1 = new RectF(40, 390, 250, 460);
	public static final RectF st_1_2 = new RectF(200, 460, 400, 530);
	public static final RectF st_1_3 = new RectF(50, 530, 250, 590);
	public static final RectF st_1_4 = new RectF(200, 660, 450, 740);
	
	public static final RectF st_2_0 = new RectF(200, 660, 450, 740);
	
	public static boolean isPointIntoRect(PointF point, RectF rect) {
		if (point.x < rect.right && point.x > rect.left && point.y > rect.top
				&& point.y < rect.bottom) {
			return true;
		}
		return false;
	}

}
