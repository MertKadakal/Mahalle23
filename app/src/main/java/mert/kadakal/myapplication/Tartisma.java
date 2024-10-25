package mert.kadakal.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Tartisma extends Fragment {
    Button yorum_ekle;
    Button çıkış_yap;
    Button hesap_oluştur;
    Button giriş_yap;
    ArrayList<String> yorumlar = new ArrayList<>();
    private ListView yorumlar_listesi;
    private ArrayList<String> yorum_id_listesi = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tartisma, container, false);
        sharedPreferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        yorum_ekle = view.findViewById(R.id.yorum_ekleme_butonu);
        çıkış_yap = view.findViewById(R.id.hesaptan_cikis_butonu);
        hesap_oluştur = view.findViewById(R.id.hesap_oluştur);
        giriş_yap = view.findViewById(R.id.giriş_yap);
        yorum_ekle.setVisibility(View.INVISIBLE);
        çıkış_yap.setVisibility(View.INVISIBLE);
        hesap_oluştur.setVisibility(View.VISIBLE);
        giriş_yap.setVisibility(View.VISIBLE);

        yorum_ekle.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), Yorum_ekle_düzenle_yanıtla.class);
            intent.putExtra("ekle_düzenle", "ekle");
            startActivity(intent);
        });
        hesap_oluştur.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), Hesap_oluştur_giriş.class);
            intent.putExtra("oluştur_giriş", "Oluştur");
            startActivity(intent);
        });
        giriş_yap.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), Hesap_oluştur_giriş.class);
            intent.putExtra("oluştur_giriş", "Giriş yap");
            startActivity(intent);
        });
        çıkış_yap.setOnClickListener(view1 -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hesap_açık_mı", false);
            editor.apply();
            Toast.makeText(getContext(), "Hesaptan çıkış yapıldı", Toast.LENGTH_SHORT).show();
        });

        //hesabın açık olup olmamasına göre alttaki butonları göster
        if (sharedPreferences.getBoolean("hesap_açık_mı", false)) {
            hesap_oluştur.setVisibility(View.VISIBLE);
            giriş_yap.setVisibility(View.VISIBLE);
        } else {
            yorum_ekle.setVisibility(View.VISIBLE);
            çıkış_yap.setVisibility(View.VISIBLE);
        }

        yorumlar_listesi = view.findViewById(R.id.yorumlar_listesi);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("yorumlar")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
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

    @Override
    public void onStart() {
        super.onStart();
        yorum_ekle.setVisibility(View.INVISIBLE);
        hesap_oluştur.setVisibility(View.INVISIBLE);
        giriş_yap.setVisibility(View.INVISIBLE);
        çıkış_yap.setVisibility(View.INVISIBLE);
        if (!sharedPreferences.getBoolean("hesap_açık_mı", false)) {
            hesap_oluştur.setVisibility(View.VISIBLE);
            giriş_yap.setVisibility(View.VISIBLE);
        } else {
            yorum_ekle.setVisibility(View.VISIBLE);
            çıkış_yap.setVisibility(View.VISIBLE);
        }
    }
}