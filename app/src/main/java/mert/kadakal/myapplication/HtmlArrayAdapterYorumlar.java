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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        TextView yapılan_yorum_isim = convertView.findViewById(R.id.yapılan_yorum_isim);
        TextView yapılan_yorum_yorum = convertView.findViewById(R.id.yapılan_yorum_yorum);
        TextView yapılan_yorum_tarih = convertView.findViewById(R.id.yapılan_yorum_tarih);
        TextView beğeni_sayısı_yorum = convertView.findViewById(R.id.yorum_beğenen_sayısı);

        //yorum_sil = convertView.findViewById(R.id.yorumu_sil);
        //yorumu_yanıtla = convertView.findViewById(R.id.yoruma_yanit_ekle_butonu);
        //yorum_düzenle = convertView.findViewById(R.id.yorumu_düzenle);
        //yorum_begen = convertView.findViewById(R.id.yorum_begen_butonu);
        //yanıtları_gör = convertView.findViewById(R.id.yanıtları_gör_butonu);

        String isim = getItem(position).split("<kay>")[0];
        String yorum = getItem(position).split("<kay>")[1];
        String tarih = getItem(position).split("<kay>")[2];
        String beğeni_sayısı = getItem(position).split("<kay>")[3];

        yapılan_yorum_isim.setText("~"+isim);
        yapılan_yorum_yorum.setText(yorum);
        yapılan_yorum_tarih.setText(tarih);
        if (Integer.parseInt(beğeni_sayısı) > 0) {
            beğeni_sayısı_yorum.setText(Html.fromHtml(String.format("<b>%s</b> ♥", beğeni_sayısı)));
        }
/*
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
            yanıtları_gör.setVisibility(View.INVISIBLE);
        }

        yorum_begen.setOnClickListener(v -> {
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

                                if (beğenilenler.contains(id_list.get(position))) {
                                    Toast.makeText(getContext(), "Bu yorumu zaten beğenmişsiniz", Toast.LENGTH_SHORT).show();
                                } else {
                                    beğenilenler.add(id_list.get(position));
                                    db.collection("hesaplar").document(document.getId())
                                            .update("beğenilen_yorumlar", beğenilenler)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(getContext(), "Beğenildi", Toast.LENGTH_SHORT).show();
                                                updateLikeCount(id_list.get(position), db);
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
        });

        yorum_sil.setOnClickListener(view -> {
            db.collection(yanıt).whereEqualTo("yorumId", id_list.get(position)).get().addOnCompleteListener(task -> {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                db.collection(yanıt).document(document.getId()).delete().addOnCompleteListener(task1 -> {
                    Toast.makeText(getContext(), "Yorum silindi", Toast.LENGTH_SHORT).show();
                });
            });
        });

        yorum_düzenle.setOnClickListener(view -> {
            db.collection(yanıt).whereEqualTo("yorumId", id_list.get(position)).get().addOnCompleteListener(task -> {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                Intent intent = new Intent(getContext(), Yorum_ekle_düzenle_yanıtla.class);
                intent.putExtra("ekle_düzenle", "düzenle");
                intent.putExtra("mevcut_yorum", document.get("yorum").toString());
                intent.putExtra("yorum_id", id_list.get(position));
                getContext().startActivity(intent);
            });
        });

        yorumu_yanıtla.setOnClickListener(view -> {
            db.collection(yanıt).whereEqualTo("yorumId", id_list.get(position)).get().addOnCompleteListener(task -> {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                Intent intent = new Intent(getContext(), Yorum_ekle_düzenle_yanıtla.class);
                intent.putExtra("ekle_düzenle", "yanıtla");
                intent.putExtra("kimeId", id_list.get(position));
                intent.putExtra("yorum_yapılan_isim", (String) document.get("isim"));
                getContext().startActivity(intent);
            });
        });

        yanıtları_gör.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), Yanıtlar.class);
            intent.putExtra("anaYorumId", id_list.get(position));

            getContext().startActivity(intent);
        });*/

        return convertView;
    }

    private void updateLikeCount(String yorumId, FirebaseFirestore db) {
        db.collection(yanıt).document(yorumId)
                .update("beğeni_sayısı", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> Log.d("TAG", "Beğeni sayısı güncellendi."))
                .addOnFailureListener(e -> Log.w("TAG", "Error updating document", e));
    }
}
