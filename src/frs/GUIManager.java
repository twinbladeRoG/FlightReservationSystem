package frs;

import org.joda.time.DateTime;

/**
 * 
 * @author Sohan
 */
public class GUIManager implements DisplayManager{

    private InputScreen inScr;
    private SearchResultScreen sRScreen;
    private BookingScreen bookScreen;
    private ConfirmationScreen conScreen;
        
    private FRSManager fRSManager;
    
    /**
     * Constructor for GUIManager
     * @param fRSManager is the reference of the FRSManager
     */
    public GUIManager(FRSManager fRSManager) {
        this.fRSManager=fRSManager;
        //Initializing the GUI Screens
        inScr = new InputScreen(this);
        sRScreen = new SearchResultScreen(this);
        bookScreen = new BookingScreen(this);
        conScreen = new ConfirmationScreen(this);
    }
    
    /**
     * Calls the FRSManager to search for Flights
     * Sets the table in SearchResultScreen
     */
    public void callSearch() {
        if (fRSManager.searchFlights()!=null)
        sRScreen.setTable(fRSManager.searchFlights());
    }
    
    /**
     * Sets the data in the FRSManager
     * @param source departure city
     * @param destination destination city
     * @param np number of passengers
     * @param travelDate date of travel
     */
    public void setData(String source, String destination, int np, DateTime travelDate) {
        fRSManager.setData(source, destination, np, travelDate);
    }
    /**
     * Sets the selected ComboFlight by the user
     * @param cf is the selected comboflight
     */
    public void setSelectedFlight(ComboFlight cf) {
        fRSManager.setSelectedFlight(cf);
        bookScreen.setFlightDetails(fRSManager.getSelectedFlight());
    }
    /**
     * Books the flight selected by the user
     * @param name is the name of the passenger
     */
    public void bookFlight(String name) {
        fRSManager.bookFlight(name);
    }
    /**
     * Sets the booking information in the Confirmation screen
     */
    public void setConfirmationDetails() {
        conScreen.setDetails(fRSManager.getBooking());
    }
    
    @Override
    public void showInputScreen() {
        inScr.setVisible(true);
        sRScreen.setVisible(false);
        bookScreen.setVisible(false);
        conScreen.setVisible(false);
    }

    @Override
    public void showSearchResultScreen() {
        inScr.setVisible(false);
        sRScreen.setVisible(true);
        bookScreen.setVisible(false);
        conScreen.setVisible(false);
    }

    @Override
    public void showBookingScreen() {
        inScr.setVisible(false);
        sRScreen.setVisible(false);
        bookScreen.setVisible(true);
        conScreen.setVisible(false);
    }

    @Override
    public void showConfirmationScreen() {
        inScr.setVisible(false);
        sRScreen.setVisible(false);
        bookScreen.setVisible(false);
        conScreen.setVisible(true);
    }
    
}
