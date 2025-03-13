package mert.kadakal.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SatilikDaireler extends AppCompatActivity {
    private ListView satiliklar_liste;
    private TextView yukleniyor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.satiliklar);

        yukleniyor = findViewById(R.id.yukleniyor_satiliklar);
        satiliklar_liste = findViewById(R.id.satiliklar_liste);

        // Create a single-threaded executor for background tasks
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Document doc;
            try {
                // Fetch the document in the background
                doc = NetworkUtils.connectToUrl("https://www.emlakjet.com/"+getIntent().getStringExtra("ilan_türü")+"-daire/bursa-nilufer-23-nisan-mahallesi/");
                //doc = NetworkUtils.connectToUrl("https://www.emlakjet.com/satilik-daire/bursa-nilufer-23-nisan-mahallesi/");

                // Update the UI on the main thread
                runOnUiThread(() -> {
                    List<HashMap<String, String>> main_items = new ArrayList<>();

                    for (int i = 0; i < doc.select(".styles_title__CN_n3").size(); i++) {
                        HashMap<String, String> items = new HashMap<>();

                        items.put("başlık", doc.select(".styles_title__CN_n3").get(i).text());
                        items.put("özellikler", doc.select(".styles_quickinfoWrapper__F5BBD").get(i).text());
                        items.put("fiyat", doc.select(".styles_price__8Z_OS").get(i).text());

                        main_items.add(items);
                    }

                    yukleniyor.setVisibility(View.INVISIBLE);
                    HtmlArrayAdapterSatilik adapter = new HtmlArrayAdapterSatilik(SatilikDaireler.this, R.layout.satiliklar_item, main_items);
                    satiliklar_liste.setAdapter(adapter);

                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    // Show error message in case of failure
                    Toast.makeText(SatilikDaireler.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        /*
        satiliklar_liste.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.emlakjet.com"+items.get(i).split("<link>")[1]));
            startActivity(intent);
        });
        */
    }
}
