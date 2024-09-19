package mert.kadakal.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
        // Fragment'ın layout dosyasını şişir
        return inflater.inflate(R.layout.fragment_nereye_gitsem, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yapilacaklar_liste = view.findViewById(R.id.yapılabilecekler_listesi);
        items = new ArrayList<>();
        items.add("Arkadaşlarla oturmak");
        items.add("Akşam yemeği");
        items.add("Kahvaltı");
        items.add("Kişisel bakım");
        items.add("Market alışverişi");
        items.add("Fırın");
        items.add("Fırın");
        items.add("Fırın");
        items.add("Fırın");
        items.add("Fırın");
        items.add("Fırın");
        items.add("Fırın");
        HtmlArrayAdapter adapter = new HtmlArrayAdapter(this.getContext(), R.layout.yapilabilecekler, items);
        yapilacaklar_liste.setAdapter(adapter);

    }
}
