package mert.kadakal.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Tartisma extends Fragment {
    Button yorum_ekle;
    Button çıkış_yap;
    Button hesap_oluştur;
    Button giriş_yap;
    TextView hesap_islemleri;
    TextView yukleniyor;
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
        yukleniyor = view.findViewById(R.id.yukleniyor_tartisma);
        hesap_islemleri = view.findViewById(R.id.hesap_islemleri);

        hesap_islemleri.setOnClickListener(view1 -> {
            ArrayList<String> opsiyonlar = new ArrayList<>();
            if (sharedPreferences.getBoolean("hesap_açık_mı", false)) {
                opsiyonlar.add("Yorum ekle");
                opsiyonlar.add("Çıkış yap");
            } else {
                opsiyonlar.add("Hesap oluştur");
                opsiyonlar.add("Giriş yap");
            }

            final String[] options = opsiyonlar.toArray(new String[0]);

            // Popup (AlertDialog)
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Ne yapmak istiyorsunuz?");
            builder.setItems(options, (dialog, which) -> {
                String selected = options[which];

                if (selected.equals("Hesap oluştur")) {
                    Intent intent = new Intent(getContext(), Hesap_oluştur_giriş.class);
                    intent.putExtra("oluştur_giriş", "Oluştur");
                    startActivity(intent);
                }

                if (selected.equals("Giriş yap")) {
                    Intent intent = new Intent(getContext(), Hesap_oluştur_giriş.class);
                    intent.putExtra("oluştur_giriş", "Giriş yap");
                    startActivity(intent);
                }

                if (selected.equals("Yorum ekle")) {
                    Intent intent = new Intent(getContext(), Yorum_ekle_düzenle_yanıtla.class);
                    intent.putExtra("ekle_düzenle", "ekle");
                    startActivity(intent);
                }

                if (selected.equals("Çıkış yap")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("hesap_açık_mı", false);
                    editor.apply();
                    Toast.makeText(getContext(), "Hesaptan çıkış yapıldı", Toast.LENGTH_SHORT).show();
                }
            });

            builder.show();
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
                                yorumlar.add(document.getString("isim") + "<kay>"
                                        + document.getString("yorum") + "<kay>"
                                        + document.getString("tarih") + "<kay>"
                                        + document.get("beğeni_sayısı"));
                                yorum_id_listesi.add(document.getString("yorumId"));
                            }
                            if (yorumlar.size() == 0) {
                                yukleniyor.setText("Henüz yorum yok");
                            } else {
                                yukleniyor.setVisibility(View.INVISIBLE);
                                HtmlArrayAdapterYorumlar adapter = new HtmlArrayAdapterYorumlar(getContext(), R.layout.yorumlar, yorumlar, yorum_id_listesi, "yorumlar");
                                yorumlar_listesi.setAdapter(adapter);
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        yorumlar_listesi.setOnItemClickListener((parent, View, position, id) -> {
            String clickedItem = yorumlar.get(position);
            Log.d("TAG", String.valueOf(yorum_id_listesi));

            ArrayList<String> opsiyonlar = new ArrayList<>();
            opsiyonlar.add("Beğen");
            opsiyonlar.add("Yanıtları gör");

            if (sharedPreferences.getBoolean("hesap_açık_mı", false)) {
                opsiyonlar.add("Yanıtla");

                if (sharedPreferences.getString("hesap_ismi", "").equals(clickedItem.split("<kay>")[0])) {
                    opsiyonlar.add("Düzenle");
                    opsiyonlar.add("Sil");
                }
            }

            final String[] options = opsiyonlar.toArray(new String[0]);

            // Popup (AlertDialog)
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Ne yapmak istiyorsunuz?");
            builder.setItems(options, (dialog, which) -> {
                String selected = options[which];

                //silme
                if (selected.equals("Sil")) {
                    db.collection("yorumlar")
                            .whereEqualTo("yorumId", yorum_id_listesi.get(position))
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                                    db.collection("yorumlar")
                                            .document(document.getId())
                                            .delete()
                                            .addOnCompleteListener(task1 ->
                                                    Toast.makeText(getContext(), "Yorum silindi", Toast.LENGTH_SHORT).show()
                                            );
                                }
                            });
                }

                //beğenme
                if (selected.equals("Beğen")) {
                    if (!(sharedPreferences.getBoolean("hesap_açık_mı", false))) {
                        Toast.makeText(getContext(), "Yorum beğenmek için oturum açmanız gerek", Toast.LENGTH_SHORT).show();
                    } else {
                        db.collection("hesaplar")
                                .whereEqualTo("isim", sharedPreferences.getString("hesap_ismi", "YOK"))
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                                        ArrayList<String> beğenilenler = (ArrayList<String>) document.get("beğenilen_yorumlar");

                                        if (beğenilenler == null) {
                                            beğenilenler = new ArrayList<>();
                                        }

                                        if (beğenilenler.contains(yorum_id_listesi.get(position))) {
                                            Toast.makeText(getContext(), "Bu yorumu zaten beğenmişsiniz", Toast.LENGTH_SHORT).show();
                                        } else {
                                            beğenilenler.add(yorum_id_listesi.get(position));
                                            db.collection("hesaplar").document(document.getId())
                                                    .update("beğenilen_yorumlar", beğenilenler)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(getContext(), "Beğenildi", Toast.LENGTH_SHORT).show();
                                                        updateLikeCount(yorum_id_listesi.get(position), db);
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.w("TAG", "Error updating document", e);
                                                    });
                                        }
                                    } else {
                                        Log.e("Firestore", "Sorgu başarısız: ", task.getException());
                                    }
                                });
                    }
                }

                //düzenleme
                if (selected.equals("Düzenle")) {
                    db.collection("yorumlar").whereEqualTo("yorumId", yorum_id_listesi.get(position)).get().addOnCompleteListener(task -> {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        Intent intent = new Intent(getContext(), Yorum_ekle_düzenle_yanıtla.class);
                        intent.putExtra("ekle_düzenle", "düzenle");
                        intent.putExtra("mevcut_yorum", document.get("yorum").toString());
                        intent.putExtra("yorum_id", yorum_id_listesi.get(position));
                        getContext().startActivity(intent);
                    });
                }

                //yanıtla
                if (selected.equals("Yanıtla")) {
                    db.collection("yorumlar").whereEqualTo("yorumId", yorum_id_listesi.get(position)).get().addOnCompleteListener(task -> {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        Intent intent = new Intent(getContext(), Yorum_ekle_düzenle_yanıtla.class);
                        intent.putExtra("ekle_düzenle", "yanıtla");
                        intent.putExtra("kimeId", yorum_id_listesi.get(position));
                        intent.putExtra("yorum_yapılan_isim", (String) document.get("isim"));
                        getContext().startActivity(intent);
                    });
                }

                //yanıtları gör
                if (selected.equals("Yanıtları gör")) {
                    Intent intent = new Intent(getContext(), Yanıtlar.class);
                    intent.putExtra("anaYorumId", yorum_id_listesi.get(position));

                    getContext().startActivity(intent);
                }
            });
            builder.show();
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //yorum_ekle.setVisibility(View.INVISIBLE);
        //hesap_oluştur.setVisibility(View.INVISIBLE);
        //giriş_yap.setVisibility(View.INVISIBLE);
        //çıkış_yap.setVisibility(View.INVISIBLE);
        //if (!sharedPreferences.getBoolean("hesap_açık_mı", false)) {
        //    hesap_oluştur.setVisibility(View.VISIBLE);
        //    giriş_yap.setVisibility(View.VISIBLE);
        //} else {
        //    yorum_ekle.setVisibility(View.VISIBLE);
        //    çıkış_yap.setVisibility(View.VISIBLE);
        //}
    }

    private void updateLikeCount(String yorumId, FirebaseFirestore db) {
        db.collection("yorumlar").document(yorumId)
                .update("beğeni_sayısı", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> Log.d("TAG", "Beğeni sayısı güncellendi."))
                .addOnFailureListener(e -> Log.w("TAG", "Error updating document", e));
    }
}
