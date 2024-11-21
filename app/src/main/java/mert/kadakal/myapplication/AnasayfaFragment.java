package mert.kadakal.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AnasayfaFragment extends Fragment implements View.OnClickListener {

    private LinearLayout muhtarlik;
    private LinearLayout hava_durumu;
    private LinearLayout toplu_ulasim;
    private LinearLayout taksi;
    private LinearLayout satilik;
    private LinearLayout kiralik;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment layout inflate
        return inflater.inflate(R.layout.fragment_anasayfa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hava_durumu = view.findViewById(R.id.hava_durumu);
        toplu_ulasim = view.findViewById(R.id.toplu_ulaşım);
        taksi = view.findViewById(R.id.taksi);
        satilik = view.findViewById(R.id.satılık_daireler);
        kiralik = view.findViewById(R.id.kiralık_daireler);
        muhtarlik = view.findViewById(R.id.muhtarlık);

        hava_durumu.setOnClickListener(this);
        toplu_ulasim.setOnClickListener(this);
        satilik.setOnClickListener(this);
        kiralik.setOnClickListener(this);
        taksi.setOnClickListener(view12 -> {
            // Telefon numarasını tanımlayın
            String phoneNumber = "tel:+905300374727";
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(phoneNumber));
            startActivity(intent);
        });
        muhtarlik.setOnClickListener(view1 -> {
            // Telefon numarasını tanımlayın
            String phoneNumber = "tel:+905417728890";
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(phoneNumber));
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View view) {
        // Tıklanan butona göre ilgili aktiviteyi başlatan yapı
        if (view.getId() == R.id.hava_durumu) {
            startListener(HavaDurumu.class);
        } else if (view.getId() == R.id.toplu_ulaşım) {
            startListener(Toplu_ulasim.class);
        } else if (view.getId() == R.id.satılık_daireler) {
            Intent intent = new Intent(getContext(), SatilikDaireler.class);
            intent.putExtra("ilan_türü", "satilik");
            startActivity(intent);
        } else if (view.getId() == R.id.kiralık_daireler) {
            Intent intent = new Intent(getContext(), SatilikDaireler.class);
            intent.putExtra("ilan_türü", "kiralik");
            startActivity(intent);
        }
    }

    // Belirtilen sınıfı başlatan metod
    private void startListener(Class<?> activityClass) {
        Intent intent = new Intent(getContext(), activityClass);
        startActivity(intent);
    }
}