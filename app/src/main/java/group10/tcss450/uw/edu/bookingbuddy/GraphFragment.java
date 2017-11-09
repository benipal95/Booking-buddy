package group10.tcss450.uw.edu.bookingbuddy;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

import group10.tcss450.uw.edu.bookingbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment implements  View.OnClickListener{
    private FlightListFragment.OnGraphInteractionListener mListener;


    public GraphFragment() {

    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        GraphView graph = (GraphView) getActivity().findViewById(R.id.graph);
//        Integer[] stockArr = new Integer[sb.size()];
//        stockArr = sb.toArray(stockArr);
//
//        DataPoint[] dp = new DataPoint[stockArr.length];
//        for(int i=0 ;i<stockArr.length;i++){
//            dp[i] = new DataPoint(i+1, stockArr[i]);
//        }
//        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(dp);
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(0);
//        graph.getViewport().setMaxX(10);
//        graph.addSeries(series);
//        TextView tx_results = getActivity().findViewById(R.id.tx_flight_list);
//        tx_results.setText(result);
//        FlightListFragment fl = new FlightListFragment();
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View gView = inflater.inflate(R.layout.fragment_graph, container, false);
        Bundle args = getArguments();
        Integer[] ar;
        ar = (Integer[]) args.getSerializable("ARRAY");
        GraphView graph = (GraphView) gView.findViewById(R.id.graph);

        DataPoint[] dp = new DataPoint[ar.length];
        for(int i=0 ;i<ar.length;i++){
            dp[i] = new DataPoint(i+1, ar[i]);
        }
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(dp);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);
        graph.addSeries(series);

        return gView;


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FlightListFragment.OnGraphInteractionListener) {
            mListener = (FlightListFragment.OnGraphInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

    }
}
