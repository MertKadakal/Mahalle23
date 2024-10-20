package mert.kadakal.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SatilikDaireler extends AppCompatActivity {
    ArrayList<String> items;
    private ListView satiliklar_liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.satilik_kiralik_daireler);

        satiliklar_liste = findViewById(R.id.satiliklar_liste);

        // Create a single-threaded executor for background tasks
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Document doc;
            try {
                // Fetch the document in the background
                doc = NetworkUtils.connectToUrl("https://www.emlakjet.com/"+getIntent().getStringExtra("ilan_türü")+"-daire/bursa-nilufer-23-nisan-mahallesi/");

                // Update the UI on the main thread
                runOnUiThread(() -> {
                    items = new ArrayList<>();

                    //daireleri listeye ekle
                    for (int i = 0; i < doc.select("._3qUI9q").size(); i += 2) {
                        Element ilan = doc.select("._3qUI9q").get(i);
                        String ilan_basligi = ilan.select("h3").text();
                        StringBuilder add = new StringBuilder();
                        add.append("<br><b>" + ilan_basligi + "</b><br><br>");
                        for (int j = 0; j < ilan.select("div._2UELHn > span").size(); j++) {
                            Element span = ilan.select("div._2UELHn > span").get(j);
                            switch (span.selectFirst("i.material-icons").text()) {
                                case "texture":
                                    add.append("<b>Ebat:</b> " + span.text().split("texture")[1]+"<br>");
                                    break;
                                case "layers":
                                    add.append("<b>Kat:</b> " + span.text().split("layers")[1]+ "<br>");
                                    break;
                                case "event":
                                    add.append("<b>İlan tarihi:</b> " + span.text().split("event")[1]+"<br>");
                                    break;
                                case "weekend":
                                    add.append("<b>Oda sayısı:</b> " + span.text().split("weekend")[1]+"<br>");
                                    break;
                            }
                        }

                        add.append("<br><b>Fiyat: </b><i>" + ilan.selectFirst("p._2C5UCT").text() + "</i><br><link>"+ilan.select("a").attr("href"));

                        items.add(add.toString());
                    }

                    HtmlArrayAdapterSatilik adapter = new HtmlArrayAdapterSatilik(SatilikDaireler.this, R.layout.satilik_kiralik_daireler, items);
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

        satiliklar_liste.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.emlakjet.com"+items.get(i).split("<link>")[1]));
            startActivity(intent);
        });
    }
}
