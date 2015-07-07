package com.leleonb.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by usuario on 05/07/2015.
 */
public class MusicInfoAdapter extends ArrayAdapter<MusicInfo> {

    //TODO: Javadoc
    public MusicInfoAdapter(Activity context, List<MusicInfo> musicElements) {
        super(context, 0, musicElements);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MusicInfo musicInfoItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_visual_info, parent, false);
        }

        //TODO: Same image size for every item
        ImageView imageView = (ImageView) convertView.findViewById(R.id.list_item_image);

        if (!musicInfoItem.getImage().isEmpty()) {
            Picasso.with(parent.getContext()).load(musicInfoItem.getImage()).into(imageView);
        }

        TextView mainTextView = (TextView) convertView.findViewById(R.id.list_item_text_main);
        mainTextView.setText(musicInfoItem.getMainText());

        return convertView;
    }
}
