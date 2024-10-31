package mert.kadakal.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NereyeGitsemFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nereye_gitsem, container, false);

        int[] buttonIds = {
                R.id.akşam_yemeği, R.id.kahvaltı, R.id.kafe_restaurant, R.id.halısaha,
                R.id.market, R.id.fırın, R.id.kişisel_bakım, R.id.yürüyüş_oyun_parkı
        };

        View.OnClickListener commonClickListener = view1 -> {
            Intent intent = new Intent(getContext(), NereyeGitsem_SecilenKategori.class);
            intent.putExtra("SEÇİLEN_KATEGORİ", getResources().getResourceEntryName(view1.getId()));
            startActivity(intent);
        };

        for (int id : buttonIds) {
            view.findViewById(id).setOnClickListener(commonClickListener);
        }

        return view;
    }
}
