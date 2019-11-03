import mail_builder.SendMail;

public class JavaMailClients {

  public static void main(String[] args) {

    SendMail.MailBuilder mailBuilder = new SendMail.MailBuilder();

    mailBuilder.setTo("to mail address");
    mailBuilder.setFrom("from mail address");
    mailBuilder.setPassword("google mail pass");
    mailBuilder.setUserName("google user name");
    mailBuilder.setSmtpHost("smtp.gmail.com");
    mailBuilder.setSmtpPort("587");


    String subject = "テストメール";
    String contents = "テスト送信";
    mailBuilder.build().sendMail(subject, contents);

  }

}
