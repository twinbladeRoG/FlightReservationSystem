package frs;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sourov Ghosh
 */
public class ComboFlight {
    private FlightSegment seg1;
    private FlightSegment seg2;
    private int transitTime;
    private int totatTravelTime;
    private int noOfPassenger;

    /**
     * @return the seg1
     */
    public FlightSegment getSeg1() {
        return seg1;
    }

    /**
     * @param seg1 the seg1 to set
     */
    public void setSeg1(FlightSegment seg1) {
        this.seg1 = seg1;
    }

    /**
     * @return the seg2
     */
    public FlightSegment getSeg2() {
        return seg2;
    }

    /**
     * @param seg2 the seg2 to set
     */
    public void setSeg2(FlightSegment seg2) {
        this.seg2 = seg2;
    }

    /**
     * @return the transitTime
     */
    public int getTransitTime() {
        return transitTime;
    }

    /**
     * @param transitTime the transitTime to set
     */
    public void setTransitTime(int transitTime) {
        this.transitTime = transitTime;
    }

    /**
     * @return the totatTravelTime
     */
    public int getTotatTravelTime() {
        return totatTravelTime;
    }

    /**
     * @param totatTravelTime the totatTravelTime to set
     */
    public void setTotatTravelTime(int totatTravelTime) {
        this.totatTravelTime = totatTravelTime;
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