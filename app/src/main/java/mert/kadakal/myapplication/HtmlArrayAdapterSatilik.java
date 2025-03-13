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
        TextView tfiyat = convertView.findViewById(R.id.fiyat);
        TextView tözellikler = convertView.findViewById(R.id.özellikler);

        // Mevcut pozisyondaki öğeyi al
        HashMap<String, String> item = items.get(position);

        // HashMap'teki değerleri al ve TextView'lara ata
        String baslik = item.get("başlık");
        String özellikler = item.get("özellikler");
        String fiyat = item.get("fiyat");

        // HTML formatında olan metinleri TextView'lara yerleştir
        if (!baslik.isEmpty()) {
            tbaslik.setText(Html.fromHtml(baslik));
        }
        if (!özellikler.isEmpty()) {
            tözellikler.setText(Html.fromHtml(özellikler));
        }
        if (!fiyat.isEmpty()) {
            tfiyat.setText(Html.fromHtml(fiyat));
        }

        return convertView;
    }
}
