package mert.kadakal.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HavaDurumu extends AppCompatActivity {

    TextView hava_durumu_bilgiler;
    TextView anlik_derece;
    TextView yukleniyor;
    ImageView suanki_hava_gorsel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hava_durumu);

        hava_durumu_bilgiler = findViewById(R.id.hava_durumu_bilgiler);
        suanki_hava_gorsel = findViewById(R.id.suanki_hava_gorsel);
        anlik_derece = findViewById(R.id.anlik_derece);
        yukleniyor = findViewById(R.id.yukleniyor);

        // Trigger AsyncTask to fetch news data in the background
        new HavaDurumu.FetchNewsTask().execute("https://www.google.com/search?q=23+nisan+mahallesi+bursa+hava+durumu&sca_esv=414fa47fd96e69ee&sca_upv=1&rlz=1C1NDCM_enTR1036TR1036&sxsrf=ADLYWIKkgG-8YcYmASlCuYex6yJwt1LlGA%3A1727115306533&ei=KrDxZoGhIPGQxc8Ph8qy2Qw&ved=0ahUKEwiB3tPy1dmIAxVxSPEDHQelLMsQ4dUDCA8&uact=5&oq=23+nisan+mahallesi+bursa+hava+durumu&gs_lp=Egxnd3Mtd2l6LXNlcnAiJDIzIG5pc2FuIG1haGFsbGVzaSBidXJzYSBoYXZhIGR1cnVtdTIKECEYoAEYwwQYCjIKECEYoAEYwwQYCkiVDFDZBVinC3ABeAGQAQCYAcMBoAG5BqoBAzAuNbgBA8gBAPgBAZgCA6ACswLCAgoQABiwAxjWBBhHmAMAiAYBkAYIkgcDMS4yoAeUHQ&sclient=gws-wiz-serp");
    }

    private class FetchNewsTask extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... urls) {
            Document doc;
            try {
                doc = Jsoup.connect(urls[0]).get();
                yukleniyor.setVisibility(TextView.INVISIBLE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            super.onPostExecute(doc);

            String suanki_derece = doc.getElementById("wob_tm").text();
            String yagis = doc.getElementById("wob_pp").text();
            String nem = doc.getElementById("wob_hm").text();
            String ruzgar = doc.getElementById("wob_ws").text();
            String tarih = doc.getElementById("wob_dts").text();
            String suanki_hava_png = doc.getElementById("wob_tci").attr("src");

            hava_durumu_bilgiler.setText(Html.fromHtml(String.format("<b>%s</b><br><br>Yağış: %s<br>Nem: %s<br>Rüzgar: %s<br>", tarih, yagis, nem, ruzgar)));
            anlik_derece.setText(suanki_derece + " °C");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Glide kullanarak görseli ImageView'a yükle
                    Glide.with(HavaDurumu.this).load("https:" + suanki_hava_png).into(suanki_hava_gorsel);
                }
            });

        }
    }
}