package com.wwmteam.wwm.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wwmteam.wwm.R;

public class MetroMapView extends ImageView implements /*GestureDetector.OnGestureListener,*/
								ScaleGestureDetector.OnScaleGestureListener {

//	protected boolean mScaling;
	private static final float MIN_SCALE = 0.5f;
	private static final float MAX_SCALE = 1.6f;
	//private static final String TAG = MetroMapView.class.getSimpleName();
	
	private Matrix mMatrix = new Matrix();
	
	Matrix matrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF last = new PointF();
	PointF start = new PointF();
	float[] m;

	float redundantXSpace, redundantYSpace;

	float width, height;
	static final int CLICK = 3;
	float saveScale = 1f;
	float right, bottom, origWidth, origHeight, bmWidth, bmHeight;

	
	//private GestureDetector mGestureDetector;
	private ScaleGestureDetector mScaleDetector;
	//private float mScale = 1.0f;
	
	private Context mContext;

	@SuppressWarnings("deprecation")
	public MetroMapView(Context context) {
		super(context);
		//mGestureDetector = new GestureDetector(this);
		mScaleDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}

	@SuppressWarnings("deprecation")
	public MetroMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//mGestureDetector = new GestureDetector(this);
		mScaleDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}

	@SuppressWarnings("deprecation")
	public MetroMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//mGestureDetector = new GestureDetector(this);
		mScaleDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}
	protected void initView() {
		setImageResource(R.drawable.map);
		
		matrix.setTranslate(1f, 1f);
	    m = new float[9];
	    setImageMatrix(matrix);
	    setScaleType(ScaleType.MATRIX);

	    setOnTouchListener(new OnTouchListener() {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            mScaleDetector.onTouchEvent(event);

	            matrix.getValues(m);
	            float x = m[Matrix.MTRANS_X];
	            float y = m[Matrix.MTRANS_Y];
	            PointF curr = new PointF(event.getX(), event.getY());

	            switch (event.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                    last.set(event.getX(), event.getY());
	                    start.set(last);
	                    mode = DRAG;
	                    break;
	                case MotionEvent.ACTION_MOVE:
	                    if (mode == DRAG) {
	                        float deltaX = curr.x - last.x;
	                        float deltaY = curr.y - last.y;
	                        float scaleWidth = Math.round(origWidth * saveScale);
	                        float scaleHeight = Math.round(origHeight * saveScale);
	                        if (scaleWidth < width) {
	                            deltaX = 0;
	                            if (y + deltaY > 0)
	                                deltaY = -y;
	                            else if (y + deltaY < -bottom)
	                                deltaY = -(y + bottom); 
	                        } else if (scaleHeight < height) {
	                            deltaY = 0;
	                            if (x + deltaX > 0)
	                                deltaX = -x;
	                            else if (x + deltaX < -right)
	                                deltaX = -(x + right);
	                        } else {
	                            if (x + deltaX > 0)
	                                deltaX = -x;
	                            else if (x + deltaX < -right)
	                                deltaX = -(x + right);

	                            if (y + deltaY > 0)
	                                deltaY = -y;
	                            else if (y + deltaY < -bottom)
	                                deltaY = -(y + bottom);
	                        }
	                        matrix.postTranslate(deltaX, deltaY);
	                        last.set(curr.x, curr.y);
	                    }
	                    break;

	                case MotionEvent.ACTION_UP:
	                    mode = NONE;
	                    int xDiff = (int) Math.abs(curr.x - start.x);
	                    int yDiff = (int) Math.abs(curr.y - start.y);
	                    if (xDiff < CLICK && yDiff < CLICK)
	                        performClick();
	                    break;

	                case MotionEvent.ACTION_POINTER_UP:
	                    mode = NONE;
	                    break;
	            }
	            setImageMatrix(matrix);
	            invalidate();
	            return true; // indicate event was handled
	        }

	    });
	}
	
	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		mScaleDetector.onTouchEvent(event);

		if (!mScaling)
			mGestureDetector.onTouchEvent(event);

		return super.onTouchEvent(event);
	}*/

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		//mScale = Math.min(Math.max(mScale * detector.getScaleFactor(), MIN_SCALE), MAX_SCALE);
	//	Toast.makeText(mContext, "mScale = " + mScale, Toast.LENGTH_SHORT).show();
		
        float scaleFactor = Math.min(Math.max(.95f, detector.getScaleFactor()), 1.05f);
        Toast.makeText(mContext, "mScale = " + scaleFactor, Toast.LENGTH_SHORT).show();
        float origScale = saveScale;
        saveScale *= scaleFactor;
        if (saveScale > MAX_SCALE) {
            saveScale = MAX_SCALE;
            scaleFactor = MAX_SCALE / origScale;
        } else if (saveScale < MIN_SCALE) {
            saveScale = MIN_SCALE;
            scaleFactor = MIN_SCALE / origScale;
        }
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
        if (origWidth * saveScale <= width || origHeight * saveScale <= height) {
            matrix.postScale(scaleFactor, scaleFactor, width / 2, height / 2);
            if (scaleFactor < 1) {
                matrix.getValues(m);
                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];
                if (scaleFactor < 1) {
                    if (Math.round(origWidth * saveScale) < width) {
                        if (y < -bottom)
                            matrix.postTranslate(0, -(y + bottom));
                        else if (y > 0)
                            matrix.postTranslate(0, -y);
                    } else {
                        if (x < -right) 
                            matrix.postTranslate(-(x + right), 0);
                        else if (x > 0) 
                            matrix.postTranslate(-x, 0);
                    }
                }
            }
        } else {
            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            matrix.getValues(m);
            float x = m[Matrix.MTRANS_X];
            float y = m[Matrix.MTRANS_Y];
            if (scaleFactor < 1) {
                if (x < -right) 
                    matrix.postTranslate(-(x + right), 0);
                else if (x > 0) 
                    matrix.postTranslate(-x, 0);
                if (y < -bottom)
                    matrix.postTranslate(0, -(y + bottom));
                else if (y > 0)
                    matrix.postTranslate(0, -y);
            }
        }
        return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector arg0) {
		//mScaling = true;
		mode = ZOOM;
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector arg0) {
		//mScaling = false;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		bmWidth = bm.getWidth();
	    bmHeight = bm.getHeight();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
	    height = MeasureSpec.getSize(heightMeasureSpec);
	    //Fit to screen.
	    float scale;
	    float scaleX =  (float)width / (float)bmWidth;
	    float scaleY = (float)height / (float)bmHeight;
	    scale = Math.min(scaleX, scaleY);
	    mMatrix.setScale(scale, scale);
	    setImageMatrix(mMatrix);
	    saveScale = 1f;

	    // Center the image
	    redundantYSpace = (float)height - (scale * (float)bmHeight) ;
	    redundantXSpace = (float)width - (scale * (float)bmWidth);
	    redundantYSpace /= (float)2;
	    redundantXSpace /= (float)2;

	    mMatrix.postTranslate(redundantXSpace, redundantYSpace);

	    origWidth = width - 2 * redundantXSpace;
	    origHeight = height - 2 * redundantYSpace;
	    right = width * saveScale - width - (2 * redundantXSpace * saveScale);
	    bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
	    setImageMatrix(mMatrix);

	}

}
