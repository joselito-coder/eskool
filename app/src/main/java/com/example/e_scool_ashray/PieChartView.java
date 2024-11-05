package com.example.e_scool_ashray;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {

    private Paint paint;
    private float scored;
    private float total;

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public void setData(float scored, float total) {
        this.scored = scored;
        this.total = total;
        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (total <= 0) return;

        float scoredPercentage = (scored / total) * 360;
        float remainingPercentage = 360 - scoredPercentage;

        // Calculate size and position of the pie chart
        int width = getWidth();
        int height = getHeight();
        int minDimension = Math.min(width, height);
        float radius = minDimension / 3f; // Smaller size
        float centerX = width / 2f;
        float centerY = height / 2f;

        // Draw scored part
        paint.setColor(Color.GREEN);
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, 0, scoredPercentage, true, paint);

        // Draw remaining part
        paint.setColor(Color.RED);
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, scoredPercentage, remainingPercentage, true, paint);

        // Draw the text
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        String text = (int) scored + "/" + (int) total;
        float textWidth = paint.measureText(text);
        canvas.drawText(text, centerX - textWidth / 2, centerY + 15, paint);
    }

}
