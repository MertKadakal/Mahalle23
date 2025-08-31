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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Yanıtlar extends AppCompatActivity {
    ArrayList<String> yorumlar = new ArrayList<>();
    TextView hnzyrmyok;
    private ListView yorumlar_listesi;
    private ArrayList<String> yorum_id_listesi = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yanitlar);

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        yorumlar_listesi = findViewById(R.id.yorumlar_listesi);
        hnzyrmyok = findViewById(R.id.textView2);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("yanıt_yorumlar")
                .whereEqualTo("kimeId", getIntent().getStringExtra("anaYorumId"))
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
                            if (yorumlar.size() > 0) {
                                HtmlArrayAdapterYorumlar adapter = new HtmlArrayAdapterYorumlar(getApplicationContext(), R.layout.yorumlar, yorumlar, yorum_id_listesi, "yanıt_yorumlar");
                                yorumlar_listesi.setAdapter(adapter);
                            } else {
                                hnzyrmyok.setVisibility(View.VISIBLE);
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
                    opsiyonlar.add("Sil");
                }
            }

            final String[] options = opsiyonlar.toArray(new String[0]);

            // Popup (AlertDialog)
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ne yapmak istiyorsunuz?");
            builder.setItems(options, (dialog, which) -> {
                String selected = options[which];

                //silme
                if (selected.equals("Sil")) {
                    db.collection("yanıt_yorumlar")
                            .whereEqualTo("yorumId", yorum_id_listesi.get(position))
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                                    db.collection("yanıt_yorumlar")
                                            .document(document.getId())
                                            .delete()
                                            .addOnCompleteListener(task1 ->
                                                    Toast.makeText(this, "Yorum silindi", Toast.LENGTH_SHORT).show()
                                            );
                                }
                            });
                }

                //beğenme
                if (selected.equals("Beğen")) {
                    if (!(sharedPreferences.getBoolean("hesap_açık_mı", false))) {
                        Toast.makeText(this, "Yorum beğenmek için oturum açmanız gerek", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(this, "Bu yorumu zaten beğenmişsiniz", Toast.LENGTH_SHORT).show();
                                        } else {
                                            beğenilenler.add(yorum_id_listesi.get(position));
                                            db.collection("hesaplar").document(document.getId())
                                                    .update("beğenilen_yorumlar", beğenilenler)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(this, "Beğenildi", Toast.LENGTH_SHORT).show();
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
                    db.collection("yanıt_yorumlar").whereEqualTo("yorumId", yorum_id_listesi.get(position)).get().addOnCompleteListener(task -> {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        Intent intent = new Intent(this, Yorum_ekle_düzenle_yanıtla.class);
                        intent.putExtra("ekle_düzenle", "düzenle");
                        intent.putExtra("mevcut_yorum", document.get("yorum").toString());
                        intent.putExtra("yorum_id", yorum_id_listesi.get(position));
                        this.startActivity(intent);
                    });
                }

                //yanıtla
                if (selected.equals("Yanıtla")) {
                    db.collection("yanıt_yorumlar").whereEqualTo("yorumId", yorum_id_listesi.get(position)).get().addOnCompleteListener(task -> {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        Intent intent = new Intent(this, Yorum_ekle_düzenle_yanıtla.class);
                        intent.putExtra("ekle_düzenle", "yanıtla");
                        intent.putExtra("kimeId", yorum_id_listesi.get(position));
                        intent.putExtra("yorum_yapılan_isim", (String) document.get("isim"));
                        this.startActivity(intent);
                    });
                }

                //yanıtları gör
                if (selected.equals("Yanıtları gör")) {
                    Intent intent = new Intent(this, Yanıtlar.class);
                    intent.putExtra("anaYorumId", yorum_id_listesi.get(position));

                    this.startActivity(intent);
                }
            });
            builder.show();
        });

    }

    private void updateLikeCount(String yorumId, FirebaseFirestore db) {
        db.collection("yanıt_yorumlar").document(yorumId)
                .update("beğeni_sayısı", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> Log.d("TAG", "Beğeni sayısı güncellendi."))
                .addOnFailureListener(e -> Log.w("TAG", "Error updating document", e));
    }
}