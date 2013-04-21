package com.wwmteam.wwm.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.wwmteam.wwm.R;
import com.wwmteam.wwm.beans.Map;
import com.wwmteam.wwm.utils.WWMUtils;

public class MetroMapView extends ImageView implements /*GestureDetector.OnGestureListener,*/
								ScaleGestureDetector.OnScaleGestureListener {

	private static final float MIN_SCALE = 1f;
	private static final float MAX_SCALE = 3f;
	
	private static final float INITIAL_WIDTH = 500f;
	private static final String TAG = MetroMapView.class.getSimpleName();
	
	Matrix matrix = new Matrix();
	protected int mMapId = 0;

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
	
	protected final RectF firstRect = new RectF(200, 50, 400, 115);//pixels on initial sizes
	protected final RectF secondRect = new RectF(40, 115, 250, 180);
	protected RectF currentFirstRect = firstRect;
	protected RectF currentSecondRect = secondRect;

	public MetroMapView(Context context) {
		super(context);
		mScaleDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}

	public MetroMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScaleDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}

	public MetroMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScaleDetector = new ScaleGestureDetector(context, this);
		mContext = context;
		initView();
	}
	
	public void setMap(Map map) {
		this.mMapId = map.id;
		setImage();
		invalidate();
		//initView();
	}
	protected void initView() {
		//setImageResource(R.drawable.map);
		setImage();
		//setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_bright));
		setClickable(true);
		matrix.setTranslate(1f, 1f);
	    m = new float[9];
	    setImageMatrix(matrix);
	    setScaleType(ScaleType.MATRIX);
	}
	
	protected void setImage() {
		int resId = R.drawable.map;
		switch(mMapId) {
		case 0:
		default:
			resId = R.drawable.map;
			break;
		case 1:
			resId = R.drawable.first_order;
			break;
		case 2:
			resId = R.drawable.ready_stations;
			break;
		}
		setImageResource(resId);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
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
                if (xDiff < CLICK && yDiff < CLICK) {
                	performClick();
                	onClick(event);
                }
                    
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
        }
        
        setImageMatrix(matrix);
        invalidate();
        return true; // indicate event was handled
    }
	
	protected void onClick(MotionEvent event) {
		matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];
		final float scale = (origWidth/INITIAL_WIDTH)*saveScale;
		currentFirstRect = new RectF(transX + firstRect.left*scale, transY + firstRect.top*scale,
					transX + firstRect.right*scale, transY + firstRect.bottom*scale);
		currentSecondRect = new RectF(transX + secondRect.left*scale, transY + secondRect.top*scale,
				transX + secondRect.right*scale, transY + secondRect.bottom*scale);
	
		if (WWMUtils.isPointIntoRect(new PointF(event.getX(), event.getY()), currentFirstRect)){
			onClickToStation(0);
		} else if (WWMUtils.isPointIntoRect(new PointF(event.getX(), event.getY()), currentSecondRect)) {
			onClickToStation(1);
		}
		
	}
	
	protected void onClickToStation(int stationId){
	}
	
	protected static final int HOTSPOT_COLOR_GRAY = 0xBB000000;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(currentFirstRect, new Paint(HOTSPOT_COLOR_GRAY));
		canvas.drawRect(currentSecondRect, new Paint(HOTSPOT_COLOR_GRAY));
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

        if (fixTransX != 0 || fixTransY != 0){
        	matrix.postTranslate(fixTransX, fixTransY);
        }
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
