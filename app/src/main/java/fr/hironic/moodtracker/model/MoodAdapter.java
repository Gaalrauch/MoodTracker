package fr.hironic.moodtracker.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import fr.hironic.moodtracker.R;
import fr.hironic.moodtracker.controller.MainActivity;

public class MoodAdapter extends ArrayAdapter<Mood> {


    public MoodAdapter (Context context, List<Mood> moods) {
        super(context, 0, moods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mood, parent, false);
        }

        MoodViewHolder viewHolder = (MoodViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MoodViewHolder();
            viewHolder.layout = convertView.findViewById(R.id.llMoodBackground);
            viewHolder.imageView = convertView.findViewById(R.id.imgSmiley);
            convertView.setTag(viewHolder);
        }

        Mood mood = getItem(position);

        // Fill view
        viewHolder.layout.setBackgroundColor(mood.getColor());
        viewHolder.imageView.setImageDrawable(mood.getIcon());

        convertView.setMinimumHeight(MainActivity.mScreenHeight);

        return convertView;
    }

    public class MoodViewHolder {
        public LinearLayout layout;
        private ImageView imageView;
    }


}
