package mert.kadakal.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
        items.add("Halısaha");

        HtmlArrayAdapter adapter = new HtmlArrayAdapter(this.getContext(), R.layout.yapilabilecekler, items, "yapılabilecekler");
        yapilacaklar_liste.setAdapter(adapter);

        // ListView'e item click listener ekle
        yapilacaklar_liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    Intent intent = new Intent(getContext(), NereyeGitsem_SecilenKategori.class);
                    intent.putExtra("SEÇİLEN_KATEGORİ", items.get(position));
                    startActivity(intent);
                } else {
                    Log.d("NereyeGitsemFragment", "View null");
                }
            }
        });

        return view;
    }

}

//switch (htmlText) {
//        case "Market alışverişi":
//Intent intent = new Intent(getContext(), Marketler.class);
//getContext().startActivity(intent);
//                            break;
//                                    case "Fırın":
//Activity activity = (Activity) getContext();
//                            if (activity != null) {
//requestLocationPermissionAndGetLocation(activity);  // Konum izin kontrolü ve alımı fonksiyona ayrıldı.
//                            }
//                                    break;
//                                    }
