package org.jaysabva.woc_crs.util;


import org.jaysabva.woc_crs.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
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
                "<p>Congratulations! Your " + accountType + " account has been successfully created by the admin. Below are your login details:</p>" +
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

    public String semesterRegistrationEmail(String studentName, String semester, String registrationStartDate, String registrationEndDate) {
        return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Semester Registration Timeline</title>" +
                "<style>body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f9f7;} .container {width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);} .header {text-align: center; margin-bottom: 30px; padding-bottom: 10px; border-bottom: 2px solid #e0e0e0;} .header h1 {color: #4CAF50; font-size: 28px; margin: 0; font-weight: 600;} .content {font-size: 16px; line-height: 1.5; color: #333333;} .content p {margin: 10px 0;} .content .highlight {color: #4CAF50; font-weight: bold;} table {width: 100%; margin-top: 20px; border-spacing: 0;} table td {padding: 12px 0; font-size: 16px; color: #555555;} table td:first-child {font-weight: bold; color: #333333;} .button {display: inline-block; padding: 12px 30px; margin-top: 25px; background-color: #4CAF50; color: white; text-decoration: none; font-size: 16px; font-weight: 500; border-radius: 5px; text-align: center;} .button:hover {background-color: #388E3C;} .footer {text-align: center; font-size: 14px; color: #777777; margin-top: 30px; padding-top: 20px; border-top: 1px solid #e0e0e0;} .footer a {color: #4CAF50; text-decoration: none;}</style></head>" +
                "<body><div class='container'><div class='header'><h1>Course Registration Timeline</h1></div><div class='content'>" +
                "<p>Dear " + studentName + ",</p>" +
                "<p>We would like to inform you about the registration timeline for the upcoming <strong>" + semester + " semester</strong>.</p>" +
                "<p>Please carefully note the following key dates:</p>" +
                "<table>" +
                "<tr><td>Registration Start Date:</td><td>" + registrationStartDate + "</td></tr>" +
                "<tr><td>Registration End Date:</td><td>" + registrationEndDate + "</td></tr>" +
                "</table>" +
                "<p>To ensure your enrollment, please complete your registration before the end date. Click the link below to begin your registration process:</p>" +
                "<a href='" + "github.com/JaySabva" + "' class='button'>Start Registration</a>" +
                "<p>If you have any questions or need assistance during the registration process, feel free to reach out to us at <a href='mailto:" + "202101224@daiict.ac.in" + "'>" + "202101224@daiict.ac.in" + "</a>.</p>" +
                "<div class='footer'><p>&copy; 2025 Your University, All Rights Reserved.</p></div></div></body></html>";
    }

    public String courseAssignmentEmail(String studentName, String semester, List<Course> assignedCourses) {
        StringBuilder coursesTable = new StringBuilder();

        for (Course course : assignedCourses) {
            coursesTable.append("<tr><td>").append(course.getCourseCode()).append("</td>")
                    .append("<td>").append(course.getCourseName()).append("</td>")
                    .append("<td>").append(course.getCredits()).append("</td>")
                    .append("<td>").append(course.getProfessor().getName()).append("</td></tr>");
        }

        return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Your Course Assignments</title>" +
                "<style>body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f9f7;} .container {width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);} .header {text-align: center; margin-bottom: 30px; padding-bottom: 10px; border-bottom: 2px solid #e0e0e0;} .header h1 {color: #4CAF50; font-size: 28px; margin: 0; font-weight: 600;} .content {font-size: 16px; line-height: 1.5; color: #333333;} .content p {margin: 10px 0;} .content .highlight {color: #4CAF50; font-weight: bold;} table {width: 100%; margin-top: 20px; border-collapse: collapse;} table td, table th {padding: 10px; text-align: left; border: 1px solid #ddd;} table th {background-color: #4CAF50; color: white;} .button {display: inline-block; padding: 12px 30px; margin-top: 25px; background-color: #4CAF50; color: white; text-decoration: none; font-size: 16px; font-weight: 500; border-radius: 5px; text-align: center;} .button:hover {background-color: #388E3C;} .footer {text-align: center; font-size: 14px; color: #777777; margin-top: 30px; padding-top: 20px; border-top: 1px solid #e0e0e0;} .footer a {color: #4CAF50; text-decoration: none;}</style></head>" +
                "<body><div class='container'><div class='header'><h1>Your Course Assignments for " + semester + "</h1></div><div class='content'>" +
                "<p>Dear " + studentName + ",</p>" +
                "<p>We are pleased to inform you that the following courses have been assigned to you for the upcoming <strong>" + semester + "</strong>:</p>" +
                "<table><thead><tr><th>Course Code</th><th>Course Name</th><th>Credits</th><th>Professor</th></tr></thead>" +
                "<tbody>" + coursesTable + "</tbody></table>" +
                "<p>Please make sure to attend all your classes and stay up to date with the course materials.</p>" +
                "<p>If you have any questions or need further assistance, feel free to reach out to us.</p>" +
                "<a href='" + "https://yourportal.com/login" + "' class='button'>Go to Your Dashboard</a>" +
                "</div><div class='footer'><p>If you need assistance, contact us at <a href='mailto:support@yourplatform.com'>support@yourplatform.com</a>.</p>" +
                "<p>&copy; 2025 Your University, All Rights Reserved.</p></div></div></body></html>";
    }

    public String forgotPasswordEmail(String resetLink) {
        return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Password Reset Request</title>" +
                "<style>body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f9f7;} .container {width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);} .header {text-align: center; margin-bottom: 30px; padding-bottom: 10px; border-bottom: 2px solid #e0e0e0;} .header h1 {color: #4CAF50; font-size: 28px; margin: 0; font-weight: 600;} .content {font-size: 16px; line-height: 1.5; color: #333333;} .content p {margin: 10px 0;} .content .highlight {color: #4CAF50; font-weight: bold;} .button {display: inline-block; padding: 12px 30px; margin-top: 25px; background-color: #4CAF50; color: white; text-decoration: none; font-size: 16px; font-weight: 500; border-radius: 5px; text-align: center;} .button:hover {background-color: #388E3C;} .footer {text-align: center; font-size: 14px; color: #777777; margin-top: 30px; padding-top: 20px; border-top: 1px solid #e0e0e0;} .footer a {color: #4CAF50; text-decoration: none;}</style></head>" +
                "<body><div class='container'><div class='header'><h1>Password Reset Request</h1></div><div class='content'>" +
                "<p>We received a request to reset the password for your account. If you did not request a password reset, please ignore this email.</p>" +
                "<p>To reset your password, please click the button below:</p>" +
                "<a href='" + resetLink + "' class='button'>Reset Your Password</a>" +
                "<p>If you have any issues or questions, feel free to contact us at <a href='mailto:support@yourplatform.com'>support@yourplatform.com</a>.</p>" +
                "<div class='footer'><p>&copy; 2025 Your Platform, All Rights Reserved.</p></div></div></body></html>";
    }

}
