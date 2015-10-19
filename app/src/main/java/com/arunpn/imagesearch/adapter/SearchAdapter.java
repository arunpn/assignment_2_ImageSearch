package com.arunpn.imagesearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arunpn.imagesearch.R;
import com.arunpn.imagesearch.model.ImageDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by a1nagar on 10/18/15.
 */
public class SearchAdapter extends ArrayAdapter<ImageDetails> {
    Context mContext;
    public SearchAdapter(Context context, List<ImageDetails> objects) {
        super(context, 0, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView!= null) {
            holder = (ViewHolder) convertView.getTag();
        }
        else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_results,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        ImageDetails imageDetails = getItem(position);
        Picasso.with(mContext)
                .load(imageDetails.getUrl())
//                .placeholder(mContext.getResources().getDrawable(R.drawable.ic_default))
                .resize(500, 500).centerInside()
                .into(holder.searchImage);

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.searchImage)
        ImageView searchImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }

}
