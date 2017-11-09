package group10.tcss450.uw.edu.bookingbuddy;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jjtowers on 11/9/2017.
 */

public class FlightListRecyclerView extends RecyclerView.Adapter<FlightListRecyclerView.ViewHolder>
{

    private final List<HashMap> mValues;

    public FlightListRecyclerView(List<HashMap> mValues) {
        this.mValues = mValues;
    }
    //private final OnListFragmentInteractionListener mListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> hashData = mValues.get(position);
        holder.mDepartDate.setText(hashData.get("depart_date"));
        holder.mReturnDate.setText(hashData.get("return_date"));
        holder.mValue.setText(hashData.get("value"));
        holder.mOrigin.setText(hashData.get("origin"));
        holder.mDestination.setText(hashData.get("destination"));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mDepartDate;
        public final TextView mReturnDate;
        public final TextView mValue;
        public final TextView mOrigin;
        public final TextView mDestination;


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
