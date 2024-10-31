package mert.kadakal.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.Font;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.HashMap;

public class NereyeGitsem_SecilenKategori extends AppCompatActivity {

    LinearLayout buttonContainer;
    HashMap<String, String> market_sube_urls = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nereyegitsem_secilenkategori);

        buttonContainer = findViewById(R.id.button_container);

        switch (getIntent().getStringExtra("SEÇİLEN_KATEGORİ")) {
            case "market":
                marketler();
                break;
            case "halısaha":
                halısaha();
                break;
            case "fırın":
                firinlar();
                break;
        }

    }

    private void halısaha() {
        buttonContainer.removeAllViews();  // Mevcut butonları temizle
        buttonContainer.setPadding(150, 0, 35, 0);  // Sol, Üst, Sağ, Alt padding

        Button btn1 = new Button(this);
        btn1.setText("\n\n\n\n\n\n\n\n\n\n\n\n\n\nÖzcan Halısaha");
        gorsel_ekle(getResources().getDrawable(R.drawable.ozcan_halisaha), btn1);
        btn1.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.tr/maps/place/%C3%B6zcan+spor+tesisleri/@40.2157779,28.9427158,183m/data=!3m1!1e3!4m6!3m5!1s0x14ca3925d4e8f03f:0x5a9e0eb39cd87162!8m2!3d40.2156384!4d28.9422879!16s%2Fg%2F11k616sf7v!5m1!1e2?entry=ttu&g_ep=EgoyMDI0MDkyOS4wIKXMDSoASAFQAw%3D%3D"));
            startActivity(intent);
        });
        buttonContainer.addView(btn1);
    }

    private void marketler() {
        buttonContainer.removeAllViews();  // Mevcut butonları temizle
        buttonContainer.setPadding(35, 0, 35, 0);  // Sol, Üst, Sağ, Alt padding

        String[] marketIsimleri = {"A101", "Migros", "Bim", "File Market", "Özhan"};
        Drawable[] marketGorseller = {
                getResources().getDrawable(R.drawable.amblem_a101),
                getResources().getDrawable(R.drawable.amblem_migros),
                getResources().getDrawable(R.drawable.amblem_bim),
                getResources().getDrawable(R.drawable.amblem_file),
                getResources().getDrawable(R.drawable.amblem_ozhan)
        };
        market_sube_urls.put("A101", "https://www.google.com.tr/maps/search/a101");
        market_sube_urls.put("Migros", "https://www.google.com.tr/maps/search/migros");
        market_sube_urls.put("Bim", "https://www.google.com.tr/maps/search/Bim");
        market_sube_urls.put("File Market", "https://www.google.com.tr/maps/search/File+Market");
        market_sube_urls.put("Özhan", "https://www.google.com.tr/maps/search/%C3%B6zhan");

        for (int i = 0; i < marketIsimleri.length; i++) {
            Button btn = new Button(this);
            btn.setTypeface(Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL));
            btn.setTextSize(20);
            btn.setText("\n\n\n\n\n\n\n\n\n\n\n\n\n" + marketIsimleri[i] + "\n\n\n");
            btn.setBackground(getResources().getDrawable(R.drawable.bg_item));
            btn.setWidth(650);
            gorsel_ekle(marketGorseller[i], btn);

            final String url = market_sube_urls.get(marketIsimleri[i]);
            btn.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });

            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 0, 10, 0); // Üst ve alt tarafa 16dp boşluk ekler
            btn.setLayoutParams(params);

            buttonContainer.addView(btn);

        }

        // HorizontalScrollView'u ilk buton konumuna kaydır
        buttonContainer.post(() -> {
            // İlk butonu görünümde göstermek için kaydır
            buttonContainer.scrollTo(0, 0); // İlk öğeden başlamak için
        });
    }


    private void firinlar() {
        buttonContainer.removeAllViews();  // Örnek olarak fırınlar için 3 öğe ekleyelim
        // Fırın bilgileri bu şekilde eklenebilir
        // gorsel_ekle() ve URL ekleme işlemleri de benzer şekilde yapılabilir
    }

    private void gorsel_ekle(Drawable img, Button myButton) {
        int width = (int) getResources().getDimension(R.dimen.amblem_width);
        int height = (int) getResources().getDimension(R.dimen.amblem_height);
        img.setBounds(0, 500, width, height+500);  // Görselin sol, üst, sağ, alt sınırlarını belirliyoruz
        myButton.setCompoundDrawables(null, img, null, null);
    }
}
