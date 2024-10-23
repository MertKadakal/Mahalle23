package mert.kadakal.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Yorum_ekle extends AppCompatActivity {

    TextView isim;
    EditText yorum;
    Button tamam;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yorum_ekle);
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        isim = findViewById(R.id.isim_olarak_yorum_yap); isim.setText(Html.fromHtml("<b>"+sharedPreferences.getString("hesap_ismi", "YOK")+"</b> olarak yorum ekliyorsunuz"));
        yorum = findViewById(R.id.yorum);
        tamam = findViewById(R.id.ekle);

        Date tarih = new Date();
        SimpleDateFormat tarihFormati = new SimpleDateFormat("dd MMMM yyyy", new Locale("tr", "TR"));
        String tarihStr = tarihFormati.format(tarih);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        tamam.setOnClickListener(view -> {
            if (yorum.getText().toString().length() == 0) {
                Toast.makeText(this, "Lütfen gerekli alanları doldurunuz", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, Object> eklenen_yorum = new HashMap<>();
                eklenen_yorum.put("isim", sharedPreferences.getString("hesap_ismi", "YOK"));
                eklenen_yorum.put("yorum", yorum.getText().toString());
                eklenen_yorum.put("tarih", tarihStr);
                eklenen_yorum.put("beğeni_sayısı", 0);
                eklenen_yorum.put("yorumId", "0");
                db.collection("yorumlar").add(eklenen_yorum).addOnSuccessListener(documentReference -> {
                            // Belge başarıyla oluşturuldu, ID'yi alın
                            String yorumId = documentReference.getId();

                            // ID'yi belgeye ekleyin
                            db.collection("yorumlar").document(yorumId)
                                    .update("yorumId", yorumId) // ID'yi bir alan olarak ekle
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("TAG", "Yorum başarıyla oluşturuldu ve ID eklendi: " + yorumId);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("TAG", "ID eklenirken hata oluştu", e);
                                    });
                        })
                        .addOnFailureListener(e -> {
                            Log.w("TAG", "Belge oluşturulurken hata oluştu", e);
                        });

                Toast.makeText(Yorum_ekle.this, "Yorum başarıyla eklendi", Toast.LENGTH_SHORT).show();
                onBackPressed();
                yorum.getText().clear();
            }
        });
    }
}
