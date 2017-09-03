package frs;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Sourov Ghosh
 */
public class FRSManager {
    //the four managers
    private DataManager dataManager;
    private SearchManager searchManager;
    private BookingManager bookingManager;
    private DisplayManager displayManager;
    //Arrays containing the info of SpiceJet and SilkAir
    private Flight[] spiceJet;
    private Flight[] silkAir;
    
    private String[][] record;//Stores the passenger record
    private ComboFlight[] comboFlights;//Stores the search results
    private Booking booking;//Stores the booking
    //Stores the input information
    private String source, destination;
    private int np;
    private DateTime travelDate;
    private ComboFlight selectedFlight;
    
    
    /**
     * Constructor to initialize FRSManager
     * @param spiceJetFileName name of the csv file for SpiceJet
     * @param silkAirFileName name of the csv file for SilkAir
     * @param uiArgs preferred UI argument
     */
    public FRSManager(String spiceJetFileName, String silkAirFileName, String uiArgs) {
        dataManager=new DataManager(spiceJetFileName, silkAirFileName, this);
        spiceJet=dataManager.readSpiceJet();
        silkAir=dataManager.readSilkAir();
        dataManager.createPassenger(spiceJet, silkAir);
        
        searchManager=new SearchManager(120, 360);     
        bookingManager=new BookingManager();    
        record=dataManager.readPassenger();
        
        selectedFlight=null;
        //Selecting GUI version according to user input
        if (uiArgs==null) {
            displayManager=new GUIManager(this);
            
        }
        else if (uiArgs.equals("text")) {
            displayManager=new TextUIManager(this);
        }
        else {
            displayManager=new GUIManager(this);
        }   
        displayManager.showInputScreen();
    }
    
    /**
     * Main method
     * @param args user arguments
     */
    public static void main(String[] args) {
        FRSManager fRSManager=null ;
        if (args.length < 2) {
            System.out.println("Missing File Name!");
            System.exit(0);
        }
        else if (args.length == 2)
            fRSManager=new FRSManager(args[0], args[1], null);
        else
            fRSManager=new FRSManager(args[0], args[1], args[2]);
        //Boolean stop=false;   
    }
    
    /**
     * Update the passenger record
     * @param comboFlight flight record to updated
     */
    private void updatePassengerRecord(ComboFlight comboFlight) {
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy");
        String spiceJetKey=comboFlight.getSeg1().getFlightKey();
        String silkAirKey=comboFlight.getSeg2().getFlightKey();
        String spiceJetDate=dtfOut.print(comboFlight.getSeg1().getDepartureDate());
        String silkAirDate=dtfOut.print(comboFlight.getSeg2().getDepartureDate());
        for(int i=0;i<record.length;i++){
            if(record[i][0].contains(spiceJetKey)){
                for(int j=i+1;j<record.length;j++){
                    if(record[j][0].contains(silkAirKey)){
                        record[i][1]=String.valueOf(Integer.parseInt(record[i][1])+comboFlight.getNoOfPassenger());
                        record[j][1]=String.valueOf(Integer.parseInt(record[j][1])+comboFlight.getNoOfPassenger());
                        return;
                    
                    }
                }
            }    
        }
    }
    
    /**
     * Sets the search details in the variables
     * @param source is the name of the departure city
     * @param destination is the name of the destination city
     * @param np is the number of passengers
     * @param travelDate is the date of departure
     */
    public void setData(String source, String destination, int np, DateTime travelDate) {
        this.source=source;
        this.destination=destination;
        this.np=np;
        this.travelDate=travelDate;
    }
    /**
     * Return the flight available
     * @return ComboFlight Array
     */
    public ComboFlight[] searchFlights() {
        comboFlights=searchManager.search(source, destination, spiceJet, silkAir, np, travelDate, record);
        
        if(comboFlights.length!=0)
            return comboFlights;
        else 
            return null;
    }
    /**
     * Stores the selected flight chosen by the user
     * @param comboFlight flight that has been chosen
     */
    public void setSelectedFlight(ComboFlight comboFlight) {
        selectedFlight=comboFlight;
    }
    /**
     * Returns the selected flight from th user
     * @return ComboFlight object
     */
    public ComboFlight getSelectedFlight () {
        return selectedFlight;
    }
    /**
     * Books a flight for the user and update the details
     * @param name name of the passenger
     */
    public void bookFlight(String name) {
        booking=bookingManager.book(name, selectedFlight);
        dataManager.writePassenger(booking);
        updatePassengerRecord(selectedFlight);
        dataManager.write(booking);
        
    }
    /**
     * Returns the Booking object
     * @return Booking object
     */
    public Booking getBooking() {
        return booking;
    }           
    /**
     * Function to exit program
     */
    public void exit() {
        System.out.println("------------------ Closing ---------------------");
        System.exit(0);
    }
}
