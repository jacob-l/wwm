package com.wwmteam.wwm.utils;

import android.graphics.PointF;
import android.graphics.RectF;

public class WWMUtils {
	
	public static boolean isPointIntoRect(PointF point, RectF rect) {
		if (point.x < rect.right && point.x > rect.left && point.y > rect.top
				&& point.y < rect.bottom) {
			return true;
		}
		return false;
	}

}
