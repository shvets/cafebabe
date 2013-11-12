// SMTPClient.java

package org.sf.cafebabe.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.io.UnsupportedEncodingException;

//import org.sf.net.AbstractClient;

// simple implementation of SMTP mail client

public class SMTPClient /*extends AbstractClient */ {
//  static final String EOL = "\r\n"; // network end of line
  //
  static final short DEFAULT_PORT = 25;
//
//  protected Socket socket;
//  protected DataInputStream in;
//  protected PrintStream out;
//
//  private String helohost;
//  private String sender;
//  private String receiver;
//  private String subject;
//  private String body;

  private int port = DEFAULT_PORT;

  private String host;
  private String fromEmail;
  private String fromName;
  private String toEmail;
  private String toName;
  private String subject;
  private String body;

  public SMTPClient(String host, String fromEmail, String fromName, String toEmail, String toName, String subject, String body) {
    this.host = host;
    this.fromEmail = fromEmail;
    this.fromName = fromName;
    this.toEmail = toEmail;
    this.toName = toName;
    this.subject = subject;
    this.body = body;
  }

//
//  public void writeRequest(Object request) throws IOException {
//    out.println((String)request);
//  }
//
//  public Object readResponse() throws IOException {
//    return in.readLine();
//  }
//
//  public void connect() throws IOException {
//    socket = new Socket(host, port);
//
//    in  = new DataInputStream(socket.getInputStream());
//    out = new PrintStream(socket.getOutputStream());
//  }
//
//  /**
//   * Disconnection from the server.
//   *
//   * @exception  IOException  if an I/O error occurs.
//   */
//  protected void disconnect() throws IOException {
//    try {
//      if(in != null)
//        in.closeFile();
//      if(out != null)
//        out.closeFile();
//      if(socket != null)
//        socket.closeFile();
//    }
//    finally {
//      in = null; out = null; socket = null;
//    }
//  }
//
//  public void setHeloHost(String helohost) {
//    this.helohost = helohost;
//  }
//
//  public void setSender(String sender) {
//    this.sender = sender;
//  }
//
//  public void setReceiver(String receiver) {
//    this.receiver = receiver;
//  }
//
//  public void setSubject(String subject) {
//    this.subject = subject;
//  }
//
//  public void setBody(String body) {
//    this.body = body;
//  }
//
//  public void interact() throws IOException {
//    connect();
//
//    stepProtocol("220", null);
//    stepProtocol("250", "HELO " + helohost);
//    stepProtocol("250", "MAIL FROM:" + sender);
//    stepProtocol("250", "RCPT TO:" + receiver);
//    stepProtocol("354", "DATA");
//    stepProtocol(null , "SUBJECT:" + subject);
//
//    // send body
//    BufferedReader reader = new BufferedReader(new StringReader(body));
//
//    while(true) {
//      String line = reader.readLine();
//
//      if(line == null) {
//        break;
//      }
//
//      out.println(line);
//    }
//
//    out.println();
//
//    reader.closeFile();
//
//    stepProtocol("250", ".");
//
//    stepProtocol("221", "QUIT");
//
//    disconnect();
//  }
//
//  void stepProtocol(String expected, String say) throws IOException {
//    if(say != null) {
//      writeRequest(say);
//    }
//
//    if(expected != null) {
//      missLine(expected);
//    }
//  }
//
//  void missLine(String expected) throws IOException {
//    String response = (String)readResponse();
//
//    if(!response.startsWith(expected)) {
//      throw new IOException(response);
//    }
//
//    while(response.startsWith(expected + "-")) {
//      response = (String)readResponse();
//    }
//  }

  public void send() throws MessagingException, UnsupportedEncodingException {
    String mailUserName = "";
    String mailPassword = "";

    Properties props = new Properties();
    props.setProperty("mail.transport.protocol", "smtp");
    props.setProperty("mail.host", host);
    props.setProperty("mail.user", "");
    props.setProperty("mail.password", "");

    Session session = Session.getDefaultInstance(props, null);

    Transport transport = null;

    try {
      transport = session.getTransport("smtp");

      transport.connect(host, mailUserName, mailPassword);

      // Define message
      MimeMessage message = new MimeMessage(session);

      message.setContent(body, "text/plain");

      message.setSubject(subject);

      Address fromAddress = new InternetAddress(fromEmail, fromName);
      Address toAddress = new InternetAddress(toEmail, toName);

      message.setFrom(fromAddress);

      message.addRecipient(Message.RecipientType.TO, toAddress);

      message.saveChanges(); // implicit with send()

      transport.sendMessage(message, message.getAllRecipients());
    }
    finally {
      if (transport != null) {
        transport.close();
      }
    }
  }

  public static void main(String args[]) throws Exception {
    String mailHost = "mail.earthlink.net";

    String heloHost = "mail.earthlink.net";
    String sender = "sf@earthlink.net";
    String receiver = "sf@earthlink.net";
    String subject = "SMTP test";
    String body = "Test";

    SMTPClient client = new SMTPClient(mailHost, sender, "", receiver, "", subject, body);

    client.send();

/*    client.setHeloHost(heloHost);
    client.setSender(sender);
    client.setReceiver(receiver);
    client.setSubject(subject);
    client.setBody(body);

    System.out.println("Waiting...");

    try {
      client.interact();
      System.out.println("e-mail was succesfully sent");
    }
    catch(IOException e) {
      System.out.println("e-mail wasn't succesfully sent: " +
                         e.getMessage());
    }
    */
  }

}


