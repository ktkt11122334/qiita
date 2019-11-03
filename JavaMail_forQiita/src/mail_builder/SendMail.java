package mail_builder;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

  // mail contents
  private String to = "";               // send target
  private String from = "";             // mail from

  // mail user settings
  private String userName = "";         // mail account(example google account)
  private String password = "";         // mail account password
  private String smtpHost = "";
  private String smtpPort = "";
  private final String smtpAuth = "true";
  private final String starttls = "true";
  private final String connectionTimeoutValue = "10000";
  private final String timeoutValue = "10000";

  // others
  private final String charSet = "ISO-2022-JP";
  private final String encoding = "base64";


  // smtp settings
  private Properties mailProperties = new Properties();
  private final String SMTP_HOST = "mail.smtp.host";
  private final String SMTP_PORT = "mail.smtp.port";
  private final String SMTP_AUTH = "mail.smtp.auth";
  private final String SMTP_START_TLS = "mail.smtp.starttls.enable";
  private final String SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
  private final String SMTP_TIMEOUT = "mail.smtp.timeout";


  public static class MailBuilder {

    private String to = "";
    private String from = "";
    private String userName = "";
    private String password = "";
    private String smtpHost = "";
    private String smtpPort = "";

    public MailBuilder setTo(String to) {
      this.to = to;
      return this;
    }

    public MailBuilder setFrom(String from) {
      this.from = from;
      return this;
    }

    public MailBuilder setUserName(String userName) {
      this.userName = userName;
      return this;
    }

    public MailBuilder setPassword(String password) {
      this.password = password;
      return this;
    }

    public MailBuilder setSmtpHost(String smtpHost) {
      this.smtpHost = smtpHost;
      return this;
    }

    public MailBuilder setSmtpPort(String smtpPort) {
      this.smtpPort = smtpPort;
      return this;
    }

    public SendMail build() {
      return new SendMail(this);
    }

  }



  private SendMail(MailBuilder mailBuilder) {
    this.to = mailBuilder.to;
    this.from = mailBuilder.from;
    this.userName = mailBuilder.userName;
    this.password = mailBuilder.password;
    this.smtpHost = mailBuilder.smtpHost;
    this.smtpPort = mailBuilder.smtpPort;
  }



  public void sendMail(String subject, String contents) {

    mailProperties.setProperty(SMTP_HOST, smtpHost);
    mailProperties.setProperty(SMTP_PORT, smtpPort);
    mailProperties.setProperty(SMTP_AUTH, smtpAuth);
    mailProperties.setProperty(SMTP_START_TLS, starttls);
    mailProperties.setProperty(SMTP_CONNECTION_TIMEOUT, connectionTimeoutValue);
    mailProperties.setProperty(SMTP_TIMEOUT, timeoutValue);

    Session session = Session.getInstance(mailProperties,
      new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password);
         }
      }
    );

    try {
      MimeMessage message = new MimeMessage(session);

      message.setFrom(new InternetAddress(from, "test_user"));
      message.setReplyTo(new Address[]{ new InternetAddress(from) });
      message.setRecipient( Message.RecipientType.TO, new InternetAddress(to) );
      message.setSubject(subject, charSet);
      message.setText(contents, charSet);
      message.setHeader("Content-Transfer-Encoding", encoding);

      Transport.send(message);

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }

  }


}
