package mert.kadakal.myapplication;

import android.widget.TextView;

import java.util.ArrayList;

public interface BusTimeCallback {
    void onBusTimeFetched(ArrayList<String> en_yakin_saatler);
}
