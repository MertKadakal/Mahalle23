package mert.kadakal.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class Toplu_ulasim extends AppCompatActivity {

    ImageView kucuk_sanayi_git;
    ImageView altinsehir_git;
    Button btn_35E2;
    TextView en_yakin_kalkis_35E2_1;
    TextView en_yakin_kalkis_35E2_2;
    Button btn_95A;
    TextView en_yakin_kalkis_95A_1;
    TextView en_yakin_kalkis_95A_2;
    Button btn_43H;
    TextView en_yakin_kalkis_43H_1;
    TextView en_yakin_kalkis_43H_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SSLHelper.disableSSLCertificateChecking();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.toplu_ulasim);
        Animation anim = AnimationUtils.loadAnimation(this, R.transition.up_down);

        kucuk_sanayi_git = findViewById(R.id.kucuk_sanayi_git);
        altinsehir_git = findViewById(R.id.altinsehir_git);
        btn_35E2 = findViewById(R.id.buton_35E2);
        en_yakin_kalkis_35E2_1 = findViewById(R.id.en_yakin_kalkis_35E2_1);
        en_yakin_kalkis_35E2_2 = findViewById(R.id.en_yakin_kalkis_35E2_2);
        btn_95A = findViewById(R.id.buton_95A);
        en_yakin_kalkis_95A_1 = findViewById(R.id.en_yakin_kalkis_95A_1);
        en_yakin_kalkis_95A_2 = findViewById(R.id.en_yakin_kalkis_95A_2);
        btn_43H = findViewById(R.id.buton_43H);
        en_yakin_kalkis_43H_1 = findViewById(R.id.en_yakin_kalkis_43H_1);
        en_yakin_kalkis_43H_2 = findViewById(R.id.en_yakin_kalkis_43H_2);

        kucuk_sanayi_git.startAnimation(anim);
        altinsehir_git.startAnimation(anim);
        btn_35E2.startAnimation(anim);
        btn_95A.startAnimation(anim);
        btn_43H.startAnimation(anim);

        View.OnClickListener listener = v -> {
            Intent intent = null;
            switch (getResources().getResourceEntryName(v.getId())) {
                case "kucuk_sanayi_git":
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.tr/maps/place/K%C3%BC%C3%A7%C3%BCk+Sanayi/@40.2131471,28.942018,16.79z/data=!4m6!3m5!1s0x14ca11500c84f00f:0x88d5372f63866bde!8m2!3d40.2120311!4d28.9416146!16s%2Fg%2F1tf8w96b?entry=ttu&g_ep=EgoyMDI0MDkzMC4wIKXMDSoASAFQAw%3D%3D"));
                    break;
                case "altinsehir_git":
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.tr/maps/place/Alt%C4%B1n%C5%9Fehir/@40.2134334,28.9363387,15.92z/data=!4m6!3m5!1s0x14ca115c8d6f5dc1:0x9a73845a335004b0!8m2!3d40.214833!4d28.93406!16s%2Fg%2F1ydp3j4qy?entry=ttu&g_ep=EgoyMDI0MDkzMC4wIKXMDSoASAFQAw%3D%3D"));
                    break;
                case "buton_35E2":
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://moovitapp.com/index/tr/toplu_ta%C5%9F%C4%B1ma-line-35e2-Bursa-3663-3732394-155946318-0"));
                    break;
                case "buton_95A":
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://moovitapp.com/index/tr/toplu_ta%C5%9F%C4%B1ma-line-95A-Bursa-3663-3732394-155946236-0"));
                    break;
                case "buton_43H":
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://moovitapp.com/index/tr/toplu_ta%C5%9F%C4%B1ma-line-95A-Bursa-3663-3732394-155946236-0"));
                    break;
            }
            startActivity(intent);
        };
        kucuk_sanayi_git.setOnClickListener(listener);
        altinsehir_git.setOnClickListener(listener);
        btn_35E2.setOnClickListener(listener);
        btn_95A.setOnClickListener(listener);
        btn_43H.setOnClickListener(listener);

        //35E2 hattı
        new FetchBusTimesTask(new BusTimeCallback() {
            @Override
            public void onBusTimeFetched(ArrayList<String> en_yakin_saatler) {
                set_bus_texts(en_yakin_saatler, en_yakin_kalkis_35E2_1, en_yakin_kalkis_35E2_2, "Özlüce'den", "Küçük Sanayi İstasyonu'ndan");
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "https://www.ntv.com.tr/burulas-otobus-saatleri/35-e-2");

        //95/A hattı
        new FetchBusTimesTask(new BusTimeCallback() {
            @Override
            public void onBusTimeFetched(ArrayList<String> en_yakin_saatler) {
                set_bus_texts(en_yakin_saatler, en_yakin_kalkis_95A_1, en_yakin_kalkis_95A_2, "Terminal'den", "Üçevler Mahallesi'nden");
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "https://www.ntv.com.tr/burulas-otobus-saatleri/95-a");

        //43/H hattı
        new FetchBusTimesTask(new BusTimeCallback() {
            @Override
            public void onBusTimeFetched(ArrayList<String> en_yakin_saatler) {
                set_bus_texts(en_yakin_saatler, en_yakin_kalkis_43H_1, en_yakin_kalkis_43H_2, "Terminal'den", "Uludağ Üniversitesi'nden");
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "https://www.ntv.com.tr/burulas-otobus-saatleri/43-h");

    }

    private void set_bus_texts(ArrayList<String> en_yakin_saatler, TextView bus_button1, TextView bus_button2, String durak1, String durak2) {
        String saat1 = en_yakin_saatler.get(0);
        String saat2 = en_yakin_saatler.get(1);
        if (saat1 == "null"){
            bus_button1.setText(Html.fromHtml("→Bugün için <b>" + durak1 + "</b> olan tüm seferler bitti"));
        } else if (dakika_farki(saat1) == 0) {
            bus_button1.setText(Html.fromHtml("→<b>" + durak1 + "</b> şimdi kalktı"));
        } else if (dakika_farki(saat1) < 60) {
            bus_button1.setText(Html.fromHtml("→<b>" + dakika_farki(saat1) + " dakika</b> sonra <b>" + durak1 + "</b> kalkış"));
        } else {
            if (dakika_farki(saat1)%60 == 0) {
                bus_button1.setText(Html.fromHtml("→<b>" + dakika_farki(saat1)/60 + " saat sonra <b>" + durak1 + "</b> kalkış"));
            } else {
                bus_button1.setText(Html.fromHtml("→<b>" + dakika_farki(saat1)/60 + " saat " + dakika_farki(saat1)%60 + " dakika</b> sonra <b>" + durak1 + "</b> kalkış"));
            }
        }
        if (saat2 == "null"){
            bus_button2.setText(Html.fromHtml("→Bugün için <b>" + durak2 + "</b> olan tüm seferler bitti"));
        } else if (dakika_farki(saat2) == 0) {
            bus_button2.setText(Html.fromHtml("→<b>" + durak2 + "</b> şimdi kalktı"));
        }else if (dakika_farki(saat2) < 60) {
            bus_button2.setText(Html.fromHtml("→<b>" + dakika_farki(saat2) + " dakika</b> sonra <b>" + durak2 + "</b> kalkış"));
        } else {
            if (dakika_farki(saat2)%60 == 0) {
                bus_button2.setText(Html.fromHtml("→<b>" + dakika_farki(saat2)/60 + " saat sonra <b>" + durak2 + "</b> kalkış"));
            } else {
                bus_button2.setText(Html.fromHtml("→<b>" + dakika_farki(saat2)/60 + " saat " + dakika_farki(saat2)%60 + " dakika</b> sonra <b>" + durak2 + "</b> kalkış"));
            }
        }
    }

    private int dakika_farki(String saat) {
        int saat1 = Integer.parseInt(saat.split(":")[0]);
        int dakika1 = Integer.parseInt(saat.split(":")[1]);
        Calendar cal = Calendar.getInstance();
        int guncel_saat = cal.get(Calendar.HOUR_OF_DAY);
        int guncel_dakika = cal.get(Calendar.MINUTE);
        int total_dakika1 = 60*saat1 + dakika1;
        int guncel_total_dakika = 60*guncel_saat + guncel_dakika;

        return total_dakika1 - guncel_total_dakika;
    }
}

class FetchBusTimesTask extends AsyncTask<String, Void, ArrayList<String>> {
    private BusTimeCallback callback;

    public FetchBusTimesTask(BusTimeCallback callback) {
        this.callback = callback;
    }

    @Override
    protected ArrayList<String> doInBackground(String... urls) {
        try {
            Document doc = Jsoup.connect(urls[0]).get();
            HashMap<String, ArrayList<String>> hatlar_ilkson = new HashMap<>();
            hatlar_ilkson.put("35E2", new ArrayList<>(Arrays.asList("Küçük Sanayi İstasyonu 4", "Özlüce Cami 2")));
            hatlar_ilkson.put("95A", new ArrayList<>(Arrays.asList("Üçevler", "Terminal Peron 4")));

            ArrayList<String> hat_isimleri = new ArrayList<>();
            hat_isimleri.add("35E2");
            hat_isimleri.add("95A");

            ArrayList<String> saatler = new ArrayList<>();
            ArrayList<String> en_yakin_saatler = new ArrayList<>();

            for (Element table : doc.select(".bus-services__hours--table")) {
                saatler.clear();
                for (Element tr : table.select("table > tbody > tr")) {
                    String is_gunu = tr.getAllElements().get(1).text();
                    String cmt = tr.getAllElements().get(2).text();
                    String pzr = tr.getAllElements().get(3).text();
                    if (Calendar.DAY_OF_WEEK <= 5) {
                        if (is_gunu.length() != 0) {
                            saatler.add(is_gunu);
                        }
                    } else if (Calendar.DAY_OF_WEEK == 6) {
                        if (cmt.length() != 0) {
                            saatler.add(cmt);
                        }
                    } else {
                        if (pzr.length() != 0) {
                            saatler.add(pzr);
                        }
                    }
                }
                en_yakin_saatler.add(en_yakin_kalkis(saatler));
            }
            return en_yakin_saatler;
        } catch (IOException e) {
            Log.d("errorrrrrr", e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<String> en_yakin_saatler) {
        if (callback != null && en_yakin_saatler != null) {
            callback.onBusTimeFetched(en_yakin_saatler);  // Callback ile sonucu dönüyoruz
        }
    }

    private String en_yakin_kalkis(ArrayList<String> saatler) {
        int min_saat_farki = Integer.MAX_VALUE;
        String en_yakin_saat = "null";
        for (String saat : saatler) {
            int saat_kacta = Integer.parseInt(saat.split(":")[0]);
            int dakika_kacta = Integer.parseInt(saat.split(":")[1]);
            int kalkis_total_dakika = 60 * saat_kacta + dakika_kacta;
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int guncel_saat_total_dakika = 60 * hour + minute;
            int saat_farki = kalkis_total_dakika - guncel_saat_total_dakika;

            if (saat_farki > 0 && saat_farki < min_saat_farki) {
                min_saat_farki = saat_farki;
                en_yakin_saat = saat;
            }
        }
        return en_yakin_saat;
    }
}