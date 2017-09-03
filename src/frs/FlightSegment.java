package frs;


import org.joda.time.DateTime;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sudipta Saha
 */
public class FlightSegment {
    private DateTime departureDate;
    private DateTime arrivalDate;
    private int noOfPassenger;
    private String FlightKey;
    private Flight flight;

    /**
     * @return the flight
     */
    public Flight getFlight() {
        return flight;
    }

    /**
     * @param flight the flight to set
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    /**
     * @return the FlightKey
     */
    public String getFlightKey() {
        return FlightKey;
    }

    /**
     * @param FlightKey the FlightKey to set
     */
    public void setFlightKey(String FlightKey) {
        this.FlightKey = FlightKey;
    }

    /**
     * @return the departureDate
     */
    public DateTime getDepartureDate() {
        return departureDate;
    }

    /**
     * @param departureDate the departureDate to set
     */
    public void setDepartureDate(DateTime departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * @return the arrivalDate
     */
    public DateTime getArrivalDate() {
        return arrivalDate;
    }

    /**
     * @param arrivalDate the arrivalDate to set
     */
    public void setArrivalDate(DateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    /**
     * @return the noOfPassenger
     */
    public int getNoOfPassenger() {
        return noOfPassenger;
    }

    /**
     * @param noOfPassenger the noOfPassenger to set
     */
    public void setNoOfPassenger(int noOfPassenger) {
        this.noOfPassenger = noOfPassenger;
    }
}
