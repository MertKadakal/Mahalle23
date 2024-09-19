package mert.kadakal.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawPathView extends View {

    private Paint paint;
    private Path path;

    public DrawPathView(Context context) {
        super(context);
        init();
    }

    public DrawPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED); // Çizgi rengi
        paint.setStrokeWidth(5); // Çizgi kalınlığı
        paint.setStyle(Paint.Style.STROKE);
        path = new Path();
    }

    public void setPath(Path path) {
        this.path = path;
        invalidate(); // Yeniden çizim yapar
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }
}
