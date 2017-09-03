package frs;


import java.util.ArrayList;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sudipta Saha.Sourov Ghosh
 */
public class SearchManager{
    private String days[]={"MON","TUE","WED","THU","FRI","SAT","SUN"};
    private int minTransitTime;
    private int maxTransitTime;
     /**
     * This is the constructor of SearchManager to initialize minimum transit time and maximum transit time
     * @param minTransitTime is minimum transit time
     * @param maxTransitTime is maximum transit time
     */
    public SearchManager(int minTransitTime, int maxTransitTime) {
        this.maxTransitTime=maxTransitTime;
        this.minTransitTime=minTransitTime;
    }
    /**
     * This function iterates over Spicejet and SilkAir flights to find ComboFlights that will travel from source to destination at given DateTime d with passengers noOfPassenger satisfying required transit time limits.
     * @param source City from where the passenger will board a flight.
     * @param destination City where the passenger will disembark.
     * @param spiceJet Flight array of Spicejet. 
     * @param silkAir Flight array of SilkAir.
     * @param noOfPassenger Number of passenger to travel.
     * @param d DateTime of travel.
     * @param record String[][] array of passenger seat record.
     * @return ComboFlight array of routes that validate given conditions.
     */
    public ComboFlight[] search(String source,String destination, Flight spiceJet[], Flight silkAir[],int noOfPassenger,DateTime d, String[][] record){
      ArrayList<ComboFlight> comboFlights = new ArrayList();
      DateTime spiceJetDate,silkAirDate, silkAirArrival,spiceJetArrival;
      DateTimeFormatter dtfOut=DateTimeFormat.forPattern("dd/MM/yyyy");
      int totalTravelTime;
      FlightSegment seg1,seg2;
      int a[];
      ComboFlight cf;
       int position=0;
        for(int i=0;i<spiceJet.length;i++){
            if(spiceJet[i].getOrigin().contains(source)){         
                if(validateFlight(spiceJet[i], d)){
       
                    for(int j=0;j<silkAir.length;j++){
                        silkAirDate=validateSilkAir(spiceJet[i],silkAir[j],d,noOfPassenger,record);
                        if(silkAirDate!=null){
                            spiceJetDate=d;
                            spiceJetDate=spiceJetDate.plusHours(Integer.parseInt(spiceJet[i].getDeparture().substring(0,2)));
                            spiceJetDate=spiceJetDate.plusMinutes(Integer.parseInt(spiceJet[i].getDeparture().substring(2))); 
                            a=checkPassengerCount(spiceJet[i],silkAir[j],spiceJetDate,silkAirDate,noOfPassenger,record);
                            if(a!=null){
                                
                                cf=new ComboFlight();
                                seg1=new FlightSegment();
                                seg1.setFlight(spiceJet[i]);
                                seg1.setDepartureDate(spiceJetDate);
                                spiceJetArrival=getFlightArrival(spiceJetDate, spiceJet[i]);
                                seg1.setArrivalDate(spiceJetArrival);
                                seg1.setFlightKey(spiceJet[i].getFlightno()+"-"+dtfOut.print(spiceJetDate));
                                seg1.setNoOfPassenger(a[0]);
                            
                                seg2=new FlightSegment();
                                seg2.setFlight(silkAir[j]);
                                seg2.setDepartureDate(silkAirDate);
                                seg2.setFlightKey(silkAir[j].getFlightno()+"-"+dtfOut.print(silkAirDate));
                                silkAirArrival=getFlightArrival(silkAirDate, silkAir[j]);
                                seg2.setArrivalDate(silkAirArrival);
                                seg2.setNoOfPassenger(a[1]);
                                totalTravelTime=getTotalTime(spiceJetDate,silkAirArrival);
                                cf.setSeg1(seg1);
                                cf.setSeg2(seg2);
                                cf.setTotatTravelTime(totalTravelTime);
                                cf.setTransitTime(timeDef(spiceJet[i].getArrival(),silkAir[j].getDeparture()));
                                cf.setNoOfPassenger(noOfPassenger);
                                comboFlights.add(cf);
                            
                                position++;              
                        
                            }    
                            
                           }
                    }
                }
            }
        }
        return comboFlights.toArray(new ComboFlight[position]);
    }
    /**
     * Validates a flight based on effective period, frequency of journey and exclusive dates.
     * @param flight Flight object containing data about a flight.
     * @param d DateTime of travel.
     * @return true if valid else false.
     */
    public boolean validateFlight(Flight flight, DateTime d){   
        if(d.isBefore(flight.getEffFrom()) || d.isAfter(flight.getEffTill())){
           return false;
        }
        if(!(flight.getFrequency().equals("DAILY") || flight.getFrequency().contains(days[d.getDayOfWeek()-1]))) {         
            return false;
        }
        if(flight.getExc()!=null){     
            DateTime dr[]=flight.getExc();
            for(int i =0;i<flight.getExc().length;i++){
                if(d.toLocalDate().compareTo(dr[i].toLocalDate())==0){
                    return false;
                }  
            }
        }
        return true;
       }
    /**
     * This function validates SilkAir flight to match with a given SpiceJet flight.
     * @param spiceJet Flight of Spicejet.
     * @param silkAir Flight of SilkAir.
     * @param spiceJetDate DateTime of spicejet journey.
     * @param noOfPassenger Number of passengers to board at once.
     * @param record String[][] array of passenger seat record.
     * @return DateTime of silkAir journey if the SilkAir flight is valid else returns null.
    */ 
    private DateTime validateSilkAir(Flight spiceJet, Flight silkAir, DateTime spiceJetDate, int noOfPassenger, String[][] record) {
        DateTime silkAirDate=spiceJetDate;
          
        silkAirDate=silkAirDate.plusHours(Integer.parseInt(spiceJet.getArrival().substring(0,2)));
        silkAirDate=silkAirDate.plusMinutes(Integer.parseInt(spiceJet.getArrival().substring(2))); 
        if(silkAir.getOrigin().contains(spiceJet.getDestination())){          
            int tranTime=timeDef(spiceJet.getArrival(),silkAir.getDeparture());       
            if(tranTime>=minTransitTime && tranTime<=maxTransitTime){
              silkAirDate=silkAirDate.plusMinutes(tranTime);   
                if(validateFlight(silkAir, silkAirDate)){
                return silkAirDate;
                }  
            }
        }
        return null;
    }   
    
    /**
     * Calculates time difference fetched in HHmm format.
     * @param t1 Time1 in HHmm format 
     * @param t2 Time2 in HHmm format
     * @return Minutes between two given time.
     */
    public int timeDef(String t1,String t2){
        DateTimeFormatter dtf = DateTimeFormat.forPattern("HHmm");
        DateTime time1=dtf.parseDateTime(t1);
        DateTime time2=dtf.parseDateTime(t2);
        int diff=Minutes.minutesBetween(time1, time2).getMinutes();
        if(diff<0)
            return (1440+diff);
        else
            return diff;
        
    }

    /**
     * This function checks if the number of passenger to board the flight are available.
     * @param spiceJet Flight array of Spicejet.
     * @param silkAir Flight array of SilkAir.
     * @param spiceJetDate DateTime of SpiceJet departure.
     * @param silkAirDate DateTime of SilkAir departure.
     * @param noOfPassenger noOfPassenger Number of passenger to travel.
     * @param record String[][] array of passenger seat record.
     * @return Array which contains remaining passenger of each flight.
     */
    private int[] checkPassengerCount(Flight spiceJet, Flight silkAir, DateTime spiceJetDate,DateTime silkAirDate, int noOfPassenger, String[][] record) {
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy");
        
        String spiceJetKey=spiceJet.getFlightno()+"-"+dtfOut.print(spiceJetDate);
        String silkAirKey=silkAir.getFlightno()+"-"+dtfOut.print(silkAirDate);
         int spiceJetPassenger=0;
         int silkAirPassenger=0;                    
         int a[]=new int[2];           
        for(int i=0;i<record.length;i++){
            if(record[i][0].contains(spiceJetKey)){
                
                for(int j=i+1;j<record.length;j++){
                    if(record[j][0].contains(silkAirKey)){
                        spiceJetPassenger=15-Integer.parseInt(record[i][1]);
                        silkAirPassenger=15-Integer.parseInt(record[j][1]);                    
                        if(noOfPassenger<=spiceJetPassenger && noOfPassenger<=silkAirPassenger){
                            a[0]=spiceJetPassenger;
                            a[1]=silkAirPassenger;
                            return a;
                        }
                    }
                }
            }
        }
        
        
        return null;
    }


    /**
     * Calculates the total time of journey.
     * @param spiceJetArrival DateTime of spicejet departure.
     * @param silkAirArrival DateTime of silkAir departure.
     * @return Total travel time in minutes.
     */
    private int getTotalTime(DateTime spiceJetDeparture,DateTime silkAirArrival) {
        int diff=Minutes.minutesBetween(spiceJetDeparture,silkAirArrival).getMinutes()-150;
        if(diff<0)
            return (1440+diff);
        else
            return diff;
    }
    
    /**
     * 
     * @param flightDate DateTime of flight
     * @param flight Flight whose date needs to be calculated.
     * @return DateTime with time addition.
     */
    public DateTime getFlightArrival(DateTime flightDate, Flight flight){
        DateTime flightArrival=flightDate;
        DateTimeFormatter localFormat = DateTimeFormat.forPattern("HHmm");
        LocalTime localTime = LocalTime.parse(flight.getArrival().substring(0, 4), localFormat);
        flightArrival=flightArrival.withTime(localTime);
        if(flight.getArrival().length()>4)
            flightArrival=flightArrival.plusDays(1);
        
        
        return flightArrival;
    }
}