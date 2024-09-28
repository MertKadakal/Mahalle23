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
        if (convertView == null) {
            if (liste_ismi.equals("haberler")) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.haberler, parent, false);
                TextView haber_sayfasi_text = convertView.findViewById(R.id.haber_sayfasi);
                String[] parts = getItem(position).split("<br>");
                if (parts.length >= 3) {  // Split işlemini güvenli hale getirdik
                    String htmlText = "<b>" + parts[1] + "</b><br><br>" + parts[2];
                    haber_sayfasi_text.setText(Html.fromHtml(htmlText));
                }
            } else if (liste_ismi.equals("yapılabilecekler")) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.yapilabilecekler, parent, false);
                TextView textView = convertView.findViewById(R.id.button);
                String htmlText = getItem(position);
                textView.setText(Html.fromHtml(htmlText));
            }
        } else {
            TextView textView = convertView.findViewById(R.id.button);
            String htmlText = getItem(position);
            textView.setText(Html.fromHtml(htmlText));
        }


        String htmlText = getItem(position);
        Button button = convertView.findViewById(R.id.button);
        button.setText(Html.fromHtml(getItem(position).split("<br>")[0]));

        // Butona tıklama olayını ekle
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liste_ismi.equals("haberler")) {
                    String[] parts = htmlText.split("<br>");
                    if (parts.length > 3) {  // URL var mı kontrolü yapıyoruz.
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(parts[3]));
                        getContext().startActivity(intent);
                    }
                } else {
                    switch (htmlText) {
                        case "Market alışverişi":
                            Intent intent = new Intent(getContext(), Marketler.class);
                            getContext().startActivity(intent);
                            break;
                        case "Fırın":
                            Activity activity = (Activity) getContext();
                            if (activity != null) {
                                requestLocationPermissionAndGetLocation(activity);  // Konum izin kontrolü ve alımı fonksiyona ayrıldı.
                            }
                            break;
                    }
                }
            }
        });

        return convertView;
    }

    private void requestLocationPermissionAndGetLocation(Activity activity) {
        // İzin kontrolü
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getLocation(activity);  // İzin verildiyse direkt konumu al
        }
    }

    private void getLocation(Activity activity) {
        // Konum bilgisi alınıyor
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    float[] results = new float[1]; // Mesafeyi saklamak için bir dizi
                    Location.distanceBetween(
                            latitude, longitude,   // İlk konumun enlem ve boylamı
                            40.20469861238746, 29.038291412807297,   // İkinci konumun enlem ve boylamı
                            results            // Sonuçları saklayacak dizi
                    );
                    float mesafeMetre = results[0]; // İki nokta arasındaki mesafe (metre cinsinden)
                    Toast.makeText(getContext(), "Mesafe: " + mesafeMetre/1000 + " kilometre", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Konum bilgisi alınamadı", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
