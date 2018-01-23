package com.pmfis.cinemaapp.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pmfis.cinemaapp.R;
import com.pmfis.cinemaapp.model.Movie;

import java.util.ArrayList;

public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.movie_title);
        TextView genre = (TextView) convertView.findViewById(R.id.movie_genre);
        TextView time = (TextView) convertView.findViewById(R.id.movie_time);
        ImageView moreAboutMovie = (ImageView) convertView.findViewById(R.id.button_movie_more_link);

        title.setText(movie.getTitle());
        genre.setText(movie.getGenre());
        time.setText(movie.getTime());
        moreAboutMovie.setContentDescription(movie.getImdbLink());

        // For easier accessing id
        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.movie_button_delete);
        deleteButton.setContentDescription(movie.getId() + "");
        ImageButton reservationsButton = (ImageButton) convertView.findViewById(R.id.movie_button_reservations);
        reservationsButton.setContentDescription(movie.getId() + "");

        moreAboutMovie.setContentDescription(movie.getId() + "");

        if (!movie.isEditable()) {
            deleteButton.setVisibility(View.INVISIBLE);
            reservationsButton.setVisibility(View.INVISIBLE);
        } else {
            moreAboutMovie.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }
}
