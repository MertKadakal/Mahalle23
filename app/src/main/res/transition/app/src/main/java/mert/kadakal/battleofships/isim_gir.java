package mert.kadakal.battleofships;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class isim_gir extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isimgir);

        TextView isim1 = findViewById(R.id.isim1);
        TextView isim2 = findViewById(R.id.isim2);
        TextView basla = findViewById(R.id.basla);

        basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(isim1.getText()) || TextUtils.isEmpty(isim2.getText())) {
                    Toast.makeText(isim_gir.this, "Lütfen iki ismi de giriniz", Toast.LENGTH_SHORT).show();
                }
                else if (isim1.getText().toString().toLowerCase().equals(isim2.getText().toString().toLowerCase())) {
                    Toast.makeText(isim_gir.this, "Lütfen farklı isimler giriniz", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(isim_gir.this, oyun.class);
                    intent.putExtra("1.oyuncu_ismi", isim1.getText().toString());
                    intent.putExtra("2.oyuncu_ismi", isim2.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }
}
