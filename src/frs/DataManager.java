package frs;
     
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Souryarup Ghosh
 */
public class DataManager {
      
    private String spiceJetFileName;
    private String silkAirFileName;
    FRSManager fRSManager;
     /**
     * Constructor of DataManager class. 
     * @param spiceJetFileName Name of file for spiceJet flight record.
     * @param silkAirFileName Name of file for silkAir flight record.
     * @param fRSManager is the FRSManager instance
     */
    public DataManager (String spiceJetFileName, String silkAirFileName,FRSManager fRSManager) {
        this.spiceJetFileName=spiceJetFileName;
        this.silkAirFileName=silkAirFileName;
        this.fRSManager=fRSManager;
    }
    /**
     * This function converts dd MMM yyyy HH:mm:ss format DateTime to dd/MM/yyyy HH:mm:ss format.
     * @param num Date in "dd MMM yyyy HH:mm:ss" format to be parsed
     * @return parsed DateTime in dd/MM/yyyy HH:mm:ss format
     */
    public DateTime DateConverter(String num){
        DateTime d;
        DateTime limit;
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMM yyyy HH:mm:ss");
        d = dtf.parseDateTime(num);
        limit=dtf.parseDateTime("13 NOV 2016 23:59:59");
        if(d.isAfter(limit))
            return limit;
        else
            return d;
    }
    /**
     * This function splits arrival and departure time separated by "/"
     * @param n String in departure/arrival format.
     * @return String array separating departure and arrival.
     */
    public String[] timeSpliter(String n){
        StringTokenizer s=new StringTokenizer(n,"/");
        String[] r={s.nextToken(),s.nextToken()};
        return r;
        
    }
    /**
     * Converts hh:mm aa format time to HHmm format.
     * @param n String containing time in 12hr format. 
     * @return String containing time in 24hr format.
     */
    public String twelveto24(String n){
        DateTimeFormatter localFormat = DateTimeFormat.forPattern("hh:mm aa");
        LocalTime localTime = LocalTime.parse(n, localFormat);
        DateTimeFormatter parseFormat = DateTimeFormat.forPattern("HHmm");
        return localTime.toString(parseFormat);
        
    }
     /**
     * Decodes the remark column of flights if any.
     * @param f Flight object to instantiate.
     * @param n Remark that is to be decoded.
     * @return Instantiated Flight object.
     */
    public Flight remarkDecoder(Flight f, String n){
        if(n.contains("Disc") && n.contains("Exc")){
            String desc=n.substring(0,n.indexOf(","));
            String exc=(n.substring(n.indexOf(",")+1).trim());
            String temp;
            int count=0;
            exc=exc.substring(exc.indexOf(" ")).trim();
            StringTokenizer st=new StringTokenizer(exc,",");
            DateTime d[]=new DateTime[st.countTokens()];
            while(st.hasMoreTokens()){
                temp=st.nextToken().trim();
                d[count]=DateConverter(temp.substring(3)+" "+temp.substring(0, 3)+" 2016 23:59:59");
                count++;
            }
            f.setEffTill(DateConverter(desc.substring((desc.indexOf(" ")+4)).trim()+" "+desc.substring(desc.indexOf(" ")+1,(desc.indexOf(" ")+4)).trim()+" 2016 23:59:59"));
            f.setExc(d);
        }
        
        else if(n.contains("Disc")){
            f.setEffTill(DateConverter(n.substring((n.indexOf(" ")+4)).trim()+" "+n.substring(n.indexOf(" ")+1,(n.indexOf(" ")+4)).trim()+" 2016 23:59:59"));
            f.setExc(null);          
        }
        else if(n.contains("Eff")){
            f.setEffFrom(DateConverter(n.substring((n.indexOf(" ")+4)).trim()+" "+n.substring(n.indexOf(" ")+1,(n.indexOf(" ")+4)).trim()+" 2016 00:00:00"));
            f.setExc(null);
        }
        else if(n.contains("-")){
            f.setEffFrom(DateConverter(n.substring(3,n.indexOf(" ")).trim()+" "+n.substring(0, 3).trim()+" 2016 00:00:00"));
            f.setEffTill(DateConverter(n.substring((n.lastIndexOf(" ")+4)).trim()+" "+n.substring(n.lastIndexOf(" ")+1,n.lastIndexOf(" ")+4).trim()+" 2016 23:59:59"));
            f.setExc(null);
        }       
        return f;
    }
    /**
     * This function reads and returns Flight array of SpiceJet.
     * @return Flight array of SpiceJet.
     */
    public Flight[] readSpiceJet(){
        int count=0;
        Flight spiceJet[]=new Flight[57];
        String line=null;
        String temp;
        Flight f;
        BufferedReader br=null;
        StringTokenizer st=null;
        try {
            FileReader flightFile = new FileReader(System.getProperty("user.dir") +File.separator+spiceJetFileName);
            
            br = new BufferedReader(flightFile);
            while((line= br.readLine()) !=null) {
                
                if(count>=5 && count<=61) {
                st=new StringTokenizer(line, "|");
              
                    while(st.hasMoreTokens()){
                  
                    f=new Flight();     
                    temp=st.nextToken().trim().toUpperCase();
                    if(temp.contains("DELHI"))
                        f.setOrigin(temp+" (DEL)");
                    else if(temp.contains("PUNE"))
                        f.setOrigin(temp+" (PNQ)");
                    else if(temp.contains("MUMBAI"))
                        f.setOrigin(temp+" (BOM)");   

                    temp=st.nextToken().trim().toUpperCase();

                    if(temp.contains("HYDERABAD"))
                        f.setDestination(temp+" (HYD)");
                    else if(temp.contains("KOLKATA"))
                        f.setDestination(temp+" (CCU)");
                    else if(temp.contains("BENGALURU"))
                        f.setDestination(temp+" (BLR)");
                    else if(temp.contains("CHENNAI"))
                        f.setDestination(temp+" (MAA)");    

                    f.setFrequency(st.nextToken().trim());
                    f.setFlightno(st.nextToken().trim()); 
                    f.setDeparture(twelveto24(st.nextToken().trim()));    
                    f.setArrival(twelveto24(st.nextToken().trim()));
                    temp=st.nextToken().trim().toUpperCase();

                    if(temp.contains("HYDERABAD"))
                        f.setVia(temp+" (HYD)");
                    else if(temp.contains("BENGALURU"))
                        f.setVia(temp+" (BLR)");
                    else if(temp.contains("-"))
                        f.setVia(temp.trim());

                    temp=st.nextToken().trim();
                    f.setEffFrom(DateConverter((temp).substring(0, 7)+"20"+temp.substring(7)+" 00:00:00"));
                    temp=st.nextToken().trim();
                    f.setEffTill(DateConverter(temp.substring(0, 7)+"20"+temp.substring(7)+" 23:59:59"));
                    f.setExc(null);
                    spiceJet[count-5]=f;         
                       
                    }
                }
                count++;
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println("Invalid File!");
            fRSManager.exit();
        }
        catch (NoSuchElementException e) {
            System.out.println("Invalid File!");
            fRSManager.exit();
        }
        catch (FileNotFoundException e) {
            System.out.println("FIle not Found!");
            fRSManager.exit();
        }
        catch (IOException ex) {
            System.out.print("Exception : "+ex.getMessage());
            System.exit(0);
        }     
        finally{
            try {
                if(br!=null)
                   br.close();
            } catch (IOException ex) {
                System.out.print("Exception : "+ex.getMessage());
            }
        }
        return spiceJet;
    }
    /**
     * This function reads and returns Flight array of SilkAir.
     * @return Flight array of SilkAir.
     */
    public Flight[] readSilkAir(){
        int count=0;
        Flight silkAir[]=new Flight[16];
        String line;
        StringTokenizer st=null;
        String temp[]=new String[2];
        int tokCount=0;
        BufferedReader br=null;
        Flight f;
        try {
            FileReader flightFile = new FileReader(System.getProperty("user.dir") +File.separator+silkAirFileName);
            br = new BufferedReader(flightFile);
            while((line= br.readLine()) !=null){
                
                if(count>=3 && count<=18){
                    st=new StringTokenizer(line, "|");

                    while(st.hasMoreTokens()){

                        f=new Flight(); 
                        tokCount=st.countTokens();
                        f.setOrigin(st.nextToken().trim().toUpperCase());
                        f.setDestination("SINGAPORE (SIN)");
                        f.setFrequency(st.nextToken().trim().toUpperCase());
                        f.setFlightno(st.nextToken().trim());
                        temp=timeSpliter(st.nextToken().trim());
                        f.setDeparture(temp[0]);
                        f.setArrival(temp[1]);
                        f.setVia("");
                        f.setEffFrom(DateConverter("01 SEP 2016 00:00:00"));
                        f.setEffTill(DateConverter("13 NOV 2016 23:59:59"));
                        if(tokCount>4) {
                            remarkDecoder(f,st.nextToken().trim());
                        }
                        silkAir[count-3]=f;
                    }
                }
                count++;
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println("Invalid File!");
            fRSManager.exit();
        }
        catch (NoSuchElementException e) {
            System.out.println("Invalid File!");
            fRSManager.exit();
        }
        catch (FileNotFoundException e) {
            System.out.println("FIle not Found!");
            fRSManager.exit();
        }
        catch (IOException ex) {
            System.out.print("Exception : "+ex.getMessage());
            System.exit(0);
        }     
        finally{
            try {
                if(br!=null)
                   br.close();
            } catch (IOException ex) {
                System.out.print("Exception : "+ex.getMessage());
            }
        }     
        return silkAir;
    }
    
    /**
     * This function writes the booking details into file.
     * @param b Booking object containing user booking data.
     */
    public void write(Booking b){
        FileWriter pasFile=null;
        BufferedWriter bw=null;
        Flight spiceJet=b.getComboFlight().getSeg1().getFlight();
        Flight silkAir=b.getComboFlight().getSeg2().getFlight();
        try{
            pasFile = new FileWriter(System.getProperty("user.dir")+File.separator+"bookingDetail.txt",true);
            bw=new BufferedWriter(pasFile);
            bw.append(" Flight Booking Number : "+String.format("%14s",b.getFlightBookingNumber())
            +" SpiceJet : "+String.format("(%8s)",spiceJet.getFlightno())
            +" from : "+String.format("%15s",spiceJet.getOrigin())
            +" will fly on : "+String.format("%20s",b.getComboFlight().getSeg1().getDepartureDate())
            +" via : "+String.format("%15s",spiceJet.getVia())
            +" at frequency : "+String.format("%30s",spiceJet.getFrequency())
            +" and reach : "+String.format("%15s",spiceJet.getDestination())
            +" at : "+spiceJet.getArrival()
            +" ---> TRANSIT TIME : "+b.getComboFlight().getTransitTime()
            +" SilkAir : "+String.format("(%8s)",silkAir.getFlightno())
            +" from : "+String.format("%20s",silkAir.getOrigin())
            +" will fly on : "+String.format("%20s",b.getComboFlight().getSeg2().getDepartureDate())
            +" at frequency : "+String.format("%30s",silkAir.getFrequency())
            +" and reach : "+String.format("%15s",silkAir.getDestination())
            +" at : "+String.format("%6s",silkAir.getArrival())
            +" TRAVEL TIME : "+String.format("%4s",b.getComboFlight().getTotatTravelTime())+" min");
            bw.newLine();
        }
        catch (FileNotFoundException e) {
            System.out.println("FIle not Found!");
        }
        catch (IOException ex) {
            System.out.print("\n"+ex.getMessage());
        }
        finally{
            try {
                if(bw!=null)
                   bw.close();
            } catch (IOException ex) {
                System.out.print("Exception : "+ex.getMessage());
            }
        }      
    }
    /**
     * This function updates PassengerCount file after transaction is complete.
     * @param b Booking object containing user booking data.
     */
    public void writePassenger(Booking b){
        FileReader readFile=null;
        FileWriter writeFile=null;
        BufferedReader br=null;
        BufferedWriter bw=null;
        StringTokenizer bt,af;
        String line;
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy");
        int passenger=b.getComboFlight().getNoOfPassenger();
        String spiceJetKey=b.getComboFlight().getSeg1().getFlightKey();
        String silkAirKey=b.getComboFlight().getSeg2().getFlightKey();
        String spiceJetDate=dtfOut.print(b.getComboFlight().getSeg1().getDepartureDate());
        String silkAirDate=dtfOut.print(b.getComboFlight().getSeg2().getDepartureDate());
        StringBuffer first,second,store;
        store=new StringBuffer("");
        try{
            readFile = new FileReader(System.getProperty("user.dir")+File.separator+"passengerCount.txt");
             
            br = new BufferedReader(readFile);
        
            while((line=br.readLine())!=null){
                af=new StringTokenizer(line, "?");
                first=new StringBuffer(af.nextToken());
                second=new StringBuffer(af.nextToken());
                if((first.indexOf(spiceJetKey)!=-1)){
                    passenger=Integer.parseInt(second.toString())+passenger;
                    store.append(first+"?"+String.format("%02d",passenger)+"?"+System.lineSeparator());
                }
                else if((first.indexOf(silkAirKey)!=-1)){
                    passenger=Integer.parseInt(second.toString())+passenger;
                    store.append(first+"?"+String.format("%02d",passenger)+"?"+System.lineSeparator());                  
                }
                else{
                    store.append(line+System.lineSeparator());
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("FIle not Found!");
        }
        catch (IOException ex) {
            System.out.print("\n"+ex.getMessage());
        }
        finally{
            try {
                if(br!=null)
                   br.close();
            } catch (IOException ex) {
                System.out.print("Exception : "+ex.getMessage());
            }
        }
        
        try{
            writeFile = new FileWriter(System.getProperty("user.dir")+File.separator+"passengerCount.txt");
            bw=new BufferedWriter(writeFile);
            bw.write(store.toString());
        }
        catch (FileNotFoundException e) {
            System.out.println("FIle not Found!");
        }
        catch (IOException ex) {
            System.out.print("\n"+ex.getMessage());
        }
        finally{
            try {
                if(bw!=null)
                    bw.close();
            } catch (IOException ex) {
                System.out.print("Exception : "+ex.getMessage());
            }
        } 
    }
    /**
     * This function reads PassengerCount from file.
     * @return String[][] array of Seat Record.
     */
    public String[][] readPassenger(){
        ArrayList<ArrayList<String>> childrenSuperList = new ArrayList<ArrayList<String>>();
        ArrayList<String> a;
        
        int count=0;
        FileReader pasFile=null;
        BufferedReader br=null;
        StringTokenizer bt,af;
        String line,first,second;   
        try{
             pasFile = new FileReader(System.getProperty("user.dir")+File.separator+"passengerCount.txt");
             br = new BufferedReader(pasFile);
             while((line=br.readLine())!=null){
                 af=new StringTokenizer(line, "?");
                 a = new ArrayList<>();
                 a.add(af.nextToken().trim());
                 a.add(af.nextToken().trim());
                 childrenSuperList.add(a);
             }
             
        }
        catch (FileNotFoundException e) {
            System.out.println("FIle not Found!");
        }
        catch (IOException ex) {
            System.out.print("\n"+ex.getMessage());
        }
        finally{
            try {
                if(br!=null)
                   br.close();
            } catch (IOException ex) {
                System.out.print("Exception : "+ex.getMessage());
            }
        }
        String[][] record = new String[childrenSuperList.size()][];
        int i = 0;
        for (ArrayList<String> row : childrenSuperList) {
            record[i] = new String[row.size()];

            for (int j=0;j<row.size();j++) {
                record[i][j] = row.get(j);       
            }
            i++;
        }
        return record;
    }
    /**
     * This function creates PassengerCount file if it doesn't exist.
     * @param spiceJet Flight array of Spicejet.
     * @param silkAir Flight array of SilkAir.
     */
    public void createPassenger(Flight[] spiceJet, Flight[] silkAir){
        FileWriter pasFile=null;
        File file=new File(System.getProperty("user.dir")+File.separator+"passengerCount.txt");
        BufferedWriter bw=null;
        DateTime start=null,end=null;
        DateTime date=null;
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy");
        try {
            if (file.createNewFile()) {
                pasFile = new FileWriter(file);
            bw = new BufferedWriter(pasFile);
            
            for(int i=0;i<spiceJet.length;i++){
                start=spiceJet[i].getEffFrom();
                end=spiceJet[i].getEffTill();
                while(!start.isAfter(end)){
                    bw.write(spiceJet[i].getFlightno()+"-"+dtfOut.print(start)+"?00?");
                    bw.newLine();
                    start=start.plusDays(1);
                }
                }
            for(int i=0;i<silkAir.length;i++){
                start=silkAir[i].getEffFrom();
                end=silkAir[i].getEffTill();
                while(!start.isAfter(end)){
                    bw.write(silkAir[i].getFlightno()+"-"+dtfOut.print(start)+"?00?");
                    bw.newLine();
                    start=start.plusDays(1);
                }
                }
            }
            else 
                return;
            
        
        }
        catch (FileNotFoundException e) {
            System.out.println("FIle not Found!");
        }
        catch (IOException ex) {
            System.out.print("\n"+ex.getMessage());
        }
        finally{
            try {
                if(bw!=null)
                   bw.close();
            } catch (IOException ex) {
                System.out.print("Exception : "+ex.getMessage());
            }
        }
    }
}
