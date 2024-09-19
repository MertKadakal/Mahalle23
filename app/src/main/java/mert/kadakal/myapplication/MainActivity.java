package mert.kadakal.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
            } else if (item.getItemId() == R.id.navigation_nasil_gidilir) {
                replaceFragment(new NasilGidilirFragment());
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
