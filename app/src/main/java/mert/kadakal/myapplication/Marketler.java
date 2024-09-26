package mert.kadakal.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Marketler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marketler);

        gorsel_ekle(getResources().getDrawable(R.drawable.amblem_a101), findViewById(R.id.birinci));
        gorsel_ekle(getResources().getDrawable(R.drawable.amblem_migros), findViewById(R.id.ikinci));
        gorsel_ekle(getResources().getDrawable(R.drawable.amblem_bim), findViewById(R.id.üçüncü));
        gorsel_ekle(getResources().getDrawable(R.drawable.amblem_file), findViewById(R.id.dördüncü));
        gorsel_ekle(getResources().getDrawable(R.drawable.amblem_ozhan), findViewById(R.id.beşinci));

    }

    private void gorsel_ekle(Drawable img, Button myButton) {
        int width = (int) getResources().getDimension(R.dimen.amblem_width);
        int height = (int) getResources().getDimension(R.dimen.amblem_height);
        myButton.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        img.setBounds(0, 500, width, height+500);  // Görselin sol, üst, sağ, alt sınırlarını belirliyoruz
        myButton.setCompoundDrawables(null, img, null, null);

    }
}