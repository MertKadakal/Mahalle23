package mert.kadakal.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class Marketler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marketler);

        Button btn1 = findViewById(R.id.birinci);
        Button btn2 = findViewById(R.id.ikinci);
        Button btn3 = findViewById(R.id.üçüncü);
        Button btn4 = findViewById(R.id.dördüncü);
        Button btn5 = findViewById(R.id.beşinci);

        gorsel_ekle(getResources().getDrawable(R.drawable.amblem_a101), findViewById(R.id.birinci));
        gorsel_ekle(getResources().getDrawable(R.drawable.amblem_migros), findViewById(R.id.ikinci));
        gorsel_ekle(getResources().getDrawable(R.drawable.amblem_bim), findViewById(R.id.üçüncü));
        gorsel_ekle(getResources().getDrawable(R.drawable.amblem_file), findViewById(R.id.dördüncü));
        gorsel_ekle(getResources().getDrawable(R.drawable.amblem_ozhan), findViewById(R.id.beşinci));

        HashMap<String, String> market_sube_urls = new HashMap<>();
        market_sube_urls.put("birinci", "https://www.google.com.tr/maps/search/a101/@40.2195637,28.9433062,15.25z/data=!5m1!1e2?entry=ttu&g_ep=EgoyMDI0MDkyNS4wIKXMDSoASAFQAw%3D%3D");
        market_sube_urls.put("ikinci", "https://www.google.com.tr/maps/search/migros/@40.2195585,28.942799,15.25z/data=!5m1!1e2?entry=ttu&g_ep=EgoyMDI0MDkyNS4wIKXMDSoASAFQAw%3D%3D");
        market_sube_urls.put("üçüncü", "https://www.google.com.tr/maps/search/Bim/@40.2196231,28.9424547,15.25z/data=!5m1!1e2?entry=ttu&g_ep=EgoyMDI0MDkyNS4wIKXMDSoASAFQAw%3D%3D");
        market_sube_urls.put("dördüncü", "https://www.google.com.tr/maps/search/File+Market/@40.2193945,28.9398734,15.25z/data=!5m1!1e2?entry=ttu&g_ep=EgoyMDI0MDkyNS4wIKXMDSoASAFQAw%3D%3D");
        market_sube_urls.put("beşinci", "https://www.google.com.tr/maps/search/%C3%B6zhan/@40.2200689,28.9409981,15.25z/data=!5m1!1e2?entry=ttu&g_ep=EgoyMDI0MDkyNS4wIKXMDSoASAFQAw%3D%3D");

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(market_sube_urls.get(getResources().getResourceEntryName(v.getId()))));
                startActivity(intent);
            }
        };

        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
        btn4.setOnClickListener(listener);
        btn5.setOnClickListener(listener);
    }

    private void gorsel_ekle(Drawable img, Button myButton) {
        int width = (int) getResources().getDimension(R.dimen.amblem_width);
        int height = (int) getResources().getDimension(R.dimen.amblem_height);
        myButton.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        img.setBounds(0, 500, width, height+500);  // Görselin sol, üst, sağ, alt sınırlarını belirliyoruz
        myButton.setCompoundDrawables(null, img, null, null);

    }
}