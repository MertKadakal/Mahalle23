package mert.kadakal.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tartisma extends Fragment {
    Button yorum_ekle;
    ArrayList<String> yorumlar = new ArrayList<>();
    private ListView yorumlar_listesi;
    private ArrayList<String> yorum_id_listesi = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tartisma, container, false);
        yorum_ekle = view.findViewById(R.id.yorum_ekleme_butonu);
        yorum_ekle.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), Yorum_ekle.class);
            startActivity(intent);
        });

        yorumlar_listesi = view.findViewById(R.id.yorumlar_listesi);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("yorumlar")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                yorumlar.add(document.getString("isim") + "<kay>"
                                        + document.getString("yorum") + "<kay>"
                                        + document.getString("tarih") + "<kay>"
                                        + document.get("beğeni_sayısı"));
                                yorum_id_listesi.add(document.getString("yorumId"));
                            }
                            HtmlArrayAdapterYorumlar adapter = new HtmlArrayAdapterYorumlar(getContext(), R.layout.yorumlar, yorumlar, yorum_id_listesi);
                            yorumlar_listesi.setAdapter(adapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        return view;
    }
}