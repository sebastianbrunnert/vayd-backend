package de.vayd.sebastianbrunnert.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Service
public class EmailService {

    private Session session;
    private InternetAddress internetAddress;

    public EmailService(
        @Value("${mailer.host}") String host,
        @Value("${mailer.port}") String port,
        @Value("${mailer.username}") String username,
        @Value("${mailer.password}") String password,
        @Value("${mailer.from}") String from
    ) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        this.session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            this.internetAddress = new InternetAddress(from, "Vayd Backend");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(List<String> to, String subject, String body, List<EmailAttachment> attachments) {
        new Thread(() -> {
            try {
                Message message = new MimeMessage(this.session);
                message.setFrom(this.internetAddress);
                message.setRecipients(Message.RecipientType.TO, to.stream().map(address -> {
                    try {
                        return new InternetAddress(address);
                    } catch (AddressException e) {
                        return null;
                    }
                }).filter(Objects::nonNull).toArray(InternetAddress[]::new));
                message.setSubject(subject);

                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(body, "text/html; charset=utf-8");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                attachments.forEach(attachment -> {
                    try {
                        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                        attachmentBodyPart.setFileName(attachment.getName());
                        attachmentBodyPart.setContent(attachment.getContent().readAllBytes(), "application/octet-stream");
                        multipart.addBodyPart(attachmentBodyPart);
                    } catch (IOException | MessagingException e) {
                        e.printStackTrace();
                    }
                });

                message.setContent(multipart);
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
