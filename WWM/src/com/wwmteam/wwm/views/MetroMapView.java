package com.wwmteam.wwm.views;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.wwmteam.wwm.R;

public class MetroMapView extends ImageView implements /*GestureDetector.OnGestureListener,*/
								ScaleGestureDetector.OnScaleGestureListener {

//	protected boolean mScaling;
	private static final float MIN_SCALE = 1f;
	private static final float MAX_SCALE = 3f;
	//private static final String TAG = MetroMapView.class.getSimpleName();
	
	Matrix matrix = new Matrix();

	// We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = 1f;
    float maxScale = 3f;
    float[] m;


    int viewWidth, viewHeight;
    static final int CLICK = 3;
    float saveScale = 1f;
    protected float origWidth, origHeight;
    int oldMeasuredWidth, oldMeasuredHeight;
	
	private ScaleGestureDetector mScaleDetector;
	
	private Context mContext;

	public MetroMapView(Context context) {
		super(context);
		//mGestureDetector = new GestureDetector(this);
		mScaleDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}

	public MetroMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//mGestureDetector = new GestureDetector(this);
		mScaleDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}

	public MetroMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//mGestureDetector = new GestureDetector(this);
		mScaleDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}
	protected void initView() {
		setImageResource(R.drawable.map);
		setClickable(true);
		matrix.setTranslate(100f, 500f);
	    m = new float[9];
	    setImageMatrix(matrix);
	    setScaleType(ScaleType.MATRIX);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mScaleDetector.onTouchEvent(event);

		mScaleDetector.onTouchEvent(event);
        PointF curr = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	last.set(curr);
                start.set(last);
                mode = DRAG;
                break;
                
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    float deltaX = curr.x - last.x;
                    float deltaY = curr.y - last.y;
                    float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
                    float fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);
                    matrix.postTranslate(fixTransX, fixTransY);
                    fixTrans();
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
	
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		float mScaleFactor = detector.getScaleFactor();
        float origScale = saveScale;
        saveScale *= mScaleFactor;
        if (saveScale > MAX_SCALE) {
            saveScale = MAX_SCALE;
            mScaleFactor = MAX_SCALE / origScale;
        } else if (saveScale < MIN_SCALE) {
            saveScale = MIN_SCALE;
            mScaleFactor = MIN_SCALE / origScale;
        }

        if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight)
            matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
        else
            matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());

        fixTrans();
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
	
	/*@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		bmWidth = bm.getWidth();
	    bmHeight = bm.getHeight();
	}*/
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        //
        // Rescales image on rotation
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;
        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            //Fit to screen.
            float scale;

            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
                return;
            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();
            
            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;
            scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            setImageMatrix(matrix);
        }
        fixTrans();
    }
	
	void fixTrans() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];
        
        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);

        if (fixTransX != 0 || fixTransY != 0)
            matrix.postTranslate(fixTransX, fixTransY);
    }

    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }
    
    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

}
