package org.wgu.software2.misc;

import org.wgu.software2.models.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

/**
 @author Nabeel Aref
 Records login attempts in the file login activity.txt.
 */
public class Logger {
    /**
     Login activity.txt keeps track of the username and date of login attempts.
     @param validLogin true if successful login.
     @throws IOException File Opening exception handler
     */
    public static void log (boolean validLogin) throws IOException {
        FileWriter logHandle = new FileWriter("login_activity.txt", true);
        System.out.println(User.getUserName() + (validLogin ? " successful" : " failed") + " login at " + Instant.now().toString());
        logHandle.write(User.getUserName() + (validLogin ? " successful" : " failed") + " login at " + Instant.now().toString());

        logHandle.close();
    }
}
