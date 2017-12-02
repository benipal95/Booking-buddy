package group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import group10.tcss450.uw.edu.bookingbuddy.Backend.Flight.Flights;
import group10.tcss450.uw.edu.bookingbuddy.Backend.Flight.SaveFlightTask;
import group10.tcss450.uw.edu.bookingbuddy.R;


/**
 * @author Jacob
 * This class handles how the data returned from a flight search will be displayed inside of the fragment.
 * This class extends RecyclerView, a way for the data to be listed in a listed view.
 */
public class FlightListRecyclerView extends RecyclerView.Adapter<FlightListRecyclerView.ViewHolder>
{

    private final List<Flights> mValues;
    private final String userEmail;

    /**
     * Constructor that accepts a hashmap containing flight data.
     * @param mValues
     */
    public FlightListRecyclerView(ArrayList<Flights> mValues, String email) {
        this.mValues = mValues;
        this.userEmail = email;
    }
    //private final OnListFragmentInteractionListener mListener;
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    /**
     * When called, creates a new view and inflates the recylcer view.
     * @param parent The parent view group.
     * @param viewType The view type.
     * @return the viewholder created in this method.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * This method sets the holders text data so that it can be represented
     * by the recycler view.
     * @param holder The view holder.
     * @param position The position in the recycler view.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*HashMap<String, String> hashData = mValues.get(position);
        holder.mDepartDate.setText(hashData.get("depart_date"));
        holder.mReturnDate.setText(hashData.get("return_date"));
        holder.mValue.setText(hashData.get("value"));
        holder.mOrigin.setText(hashData.get("origin"));
        holder.mDestination.setText(hashData.get("destination"));*/

        Flights flight = mValues.get(position);
        if(flight.isFlightSaved()) {
            holder.saveFlightButton.setVisibility(View.INVISIBLE);
        }
        if(flight.getFormatType() == 0)
        {
            holder.mDepartDate.setText(flight.getNiceDepartDate());
            holder.mReturnDate.setText(flight.getNiceReturnDate());
            holder.mValue.setText(flight.getNiceValue());
            holder.mOrigin.setText(flight.getOrigin());
            holder.mDestination.setText(flight.getDestination());
        }
        else if (flight.getFormatType() == 1)
        {
            holder.mDepartDate.setText(flight.getNiceDepartDate());
            holder.mReturnDate.setText(flight.getNiceReturnDate());
            holder.mValue.setText(flight.getNiceValue());
            holder.mOrigin.setText(flight.getNiceAirline());
            holder.mDestination.setText(flight.getNiceFlightNo());
        }

        holder.saveFlightButton.setTag(position);
    }



    /**
     * This method will return the number of items in the recylcer view.
     * @return The number of items in the recylcer view.
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * This class holds the views that will be displayed by recycler view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public final CardView card;
        public final View mView;
        public final TextView mDepartDate;
        public final TextView mReturnDate;
        public final TextView mValue;
        public final TextView mOrigin;
        public final TextView mDestination;
        public final Button saveFlightButton;


        /**
         * Constructor that takes in the view that these items will be displayed on.
         * @param itemView The view that the  UI elements will be displayed on.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mDepartDate = itemView.findViewById(R.id.flight_departure_date);
            card = itemView.findViewById(R.id.flight_card);
            mReturnDate = itemView.findViewById(R.id.flight_return_date);
            mValue = itemView.findViewById(R.id.flight_cost);
            mOrigin = itemView.findViewById(R.id.flight_origin);
            mDestination = itemView.findViewById(R.id.flight_dest);
            saveFlightButton = itemView.findViewById(R.id.save_flight_button);
            saveFlightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = (int) saveFlightButton.getTag();
                    Flights flight = mValues.get(position);
                    String origin = flight.getRawAirline();
                    String dest = ""+ flight.getRawFlightNo();
                    String dept =""+ flight.getFormattedRawDepartDate();
                    String ret_date = ""+ flight.getFormattedRawReturnDate();
                    String price =""+ flight.getFormattedRawPrice();


                    SaveFlightTask saveFlight = new SaveFlightTask();
                    saveFlight.execute(userEmail,origin,dest,dept,ret_date,price);
                    saveFlightButton.setEnabled(false);


                }
            });
        }
    }
}
