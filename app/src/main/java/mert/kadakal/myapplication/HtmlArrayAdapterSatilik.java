package mert.kadakal.myapplication;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class HtmlArrayAdapterSatilik extends ArrayAdapter<HashMap<String, String>> {
    private Context context;
    private int resource;
    private List<HashMap<String, String>> items;

    public HtmlArrayAdapterSatilik(Context context, int resource, List<HashMap<String, String>> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // LayoutInflater kullanarak satır görünümünü oluştur
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        // Görünüm elemanlarını tanımla
        TextView tbaslik = convertView.findViewById(R.id.başlık);
        TextView tebat = convertView.findViewById(R.id.ebat);
        TextView tkat = convertView.findViewById(R.id.kat);
        TextView ttarih = convertView.findViewById(R.id.tarih);
        TextView toda = convertView.findViewById(R.id.oda);
        TextView tfiyat = convertView.findViewById(R.id.fiyat);

        // Mevcut pozisyondaki öğeyi al
        HashMap<String, String> item = items.get(position);
        Log.d("tag", item.get("başlık"));

        // HashMap'teki değerleri al ve TextView'lara ata
        String baslik = item.get("başlık");
        String ebat = item.get("ebat");
        String kat = item.get("kat");
        String tarih = item.get("tarih");
        String oda = item.get("oda");
        String fiyat = item.get("fiyat");

        // HTML formatında olan metinleri TextView'lara yerleştir
        if (!baslik.isEmpty()) {
            tbaslik.setText(Html.fromHtml(baslik));
        }
        if (!ebat.isEmpty()) {
            tebat.setText(Html.fromHtml("<b>Ebat: </b>" + ebat));
        }
        if (!kat.isEmpty()) {
            tkat.setText(Html.fromHtml("<b>Kat: </b>" + kat));
        }
        if (!tarih.isEmpty()) {
            ttarih.setText(Html.fromHtml("<b>Tarih: </b>" + tarih));
        }
        if (!oda.isEmpty()) {
            toda.setText(Html.fromHtml("<b>Oda: </b>" + oda));
        }
        if (!fiyat.isEmpty()) {
            tfiyat.setText(Html.fromHtml(fiyat));
        }

        return convertView;
    }
}
