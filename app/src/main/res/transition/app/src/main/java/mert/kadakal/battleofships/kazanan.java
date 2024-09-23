package mert.kadakal.battleofships;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class kazanan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kazanan);

        TextView kazanan = findViewById(R.id.kazanan);
        kazanan.setText(Html.fromHtml(String.format("<b>%s</b> kazandı!", getIntent().getStringExtra("kazanan"))));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Bu satırı eklemezsen, geri tuşuna basıldığında uygulama kapanmaz
        Intent intent = new Intent(kazanan.this, anasayfa.class);
        startActivity(intent);
    }

}
