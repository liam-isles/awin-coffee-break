package com.awin.coffeebreak.services;

import com.awin.coffeebreak.entity.CoffeeBreakPreference;
import com.awin.coffeebreak.entity.StaffMember;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import java.util.List;
import java.util.Properties;

public class EmailNotifier {

    /**
        Method to notify staff member via email of their coffee preference.
     */
    public boolean notifyStaffMember(final StaffMember staffMember, final List<CoffeeBreakPreference> preferences) {

        if (staffMember.getEmail().isEmpty()) {
            throw new RuntimeException();
        }

        String recipient = staffMember.getEmail();
        String sender = "coffeeMaster@awin.com";
        String host = "8080";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        // creating session object to get properties
        Session session = Session.getDefaultInstance(properties);
        try
        {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Coffee Break Preference");
            message.setText("Here is your preference! " + preferences.toString());
            //Transport.send(message);
        }
        catch (MessagingException mex)
        {
            mex.printStackTrace();
        }

        return true;
    }

}
