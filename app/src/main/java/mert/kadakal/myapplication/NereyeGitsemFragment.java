package mert.kadakal.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class NereyeGitsemFragment extends Fragment {

    ArrayList<String> items;
    private ListView yapilacaklar_liste;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nereye_gitsem, container, false);

        yapilacaklar_liste = view.findViewById(R.id.yapilabilecekler_listesi);
        items = new ArrayList<>();
        items.add("Arkadaşlarla oturmak");
        items.add("Akşam yemeği");
        items.add("Kahvaltı");
        items.add("Kişisel bakım");
        items.add("Market alışverişi");
        items.add("Fırın");

        HtmlArrayAdapter adapter = new HtmlArrayAdapter(this.getContext(), R.layout.yapilabilecekler, items, "yapılabilecekler");
        yapilacaklar_liste.setAdapter(adapter);

        // ListView'e item click listener ekle
        yapilacaklar_liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    String selectedItem = items.get(position);
                    Toast.makeText(requireContext(), "Seçilen: " + selectedItem, Toast.LENGTH_LONG).show();
                    Log.d("NereyeGitsemFragment", "Seçilen öğe: " + selectedItem);
                } else {
                    Log.d("NereyeGitsemFragment", "View null");
                }
            }
        });

        return view;
    }

}
