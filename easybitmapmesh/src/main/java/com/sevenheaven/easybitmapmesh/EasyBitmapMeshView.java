package com.sevenheaven.easybitmapmesh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by 7heaven on 15/10/18.
 */
public abstract class EasyBitmapMeshView extends View {

    private static final boolean DEBUG = false;

    private Bitmap bitmap;
    private Bitmap normalmap;
    private int rows = 30;
    private int lines = 30;

    private float[] verts;
    private float[] offsetVerts;

    private Paint paint;

    public EasyBitmapMeshView(Context context){
        this(context, null);
    }

    public EasyBitmapMeshView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public EasyBitmapMeshView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        bitmap = getTexture();

        calculateVerts();
    }

    private void calculateVerts(){
        normalmap = getNormalMap();

        if(normalmap != null){

            int index = 0;

            verts = new float[(rows + 1) * (lines + 1) * 2];

            int blockWidth = bitmap.getWidth() / rows;
            int blockHeight = bitmap.getHeight() / lines;
            int normalBlockWidth = normalmap.getWidth() / rows;
            int normalBlockHeight = normalmap.getHeight() / lines;


            for(int i = 0; i <= lines; i++){
                for(int j = 0; j <= rows; j++){
                    int x = j * normalBlockWidth;
                    int y = i * normalBlockHeight;

                    Log.i("factor", "x:" + x + ",y:" + y);

                    if(x < 0) x = 0;
                    if(y < 0) y = 0;
                    if(x >= normalmap.getWidth()) x = normalmap.getWidth() - 1;
                    if(y >= normalmap.getHeight()) y = normalmap.getHeight() - 1;

                    int color = normalmap.getPixel(x, y);
                    int norR = color >> 16 & 0xFF;
                    int norG = color >> 8 & 0xFF;
                    int norB = color & 0xFF;

                    float xOffset = (float) (norB - norG) * 0.1F;
                    float yOffset = (float) (-norR) * 0.1F;

                    verts[index * 2] = j * blockWidth + xOffset;
                    verts[index * 2 + 1] = i * blockHeight + yOffset;

//                    Log.i("factor", "i:" + i + ", verts[" + index * 2 + "]:" + verts[index * 2]);
//                    Log.i("factor", "j:" + j + ", verts[" + index * 2 + 1 + "]:" + verts[index * 2 + 1]);



                    index ++;
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(verts != null && bitmap != null){

            canvas.drawBitmapMesh(bitmap, rows, lines, verts, 0, null, 0, paint);


            if(DEBUG){
                int index = 0;
                for(int i = 0; i <= lines; i++){
                    for(int j = 0; j <= rows; j++){
                        paint.setColor(0xFFFF7733);

                        canvas.drawCircle(verts[index * 2], verts[index * 2 + 1], 2, paint);

                        paint.setColor(0xFF0099CC);

                        if(j < rows){
                            canvas.drawLine(verts[index * 2], verts[index * 2 + 1], verts[(index + 1) * 2], verts[(index + 1) * 2 + 1], paint);
                        }

                        if(i < lines){
                            canvas.drawLine(verts[index * 2], verts[index * 2 + 1], verts[(index + lines + 1) * 2], verts[(index + lines + 1) * 2 + 1], paint);
                        }

                        index++;
                    }
                }
            }

            Rect src = new Rect();
            src.left = 0;
            src.top = 0;
            src.bottom = normalmap.getHeight();
            src.right = normalmap.getWidth();

            Rect dst = new Rect();
            dst.left = bitmap.getWidth();
            dst.right = bitmap.getWidth() * 2;
            dst.top = 0;
            dst.bottom = bitmap.getHeight();

            canvas.drawBitmap(normalmap, src, dst, paint);
        }
    }

    /**
     *
     * 返回法向量图
     *
     * @return
     */
    public abstract Bitmap getNormalMap();

    /**
     *
     * 返回纹理贴图
     *
     * @return
     */
    public abstract Bitmap getTexture();
}
