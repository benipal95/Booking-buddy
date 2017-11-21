package group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import group10.tcss450.uw.edu.bookingbuddy.R;

/**
 * @Author Tanvir
 * This class will display a graph of the data that results from searching for a flight.
 */
public class GraphFragment extends Fragment implements  View.OnClickListener{
    private FlightListFragment.OnGraphInteractionListener mListener;


    /**
     * Empty Constructor
     */
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


    /**
     * When the view is created all UI elements will be instantiated and
     * the graph built.
     * @param inflater The inflater.
     * @param container The fragments container.
     * @param savedInstanceState The saved instance of this fragment.
     * @return Returns the view created by this method.
     */
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

    /**
     * When this fragment is attached, sets the mListerner context to the context
     * of this fragment.
     * @param context The applications context.
     */
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

    /**
     * Factory detach, sets the mListener context to null.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Handles clicks on this view, currently not implemented
     * for phase 1.
     * @param view The view that has been clicked.
     */
    @Override
    public void onClick(View view) {

    }
}
