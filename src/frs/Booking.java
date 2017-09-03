package frs;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sudipta Saha
 */
public class Booking {
    private ComboFlight comboFlight;
    private String name;
    private String flightBookingNumber; 

    /**
     * @return the comboFlight
     */
    public ComboFlight getComboFlight() {
        return comboFlight;
    }

    /**
     * @param comboFlight the comboFlight to set
     */
    public void setComboFlight(ComboFlight comboFlight) {
        this.comboFlight = comboFlight;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the flightBookingNumber
     */
    public String getFlightBookingNumber() {
        return flightBookingNumber;
    }

    /**
     * @param flightBookingNumber the flightBookingNumber to set
     */
    public void setFlightBookingNumber(String flightBookingNumber) {
        this.flightBookingNumber = flightBookingNumber;
    }
}
