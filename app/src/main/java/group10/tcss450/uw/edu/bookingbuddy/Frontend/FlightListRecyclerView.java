package group10.tcss450.uw.edu.bookingbuddy.Frontend;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import group10.tcss450.uw.edu.bookingbuddy.R;


/**
 * @author Jacob
 * This class handles how the data returned from a flight search will be displayed inside of the fragment.
 * This class extends RecyclerView, a way for the data to be listed in a listed view.
 */
public class FlightListRecyclerView extends RecyclerView.Adapter<FlightListRecyclerView.ViewHolder>
{

    private final List<HashMap> mValues;

    /**
     * Constructor that accepts a hashmap containing flight data.
     * @param mValues
     */
    public FlightListRecyclerView(List<HashMap> mValues) {
        this.mValues = mValues;
    }
    //private final OnListFragmentInteractionListener mListener;

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
        return new ViewHolder(view);
    }

    /**
     * This method sets the holders text data so that it can be represented
     * by the recycler view.
     * @param holder The view holder.
     * @param position The position in the recycler view.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> hashData = mValues.get(position);
        holder.mDepartDate.setText(hashData.get("depart_date"));
        holder.mReturnDate.setText(hashData.get("return_date"));
        holder.mValue.setText(hashData.get("value"));
        holder.mOrigin.setText(hashData.get("origin"));
        holder.mDestination.setText(hashData.get("destination"));
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

        public final View mView;
        public final TextView mDepartDate;
        public final TextView mReturnDate;
        public final TextView mValue;
        public final TextView mOrigin;
        public final TextView mDestination;


        /**
         * Constructor that takes in the view that these items will be displayed on.
         * @param itemView The view that the  UI elements will be displayed on.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mDepartDate = itemView.findViewById(R.id.r_depart_date);
            mReturnDate = itemView.findViewById(R.id.r_return_date);
            mValue = itemView.findViewById(R.id.r_value);
            mOrigin = itemView.findViewById(R.id.r_origin);
            mDestination = itemView.findViewById(R.id.r_destination);
        }
    }
}
