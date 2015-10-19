package com.sevenheaven.easebitmapmesh.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.sevenheaven.easybitmapmesh.EasyBitmapMeshView;

public class MainActivity extends AppCompatActivity {

    private EasyBitmapMeshView meshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        meshView = new EasyBitmapMeshView(this) {
            @Override
            public Bitmap getNormalMap() {
                Bitmap normalmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(normalmap);

                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(0xFF00FF00);
                canvas.drawCircle(50, 50, 25, paint);

                return normalmap;
            }

            @Override
            public Bitmap getTexture() {
                return BitmapFactory.decodeResource(getResources(), R.drawable.lena);
            }
        };

        setContentView(meshView);
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
