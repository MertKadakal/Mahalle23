package mert.kadakal.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

public class HtmlArrayAdapterYorumlar extends ArrayAdapter<String> {
    Button yorum_begen;
    ArrayList<String> id_list;

    public HtmlArrayAdapterYorumlar(Context context, int resource, List<String> items, ArrayList<String> id_list) {
        super(context, resource, items);
        this.id_list = id_list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.yorumlar, parent, false);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        yorum_begen = convertView.findViewById(R.id.yorum_begen_butonu);
        yorum_begen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Firestore'da beğeni sayısını artırma işlemi
                db.collection("yorumlar").document(id_list.get(position))
                        .update("beğeni_sayısı", FieldValue.increment(1)) // veya beğeniler koleksiyonu oluşturup burada saklayabilirsiniz
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Beğenildi!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.w("TAG", "Error updating document", e);
                        });
            }
        });

        TextView textView = convertView.findViewById(R.id.yorumlar_item);
        TextView textView2 = convertView.findViewById(R.id.yorumlar_item2);
        String isim = getItem(position).split("<kay>")[0];
        String yorum = getItem(position).split("<kay>")[1];
        String tarih = getItem(position).split("<kay>")[2];
        String beğeni_sayısı = getItem(position).split("<kay>")[3];
        textView.setText(Html.fromHtml(String.format("<b>%s</b><br>%s<br>", isim, tarih)));
        textView2.setText(Html.fromHtml(String.format("%s<br><br><b>%s</b> beğeni", yorum, beğeni_sayısı)));

        return convertView;
    }


}
