package frs;


import java.util.Random;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sudipta Saha
 */
public class BookingManager {
    /**
     * Creates an object of booking with the following data as parameter and produces unique flight booking number.
     * @param name Name of Passenger booking tickets.
     * @param comboFlight Object of ComboFlight class.
     * @return Object of Booking class
     */
    public Booking book(String name,ComboFlight comboFlight){
        Booking booking=new Booking();
        booking.setComboFlight(comboFlight);
        booking.setName(name);
        booking.setFlightBookingNumber(generateFlightBookingNumber());
        return booking;
    }
    
    /**
     * Function to produce unique flight booking number by concatenating current Date and Time along with acronym of application.
     * @return Unique Flight booking number. 
     */
    public String generateFlightBookingNumber(){
        
        DateTimeFormatter dtfOut=DateTimeFormat.forPattern("ddMMyyyy-HHmmss");
        return "DSFRS-"+dtfOut.print(new DateTime());
    } 
    
}
