package com.pmfis.cinemaapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pmfis.cinemaapp.R;

import java.util.ArrayList;

public class ReservationAdapter extends ArrayAdapter<String> {

    public ReservationAdapter(Context context, ArrayList<String> reservations) {
        super(context, 0, reservations);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String reservation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_reservation, parent, false);
        }

        TextView person = (TextView) convertView.findViewById(R.id.worker_movie_reservations_person);
        String[] reservationInfo = reservation.split(",");
        String personUsername = reservationInfo[0];
        String personId = reservationInfo[1];

        person.setText(personUsername);

        // For later accessing username so we can delete the right reservation
        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.worker_movie_reservations_delete_button);
        deleteButton.setContentDescription(personId);

        return convertView;
    }
}
