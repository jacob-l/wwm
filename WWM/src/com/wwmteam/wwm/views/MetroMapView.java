package com.wwmteam.wwm.views;

import com.wwmteam.wwm.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Toast;

public class MetroMapView extends ImageView implements GestureDetector.OnGestureListener,
								ScaleGestureDetector.OnScaleGestureListener {

	protected boolean mScaling;
	private static final float MIN_SCALE = 1.0f;
	private static final float MAX_SCALE = 2.0f;
	//private static final String TAG = MetroMapView.class.getSimpleName();
	
	private GestureDetector mGestureDetector;
	private ScaleGestureDetector mScaleGestureDetector;
	private float mScale = 1.0f;
	
	private Context mContext;

	@SuppressWarnings("deprecation")
	public MetroMapView(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(this);
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}

	@SuppressWarnings("deprecation")
	public MetroMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(this);
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}

	@SuppressWarnings("deprecation")
	public MetroMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mGestureDetector = new GestureDetector(this);
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}
	protected void initView() {
		setImageResource(R.drawable.map);
		//setImageResource(R.drawable.ic_launcher);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mScaleGestureDetector.onTouchEvent(event);

		if (!mScaling)
			mGestureDetector.onTouchEvent(event);

		return super.onTouchEvent(event);
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		//float previousScale = mScale;
		mScale = Math.min(Math.max(mScale * detector.getScaleFactor(), MIN_SCALE), MAX_SCALE);
		//float factor = mScale/previousScale;
		Toast.makeText(mContext, "mScale = " + mScale, Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector arg0) {
		mScaling = true;
		Toast.makeText(mContext, "on Scale Begin", Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector arg0) {
		mScaling = false;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		Toast.makeText(mContext, "on Single Tap Up", Toast.LENGTH_SHORT).show();
		return true;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	/*private void scaleImage() {
		
	}*/

}
