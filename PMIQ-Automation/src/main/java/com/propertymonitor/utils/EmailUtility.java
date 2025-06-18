package com.propertymonitor.utils;

import java.io.InputStream;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtility {

    private static final String FROM_EMAIL = "ravinder.singh.lingoful@gmail.com"; // Your Gmail address
    private static final String PASSWORD = "bddgjoxhwnoafpnw"; // App-specific password from Google

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "465";

    /**
     * Sends the test summary report email to recipients configured in config.properties.
     *
     * @param total   Total number of tests run
     * @param passed  Number of tests passed
     * @param failed  Number of tests failed
     * @param skipped Number of tests skipped
     */
    public static void sendReportEmail(int total, int passed, int failed, int skipped) {
        try {
            // Load recipients from config.properties
            Properties configProps = new Properties();
            try (InputStream input = EmailUtility.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    System.err.println("❌ config.properties not found in classpath.");
                    return;
                }
                configProps.load(input);
            }

            String recipients = configProps.getProperty("email.recipients");
            if (recipients == null || recipients.trim().isEmpty()) {
                System.err.println("❌ No recipients found for 'email.recipients' in config.properties.");
                return;
            }

            String subject = "PMIQ-V3 - Test Summary Report";
            String reportUrl = "https://automation-reports-pi.vercel.app/ExtentReport.html";

            // Build HTML email body with dynamic summary
            String body = "<html><body>"
                    + "<h2>Test Execution Completed</h2>"
                    + "<p>Dear Team,</p>"
                    + "<p>The automated test suite for Property Monitor IQ V3 has completed successfully. It covers login and project search pages features to ensure the application is functioning as expected.</p>"
                    + "<p>Here is the latest test run summary:</p>"
                    + "<table border='1' cellpadding='5' cellspacing='0' style='border-collapse: collapse;'>"
                    + "<tr><th>Total Tests</th><th>Passed</th><th>Failed</th><th>Skipped</th></tr>"
                    + "<tr>"
                    + "<td align='center'>" + total + "</td>"
                    + "<td align='center' style='color:green;'>" + passed + "</td>"
                    + "<td align='center' style='color:red;'>" + failed + "</td>"
                    + "<td align='center'>" + skipped + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "<p><br/><a href='" + reportUrl + "' target='_blank'>Click here to view the full report</a></p>"
                    + "<br/>Best Regards,<br/>Ravinder Singh,<br/>QA Engineer"
                    + "</body></html>";

            // SMTP server properties for SSL (port 465)
            Properties mailProps = new Properties();
            mailProps.put("mail.smtp.auth", "true");
            mailProps.put("mail.smtp.host", SMTP_HOST);
            mailProps.put("mail.smtp.port", SMTP_PORT);
            mailProps.put("mail.smtp.ssl.enable", "true");
            mailProps.put("mail.smtp.socketFactory.port", SMTP_PORT);
            mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getInstance(mailProps, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            message.setSubject(subject);
            message.setContent(body, "text/html");

            Transport.send(message);
            System.out.println("✅ Email sent successfully to: " + recipients);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("❌ Unexpected error in sendReportEmail: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}