package mert.kadakal.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Toplu_ulasim extends AppCompatActivity {

    ImageView kucuk_sanayi_git;
    ImageView altinsehir_git;
    Button btn_35E2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toplu_ulasim);
        Animation anim = AnimationUtils.loadAnimation(this, R.transition.up_down);

        kucuk_sanayi_git = findViewById(R.id.kucuk_sanayi_git);
        altinsehir_git = findViewById(R.id.altinsehir_git);
        btn_35E2 = findViewById(R.id.buton_35E2);

        kucuk_sanayi_git.startAnimation(anim);
        altinsehir_git.startAnimation(anim);

        View.OnClickListener listener = v -> {
            Intent intent = null;
            switch (getResources().getResourceEntryName(v.getId())) {
                case "kucuk_sanayi_git":
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.tr/maps/place/K%C3%BC%C3%A7%C3%BCk+Sanayi/@40.2131471,28.942018,16.79z/data=!4m6!3m5!1s0x14ca11500c84f00f:0x88d5372f63866bde!8m2!3d40.2120311!4d28.9416146!16s%2Fg%2F1tf8w96b?entry=ttu&g_ep=EgoyMDI0MDkzMC4wIKXMDSoASAFQAw%3D%3D"));
                    break;
                case "altinsehir_git":
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.tr/maps/place/Alt%C4%B1n%C5%9Fehir/@40.2134334,28.9363387,15.92z/data=!4m6!3m5!1s0x14ca115c8d6f5dc1:0x9a73845a335004b0!8m2!3d40.214833!4d28.93406!16s%2Fg%2F1ydp3j4qy?entry=ttu&g_ep=EgoyMDI0MDkzMC4wIKXMDSoASAFQAw%3D%3D"));
                    break;
                case "buton_35E2":
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://moovitapp.com/index/tr/toplu_ta%C5%9F%C4%B1ma-line-35e2-Bursa-3663-3732394-155946318-0"));
                    break;
            }
            startActivity(intent);
        };
        kucuk_sanayi_git.setOnClickListener(listener);
        altinsehir_git.setOnClickListener(listener);
        btn_35E2.setOnClickListener(listener);
    }
}
