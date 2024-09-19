package mert.kadakal.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;

public class CustomImageView extends ImageView {
    private Paint paint;
    private float startX, startY, stopX, stopY;
    private ArrayList<Line> lines; // Çizgi verilerini saklamak için bir liste
    Canvas canvas;


    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);  // Çizgi rengini buradan ayarlayabilirsiniz.
        paint.setStrokeWidth(5);    // Çizgi kalınlığını buradan ayarlayabilirsiniz.
        lines = new ArrayList<>(); // Çizgi listesini başlat
    }

    // Çizgi koordinatlarını ayarlamak için bir metod
    public void setLineCoordinates(float startX, float startY, float endX, float endY) {
        lines.add(new Line(startX, startY, endX, endY));
        invalidate(); // Görünümü güncelle
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        for (Line line : lines) {
            canvas.drawLine(line.startX, line.startY, line.endX, line.endY, paint);
        }
    }

    // Çizgileri temizlemek için yeni bir metot
    public void clearLines() {
        lines.clear(); // Çizgi listesini temizle
        invalidate(); // Görünümü güncelle
    }

    // Çizgi sınıfı
    private static class Line {
        float startX, startY, endX, endY;

        Line(float startX, float startY, float endX, float endY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }
    }
}
