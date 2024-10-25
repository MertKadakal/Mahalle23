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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    }
}