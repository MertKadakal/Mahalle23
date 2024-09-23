package mert.kadakal.battleofships;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class oyun extends AppCompatActivity {

    //oyun sürecinde gerekli değişken ve listeler
    HashMap<Integer, String> oyuncu_isimleri;
    int turn = 0;
    TextView adetler_1o;
    TextView adetler_2o;
    TextView kim_ne_batırdı;
    HashMap<String, Integer> adetler_1;
    HashMap<String, Integer> adetler_2;
    ArrayList<ArrayList<Integer>> tablo_1_gorunurluk;
    ArrayList<ArrayList<Integer>> tablo_2_gorunurluk;
    ArrayList<ArrayList<String>> tablo_1;
    ArrayList<ArrayList<String>> tablo_2;
    int emptySquareId;
    int waterSquareId;
    int battleshipId;
    int carrierId;
    int destroyerId;
    int submarineId;
    int patrol_boatId;
    GridLayout gridLayout;
    TextView kim_saldiriyor;
    TextView kimin_tahtasi;
    ArrayList<ArrayList<ArrayList<Integer>>> battleship_xy_1;
    ArrayList<ArrayList<ArrayList<Integer>>> patrolboat_xy_1;
    ArrayList<ArrayList<ArrayList<Integer>>> battleship_xy_2;
    ArrayList<ArrayList<ArrayList<Integer>>> patrolboat_xy_2;
    Animation slide_c_from_l;
    Animation slide_r_from_c;
    boolean grid_tiklanabilirligi = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oyun);

        kim_ne_batırdı = findViewById(R.id.gemi_batirildi);
        kim_ne_batırdı.setVisibility(View.INVISIBLE);

        slide_c_from_l = AnimationUtils.loadAnimation(this, R.transition.slide_center_from_left);
        slide_r_from_c = AnimationUtils.loadAnimation(this, R.transition.slide_right_from_center);

        //oyun esnasında kullanılacak veriler
        oyuncu_isimleri = new HashMap<>();
        oyuncu_isimleri.put(0, getIntent().getStringExtra("1.oyuncu_ismi"));
        oyuncu_isimleri.put(1, getIntent().getStringExtra("2.oyuncu_ismi"));
        turn = 0;
        kim_saldiriyor = findViewById(R.id.kimin_saldirdigi);
        kim_saldiriyor.setText(Html.fromHtml(String.format("<b>%s</b> saldırıyor!", oyuncu_isimleri.get(turn))));
        kimin_tahtasi = findViewById(R.id.kimin_tahtasi);
        kimin_tahtasi.setText(Html.fromHtml(String.format("<b>%s</b> oyuncusunun tahtası", oyuncu_isimleri.get(1-turn))));

        //10x10 tablo oluştur
        //---------------------------------------------------------------------------------
        gridLayout = findViewById(R.id.grid_layout);
        int numRows = 10;
        int numColumns = 10;

        // Görsel kaynaklar
        emptySquareId = R.drawable.empty_square;
        waterSquareId = R.drawable.water_square;
        carrierId = R.drawable.carrier;
        battleshipId = R.drawable.battleship;
        destroyerId = R.drawable.destroyer;
        submarineId = R.drawable.submarine;
        patrol_boatId = R.drawable.patrol_boat;

        // GridLayout ayarları
        gridLayout.setRowCount(numRows);
        gridLayout.setColumnCount(numColumns);

        // Her hücre için ImageView ekle
        for (int i = 0; i < numRows * numColumns; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(emptySquareId);

            // Hücre boyutlarını ayarla
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; // Sütun genişliği
            params.height = 0; // Satır yüksekliği
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            imageView.setLayoutParams(params);

            imageView.setTag(i);  // Tag olarak sırasını kullanıyoruz

            //tıklanan hücreyi algıla, saldırıyı gerçekleştir
            View.OnClickListener imageClickListener = new View.OnClickListener() {
                @Override
                public void onClick(@NonNull View v) {
                    if (grid_tiklanabilirligi) {
                        // Tıklanan ImageView'ı tag'ine göre belirleme
                        int position = (int) v.getTag();  // Tag ile sırasını alıyoruz
                        Log.d("turn", String.valueOf(turn));
                        saldir(position/10, position%10);
                    }

                }
            };

            imageView.setOnClickListener(imageClickListener);
            gridLayout.addView(imageView);
        }
        //---------------------------------------------------------------------------------

        //oyuncuların gemilerini tablolarına yerleştir
        Random random = new Random();
        ArrayList<String> gemi_isimleri = new ArrayList<>();
        gemi_isimleri.add("carrier");
        gemi_isimleri.add("battleship");
        gemi_isimleri.add("destroyer");
        gemi_isimleri.add("submarine");
        gemi_isimleri.add("patrol boat");
        HashMap<Integer, String> yon = new HashMap<>(); //rastgele yünler
        yon.put(0, "Dikey");
        yon.put(1, "Yatay");
        HashMap<String, Integer> max_ilk_hucreler = new HashMap<>(); //gemilerin kontrole başlanabileceği max hücre
        max_ilk_hucreler.put("carrier", 5);
        max_ilk_hucreler.put("battleship", 6);
        max_ilk_hucreler.put("destroyer", 7);
        max_ilk_hucreler.put("submarine", 7);
        max_ilk_hucreler.put("patrol boat", 8);
        HashMap<String, Integer> uzunluklar = new HashMap<>(); //gemilerin uzunlukları
        uzunluklar.put("carrier", 5);
        uzunluklar.put("battleship", 4);
        uzunluklar.put("destroyer", 3);
        uzunluklar.put("submarine", 3);
        uzunluklar.put("patrol boat", 2);
        HashMap<String, Integer> adetler = new HashMap<>(); //gemilerin sayıları
        adetler.put("carrier", 1);
        adetler.put("battleship", 2);
        adetler.put("destroyer", 1);
        adetler.put("submarine", 1);
        adetler.put("patrol boat", 4);
        adetler_1 = new HashMap<>(); //gemilerin sayıları
        adetler_1.put("carrier", 1);
        adetler_1.put("battleship", 2);
        adetler_1.put("destroyer", 1);
        adetler_1.put("submarine", 1);
        adetler_1.put("patrol boat", 4);
        adetler_2 = new HashMap<>(); //gemilerin sayıları
        adetler_2.put("carrier", 1);
        adetler_2.put("battleship", 2);
        adetler_2.put("destroyer", 1);
        adetler_2.put("submarine", 1);
        adetler_2.put("patrol boat", 4);

        //oyuncuların boş tablolarını oluştur
        tablo_1 = new ArrayList<>();
        tablo_2 = new ArrayList<>();
        tablo_1_gorunurluk = new ArrayList<>();
        tablo_2_gorunurluk = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ArrayList<String> satir1 = new ArrayList<>();
            ArrayList<Integer> satir2 = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                satir1.add("x");
                satir2.add(0);
            }
            tablo_1.add(satir1);
            tablo_1_gorunurluk.add(satir2);
        }
        for (int i = 0; i < 10; i++) {
            ArrayList<String> satir1 = new ArrayList<>();
            ArrayList<Integer> satir2 = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                satir1.add("x");
                satir2.add(0);
            }
            tablo_2.add(satir1);
            tablo_2_gorunurluk.add(satir2);
        }

        //her gemi için adetleri kadar yerleştirme yap
        battleship_xy_1 = new ArrayList<>();
        patrolboat_xy_1 = new ArrayList<>();
        battleship_xy_2 = new ArrayList<>();
        patrolboat_xy_2 = new ArrayList<>();
        HashMap<Integer, ArrayList<ArrayList<String>>> oyuncu_tabloları = new HashMap<>();
        oyuncu_tabloları.put(0, tablo_1);
        oyuncu_tabloları.put(1, tablo_2);
        for (int oyuncu=0; oyuncu<2; oyuncu++) {
            for (String gemi_ismi : gemi_isimleri) {
                for (int j=0; j<adetler.get(gemi_ismi); j++) {
                    while (true) {
                        String dikey_yatay = yon.get(random.nextInt(2));
                        int ilk_satir = random.nextInt(max_ilk_hucreler.get(gemi_ismi));
                        int ilk_sutun = random.nextInt(max_ilk_hucreler.get(gemi_ismi));
                        int uzunluk = uzunluklar.get(gemi_ismi);
                        ArrayList<ArrayList<Integer>> dolduralacak_hucreler = new ArrayList<>();
                        if (dikey_yatay.equals("Dikey")) {
                            for (int i = 0; i < uzunluk; i++) {
                                ArrayList<Integer> konum = new ArrayList<>();
                                konum.add(ilk_satir+i);
                                konum.add(ilk_sutun);
                                dolduralacak_hucreler.add(konum);
                            }
                        } else {
                            for (int i = 0; i < uzunluk; i++) {
                                ArrayList<Integer> konum = new ArrayList<>();
                                konum.add(ilk_satir);
                                konum.add(ilk_sutun+i);
                                dolduralacak_hucreler.add(konum);
                            }
                        }
                        if (check_if_placable(dolduralacak_hucreler, oyuncu_tabloları.get(oyuncu))) {
                            for (ArrayList<Integer> item : dolduralacak_hucreler) {
                                oyuncu_tabloları.get(oyuncu).get(item.get(0)).set(item.get(1), String.valueOf(gemi_ismi.charAt(0)));
                            }
                            //battleship ve patrol boatların konumlarını kaydet
                            ArrayList<ArrayList<Integer>> konumlar = new ArrayList<>();
                            if (dolduralacak_hucreler.size() == 4) {
                                if (oyuncu == 0) {
                                    konumlar.clear();
                                    for (ArrayList<Integer> item : dolduralacak_hucreler) {
                                        konumlar.add(item);
                                    }
                                    battleship_xy_1.add(konumlar);
                                } else {
                                    konumlar.clear();
                                    for (ArrayList<Integer> item : dolduralacak_hucreler) {
                                        konumlar.add(item);
                                    }
                                    battleship_xy_2.add(konumlar);
                                }
                            } else if (dolduralacak_hucreler.size() == 2) {
                                if (oyuncu == 0) {
                                    konumlar.clear();
                                    for (ArrayList<Integer> item : dolduralacak_hucreler) {
                                        konumlar.add(item);
                                    }
                                    patrolboat_xy_1.add(konumlar);
                                } else {
                                    konumlar.clear();
                                    for (ArrayList<Integer> item : dolduralacak_hucreler) {
                                        konumlar.add(item);
                                    }
                                    patrolboat_xy_2.add(konumlar);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        //oyuncuların kaçar gemisi kaldığını tablo altına yaz
        adetleri_guncelle();
    }

    //bir hücre tıklandığında saldır
    private void saldir(int sat, int sut) {
        if (turn == 0) {
            if (tablo_2_gorunurluk.get(sat).get(sut) == 0) {
                tablo_2_gorunurluk.get(sat).set(sut, 1); //tablo_2'nin saldırılan konumunu görünür yap
                tabloyu_yukle(0);
                // 2 saniye (2000 milisaniye) bekle
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                check_capsized_battles(tablo_2);
            }
        } else {
            if (tablo_1_gorunurluk.get(sat).get(sut) == 0) {
                tablo_1_gorunurluk.get(sat).set(sut, 1); //tablo_1'nin saldırılan konumunu görünür yap
                tabloyu_yukle(1);
                // 2 saniye (2000 milisaniye) bekle
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                check_capsized_battles(tablo_1);
            }
        }
        turn = 1 - turn;
        tabloyu_yukle(turn);
    }

    //saldırıdan sonra herhangi bir gemi battı mı kontrol et
    private void check_capsized_battles(ArrayList<ArrayList<String>> tablo) {
        int c = 0;
        int d = 0;
        int s = 0;
        ArrayList<ArrayList<Integer>> battleships = new ArrayList<>();
        ArrayList<ArrayList<Integer>> patrol_boats = new ArrayList<>();
        if (turn == 0) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (tablo_2_gorunurluk.get(i).get(j) == 1) {
                        switch (tablo.get(i).get(j)) {
                            case "c":
                                c++;
                                break;
                            case "d":
                                d++;
                                break;
                            case "s":
                                s++;
                                break;
                            case "b":
                                ArrayList<Integer> konumlar = new ArrayList<>();
                                konumlar.add(i);
                                konumlar.add(j);
                                battleships.add(konumlar);
                                break;
                            case "p":
                                ArrayList<Integer> konumlar1 = new ArrayList<>();
                                konumlar1.add(i);
                                konumlar1.add(j);
                                patrol_boats.add(konumlar1);
                                break;
                        }
                    }
                }
            }
            //kontrol et
            if (c == 5 && adetler_2.get("carrier") != 0) {
                adetler_2.replace("carrier", 0);
                if (!(oyun_bitti_mi())) {
                    kayan_batirildi_yazisi("Carrier");
                }
            }
            if (d == 3 && adetler_2.get("destroyer") != 0) {
                adetler_2.replace("destroyer", 0);
                if (!(oyun_bitti_mi())) {
                    kayan_batirildi_yazisi("Destroyer");
                }
            }
            if (s == 3 && adetler_2.get("submarine") != 0) {
                adetler_2.replace("submarine", 0);
                if (!(oyun_bitti_mi())) {
                    kayan_batirildi_yazisi("Submarine");
                }
            }
            if (battleships.size() > 3 && adetler_2.get("battleship") != 0 && check_for_btshp_ptrlbt(battleships, battleship_xy_2)) {
                adetler_2.replace("battleship", adetler_2.get("battleship")-1);
                if (!(oyun_bitti_mi())) {
                    kayan_batirildi_yazisi("Battleship");
                }
            }
            if (patrol_boats.size() > 1 && adetler_2.get("patrol boat") != 0 && check_for_btshp_ptrlbt(patrol_boats, patrolboat_xy_2)) {
                adetler_2.replace("patrol boat", adetler_2.get("patrol boat")-1);
                if (!(oyun_bitti_mi())) {
                    kayan_batirildi_yazisi("Patrol Boat");
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (tablo_1_gorunurluk.get(i).get(j) == 1) {
                        switch (tablo.get(i).get(j)) {
                            case "c":
                                c++;
                                break;
                            case "d":
                                d++;
                                break;
                            case "s":
                                s++;
                                break;
                            case "b":
                                ArrayList<Integer> konumlar = new ArrayList<>();
                                konumlar.add(i);
                                konumlar.add(j);
                                battleships.add(konumlar);
                                break;
                            case "p":
                                ArrayList<Integer> konumlar1 = new ArrayList<>();
                                konumlar1.add(i);
                                konumlar1.add(j);
                                patrol_boats.add(konumlar1);
                                break;
                        }
                    }
                }
            }
            //kontrol et
            if (c == 5 && adetler_1.get("carrier") != 0) {
                adetler_1.replace("carrier", 0);
                if (!(oyun_bitti_mi())) {
                    kayan_batirildi_yazisi("Carrier");
                }
            }
            if (d == 3 && adetler_1.get("destroyer") != 0) {
                adetler_1.replace("destroyer", 0);
                if (!(oyun_bitti_mi())) {
                    kayan_batirildi_yazisi("Destroyer");
                }
            }
            if (s == 3 && adetler_1.get("submarine") != 0) {
                adetler_1.replace("submarine", 0);
                if (!(oyun_bitti_mi())) {
                    kayan_batirildi_yazisi("Submarine");
                }
            }
            if (battleships.size() > 3 && adetler_1.get("battleship") != 0 && check_for_btshp_ptrlbt(battleships, battleship_xy_1)) {
                adetler_1.replace("battleship", adetler_1.get("battleship")-1);
                if (!(oyun_bitti_mi())) {
                    kayan_batirildi_yazisi("Battleship");
                }
            }
            if (patrol_boats.size() > 1 && adetler_1.get("patrol boat") != 0 && check_for_btshp_ptrlbt(patrol_boats, patrolboat_xy_1)) {
                adetler_1.replace("patrol boat", adetler_1.get("patrol boat")-1);
                if (!(oyun_bitti_mi())) {
                    kayan_batirildi_yazisi("Patrol Boat");
                }
            }
        }
    }

    private boolean oyun_bitti_mi() {
        if (adetler_1.get("carrier") == 0 &&
                adetler_1.get("battleship") == 0 &&
                adetler_1.get("destroyer") == 0 &&
                adetler_1.get("submarine") == 0 &&
                adetler_1.get("patrol boat") == 0) {
            Intent intent = new Intent(oyun.this, kazanan.class);
            intent.putExtra("kazanan", oyuncu_isimleri.get(1));
            startActivity(intent);
            return true;
        }
        if (adetler_2.get("carrier") == 0 &&
                adetler_2.get("battleship") == 0 &&
                adetler_2.get("destroyer") == 0 &&
                adetler_2.get("submarine") == 0 &&
                adetler_2.get("patrol boat") == 0) {
            Intent intent = new Intent(oyun.this, kazanan.class);
            intent.putExtra("kazanan", oyuncu_isimleri.get(0));
            startActivity(intent);
            return true;
        }
        return false;
    }

    //bir gemi batırılırsa ekranda sağa kayan yazıyı göster
    private void kayan_batirildi_yazisi(String batan) {
        grid_tiklanabilirligi = false;
        // Soldan kayarak ekrana gelsin
        kim_ne_batırdı.setText(Html.fromHtml(String.format("%s <br><b>%s</b><br>gemisi batırdı", oyuncu_isimleri.get(turn), batan)));
        kim_ne_batırdı.setVisibility(View.VISIBLE);
        kim_ne_batırdı.startAnimation(slide_c_from_l);

        // 2 saniye bekledikten sonra sağa kayarak kaybolsun
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                kim_ne_batırdı.startAnimation(slide_r_from_c);
                kim_ne_batırdı.setVisibility(View.INVISIBLE);
            }
        }, 2000); // 2 saniye bekle
        grid_tiklanabilirligi = true;
    }

    //battleship veya patrol boatlardan biri batırıldı mı kontrol et
    private boolean check_for_btshp_ptrlbt(ArrayList<ArrayList<Integer>> bulunanlar, ArrayList<ArrayList<ArrayList<Integer>>> mevcutlar) {
        for (ArrayList<ArrayList<Integer>> sublist : mevcutlar) {
            int matchCount = 0;
            for (ArrayList<Integer> element : sublist) {
                if (bulunanlar.contains(element)) {
                    matchCount++;
                }
            }
            if (matchCount == sublist.size()) {
                mevcutlar.remove(sublist);
                return true; // Eğer 4 öğenin tamamı liste2'de bulunuyorsa, true döner
            }
        }
        return false; // Hiçbir alt liste 4 öğenin hepsi liste2'de bulunmuyorsa, false döner
    }
    
    private void tabloyu_yukle(int turn) {
        gridLayout.removeAllViews();
        kim_saldiriyor.setText(Html.fromHtml(String.format("<b>%s</b> saldırıyor!", oyuncu_isimleri.get(turn))));
        kimin_tahtasi.setText(Html.fromHtml(String.format("<b>%s</b> oyuncusunun tahtası", oyuncu_isimleri.get(1-turn))));
        for (int i = 0; i < 100; i++) {
            ImageView imageView = new ImageView(this);

            if (turn == 1) {
                if (tablo_1_gorunurluk.get(i/10).get(i%10) == 1) {
                    switch (tablo_1.get(i/10).get(i%10)) {
                        case "c":
                            imageView.setImageResource(carrierId);
                            break;
                        case "b":
                            imageView.setImageResource(battleshipId);
                            break;
                        case "d":
                            imageView.setImageResource(destroyerId);
                            break;
                        case "s":
                            imageView.setImageResource(submarineId);
                            break;
                        case "p":
                            imageView.setImageResource(patrol_boatId);
                            break;
                        case "x":
                            imageView.setImageResource(waterSquareId);
                            break;
                    }
                } else {
                    imageView.setImageResource(emptySquareId);
                }
            } else {
                if (tablo_2_gorunurluk.get(i/10).get(i%10) == 1) {
                    switch (tablo_2.get(i/10).get(i%10)) {
                        case "c":
                            imageView.setImageResource(carrierId);
                            break;
                        case "b":
                            imageView.setImageResource(battleshipId);
                            break;
                        case "d":
                            imageView.setImageResource(destroyerId);
                            break;
                        case "s":
                            imageView.setImageResource(submarineId);
                            break;
                        case "p":
                            imageView.setImageResource(patrol_boatId);
                            break;
                        case "x":
                            imageView.setImageResource(waterSquareId);
                            break;
                    }
                } else {
                    imageView.setImageResource(emptySquareId);
                }
            }

            // Hücre boyutlarını ayarla
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; // Sütun genişliği
            params.height = 0; // Satır yüksekliği
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            imageView.setLayoutParams(params);

            //tıklanan hücreyi algıla, saldırıyı gerçekleştir
            View.OnClickListener imageClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (grid_tiklanabilirligi) {
                        // Tıklanan ImageView'ı tag'ine göre belirleme
                        int position = (int) v.getTag();  // Tag ile sırasını alıyoruz
                        Log.d("turn", String.valueOf(turn));
                        if (turn == 0) {
                            if (tablo_2_gorunurluk.get(position/10).get(position%10) == 1) {
                                Toast.makeText(oyun.this, "Boş bir hücre seçiniz", Toast.LENGTH_SHORT).show();
                            } else {
                                saldir(position/10, position%10);
                                adetleri_guncelle();
                            }
                        } else {
                            if (tablo_1_gorunurluk.get(position/10).get(position%10) == 1) {
                                Toast.makeText(oyun.this, "Boş bir hücre seçiniz", Toast.LENGTH_SHORT).show();
                            } else {
                                saldir(position/10, position%10);
                                adetleri_guncelle();
                            }
                        }
                    }
                }
            };

            imageView.setOnClickListener(imageClickListener);
            imageView.setTag(i);  // Tag olarak sırasını kullanıyoruz
            gridLayout.addView(imageView);
        }
    }

    private void adetleri_guncelle() {
        adetler_1o = findViewById(R.id.adetler_1);
        adetler_2o = findViewById(R.id.adetler_2);
        adetler_1o.setText(Html.fromHtml(String.format("<b>%s</b><br><br>" +
                "Carrier: %s<br>" +
                "Battleship: %s<br>" +
                "Destroyer: %s<br>" +
                "Submarine: %s<br>" +
                "Patrol Boat: %s<br>",
                oyuncu_isimleri.get(0),
                "●".repeat(adetler_1.get("carrier")),
                "●".repeat(adetler_1.get("battleship")),
                "●".repeat(adetler_1.get("destroyer")),
                "●".repeat(adetler_1.get("submarine")),
                "●".repeat(adetler_1.get("patrol boat")))));
        adetler_2o.setText(Html.fromHtml(String.format("<b>%s</b><br><br>" +
                "Carrier: %s<br>" +
                "Battleship: %s<br>" +
                "Destroyer: %s<br>" +
                "Submarine: %s<br>" +
                "Patrol Boat: %s<br>",
                oyuncu_isimleri.get(1),
                "●".repeat(adetler_2.get("carrier")),
                "●".repeat(adetler_2.get("battleship")),
                "●".repeat(adetler_2.get("destroyer")),
                "●".repeat(adetler_2.get("submarine")),
                "●".repeat(adetler_2.get("patrol boat")))));
    }

    public boolean check_if_placable(ArrayList<ArrayList<Integer>> list, ArrayList<ArrayList<String>> tablo) {
        for (ArrayList<Integer> item : list) {
            if (!(tablo.get(item.get(0)).get(item.get(1)).equals("x"))) {
                return false;
            }
        }
        return true;
    }
}