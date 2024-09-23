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

public class AnasayfaFragment extends Fragment {

    private Button muhtarlik;
    private Button hava_durumu;

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

        muhtarlik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Muhtarlik.class);
                startActivity(intent);
            }
        });
    }
}
