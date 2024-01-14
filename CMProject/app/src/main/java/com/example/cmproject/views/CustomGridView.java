package com.example.cmproject.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class CustomGridView extends View {
    private BitmapDrawable bm;
    private Map<String, Integer> cellColors = new HashMap<>();

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Drawable d = getBackground();
        if (d != null) {
            Bitmap b = drawableToBitmap(d);
            bm = new BitmapDrawable(getResources(), b);
            bm.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int squareSize = bm.getIntrinsicWidth(); // Assuming square tiles

        // Calculate the number of squares needed both horizontally and vertically
        int numSquaresX = (int) Math.ceil((float) canvas.getWidth() / squareSize);
        int numSquaresY = (int) Math.ceil((float) canvas.getHeight() / squareSize);

        // Adjust the bounds to fit the required number of squares with proper spacing
        int adjustedWidth = numSquaresX * squareSize;
        int adjustedHeight = numSquaresY * squareSize;

        int left = (canvas.getWidth() - adjustedWidth) / 2;
        int top = (canvas.getHeight() - adjustedHeight) / 2;

        bm.setBounds(left, top, left + adjustedWidth, top + adjustedHeight);

        // Draw the adjusted background
        bm.draw(canvas);

        // Draw cell colors
        for (Map.Entry<String, Integer> entry : cellColors.entrySet()) {
            String[] coordinates = entry.getKey().split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);

            int cellLeft = left + x * squareSize;
            int cellTop = top + y * squareSize;
            int cellRight = cellLeft + squareSize;
            int cellBottom = cellTop + squareSize;

            canvas.drawRect(cellLeft, cellTop, cellRight, cellBottom, entry.getValue() != null ?
                    new android.graphics.Paint() {{
                        setColor(entry.getValue());
                    }} :
                    new android.graphics.Paint() {{
                        setColor(Color.TRANSPARENT);
                    }});
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}