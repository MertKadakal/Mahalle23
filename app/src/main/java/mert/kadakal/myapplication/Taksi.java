package mert.kadakal.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class Taksi extends AppCompatActivity {

    ImageView btn1;
    TextView btn2;
    TextView btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taksi);

        btn1 = findViewById(R.id.taksi_foto);
        btn2 = findViewById(R.id.taksi_tel);
        btn2.setText("0530 037 47 27");
        btn3 = findViewById(R.id.taksi_ara);

        Animation slideDown = AnimationUtils.loadAnimation(this, R.transition.slide_down);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.transition.slide_up);

        // Animasyonları butonlara uygula
        btn1.startAnimation(slideDown);
        btn2.startAnimation(slideUp);
        btn3.startAnimation(slideUp);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:05300374727")));
            }
        });
    }
}