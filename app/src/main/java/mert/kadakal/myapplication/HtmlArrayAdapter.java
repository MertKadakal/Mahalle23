package mert.kadakal.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HtmlArrayAdapter extends ArrayAdapter<String> {
    String liste_ismi;
    public HtmlArrayAdapter(Context context, int resource, List<String> items, String liste_ismi) {
        super(context, resource, items);
        this.liste_ismi = liste_ismi;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (liste_ismi.equals("haberler")) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.haberler, parent, false);
                TextView haber_sayfasi_text = convertView.findViewById(R.id.haber_sayfasi);
                String htmlText = "<b>"+getItem(position).split("<br>")[1]+"</b><br><br>" + getItem(position).split("<br>")[2];
                haber_sayfasi_text.setText(Html.fromHtml(htmlText));
                TextView textView = convertView.findViewById(R.id.button);
                textView.setText(Html.fromHtml(getItem(position).split("<br>")[0]));
            } else if (liste_ismi.equals("yapılabilecekler")) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.yapilabilecekler, parent, false);
                TextView textView = convertView.findViewById(R.id.button);
                String htmlText = getItem(position);
                textView.setText(Html.fromHtml(htmlText));
            }
        } else {
            // convertView null değilse, mevcut öğeyi güncelle
            TextView textView = convertView.findViewById(R.id.button);
            String htmlText = getItem(position);
            textView.setText(Html.fromHtml(htmlText));
        }

        Button button = convertView.findViewById(R.id.button);
        String htmlText = "";
        if (liste_ismi.equals("yapılabilecekler")) {
            htmlText = getItem(position);
        } else if (liste_ismi.equals("haberler")) {
            htmlText = getItem(position).split("<br>")[0];
        }
        button.setText(Html.fromHtml(htmlText));

        // Butona tıklama olayını ekle
        String finalHtmlText = getItem(position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(finalHtmlText.split("<br>")[3]));
                getContext().startActivity(intent);

            }
        });
        return convertView;
    }

}
