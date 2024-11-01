package mert.kadakal.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class HaberlerFragment extends Fragment {

    ArrayList<String> items;
    private ListView haberler_listesi;
    private TextView yukleniyor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment layout inflate
        return inflater.inflate(R.layout.fragment_haberler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        haberler_listesi = view.findViewById(R.id.haberler_listesi);
        yukleniyor = view.findViewById(R.id.yukleniyor);
        items = new ArrayList<>();

        haberler_listesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] parts = items.get(i).split("<br>");
                if (parts.length > 3) {  // URL var mı kontrolü yapıyoruz.
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(parts[3]));
                    getContext().startActivity(intent);
                }
            }
        });

        // Trigger AsyncTask to fetch news data in the background
        new FetchNewsTask().execute("https://www.google.com/search?q=bursa+23+nisan+%22mahallesi%22&sca_esv=cdf69acd155fddf3&sca_upv=1&rlz=1C1NDCM_enTR1036TR1036&biw=1536&bih=695&tbm=nws&sxsrf=ADLYWIIzQuPZTYitT1cClD2BE4EqApzD3g%3A1726767805848&ei=vWLsZvC6M--J7NYPm_uUgQg&ved=0ahUKEwiwjbWtx8-IAxXvBNsEHZs9JYAQ4dUDCA0&uact=5&oq=bursa+23+nisan+%22mahallesi%22&gs_lp=Egxnd3Mtd2l6LW5ld3MiGmJ1cnNhIDIzIG5pc2FuICJtYWhhbGxlc2kiMgUQABiABDIFEAAYgAQyBRAAGIAEMgUQABiABDIFEAAYgAQyBRAAGIAEMgUQABiABDIFEAAYgAQyBRAAGIAEMgUQABiABEjqDFC4BljJCXAAeACQAQCYAbsGoAGXCKoBBzAuMi42LTG4AQPIAQD4AQGYAgKgAvABwgIKEAAYgAQYQxiKBZgDAIgGAZIHAzAuMqAH1xM&sclient=gws-wiz-news");
    }

    private class FetchNewsTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... urls) {

            ArrayList<String> titles = new ArrayList<>();
            try {
                // Fetch the web page using Jsoup
                Document doc = Jsoup.connect(urls[0]).get();

                Elements elements;
                elements = doc.select(".MgUUmf.NUnG9d > span");
                for (Element element : elements) {
                    String baslik = element.text();
                    titles.add(baslik);
                }

                elements = doc.select(".n0jPhd.ynAwRc.MBeuO.nDgy9d");

                for (Element element : elements) {
                    String sayfa = element.text();
                    titles.add(sayfa);
                }

                elements = doc.select(".GI74Re.nDgy9d");

                for (Element element : elements) {
                    String aciklama = element.text();
                    Log.d("tag", aciklama);
                    titles.add(aciklama);
                }

                elements = doc.select(".SoaBEf");

                for (Element element : elements) {
                    String link = element.select("a").attr("href");
                    titles.add(link);
                }

            } catch (IOException e) {
                Log.d("bruh", e.toString());
            }
            yukleniyor.setVisibility(View.INVISIBLE);
            return titles;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            // Update the ListView with the fetched data
            if (result != null) {
                for (int i=0; i<result.size()/4; i++) {
                    items.add(String.format("%s<br>%s<br>%s<br>%s", result.get(i), result.get(i+(result.size()/4)), result.get(i+2*(result.size()/4)), result.get(i+3*(result.size()/4))));
                }
                HtmlArrayAdapter adapter = new HtmlArrayAdapter(getContext(), R.layout.haberler, items, "haberler");
                haberler_listesi.setAdapter(adapter);
            }
        }
    }
}
