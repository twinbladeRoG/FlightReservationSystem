/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Sohan Duuta
 */
public class TextUIManager implements DisplayManager {
    
    FRSManager fRSManager;
    Scanner sc;
    
    String source, destination;
    int np;
    DateTime travelDate;
    
    ComboFlight[] comboFlights;
    ComboFlight selectedFlight;
    
    /**
     * Constructor for TextUIManager
     * @param fRSManager is the instance of the FRSManager 
     */
    public TextUIManager (FRSManager fRSManager) {
        this.fRSManager=fRSManager;
        comboFlights=null;
        destination="SINGAPORE";
        System.out.println("-------------------- Welcome to DarkSwag FRS --------------------");
        System.out.println("-----------------------------------------------------------------\n");
    }

    @Override
    public void showInputScreen() {   
        System.out.println("Select departure city :-");
        System.out.println("1. Delhi \n2. Pune \n3. Mumbai");
        int ch=0;
        do {
            System.out.println("\nEnter your choice:");
            try {
                sc=new Scanner(System.in);
                ch=sc.nextInt();//takes the user input
                if (ch >=1 && ch <=3)//check for valid input
                    break;
                else
                    System.out.println("Invalid Choice!");
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid choice!!");
            }
        }while (true);
        switch (ch) {
            case 1: source="Delhi"; break;
            case 2: source="Pune";  break;
            case 3: source="Mumbai"; break;
        }
        
        do {
            System.out.println("\nEnter number of passengers (1 to 10):");
            try {
                sc=new Scanner(System.in);
                np=sc.nextInt();//takes user input
                if (np >=1 && np <=10)//check for valid input
                    break;
                else
                    System.out.println("Invalid input!");
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid Input!");
            }
            
        } while (true);
        
        do {
            try {
                System.out.println("\nEnter travel date (dd/mm/yyyy):");
                String date=sc.next();//takes user input
                DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
                travelDate = formatter.parseDateTime(date);
            
                DateTime start = formatter.parseDateTime("30/09/2016");
                DateTime end   = formatter.parseDateTime("14/11/2016");
        
                if (travelDate.isAfter(start) && travelDate.isBefore(end)) //check for valid date input
                    break;
                else
                    System.out.println("Travel date must be between 01/10/2016 to 13/11/2016!");
            }
            catch (IllegalArgumentException e) {
                System.out.println("Invalid date!"); 
            } 
        } while (true);
        
        do {
            try {
                System.out.println("\n1. Proceed to Search \n2. Change search details \n3. Exit");
                System.out.println("Enter your Choice:");
                sc = new Scanner(System.in);
                ch = sc.nextInt();
                if (ch==1 || ch==2 || ch==3)
                    break;
                else
                    System.out.println("Invalid choice!");
            } 
            catch (InputMismatchException e) {
                System.out.println("Invalid choice!");
            }
        } while (true);
        
        switch (ch) {
            case 1:
                fRSManager.setData(source.toUpperCase(), destination.toUpperCase(), np, travelDate);

                comboFlights=fRSManager.searchFlights();
                if (comboFlights.length!=0) {
                    showSearchResultScreen();
                }
                else {
                    System.out.println("No flights available");
                    showInputScreen();
                }
                break;
            case 2:
                showInputScreen();
                break;
            case 3:
                exit();
                break;
        }
    }

    @Override
    public void showSearchResultScreen() {
        System.out.println("------------------------ Search Results -------------------------");
        System.out.println("-----------------------------------------------------------------\n");
        
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy");
        Flight spiceJet=null;
        Flight silkAir=null;
        
        
        System.out.print("Flight available from: "+comboFlights[0].getSeg1().getFlight().getOrigin()+
                         " To: "+comboFlights[0].getSeg2().getFlight().getDestination()+
                         " on: "+dtfOut.print(comboFlights[0].getSeg1().getDepartureDate())+"\n");
        
        for (int i=1;i<=169;i++)
            System.out.print("_");
        System.out.println("");
          
        System.out.println("| "+
                String.format("%-2s",  "#")            +" | "+
                String.format("%-8s",  "SpiceJet")     +" | "+
                String.format("%-18s", "via")          +" | "+
                String.format("%-5s",  "Seats")        +" | "+
                String.format("%-10s", "Dept. Time")   +" | "+
                String.format("%-12s", "Arrival Time") +" | "+
                String.format("%-7s",  "SilkAir")      +" | "+
                String.format("%-18s", "From")         +" | "+
                String.format("%-12s", "Transit Time") +" | "+
                String.format("%-10s", "Dept. Time")   +" | "+
                String.format("%-12s", "Arrival Time") +" | "+
                String.format("%-5s",  "Seats")        +" | "+
                String.format("%-10s", "Total Time") +" |");
        
        for (int i=1;i<=169;i++)
            System.out.print("_");
        System.out.println("");     
        
        for (int i=0;i<comboFlights.length;i++) {
            spiceJet=comboFlights[i].getSeg1().getFlight();
            silkAir=comboFlights[i].getSeg2().getFlight();
            //prints the flight details 
            System.out.print("| "+
                String.format("%-2s",  (i+1)) +" | "+
                String.format("%-8s",  spiceJet.getFlightno()) +" | "+
                String.format("%-18s", cityNameFormatter(spiceJet.getVia())) +" | "+
                String.format("%-5s",  comboFlights[i].getSeg1().getNoOfPassenger()) +" | "+
                String.format("%-10s", dateFormatter(spiceJet.getDeparture())) +" | "+
                String.format("%-12s", dateFormatter(spiceJet.getArrival())) +" | "+
                String.format("%-7s",  silkAir.getFlightno()) +" | "+
                String.format("%-18s", silkAir.getOrigin()) +" | "+
                String.format("%-12s", transitimeFormatter(comboFlights[i].getTransitTime())) +" | "+
                String.format("%-10s", dateFormatter(silkAir.getDeparture())) +" | "+
                String.format("%-12s", dateFormatter(silkAir.getArrival())) +" | "+
                String.format("%-5s",  comboFlights[i].getSeg2().getNoOfPassenger()) +" | "+
                String.format("%-10s", transitimeFormatter(comboFlights[i].getTotatTravelTime()))+" |");
            System.out.println();
            if (i == comboFlights.length-1) {
                for (int j=1;j<=169;j++)
                    System.out.print("_");
                System.out.println("");
            }
            else {
                for (int j=1;j<=169;j++)
                    System.out.print(".");
                System.out.println("");
            }
        }
              
        int flightIndex;
        do {
            System.out.println("\nSelect your flight:");
            try {
                sc=new Scanner(System.in);
                flightIndex=sc.nextInt();
                if (flightIndex>=1 && flightIndex<=comboFlights.length) 
                    break;
                else
                    System.out.println("Invalid choice!");
            }
            catch(InputMismatchException e) {
                System.out.println("Invalid Choice!");
            }
        } while(true);
        
        int ch;
        do {
            try {
                System.out.println("\n1. Proceed to Booking \n2. Search Again \n3. Select Different Flight \n4. Exit");
                System.out.println("Enter your Choice:");
                sc = new Scanner(System.in);
                ch = sc.nextInt();
                if (ch==1 || ch==2 || ch==3 || ch==4)
                    break;
                else
                    System.out.println("Invalid choice!");
            } 
            catch (InputMismatchException e) {
                System.out.println("Invalid choice!");
            }
        } while (true);
        
        switch (ch) {
            case 1:
                fRSManager.setSelectedFlight(comboFlights[flightIndex-1]);
                showBookingScreen();
            case 2:
                showInputScreen();
                break;
            case 3:
                showSearchResultScreen();
                break;
            case 4:
                exit();
                break;
        }
    }

    @Override
    public void showBookingScreen() {
        System.out.println("---------------------------- Booking ----------------------------");
        System.out.println("-----------------------------------------------------------------\n");
        
        String fname, lname;
        
        System.out.println("Enter first name: ");
        fname=sc.next();
        System.out.println("Enter last name:");
        lname=sc.next();
        
        int ch;
        do {
            try {
                System.out.println("\n1. Proceed to Confirmation \n2. Search Again \n3. Select Different Flight \n4. Change Name \n5. Exit");
                System.out.println("Enter your Choice:");
                sc = new Scanner(System.in);
                ch = sc.nextInt();
                if (ch==1 || ch==2 || ch==3 || ch==4 || ch==5)
                    break;
                else
                    System.out.println("Invalid choice!");
            } 
            catch (InputMismatchException e) {
                System.out.println("Invalid choice!");
            }
        } while (true);
        
        switch (ch) {
            case 1:
                fRSManager.bookFlight(fname.trim()+" "+lname.trim());                
                showConfirmationScreen();
                break;
            case 2:
                showInputScreen();
                break;
            case 3:
                showSearchResultScreen();
                break;
            case 4:
                showBookingScreen();
                break;
            case 5:
                exit();
                break;
        }
    }

    @Override
    public void showConfirmationScreen() {
        
        System.out.println("--------------------- Your Flight is Booked --------------------");
        System.out.println("-----------------------------------------------------------------\n");
        showBookingDetails(fRSManager.getBooking());
                
        int ch;
        do {
            try {
                System.out.println("\n1. Exit \n2. Search Again");
                System.out.println("Enter your choice:");
                sc = new Scanner(System.in);
                ch = sc.nextInt();
                if (ch==1 || ch==2)
                    break;
                else
                    System.out.println("Invalid choice!");
            } 
            catch (InputMismatchException e) {
                System.out.println("Invalid choice!");
            }
        } while (true);
        
        switch (ch) {
            case 1:
                exit();
                break;
            case 2:
                showInputScreen();
                break;
        }
    }
    /**
     * Function to exit the program
     */
    private void exit() {        
        System.out.println("----------------- Thank You For Flying With Us! -----------------");
        System.out.println("-----------------------------------------------------------------\n");
        if (sc!=null)
            sc.close();
        fRSManager.exit();
    }
    /**
     * Displays the booking details
     * @param b is the Booking object to be displayed
     */
    private void showBookingDetails(Booking b) {
        ComboFlight cf=b.getComboFlight();
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy");
        Flight spiceJet=cf.getSeg1().getFlight();
        Flight silkAir=cf.getSeg2().getFlight();
        
        System.out.println( "______________________________________________________________________________________________________________\n"+
                            "                                       DARKSWAG FLIGHT RESERVATION SYSTEM                                     \n"+
                            "--------------------------------------------------------------------------------------------------------------\n");
        System.out.println( String.format("%-120s", "Name: "+b.getName())+ 
                            "\n"+
                            String.format("%-120s", "Reference Number:"+b.getFlightBookingNumber())+                            
                            "\n"+
                            String.format("%-60s", "SpiceJet: "+spiceJet.getFlightno())+
                            String.format("%-60s", "SilkAir: "+silkAir.getFlightno())+
                            "\n"+
                            String.format("%-40s", cityNameFormatter(spiceJet.getOrigin() +" > ")) + 
                            String.format("%-40s", cityNameFormatter(silkAir.getOrigin()  +" > ")) + 
                            String.format("%-40s", cityNameFormatter(silkAir.getDestination()))   +
                            "\n"+
                            String.format("%-40s", dateFormatter(spiceJet.getDeparture()))  + 
                            String.format("%-40s", dateFormatter(spiceJet.getArrival())+" | "+dateFormatter(silkAir.getDeparture())) + 
                            String.format("%-40s", dateFormatter(silkAir.getArrival())) +
                            "\n"+
                            String.format("%-40s", dtfOut.print(cf.getSeg1().getDepartureDate())) + 
                            String.format("%-40s", dtfOut.print(cf.getSeg1().getArrivalDate()) +" | "+ dtfOut.print(cf.getSeg2().getDepartureDate())) + 
                            String.format("%-40s", dtfOut.print(cf.getSeg2().getArrivalDate()))   + 
                            "\n"+        
                            String.format("%-40s", "Transit Time:: "+transitimeFormatter(cf.getTransitTime())) + 
                            String.format("%-40s", "Total Time: "+transitimeFormatter(cf.getTotatTravelTime())) + 
                            String.format("%-40s", "Seats: "+cf.getNoOfPassenger())
        );
        System.out.println("______________________________________________________________________________________________________________\n");
    }
    
    /**
     * Formats a 24hour format time to 12hour format
     * @param date is the date to be formatted
     * @return formated date 12 hour format
     */
    private String dateFormatter(String date) {
        String hr=date.substring(0, 2);
        String min=date.substring(2, 4);
        String time=hr+":"+min;
        String formatTime=null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            SimpleDateFormat sdf12 = new SimpleDateFormat("KK:mm aa");
            Date dateObj = sdf.parse(time);
            formatTime=sdf12.format(dateObj);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return formatTime;
    }
    
    /**
     * Formats the city name in title case
     * @param name is the city name to be formatted
     * @return the formatted city name
     */
    private String cityNameFormatter(String name) {
        if (!name.equalsIgnoreCase("-")) {
            CharSequence ch="(";
            if (name.contains(ch)) {
                String fname=null;
                fname=name.substring(0,1).toUpperCase()+
                        name.substring(1, name.indexOf("(")).toLowerCase()+
                        name.substring(name.indexOf("("), name.length());
                return fname;
            }
            else {
                String fname=null;
                fname=name.substring(0,1).toUpperCase()+name.substring(1, name.length()).toLowerCase();
                return fname;
            }
        }
        else 
            return "-";
    }
    
    /**
     * Converts the time into hours and minutes
     * @param time the time to be formatted
     * @return formatted time for display
     */
    private String transitimeFormatter(int time) {
        String fTime = time/60 + "h " + time%60 +"m";
        return fTime;
    }
}
