package mert.kadakal.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Hesap_oluştur_giriş extends AppCompatActivity {

    EditText isim;
    EditText şifre;
    Button tamam;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hesap_ekle_veya_giris);
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        isim = findViewById(R.id.isim);
        şifre = findViewById(R.id.şifre);
        tamam = findViewById(R.id.hesap_ekle_giriş);
        tamam.setText(getIntent().getStringExtra("oluştur_giriş"));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        tamam.setOnClickListener(view -> {
            if (isim.getText().toString().length() == 0 || şifre.getText().toString().length() == 0) {
                Toast.makeText(this, "Lütfen gerekli alanları doldurunuz", Toast.LENGTH_SHORT).show();
            } else if (getIntent().getStringExtra("oluştur_giriş").equals("Oluştur")) {
                db.collection("hesaplar")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean isim_kontrol = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getString("isim").equals(isim.getText().toString())) {
                                            isim_kontrol = true;
                                        }
                                    }
                                    if (isim_kontrol) {
                                        Toast.makeText(Hesap_oluştur_giriş.this, "Bu isim kullanılıyor", Toast.LENGTH_SHORT).show();
                                        isim.getText().clear();
                                        şifre.getText().clear();
                                    } else {
                                        Map<String, Object> eklenen_hesap = new HashMap<>();
                                        eklenen_hesap.put("isim", isim.getText().toString());
                                        eklenen_hesap.put("şifre", şifre.getText().toString());
                                        eklenen_hesap.put("beğenilen_yorumlar", new ArrayList<String>());
                                        db.collection("hesaplar").add(eklenen_hesap).addOnSuccessListener(documentReference -> {
                                            String hesapId = documentReference.getId();
                                            // ID'yi belgeye ekleyin
                                            db.collection("hesaplar").document(hesapId)
                                                    .update("hesapId", hesapId);// ID'yi bir alan olarak ekle
                                        });

                                        Toast.makeText(Hesap_oluştur_giriş.this, "Hesap başarıyla oluşturuldu, oturum açıldı", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("hesap_açık_mı", true);
                                        editor.putString("hesap_ismi", isim.getText().toString());
                                        editor.putString("hesap_şifresi", şifre.getText().toString());
                                        editor.apply();
                                        onBackPressed();

                                        isim.getText().clear();
                                        şifre.getText().clear();
                                    }
                                }
                            }
                        });
            } else if (getIntent().getStringExtra("oluştur_giriş").equals("Giriş yap")) {
                db.collection("hesaplar")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean şifre_kontrol = false;
                                    boolean isim_kontrol = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getString("isim").equals(isim.getText().toString())) {
                                            isim_kontrol = true;
                                            if (document.getString("şifre").equals(şifre.getText().toString())) {
                                                şifre_kontrol = true;
                                            }
                                        }
                                    }
                                    if (!şifre_kontrol || !isim_kontrol) {
                                        Toast.makeText(Hesap_oluştur_giriş.this, "İsim veya şifre yanlış girildi", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Hesap_oluştur_giriş.this, Html.fromHtml(isim.getText().toString() + " hesabına giriş yapıldı"), Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("hesap_açık_mı", true);
                                        editor.putString("hesap_ismi", isim.getText().toString());
                                        editor.putString("hesap_şifresi", şifre.getText().toString());
                                        editor.apply();
                                        onBackPressed();

                                        isim.getText().clear();
                                        şifre.getText().clear();
                                    }
                                }
                            }
                        });
            }
        });
    }
}
