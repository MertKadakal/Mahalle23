package mert.kadakal.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class HtmlArrayAdapterSatilik extends ArrayAdapter<String> {
    public HtmlArrayAdapterSatilik(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.satiliklar, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.satiliklar_item);
        String htmlText = getItem(position).split("<link>")[0];
        textView.setText(Html.fromHtml(htmlText));


        return convertView;
    }
}
