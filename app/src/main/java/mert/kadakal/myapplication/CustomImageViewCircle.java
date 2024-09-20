package mert.kadakal.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomImageViewCircle extends ImageView {
    private Paint paint;
    private float circleX = 500; // Dairenin X koordinatı
    private float circleY = 500; // Dairenin Y koordinatı
    private float circleRadius = 20; // Dairenin yarıçapı

    public CustomImageViewCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomImageViewCircle(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);  // Daire rengini buradan ayarlayabilirsiniz.
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Daireyi çiz
        canvas.drawCircle(circleX, circleY, circleRadius, paint);
    }

    // Daireyi (500, 500) noktasında tutmak için bir metod
    public void updateCirclePosition() {
        // Dairenin koordinatlarını güncelle
        circleX = 500;
        circleY = 500;
        invalidate(); // Görünümü güncelle
    }
}
