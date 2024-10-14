package mert.kadakal.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AnasayfaFragment extends Fragment implements View.OnClickListener {

    private Button muhtarlik;
    private Button hava_durumu;
    private Button toplu_ulasim;
    private Button taksi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment layout inflate
        return inflater.inflate(R.layout.fragment_anasayfa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        muhtarlik = view.findViewById(R.id.muhtarlik);
        hava_durumu = view.findViewById(R.id.hava_durumu);
        toplu_ulasim = view.findViewById(R.id.toplu_ulasim);
        taksi = view.findViewById(R.id.taksi);

        // Tek bir OnClickListener ile bütün butonlar için tıklama olaylarını ayarlıyoruz
        muhtarlik.setOnClickListener(this);
        hava_durumu.setOnClickListener(this);
        toplu_ulasim.setOnClickListener(this);
        taksi.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Tıklanan butona göre ilgili aktiviteyi başlatan yapı
        if (view.getId() == R.id.muhtarlik) {
            startListener(Muhtarlik.class);
        } else if (view.getId() == R.id.hava_durumu) {
            startListener(HavaDurumu.class);
        } else if (view.getId() == R.id.toplu_ulasim) {
            startListener(Toplu_ulasim.class);
        } else if (view.getId() == R.id.taksi) {
            startListener(Taksi.class);
        }
    }

    // Belirtilen sınıfı başlatan metod
    private void startListener(Class<?> activityClass) {
        Intent intent = new Intent(getContext(), activityClass);
        startActivity(intent);
    }
}
