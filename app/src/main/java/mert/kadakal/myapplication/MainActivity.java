package mert.kadakal.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import mert.kadakal.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            isFirstRun = false;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hesap_açık_mı", false);
            editor.putString("hesap_ismi", "");
            editor.putString("hesap_şifresi", "");
            editor.apply();
        }

        // View Binding'i etkinleştir
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new AnasayfaFragment());
        // bottomNavigationView'e erişim
        binding.navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_anasayfa) {
                replaceFragment(new AnasayfaFragment());
            } else if (item.getItemId() == R.id.navigation_nereye_gitsem) {
                replaceFragment(new NereyeGitsemFragment());
            } else if (item.getItemId() == R.id.navigation_topluluk) {
                replaceFragment(new Tartisma());
            } else if (item.getItemId() == R.id.navigation_haberler) {
                replaceFragment(new HaberlerFragment());
            }

            return true;
        } );
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

}
