package mert.kadakal.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HtmlArrayAdapterYorumlar extends ArrayAdapter<String> {
    Button yorum_begen;
    Button yanıtları_gör;
    Button yorumu_yanıtla;
    Button yorum_sil;
    Button yorum_düzenle;
    ArrayList<String> id_list;
    String yanıt;

    public HtmlArrayAdapterYorumlar(Context context, int resource, List<String> items, ArrayList<String> id_list, String yanıt) {
        super(context, resource, items);
        this.id_list = id_list;
        this.yanıt = yanıt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.yorumlar, parent, false);
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPrefs", MODE_PRIVATE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextView textView = convertView.findViewById(R.id.yorumlar_item);
        TextView textView2 = convertView.findViewById(R.id.yorumlar_item2);

        yorum_sil = convertView.findViewById(R.id.yorumu_sil);
        yanıtları_gör = convertView.findViewById(R.id.yanıtları_gör_butonu);
        yorumu_yanıtla = convertView.findViewById(R.id.yoruma_yanit_ekle_butonu);
        yorum_düzenle = convertView.findViewById(R.id.yorumu_düzenle);
        yorum_begen = convertView.findViewById(R.id.yorum_begen_butonu);

        String isim = getItem(position).split("<kay>")[0];
        String yorum = getItem(position).split("<kay>")[1];
        String tarih = getItem(position).split("<kay>")[2];
        String beğeni_sayısı = getItem(position).split("<kay>")[3];
        textView.setText(Html.fromHtml(String.format("<b>%s</b><br>%s<br>", isim, tarih)));
        textView2.setText(Html.fromHtml(String.format("%s<br><br><b>%s</b> beğeni", yorum, beğeni_sayısı)));

        if (sharedPreferences.getBoolean("hesap_açık_mı", false)) {
            yorumu_yanıtla.setVisibility(View.VISIBLE);
            yorum_begen.setVisibility(View.VISIBLE);
        }

        if (sharedPreferences.getBoolean("hesap_açık_mı", false) && isim.equals(sharedPreferences.getString("hesap_ismi", "YOK"))) {
            yorum_sil.setVisibility(View.VISIBLE);
            yorum_düzenle.setVisibility(View.VISIBLE);
        }

        if (yanıt.equals("yanıt_yorumlar")) {
            yorumu_yanıtla.setVisibility(View.INVISIBLE);
            yorum_düzenle.setVisibility(View.INVISIBLE);
        }

        if (yanıt.equals("yorumlar")) {
            db.collection("yanıt_yorumlar").whereEqualTo("kimeId", id_list.get(position)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    int count = task.getResult().getDocuments().size();
                    if (count > 0) {
                        yanıtları_gör.setText("Yanıtlar ("+count+")");
                        yanıtları_gör.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        yorum_begen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(sharedPreferences.getBoolean("hesap_açık_mı", false))) {
                    Toast.makeText(getContext(), "Yorum beğenmek için oturum açmanız gerek", Toast.LENGTH_SHORT).show();
                } else {
                    db.collection("hesaplar")
                            .whereEqualTo("isim", sharedPreferences.getString("hesap_ismi", "YOK"))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                                        ArrayList<String> beğenilenler = (ArrayList<String>) document.get("beğenilen_yorumlar");

                                        if (beğenilenler == null) {
                                            beğenilenler = new ArrayList<>(); // Null kontrolü
                                        }

                                        if (beğenilenler.contains(id_list.get(position))) {
                                            Toast.makeText(getContext(), "Bu yorumu zaten beğenmişsiniz", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Yorum ID'sini "beğenilen_yorumlar" listesine ekle
                                            beğenilenler.add(id_list.get(position));

                                            db.collection("hesaplar").document(document.getId())
                                                    .update("beğenilen_yorumlar", beğenilenler)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(getContext(), "Beğenildi", Toast.LENGTH_SHORT).show();
                                                        // Ayrıca yorumun beğeni sayısını güncelle
                                                        updateLikeCount(id_list.get(position), db);
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.w("TAG", "Error updating document", e);
                                                    });
                                        }
                                    } else {
                                        Log.e("Firestore", "Sorgu başarısız: ", task.getException());
                                    }
                                }
                            });
                }
            }
        });

        yorum_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection(yanıt).whereEqualTo("yorumId", id_list.get(position)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        db.collection(yanıt).document(document.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Yorum silindi", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        yorum_düzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection(yanıt).whereEqualTo("yorumId", id_list.get(position)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        Intent intent = new Intent(getContext(), Yorum_ekle_düzenle_yanıtla.class);
                        intent.putExtra("ekle_düzenle", "düzenle");
                        intent.putExtra("mevcut_yorum", document.get("yorum").toString());
                        intent.putExtra("yorum_id", id_list.get(position));
                        getContext().startActivity(intent);
                    }
                });
            }
        });

        yorumu_yanıtla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection(yanıt).whereEqualTo("yorumId", id_list.get(position)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        Intent intent = new Intent(getContext(), Yorum_ekle_düzenle_yanıtla.class);
                        intent.putExtra("ekle_düzenle", "yanıtla");
                        intent.putExtra("kimeId", id_list.get(position));
                        intent.putExtra("yorum_yapılan_isim", (String) document.get("isim"));
                        getContext().startActivity(intent);
                    }
                });
            }
        });

        yanıtları_gör.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Yanıtlar.class);
                intent.putExtra("anaYorumId", id_list.get(position));
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private void updateLikeCount(String yorumId, FirebaseFirestore db) {
        db.collection(yanıt).document(yorumId)
                .update("beğeni_sayısı", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> Log.d("TAG", "Beğeni sayısı güncellendi."))
                .addOnFailureListener(e -> Log.w("TAG", "Error updating document", e));
    }
}
