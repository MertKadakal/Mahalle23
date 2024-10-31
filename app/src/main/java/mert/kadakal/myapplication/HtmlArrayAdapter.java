package mert.kadakal.myapplication;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.Manifest;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class HtmlArrayAdapter extends ArrayAdapter<String> {
    String liste_ismi;
    FusedLocationProviderClient fusedLocationClient;

    public HtmlArrayAdapter(Context context, int resource, List<String> items, String liste_ismi) {
        super(context, resource, items);
        this.liste_ismi = liste_ismi;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context); // fusedLocationClient'ı burada oluşturduk.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (liste_ismi.equals("haberler")) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.haberler, parent, false);
            TextView haber_sayfasi = convertView.findViewById(R.id.haber_sayfasi);
            TextView haber_açıklaması = convertView.findViewById(R.id.haber_açıklaması);
            TextView haber_başlığı = convertView.findViewById(R.id.haber_başlığı);
            String[] parts = getItem(position).split("<br>");
            if (parts.length >= 3) {  // Split işlemini güvenli hale getir
                String htmlTextBaşlık = "<i>" + parts[0] + "</i>";
                haber_sayfasi.setText(Html.fromHtml(htmlTextBaşlık));

                String htmlTexthaber_açıklaması =parts[2];
                haber_açıklaması.setText(Html.fromHtml(htmlTexthaber_açıklaması));

                String htmlTexthaber_sayfasi =  "<b>" + parts[1] + "</b>";
                haber_başlığı.setText(Html.fromHtml(htmlTexthaber_sayfasi));
            }
        } else if (liste_ismi.equals("yapılabilecekler")) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.yapilabilecekler, parent, false);
            TextView textView = convertView.findViewById(R.id.nereye_gitsem_item);
            String htmlText = getItem(position);
            textView.setText(Html.fromHtml(htmlText));
        }
        return convertView;
    }
}