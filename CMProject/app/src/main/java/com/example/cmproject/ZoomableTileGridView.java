package com.example.cmproject;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ZoomableTileGridView extends View {

    private static final int NUM_ROWS = 5;  // Number of rows in the grid
    private static final int NUM_COLS = 5;  // Number of columns in the grid

    private Paint cellPaint;
    private Paint borderPaint;

    private int selectedRow = -1;
    private int selectedCol = -1;
    private OnCellClickListener onCellClickListener;

    public ZoomableTileGridView(Context context) {
        super(context);
        init();
    }

    public ZoomableTileGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomableTileGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        cellPaint = new Paint();
        cellPaint.setColor(Color.WHITE);

        borderPaint = new Paint();
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cellWidth = getWidth() / NUM_COLS;
        int cellHeight = getHeight() / NUM_ROWS;

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                float left = col * cellWidth;
                float top = row * cellHeight;
                float right = left + cellWidth;
                float bottom = top + cellHeight;

                // Draw cell
                canvas.drawRect(left, top, right, bottom, cellPaint);

                // Draw border
                canvas.drawLine(left, top, right, top, borderPaint);  // top border
                canvas.drawLine(left, top, left, bottom, borderPaint); // left border
                canvas.drawLine(right, top, right, bottom, borderPaint); // right border
                canvas.drawLine(left, bottom, right, bottom, borderPaint); // bottom border
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        int cellWidth = getWidth() / NUM_COLS;
        int cellHeight = getHeight() / NUM_ROWS;

        int clickedCol = (int) (x / cellWidth);
        int clickedRow = (int) (y / cellHeight);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Handle the touch down event
                // Here, you can perform any action you need when the user presses on a cell
                selectedRow = clickedRow;
                selectedCol = clickedCol;
                invalidate(); // Redraw the view
                break;
            case MotionEvent.ACTION_UP:
                // Handle the touch up event
                break;
            case MotionEvent.ACTION_MOVE:
                // Handle the touch move event
                break;
        }

        return true;
    }

    public interface OnCellClickListener {
        void onCellClick(int row, int col);
    }

    // Setter for the OnCellClickListener
    public void setOnCellClickListener(OnCellClickListener listener) {
        this.onCellClickListener = listener;
    }
}