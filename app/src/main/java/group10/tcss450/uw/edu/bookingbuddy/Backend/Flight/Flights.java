package group10.tcss450.uw.edu.bookingbuddy.Backend.Flight;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Created by jjtowers on 11/21/2017.
 */

public class Flights implements Comparable<Flights>
{
    private long mDepartDate;
    private long mReturnDate;
    private double mValue;
    private String mOrigin;
    private String mDestination;
    private String mNiceDepartDate;
    private String mNiceReturnDate;
    private String mAirline;
    private int mFlightNumber;
    private final int mFormatType;
    private String mTrueName;

    private String mRawRetDate;
    private String mRawDeptDate;
    private String mRawPrice;

    /**
     * sortBy is used to change up how the compareTo implementation will work.
     * This is to allow for different ways to sort flights by using the same function.
     *
     * sortBy set to                    Flights sorts by
     *      0                                Lowest mValue (default)
     *      1                                Earliest departure date
     */
    private int sortBy;

    public Flights(String theDepartDate, String theReturnDate, String theValue, String theOrigin, String theDestination)
    {
        initializationHelper(theReturnDate, theDepartDate, theValue);
        mOrigin = theOrigin;
        mDestination = theDestination;
        mNiceDepartDate = theDepartDate;
        mNiceReturnDate = theReturnDate;
        sortBy = 0;
        mFormatType = 0;
    }

    public Flights(String theDepartDate, String theReturnDate, String theValue, String theAirline, int theFlightNumber, String theTrueName)
    {
        initializationHelper(theReturnDate, theDepartDate, theValue);
        mAirline = theAirline;
        mTrueName = theTrueName;
        mFlightNumber = theFlightNumber;
        mNiceDepartDate = theDepartDate;
        mNiceReturnDate = theReturnDate;
        mRawPrice = theValue;
        mRawDeptDate = theDepartDate;
        mRawRetDate = theReturnDate;
        sortBy = 0;
        mFormatType = 1;
    }

    private void initializationHelper(String theDepartDate, String theReturnDate, String theValue)
    {
        String tmp = theDepartDate.replaceAll("-", "");
        tmp = tmp.replaceAll(":", "");
        tmp = tmp.replaceAll("T", "");
        tmp = tmp.replaceAll("Z", "");
        mDepartDate = Long.parseLong(tmp);

        tmp = theReturnDate.replaceAll("-", "");
        tmp = tmp.replaceAll(":", "");
        tmp = tmp.replaceAll("T", "");
        tmp = tmp.replaceAll("Z", "");
        mReturnDate = Long.parseLong(tmp);

        mValue = Double.parseDouble(theValue);
    }

    /**
     * This changes the flag that sets how compareTo is run. It is intended for use with sorting
     * algorithms. Will throw an IllegalArgumentException if the value you attempt to set the
     * flag to is not recognized.
     *
     * @param newSort the new value to set the sorting/comparable flag to
     */
    public void setSortBy(int newSort)
    {
        if(newSort >= 0 && newSort <= 1)
        {
            sortBy = newSort;
        }
        else
        {
            throw new IllegalArgumentException(newSort + " is not a valid sort flag.");
        }
    }


    public String getFormattedRawDepartDate() {
        return mRawDeptDate;
    }

    public String getFormattedRawReturnDate() {
        return mRawRetDate;
    }

    public String getFormattedRawPrice() {
        return mRawPrice;
    }
    /**
     * Outputs raw, unformatted date integer representing the departure date.
     * Suitable for internal backend use.
     *
     * @return unformatted integer
     */
    public long getRawDepartDate()
    {
        return mDepartDate;
    }

    /**
     * Outputs raw, unformatted date integer representing the return date.
     * Suitable for internal backend use.
     *
     * @return unformatted integer
     */
    public long getRawReturnDate()
    {
        return mReturnDate;
    }

    /**
     * Outputs raw, unformatted double representing the ticket price for this flight.
     * Suitable for internal backend use.
     *
     * @return unformatted double
     */
    public double getRawValue()
    {
        return mValue;
    }

    public String getOrigin()
    {
        return "Origin Airport: " + mOrigin;
    }

    public String getDestination()
    {
        return "Destination Airport: " + mDestination;
    }

    /**
     * Outputs formatted string suitable for display to the user.
     *
     * @return "Departure Date: MM/DD/YYYY"
     */
    public String getNiceDepartDate()
    {
        return "Departure Date: " + mNiceDepartDate;
    }

    /**
     * Outputs formatted string suitable for display to the user.
     *
     * @return "Return Date: MM/DD/YYYY"
     */
    public String getNiceReturnDate()
    {
        return "Return Date: " + mNiceReturnDate;
    }

    /**
     * Outputs formatted string suitable for display to the user.
     *
     * @return "Ticket Price: $###.##"
     */
    public String getNiceValue()
    {
        String output = Double.toString(mValue);
        output = "Ticket Price: $" + output;
        return output;
    }

    public int getFormatType()
    {
        return mFormatType;
    }

    public String getNiceAirline()
    {
        if (mTrueName != null)
            return "Airline: " + mTrueName;
        else
            return "Airline Code: " + mAirline;
    }

    public String getRawAirline()
    {
        return mAirline;
    }

    public String getNiceFlightNo()
    {
        return "Flight Number: " + mFlightNumber;
    }

    public int getRawFlightNo()
    {
        return mFlightNumber;
    }

    @Override
    public int compareTo(@NonNull Flights flights) {
        int result = 0;
        if(sortBy == 0)
        {
            if(this.mValue - flights.mValue < 0)
                result = (int) (this.mValue - flights.mValue);
            else if(this.mValue - flights.mValue > 0)
                result = (int) (this.mValue - flights.mValue);
            else
                return 0;
        }
        else if (sortBy == 1)
        {
            result = (int) (this.mDepartDate - flights.mDepartDate);
        }

        return result;
    }
}
