package mert.kadakal.battleofships;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class anasayfa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anasayfa);

        TextView yeni_oyun = findViewById(R.id.text_yeni_oyun);
        TextView oynanis = findViewById(R.id.text_oynanis);
        TextView oyun_ismi = findViewById(R.id.text_oyun_ismi);

        // Animasyonları yükle
        Animation slideDown = AnimationUtils.loadAnimation(this, R.transition.slide_down);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.transition.slide_up);

        // Animasyonları butonlara uygula
        yeni_oyun.startAnimation(slideUp);
        oynanis.startAnimation(slideUp);
        oyun_ismi.startAnimation(slideDown);

        yeni_oyun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(anasayfa.this, isim_gir.class);
                startActivity(intent);
            }
        });

        oynanis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(anasayfa.this, oynanis.class);
                startActivity(intent);
            }
        });
    }
}
