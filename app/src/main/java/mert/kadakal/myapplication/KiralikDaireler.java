package mert.kadakal.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class KiralikDaireler extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.satilik_kiralik_daireler);

        if (getIntent().getStringExtra("class_name").equals("KiralikDaireler")) {

        }
    }
}
