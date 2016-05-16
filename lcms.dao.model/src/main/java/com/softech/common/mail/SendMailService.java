package com.softech.common.mail;

import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

public class SendMailService {
   private static Logger log = Logger.getLogger(SendMailService.class.getName());
		
   public static boolean sendSMTPMessage(String[] toAddr,
                                         String[] ccAddr,
                                         String[] bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body) {


      return sendSMTPMessage(toAddr,
                             ccAddr,
                             bccAddr,
                             fromAddr,
                             fromAddrPersonalName,
                             subject,
                             body,
                             null,
                             null,
                             null,
                             null);
   }


   public static boolean sendSMTPMessage(String[] toAddr,
                                         String[] ccAddr,
                                         String[] bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         byte[] fileData,
                                         String filename,
                                         String attachment,
                                         Map<Object,Object> headers) {


      Session session = Session.getDefaultInstance(LCMSProperties.getLCMSProperties(), null);
      session.setDebug(log.isDebugEnabled());

      try {

          // create a message
          MimeMessage msg = new MimeMessage(session);

          InternetAddress address = new InternetAddress(fromAddr);
          if ( fromAddrPersonalName != null ) {
             try {
                address.setPersonal(fromAddrPersonalName);
             }
             catch(java.io.UnsupportedEncodingException uee) {
               log.error("Could not set Personal Name:"+fromAddrPersonalName, uee);
             }
          }
          msg.setFrom(address);

          InternetAddress[] toAddresses = new InternetAddress[toAddr.length];
          for ( int i = 0; i < toAddr.length; ++i) {
             toAddresses[i] = new InternetAddress(toAddr[i]);

          }
          msg.setRecipients(Message.RecipientType.TO, toAddresses);


          if ( ccAddr != null && ccAddr.length > 0 ) {
             InternetAddress[] ccAddresses = new InternetAddress[ccAddr.length];
             for ( int i = 0; i < ccAddr.length; ++i) {
                ccAddresses[i] = new InternetAddress(ccAddr[i]);
           }
             msg.setRecipients(Message.RecipientType.CC, ccAddresses);
          }


          if ( bccAddr != null && bccAddr.length > 0 ) {
             InternetAddress[] bccAddresses = new InternetAddress[bccAddr.length];
             for ( int i = 0; i < bccAddr.length; ++i) {
                bccAddresses[i] = new InternetAddress(bccAddr[i]);
             }
             msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
          }


          msg.setSubject(subject);
          msg.setSentDate(new Date());

          // create and fill the first message part
          MimeBodyPart mbp1 = new MimeBodyPart();
          mbp1.setContent(body, "text/html");

          // create the Multipart and its parts to it
          Multipart mp = new MimeMultipart();
          mp.addBodyPart(mbp1);


             // Set the body part headers... if any were specified
             if (headers!=null && !headers.isEmpty()) {
                 /*for (Iterator<Object> iter=headers.keySet().iterator(); iter.hasNext(); ) {
                     String key = (String) iter.next();
                     String value = (String) headers.get(key);
                     mbp1.setHeader(key, value);
                 }*/
                 for (Map.Entry<Object,Object> iter : headers.entrySet()) {
                     mbp1.setHeader(String.valueOf(iter.getKey()), String.valueOf(iter.getValue()));
                 }

             }


          if (fileData != null ) {
             // create the second message part
             /*MimeBodyPart mbp2 = new MimeBodyPart();

             // attach the file to the message - TODO:  change to actual mime type of attachment.
             mbp2.setDataHandler(new DataHandler(attachment, "text/plain"));
             mbp2.setFileName(filename);*/
             
                           
              // Set the email attachment file             
              MimeBodyPart attachmentPart = new MimeBodyPart(); 
               String mimneType= "application/octet-stream";
               ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(fileData,mimneType) ;                  
                  
              /*FileDataSource fileDataSource = new FileDataSource(filePath) {
              @Override
              public String getContentType() {
                 return "application/octet-stream";
                }
              };*/
              attachmentPart.setDataHandler(new DataHandler(byteArrayDataSource));
              attachmentPart.setFileName(filename);
             
              mp.addBodyPart(attachmentPart);

          }

          // add the Multipart to the message
          msg.setContent(mp);

          // send the message
          Transport.send(msg);
      }
      catch (MessagingException mex) {
         log.error("Exception occurred during send of email message", mex);
         Exception ex = null;
         if ((ex = mex.getNextException()) != null) {
            log.error("NestedException:", ex);
            ex.printStackTrace();
         }
         return false;
      }
      
      return true;

   }

   public static boolean sendSMTPMessageMIME(String[] toAddr,
                                         String[] ccAddr,
                                         String[] bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         String filename,
                                         String attachment) {

   //THIS METHOD ADDED TO SUPPORT .JAR ATTACHMENTS
      Session session = Session.getDefaultInstance(LCMSProperties.getLCMSProperties(), null);
      session.setDebug(log.isDebugEnabled());

      try {

          // create a message
          MimeMessage msg = new MimeMessage(session);

          InternetAddress address = new InternetAddress(fromAddr);
          if ( fromAddrPersonalName != null ) {
             try {
                address.setPersonal(fromAddrPersonalName);
             }
             catch(java.io.UnsupportedEncodingException uee) {
               log.error("Could not set Personal Name:"+fromAddrPersonalName, uee);
             }
          }
          msg.setFrom(address);


          InternetAddress[] toAddresses = new InternetAddress[toAddr.length];
          for ( int i = 0; i < toAddr.length; ++i) {
             toAddresses[i] = new InternetAddress(toAddr[i]);

          }
          msg.setRecipients(Message.RecipientType.TO, toAddresses);


          if ( ccAddr != null && ccAddr.length > 0 ) {
             InternetAddress[] ccAddresses = new InternetAddress[ccAddr.length];
             for ( int i = 0; i < ccAddr.length; ++i) {
                ccAddresses[i] = new InternetAddress(ccAddr[i]);
           }
             msg.setRecipients(Message.RecipientType.CC, ccAddresses);
          }


          if ( bccAddr != null && bccAddr.length > 0 ) {
             InternetAddress[] bccAddresses = new InternetAddress[bccAddr.length];
             for ( int i = 0; i < bccAddr.length; ++i) {
                bccAddresses[i] = new InternetAddress(bccAddr[i]);
             }
             msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
          }


          msg.setSubject(subject);
          msg.setSentDate(new Date());

          // create and fill the first message part
          MimeBodyPart mbp1 = new MimeBodyPart();
          mbp1.setContent(body, "text/html");

          // create the Multipart and its parts to it
          Multipart mp = new MimeMultipart();
          mp.addBodyPart(mbp1);


          if (filename != null ) {
             // create the second message part
             MimeBodyPart mbp2 = new MimeBodyPart();

             // attach the file to the message
             DataSource ds = new  FileDataSource(attachment);
             mbp2.setDataHandler(new DataHandler(ds));
             mbp2.setFileName(filename);
             mp.addBodyPart(mbp2);

          }

          // add the Multipart to the message
          msg.setContent(mp);

          // send the message
          Transport.send(msg);
      }
      catch (MessagingException mex) {
         log.error("Exception occurred during send of email message", mex);
         Exception ex = null;
         if ((ex = mex.getNextException()) != null) {
            log.error("NestedException:", ex);
            ex.printStackTrace();
         }
         return false;
      }catch(NullPointerException ex){
          log.error("Exception occurred during send of email message", ex);

      }
      return true;

   }



   public static boolean sendSMTPMessage(String[] toAddr,
                                         String[] ccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body) {
      return sendSMTPMessage(toAddr, ccAddr, null, fromAddr, fromAddrPersonalName, subject, body);
   }


   public static boolean sendSMTPMessage(String[] toAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body) {
      return sendSMTPMessage(toAddr, null, null, fromAddr, fromAddrPersonalName, subject, body);
   }


   public static boolean sendSMTPMessage(String toAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body) {
      String[] toAddrs = new String[1];
      toAddrs[0] = toAddr;
      return sendSMTPMessage(toAddrs, null, null, fromAddr, fromAddrPersonalName, subject, body);
   }


   public static boolean sendSMTPMessage(String[] toAddrs,
                                         String[] ccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         byte[] fileData,
                                         String filename) {
      
      return sendSMTPMessage(toAddrs, ccAddr, null, fromAddr, fromAddrPersonalName, subject, body, fileData,filename, null,null);
   }

   public static boolean sendSMTPMessageMIME(String toAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         String filename,
                                         String attachment) {
      String[] toAddrs = new String[1];
      toAddrs[0] = toAddr;
      return sendSMTPMessageMIME(toAddrs, null, null, fromAddr, fromAddrPersonalName, subject, body, filename, attachment);
   }

    public static boolean sendSMTPMessage(String[] toAddr,
                                         String[] ccAddr,
                                         String[] bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         HashMap<Object,Object> headers) {


        Session session = Session.getDefaultInstance(LCMSProperties.getLCMSProperties(), null);
        session.setDebug(log.isDebugEnabled());

        try {

            // Create a message
            MimeMessage msg = new MimeMessage(session);

            // Set the from address
            InternetAddress address = new InternetAddress(fromAddr);
            if ( fromAddrPersonalName != null ) {
                try {
                    address.setPersonal(fromAddrPersonalName);
                }
                catch(java.io.UnsupportedEncodingException uee) {
                    log.error("Could not set Personal Name:"+fromAddrPersonalName, uee);
                }
            }
            msg.setFrom(address);

            // Set the to addresses
            InternetAddress[] toAddresses = new InternetAddress[toAddr.length];
            for ( int i = 0; i < toAddr.length; ++i) {
                toAddresses[i] = new InternetAddress(toAddr[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, toAddresses);

            // Set the CC addresses
            if ( ccAddr != null && ccAddr.length > 0 ) {
                InternetAddress[] ccAddresses = new InternetAddress[ccAddr.length];
                for ( int i = 0; i < ccAddr.length; ++i) {
                    ccAddresses[i] = new InternetAddress(ccAddr[i]);
                }
                msg.setRecipients(Message.RecipientType.CC, ccAddresses);
            }

            // Set the BCC addresses
            if ( bccAddr != null && bccAddr.length > 0 ) {
                InternetAddress[] bccAddresses = new InternetAddress[bccAddr.length];
                for ( int i = 0; i < bccAddr.length; ++i) {
                    bccAddresses[i] = new InternetAddress(bccAddr[i]);
                }
                msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
            }

            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // Create the Multipart
            Multipart mp = new MimeMultipart();

            // Create the body part
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(body, "text/html");
            mp.addBodyPart(bodyPart);

            // Set the body part headers... if any were specified
            if (headers!=null && !headers.isEmpty()) {
                /*for (Iterator<Object> iter=headers.keySet().iterator(); iter.hasNext(); ) {
                    String key = (String) iter.next();
                    String value = (String) headers.get(key);
                    bodyPart.setHeader(key, value);
                }*/
                for (Map.Entry<Object,Object> iter : headers.entrySet()) {
                    bodyPart.setHeader(String.valueOf(iter.getKey()), String.valueOf(iter.getValue()));
                }
            }

            // Add the Multipart to the message
            msg.setContent(mp);

            // Send the message
            Transport.send(msg);
      }catch (MessagingException mex) {
         log.error("Exception occurred during send of email message", mex);
         Exception ex = null;
         if ((ex = mex.getNextException()) != null) {
            log.error("NestedException:", ex);
            ex.printStackTrace();
         }
         return false;
      }catch(NullPointerException ex){
            log.error("Exception occurred during send of email message", ex);

        }
      return true;

   }


    public static boolean sendSMTPMessage(String toAddr,
                                         String ccAddr,
                                         String bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         HashMap<Object,Object> headers) {

        String[] toAddrs = null;
        if (toAddr!=null) {
            toAddrs = new String[1];
            toAddrs[0] = toAddr;
        }

        String[] ccAddrs = null;
        if (ccAddr!=null) {
            ccAddrs = new String[1];
            ccAddrs[0] = ccAddr;
        }

        String[] bccAddrs = null;
        if (bccAddr!=null) {
            bccAddrs = new String[1];
            bccAddrs[0] = bccAddr;
        }

      return sendSMTPMessage(toAddrs, ccAddrs, bccAddrs, fromAddr, fromAddrPersonalName, subject, body, headers);
    }


    public static boolean sendSMTPMessage(String smtpHost,
    									 String smtpPort,
                                         String[] toAddr,
                                         String[] ccAddr,
                                         String[] bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         HashMap<Object,Object> headers) {


        Properties tempProps = new Properties();
        tempProps.put("mail.smtp.host", smtpHost);
        tempProps.put("mail.smtp.port", smtpPort);
        Session session = Session.getDefaultInstance(tempProps, null);
        session.setDebug(log.isDebugEnabled());

        try {

            // Create a message
            MimeMessage msg = new MimeMessage(session);

            // Set the from address
            InternetAddress address = new InternetAddress(fromAddr);
            if ( fromAddrPersonalName != null ) {
                try {
                    address.setPersonal(fromAddrPersonalName);
                }
                catch(java.io.UnsupportedEncodingException uee) {
                    log.error("Could not set Personal Name:"+fromAddrPersonalName, uee);
                }
            }
            msg.setFrom(address);

            // Set the to addresses
            InternetAddress[] toAddresses = new InternetAddress[toAddr.length];
            for ( int i = 0; i < toAddr.length; ++i) {
                toAddresses[i] = new InternetAddress(toAddr[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, toAddresses);

            // Set the CC addresses
            if ( ccAddr != null && ccAddr.length > 0 ) {
                InternetAddress[] ccAddresses = new InternetAddress[ccAddr.length];
                for ( int i = 0; i < ccAddr.length; ++i) {
                    ccAddresses[i] = new InternetAddress(ccAddr[i]);
                }
                msg.setRecipients(Message.RecipientType.CC, ccAddresses);
            }

            // Set the BCC addresses
            if ( bccAddr != null && bccAddr.length > 0 ) {
                InternetAddress[] bccAddresses = new InternetAddress[bccAddr.length];
                for ( int i = 0; i < bccAddr.length; ++i) {
                    bccAddresses[i] = new InternetAddress(bccAddr[i]);
                }
                msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
            }

            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // Create the Multipart
            Multipart mp = new MimeMultipart();

            // Create the body part
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(body, "text/html");
            mp.addBodyPart(bodyPart);

            // Set the body part headers... if any were specified
            if (headers!=null && !headers.isEmpty()) {
                /*for (Iterator<Object> iter=headers.keySet().iterator(); iter.hasNext(); ) {
                    //String key = (String) iter.next();
                    String value = (String) headers.get(key);
                    bodyPart.setHeader(key, value);
                }*/
                for (Map.Entry<Object,Object> iter : headers.entrySet()) {
                    bodyPart.setHeader(String.valueOf(iter.getKey()), String.valueOf(iter.getValue()));
                }
            }

            // Add the Multipart to the message
            msg.setContent(mp);

            // Send the message
            Transport.send(msg);
      }
      catch (MessagingException mex) {
         log.error("Exception occurred during send of email message", mex);
         Exception ex = null;
         if ((ex = mex.getNextException()) != null) {
            log.error("NestedException:", ex);
            ex.printStackTrace();
         }
         return false;
      }catch(NullPointerException ex){
            log.error("Exception occurred during send of email message", ex);

        }
      return true;

   }


    public static boolean sendSMTPMessage(String smtpHost,
    									 String smtpPort,
                                         String toAddr,
                                         String ccAddr,
                                         String bccAddr,
                                         String fromAddr,
                                         String fromAddrPersonalName,
                                         String subject,
                                         String body,
                                         HashMap<Object,Object> headers) {

        String[] toAddrs = null;
        if (toAddr!=null) {
            toAddrs = new String[1];
            toAddrs[0] = toAddr;
        }

        String[] ccAddrs = null;
        if (ccAddr!=null) {
            ccAddrs = new String[1];
            ccAddrs[0] = ccAddr;
        }

        String[] bccAddrs = null;
        if (bccAddr!=null) {
            bccAddrs = new String[1];
            bccAddrs[0] = bccAddr;
        }

      return sendSMTPMessage(smtpHost, smtpPort, toAddrs, ccAddrs, bccAddrs, fromAddr, fromAddrPersonalName, subject, body, headers);
    }
}
