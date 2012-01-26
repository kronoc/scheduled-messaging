package net.conor.jms;

import java.util.Calendar;

public enum Time {

    INSTANCE;

    public static long now(){
        return Calendar.getInstance().getTimeInMillis();
    }
    
}
