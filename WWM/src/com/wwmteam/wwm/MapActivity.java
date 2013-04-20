package com.wwmteam.wwm;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
public class MapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
     /*// ImageViews must be under LinearLayout in the xml or the code crashes into scaleImage(). Tune scaleImage() into your needs.
        ImageView view1 = (ImageView) findViewById(R.id.test1);
        ImageView view2 = (ImageView) findViewById(R.id.test2);

        scaleImage(view1, 250); // in dp
        scaleImage(view2, 100); // in dp
*/    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }
    
/*    private void scaleImage(ImageView view, int boundBoxInDp) {
		// Get the ImageView and its bitmap
	    Drawable drawing = view.getDrawable();
	    Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

	    // Get current dimensions
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();

	    // Determine how much to scale: the dimension requiring less scaling is
	    // closer to the its side. This way the image always stays inside your
	    // bounding box AND either x/y axis touches it.
	    float xScale = ((float) boundBoxInDp) / width;
	    float yScale = ((float) boundBoxInDp) / height;
	    float scale = (xScale <= yScale) ? xScale : yScale;

	    // Create a matrix for the scaling and add the scaling data
	    Matrix matrix = new Matrix();
	    matrix.postScale(scale, scale);

	    // Create a new bitmap and convert it to a format understood by the ImageView
	    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	    BitmapDrawable result = new BitmapDrawable(scaledBitmap);
	    width = scaledBitmap.getWidth();
	    height = scaledBitmap.getHeight();

	    // Apply the scaled bitmap
	    view.setImageDrawable(result);

	    // Now change ImageView's dimensions to match the scaled image
	    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
	    params.width = width;
	    params.height = height;
	    view.setLayoutParams(params);

	}*/
    
    
}
