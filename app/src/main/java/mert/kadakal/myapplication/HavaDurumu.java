package mert.kadakal.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class HavaDurumu extends AppCompatActivity {

    TextView hava_durumu_bilgiler;
    TextView anlik_derece;
    TextView yukleniyor;
    TextView hissedilen;

    TextView sonrakigun1;
    TextView sonrakigun2;
    TextView sonrakigun3;
    TextView sonrakigun4;
    TextView sonrakigun5;
    TextView sonrakigun6;

    LottieAnimationView sembol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hava_durumu);

        hava_durumu_bilgiler = findViewById(R.id.hava_durumu_bilgiler);
        anlik_derece = findViewById(R.id.anlik_derece);
        yukleniyor = findViewById(R.id.yukleniyor);
        hissedilen = findViewById(R.id.hissedilen);

        sonrakigun1 = findViewById(R.id.sonrakigun1);
        sonrakigun2 = findViewById(R.id.sonrakigun2);
        sonrakigun3 = findViewById(R.id.sonrakigun3);
        sonrakigun4 = findViewById(R.id.sonrakigun4);
        sonrakigun5 = findViewById(R.id.sonrakigun5);
        sonrakigun6 = findViewById(R.id.sonrakigun6);

        sembol = findViewById(R.id.lottieAnimationView);

        // Trigger AsyncTask to fetch news data in the background
        new HavaDurumu.FetchNewsTask().execute("https://havadurumu15gunluk.xyz/havadurumu7/293/bursa-hava-durumu-7-gunluk.html");
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

            Elements liElements = doc.select("ul.aktuel > li");
            ArrayList<String> lielements = new ArrayList<>();
            for (Element li : liElements) {
                String label = li.textNodes().get(0).text().trim(); // Etiket ismini alır
                String value = li.select("span.bold").text().trim(); // Span içindeki değeri alır
                if (!(label.equals("Görüş Uzaklığı")) && !(label.equals("Çiy Noktası")) && !(label.equals("Bulut Oranı")) && !(label.equals("Basınç")) && !(label.equals("Rakım"))) {
                    lielements.add(value);
                }
            }

            Elements trElements = doc.select("div.weather-forecast > table > tbody > tr");
            sonrakigun1.setText(Html.fromHtml("<b>"+trElements.get(2).text().replace("Saatlik ", "").substring(0, trElements.get(2).text().replace("Saatlik ", "").indexOf(" ")) + "</b><br>" + trElements.get(2).text().replace("Saatlik ", "").substring(trElements.get(2).text().replace("Saatlik ", "").indexOf(" ") + 1)));
            sonrakigun2.setText(replaceThirdSpaceWithNewline(trElements.get(3).text().replace("Saatlik ", "")));
            sonrakigun3.setText(replaceThirdSpaceWithNewline(trElements.get(4).text()));
            sonrakigun4.setText(replaceThirdSpaceWithNewline(trElements.get(5).text()));
            sonrakigun5.setText(replaceThirdSpaceWithNewline(trElements.get(6).text()));
            sonrakigun6.setText(replaceThirdSpaceWithNewline(trElements.get(7).text()));


            String suanki_derece = doc.selectFirst("span.temperature.type-1").text();
            String hissedilen_derece = doc.selectFirst("span.temperature.type-2").text();
            String ruzgar = lielements.get(0);
            String nem = lielements.get(1);
            String gun_d = lielements.get(2);
            String gun_b = lielements.get(3);

            hava_durumu_bilgiler.setText(Html.fromHtml(String.format("<b>Nem: </b>%s<br><b>Rüzgar: </b>%s<br><b>Gün Doğumu: </b>%s<br><b>Gün Batımı: </b>%s", nem, ruzgar, gun_d, gun_b)));
            anlik_derece.setText(suanki_derece);
            hissedilen.setText(hissedilen_derece);

            if (doc.selectFirst("span.status").text().contains("bulutlu")) {
                sembol.setAnimation(R.raw.bulutlu);
            } else {
                switch (doc.selectFirst("span.status").text()) {
                    case "Güneşli":
                        sembol.setAnimation(R.raw.gunesli);
                        break;
                    case "Çoğunlukla güneşli":
                        sembol.setAnimation(R.raw.parcali_bulutlu);
                        break;
                    case "Kısmen güneşli":
                        sembol.setAnimation(R.raw.parcali_bulutlu);
                        break;
                    case "Sağanak yağış":
                        sembol.setAnimation(R.raw.saganak);
                        break;
                    default:
                        sembol.setAnimation(R.raw.gunesli);
                        break;
                }
            }
            sembol.setVisibility(View.VISIBLE);
        }

        private Spanned replaceThirdSpaceWithNewline(String input) {
            int firstIndex = input.indexOf(" ");
            int secondIndex = input.indexOf(" ", firstIndex + 1);
            int thirdIndex = input.indexOf(" ", secondIndex + 1);

            if (thirdIndex != -1) {
                return Html.fromHtml(String.format("<b>%s</b><br>%s", input.substring(0, thirdIndex), input.substring(thirdIndex + 1)));
            }
            return Html.fromHtml(input);  // Üçüncü boşluk yoksa orijinal stringi döndür
        }
    }
}