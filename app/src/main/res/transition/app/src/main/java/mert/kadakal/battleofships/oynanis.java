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

public class oynanis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oynanis);

        TextView bilgi = findViewById(R.id.scrollableTextView);
        bilgi.setText(Html.fromHtml(String.format(
                "<b>Nedir?</b><br><br>Amiralbattı, tahmine dayanan iki kişilik bir oyundur. Oyuncular sırayla " +
                "rakibin tahtasında bir hücre seçer ve sonuçlara göre tahmin yürüterek rakip gemilerinin ait olduğu tüm " +
                "hücreleri bularak batırmaya çalışırlar. Rakibin tüm gemilerini ilk batıran oyuncu oyunu kazanır.<br><br>" +
                "<b>Gemiler ve Özellikleri</b><br><br>" +
                "<b>Carrier: </b><i>Adet: </i> 1, <i>Uzunluk: </i>5<br>" +
                "<b>Destroyer: </b><i>Adet: </i> 1, <i>Uzunluk: </i>3<br>" +
                "<b>Battleship: </b><i>Adet: </i> 2, <i>Uzunluk: </i>4<br>" +
                "<b>Submarine: </b><i>Adet: </i> 1, <i>Uzunluk: </i>3<br>" +
                "<b>Patrol Boat: </b><i>Adet: </i> 4, <i>Uzunluk: </i>2<br><br>" +
                "<b>Oyun Tahtası Özellikleri</b><br><br>" +
                "Oyun tahtası 10x10 boyutundadır. Her geminin farklı rengi olup deniz rengi mavi olarak belirtilmiştir."
        )));
    }
}