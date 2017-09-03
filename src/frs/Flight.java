package frs;


import java.util.Date;
import org.joda.time.DateTime;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sourov Ghosh
 */
public class Flight {
    private String origin;
    private String destination;
    private String frequency;
    private String flightno;
    private String departure;
    private String arrival;
    private String via;
    private DateTime effFrom;
    private DateTime effTill;
    private DateTime exc[];
    /**
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return the frequency
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * @return the flightno
     */
    public String getFlightno() {
        return flightno;
    }

    /**
     * @param flightno the flightno to set
     */
    public void setFlightno(String flightno) {
        this.flightno = flightno;
    }

    /**
     * @return the departure
     */
    public String getDeparture() {
        return departure;
    }

    /**
     * @param departure the departure to set
     */
    public void setDeparture(String departure) {
        this.departure = departure;
    }

    /**
     * @return the arrival
     */
    public String getArrival() {
        return arrival;
    }

    /**
     * @param arrival the arrival to set
     */
    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    /**
     * @return the via
     */
    public String getVia() {
        return via;
    }

    /**
     * @param via the via to set
     */
    public void setVia(String via) {
        this.via = via;
    }

    /**
     * @return the effFrom
     */
    public DateTime getEffFrom() {
        return effFrom;
    }

    /**
     * @param effFrom the effFrom to set
     */
    public void setEffFrom(DateTime effFrom) {
        this.effFrom = effFrom;
    }

    /**
     * @return the effTill
     */
    public DateTime getEffTill() {
        return effTill;
    }

    /**
     * @param effTill the effTill to set
     */
    public void setEffTill(DateTime effTill) {
        this.effTill = effTill;
    }

    /**
     * @return the exc
     */
    public DateTime[] getExc() {
        return exc;
    }

    /**
     * @param exc the exc to set
     */
    public void setExc(DateTime[] exc) {
        this.exc = exc;
    }

    /**
     * @return the effFrom
     */
 
}
