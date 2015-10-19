package com.sevenheaven.easebitmapmesh.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.sevenheaven.easybitmapmesh.EasyBitmapMeshView;

public class MainActivity extends AppCompatActivity {

    private EasyBitmapMeshView meshView;
    private Bitmap normalMapBitmap;
    private Canvas normalMapCanvas;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap texture;

    float xFactor;
    float yFactor;

    private Shader shader;
    private Matrix matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        normalMapBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        normalMapCanvas = new Canvas(normalMapBitmap);


        texture = BitmapFactory.decodeResource(getResources(), R.drawable.binary);

        matrix = new Matrix();
        shader = new SweepGradient(0, 0, new int[]{0xFF008888, 0xFF00FF00, 0xFFFF0000, 0xFF0000FF, 0xFF008888}, new float[]{0, 0.15F, 0.5F, 0.85F, 1});
        paint.setShader(shader);

        xFactor = 100.0F / (float) texture.getWidth();
        yFactor = 100.0F / (float) texture.getHeight();

        Log.i("factor", "xFactor:" + xFactor + ", yFactor:" + yFactor);

        meshView = new EasyBitmapMeshView(this) {
            @Override
            public Bitmap getNormalMap() {
                return normalMapBitmap;
            }

            @Override
            public Bitmap getTexture() {
                return texture;
            }
        };

        setContentView(meshView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_MOVE:

                paint.setColor(0xFF000000);
                paint.setShader(null);
                normalMapCanvas.drawRect(0, 0, 100, 100, paint);

                paint.setColor(0xFFFF0000);

                float x = event.getX() * xFactor;
                float y = event.getY() * yFactor;

                paint.setShader(shader);
                matrix.setRotate(90);
                matrix.postTranslate(x, y);
                shader.setLocalMatrix(matrix);

                normalMapCanvas.drawCircle(x, y, 20, paint);

                meshView.invalidate();
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
