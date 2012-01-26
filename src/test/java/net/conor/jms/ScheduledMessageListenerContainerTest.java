package net.conor.jms;

import org.apache.commons.logging.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Time.class)
public class ScheduledMessageListenerContainerTest {

    private static final String TEST_MESSAGE_SELECTOR_1 = "TEST_MESSAGE_SELECTOR_1";
    private static final String TEST_MESSAGE_SELECTOR_2 = "TEST_MESSAGE_SELECTOR_2";

    @Test
    public void testGetMessageSelectorWithNullDefaultMessageSelector(){
        ScheduledMessageListenerContainer scheduledMessageListenerContainer = new ScheduledMessageListenerContainer();
        scheduledMessageListenerContainer.setMessageSelector(null);
        long time = new Random().nextLong();
        mockStatic(Time.class);
        when(Time.now()).thenReturn(time);
        String messageSelector = scheduledMessageListenerContainer.getMessageSelector();
        assertEquals("(DELIVERY_TIMESTAMP BETWEEN 0 AND " + time + ")", messageSelector);
    }

    @Test
    public void testGetMessageSelectorWithEmptyDefaultMessageSelector(){
        ScheduledMessageListenerContainer scheduledMessageListenerContainer = new ScheduledMessageListenerContainer();
        scheduledMessageListenerContainer.setMessageSelector("");
        long time = new Random().nextLong();
        mockStatic(Time.class);
        when(Time.now()).thenReturn(time);
        String messageSelector = scheduledMessageListenerContainer.getMessageSelector();
        assertEquals("(DELIVERY_TIMESTAMP BETWEEN 0 AND " + time + ")", messageSelector);
    }

    @Test
    public void testGetMessageSelector(){
        ScheduledMessageListenerContainer scheduledMessageListenerContainer = new ScheduledMessageListenerContainer();
        long time = new Random().nextLong();
        mockStatic(Time.class);
        when(Time.now()).thenReturn(time);
        String messageSelector = scheduledMessageListenerContainer.getMessageSelector();
        assertEquals("(DELIVERY_TIMESTAMP BETWEEN 0 AND " + time + ")", messageSelector);
    }

    @Test
    public void testGetMessageSelectorWithDefaultMessageSelector1(){
        ScheduledMessageListenerContainer scheduledMessageListenerContainer = new ScheduledMessageListenerContainer();
        scheduledMessageListenerContainer.setMessageSelector(TEST_MESSAGE_SELECTOR_1);
        long time = new Random().nextLong();
        mockStatic(Time.class);
        when(Time.now()).thenReturn(time);
        String messageSelector = scheduledMessageListenerContainer.getMessageSelector();
        assertEquals("Failed with random time " + time + ".", "(" + TEST_MESSAGE_SELECTOR_1 +") AND (DELIVERY_TIMESTAMP BETWEEN 0 AND " + time + ")", messageSelector);
    }

    @Test
    public void testGetMessageSelectorWithDefaultMessageSelector2(){
        ScheduledMessageListenerContainer scheduledMessageListenerContainer = new ScheduledMessageListenerContainer();
        scheduledMessageListenerContainer.setMessageSelector(TEST_MESSAGE_SELECTOR_2);
        long time = new Random().nextLong();
        mockStatic(Time.class);
        when(Time.now()).thenReturn(time);
        String messageSelector = scheduledMessageListenerContainer.getMessageSelector();
        assertEquals("Failed with random time " + time + ".", "(" + TEST_MESSAGE_SELECTOR_2 +") AND (DELIVERY_TIMESTAMP BETWEEN 0 AND " + time + ")", messageSelector);
    }

    @Test
    public void testGetMessageSelectorWithTraceEnabled(){
        Log logger = mock(Log.class);
        when(logger.isTraceEnabled()).thenReturn(true);
        ScheduledMessageListenerContainer scheduledMessageListenerContainer = new ScheduledMessageListenerContainer();
        ReflectionTestUtils.setField(scheduledMessageListenerContainer, "logger", logger);
        scheduledMessageListenerContainer.getMessageSelector();
        verify(logger).trace(any());
    }
}
