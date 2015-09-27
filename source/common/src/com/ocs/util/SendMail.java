/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.util;

import com.ocs.common.ErrorCode;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class SendMail {

    private static final Logger log = Logger.getLogger(SendMail.class);
    private static final int DEFAULT_SMTP_PORT = 25;
    private String defaultHost = null;
    private int defaultPort = DEFAULT_SMTP_PORT;
    private Session session = null;

    public SendMail(String host, String username, String password) {
        this(host, DEFAULT_SMTP_PORT, "true", username, password);
    }

    public SendMail(String host, String needAuth, String username, String password) {
        this(host, DEFAULT_SMTP_PORT, needAuth, username, password);
    }

    public SendMail(String host, int port, String needAuth, String username, String password) {
        this.defaultHost = host;
        this.defaultPort = port;
        Properties props = System.getProperties();
        props.put("mail.smtp.host", defaultHost);
        props.put("mail.smtp.port", defaultPort);
        props.put("mail.smtp.auth", needAuth);
        MailAuthenticator auth = new MailAuthenticator(username, password);
        // Get a mail session
        session = Session.getDefaultInstance(props, auth);
    }

    public int send(String from, String to, String subject, String body) {
        return send(from, to, subject, body, null);
    }

    public int send(String from, String to, String subject, String body, String[] attachments) {
        log.debug("[smtpHost=" + defaultHost + ", smtpPort=" + defaultPort + ", from=" + from + ", to=" + to + ", subject=" + subject + "].");
        int result = ErrorCode.OK;
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            // Create a message part to represent the body text
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html;charset=utf-8");
            //use a MimeMultipart as we need to handle the file attachments
            Multipart multipart = new MimeMultipart();
            //add the message body to the mime message
            multipart.addBodyPart(messageBodyPart);

            // add any file attachments to the message
            if (attachments != null && attachments.length > 0) {
                addAtachments(attachments, multipart);
            }
            // Put all message parts in the message
            message.setContent(multipart);
            // Send the message
            Transport.send(message);
        } catch (MessagingException ex) {
            result = ErrorCode.ERR_SEND_EMAIL;
            log.error("Fail to send email: [from=" + from + ", to=" + to + ", subject=" + subject + "].", ex);
        } catch (Exception ex){
            result = ErrorCode.ERR_SEND_EMAIL;
            log.error("Fail to send email: [from=" + from + ", to=" + to + ", subject=" + subject + "].", ex);
        }
        return result;
    }

    private void addAtachments(String[] attachments, Multipart multipart)
            throws MessagingException, AddressException {
        for (int i = 0; i <= attachments.length - 1; i++) {
            String filename = attachments[i];
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();

            //use a JAF FileDataSource as it does MIME type detection
            DataSource source = new FileDataSource(filename);
            attachmentBodyPart.setDataHandler(new DataHandler(source));

            //assume that the filename you want to send is the same as the
            //actual file name - could alter this to remove the file path
            attachmentBodyPart.setFileName(filename);

            //add the attachment
            multipart.addBodyPart(attachmentBodyPart);
        }
    }

    public static void main(String args[]) {
        SendMail sendMail = new SendMail("smtp.163.com", "chuan_chuan", "!iamjeff0");
        sendMail.send("chuan_chuan@163.com", "chuan_chuan@163.com", "Test", "This is a test message!");
    }
    
    public static boolean ValidateAddress(String address) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(address);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}

class MailAuthenticator extends Authenticator {

    private String username = null;
    private String password = null;

    public MailAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
