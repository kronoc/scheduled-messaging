package net.conor.jms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Calendar.class, Time.class})
public class TimeTest {

    @Test
    public void testNow() throws Exception {
        long time = new Random().nextLong();
        mockStatic(Calendar.class);
        Calendar calendar = mock(Calendar.class);
        when(Calendar.getInstance().getTimeInMillis()).thenReturn(time);
        when(calendar.getTimeInMillis()).thenReturn(time);
        assertEquals(time, Time.now()); 
    }
    
}
