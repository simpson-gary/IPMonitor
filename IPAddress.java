// Program Name:	IPAddress.java
// Developer:				Gary Simpson 
// Date:       					September 29, 2014
// Purpose:      			To retrieve IP address of the local machine and 
//										public facing address as well. To then store this 
//										information in a text file and send a notification 
//										when the information changes in any way.
//						
//										CHANGE LOG:
//										  	-
//										 	-


import java.io.*;
import java.net.*;
import java.util.*;
import java.net.HttpURLConnection; // used for http connection
import java.util.regex.Matcher;// used for Pattern Matcher
import java.util.regex.Pattern;// used for Patter Matcher
import java.util.logging.Logger;// used for Logger
import static java.lang.System.out;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;


public class IPAddress extends JFrame implements ActionListener
{
   private static final int DELAY = 60000;// 60000 delay = 1 minute
   private static final int LOOP_COUNT = 180;//180 =3hr total number of time to loop timer
   private static int i = 0; // counter used for timer loop
   private static Timer stopWatch;//timer driver
   static IPAddress frame = new IPAddress(); // create frame
   private BorderLayout layout; // layout for main window
   private Container con;
   
   public static String oldIP = "67.8.200.194"; 
   public static String currentIP = "";
   public static JLabel lblWANAddress;
   public static JLabel lblWANAddressDisplay;
   // Create container panels
   
   
   public  IPAddress()
   {
      super("IP Address");
      layout = new BorderLayout(5,5);// new border layout
      Container con = getContentPane();
      setLayout(layout);//set frame layout
      
      lblWANAddress = new JLabel("WAN IP Address:");
      lblWANAddressDisplay = new JLabel("\t" + oldIP );
      
      con.add(lblWANAddress, BorderLayout.NORTH);
      con.add(lblWANAddressDisplay, BorderLayout.CENTER);
      startStopWatch();
   }//end constructor IPAddress
   
   public static void main(String args[]) throws SocketException
   {
      final int FRAME_WIDTH = 256, FRAME_HEIGHT = 128; // set frame with and height.
      frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
      frame.setSize ( FRAME_WIDTH, FRAME_HEIGHT); //set the size of the Frame
      frame.setVisible( true ) ; // display frame
      
      displayIP();
      
      Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
      for (NetworkInterface netint : Collections.list(nets)) displayInterfaceInformation(netint);
      
   }// end method main
   
   public void actionPerformed(ActionEvent event)
   {
   }// end method actionPerformed
   
   static void displayInterfaceInformation(NetworkInterface netint) throws SocketException 
   {
      out.printf("Display name: %s\n", netint.getDisplayName());
      out.printf("Name: %s\n", netint.getName());
      Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
      for (InetAddress inetAddress : Collections.list(inetAddresses)) 
      {
         out.printf("InetAddress: %s\n", inetAddress);
      }
      out.printf("\n");
      
      //getRouterIP();
   }// end method main
   
   public static void displayIP()
   { 
      try {
         java.net.URL url = new java.net.URL(
                    "http://checkip.amazonaws.com/");
         
         java.net.HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
         
         java.io.InputStream stream = con.getInputStream();
         
         java.io.InputStreamReader reader = new java.io.InputStreamReader(
                    stream);
         
         java.io.BufferedReader bReader = new java.io.BufferedReader(reader);
         currentIP =   bReader.readLine() ;
         
         System.out.print("Check #1, Your WAN IP address is:  " + currentIP +"\n\n");
         lblWANAddressDisplay.setText( "\t " + currentIP );
      }//end try 
      catch (Exception e)
      {
         e.printStackTrace();
      }// end catch
         //System.out.println(InetAddress.getLocalHost().getHostAddress());
   }// end method displayIP
   
   public static void sendEmail()
   {
      SendEmail send = new SendEmail(oldIP, currentIP);
      out.println("Address sent to SendEmail class.");
   }// end method sendEmail
   
   
   // start stopwatch
   public void startStopWatch()
   {
      // create timer
      stopWatch = new Timer(DELAY , new StopWatchHandler() ); // .start used to avoid stopWatch.start(); to start timer
      stopWatch.start();
   } // end startTimer method

   
    // stop stop watch
   public static void stopStopWatch()
   {
      stopWatch.stop();
   } // end stop watch method

   
   private class StopWatchHandler implements ActionListener
   {
        // respond to Timer's event
      public void actionPerformed( ActionEvent actionEvent )
      {
         i++;
         displayIP();
         if(i == LOOP_COUNT)
         {	
            if(  !oldIP.equals(currentIP)  )
            {
               sendEmail();
               i = 0;
            }// end if statement for ip check	
         }// END IF FOR LOOP COUNT
      }  //  end method actionPerformed
   }// end class timer handler
   
   
} // end IP Address
