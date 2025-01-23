package org.jaysabva.woc_crs.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("admin@crs.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setCc("202101224@daiict.ac.in");

        mailSender.send(message);

        System.out.println("Email sent successfully to " + toEmail);
    }

    public String registrationEmail(String name, String email, String password, String accountType, String loginUrl) {
        return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Welcome to Our Platform</title>" +
                "<style>body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f9f7;} .container {width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);} .header {text-align: center; margin-bottom: 30px; padding-bottom: 10px; border-bottom: 2px solid #e0e0e0;} .header h1 {color: #4CAF50; font-size: 28px; margin: 0; font-weight: 600;} .content {font-size: 16px; line-height: 1.5; color: #333333;} .content p {margin: 10px 0;} .content .highlight {color: #4CAF50; font-weight: bold;} table {width: 100%; margin-top: 20px; border-spacing: 0;} table td {padding: 12px 0; font-size: 16px; color: #555555;} table td:first-child {font-weight: bold; color: #333333;} .button {display: inline-block; padding: 12px 30px; margin-top: 25px; background-color: #4CAF50; color: white; text-decoration: none; font-size: 16px; font-weight: 500; border-radius: 5px; text-align: center;} .button:hover {background-color: #388E3C;} .footer {text-align: center; font-size: 14px; color: #777777; margin-top: 30px; padding-top: 20px; border-top: 1px solid #e0e0e0;} .footer a {color: #4CAF50; text-decoration: none;}</style></head>" +
                "<body><div class='container'><div class='header'><h1>Welcome to Our Platform!</h1></div><div class='content'>" +
                "<p>Dear " + name + ",</p>" +
                "<p>Congratulations! Your " + accountType + "account has been successfully created by the admin. Below are your login details:</p>" +
                "<table>" +
                "<tr><td>Name:</td><td>" + name + "</td></tr>" +
                "<tr><td>Email:</td><td>" + email + "</td></tr>" +
                "<tr><td>Password:</td><td>" + password + "</td></tr>" +
                "</table>" +
                "<p>You can now log in to your " + accountType + " account using these details. Please keep them safe.</p>" +
                "<a href='" + loginUrl + "' class='button'>Login to Your Account</a></div>" +
                "<div class='footer'><p>If you have any questions or need assistance, feel free to <a href='mailto:support@yourplatform.com'>contact us</a>.</p>" +
                "<p>&copy; 2025 Your Platform, All Rights Reserved.</p></div></div></body></html>";
    }
}
