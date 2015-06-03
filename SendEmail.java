// Program Name:	SendEmail.java
// Developer:				Gary Simpson 
// Date:       					May 26, 2015
/* Purpose:      			A helper class used to send email. 
  										This will send the specified text to an
  										admin email address.
  										
  										NOTE:
  										JavaMail API  - https://java.net/projects/javamail/pages/Home   and
  										Java Activation Framework (JAF) - http://www.oracle.com/technetwork/articles/java/index-135046.html								
  										are required for app to function properly.
  										
  										ADD mail.jar and activation.jar to CLASSPATH
  										  
  										  code studied from http://www.tutorialspoint.com/java/java_sending_email.htm
  										CHANGE LOG:
  */										  	

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
//import javax.activation.*;
									
public class SendEmail 
{



   public SendEmail(String oldIP, String newIP) //oldIP not used
   {    
   
      // Recipient's email ID needs to be mentioned.
      String to = " ---to_email--- ";
      // Sender's email ID needs to be mentioned
      String from = " ---from_email--- ";
   
      // Assuming you are sending email from localhost
     //-------- String host = "localhost";
   
      // Get system properties
      Properties properties = new Properties(); //System.getProperties();
   
   
      properties.put("mail.transport.protocol", "smtp");
      properties.put("mail.smtp.host", " --smtp.mail.com-- ");
      properties.put("mail.smtp.auth", "true");
   
      try
      {
         Authenticator auth = new SMTPAuthenticator();
         Session session = Session.getDefaultInstance(properties, auth);
         // uncomment for debugging infos to stdout
         // mailSession.setDebug(true);
         Transport transport = session.getTransport();
        
        
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);
      
         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));
         message.setSentDate(new Date());
      
         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(to));
      
         // Set Subject: header field
         message.setSubject("IP Address Changed!");
      
         // Now set the actual message
         message.setContent("Your IP address has changed. \n\nOld Address:  " + 
            								oldIP+ "\nNew Address:  " + newIP, "text/plain");
         
         
         transport.connect();
         transport.sendMessage(message,
             message.getRecipients(Message.RecipientType.TO));
         transport.close();
         
         
      //         // Send message
      //         Transport.send(message);
         System.out.println("Sent message successfully....");
      }// end try
      
      catch (MessagingException mex) 
      {
         mex.printStackTrace();
      }// end catch
   }// end defaut constructor SendEmail


   private class SMTPAuthenticator extends javax.mail.Authenticator {
      public PasswordAuthentication getPasswordAuthentication() {
         String username = " ---from_email--- ";
         String password = " ---password--- ";
         return new PasswordAuthentication(username, password);
      }
   }



}// end class SendEmail
