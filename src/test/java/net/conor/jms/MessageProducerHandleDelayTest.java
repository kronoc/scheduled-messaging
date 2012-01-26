package net.conor.jms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jms.Message;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest(Time.class)
public class MessageProducerHandleDelayTest {

    @Test
    public void testHandleDelayWithRandomNumber() throws Exception {
        Message message = new MockJmsMessage();
        long time = new Random().nextLong();
        mockStatic(Time.class);
        when(Time.now()).thenReturn(time);
        Long delay = new Random().nextLong();
        Map<String, Serializable> inHeaders = new HashMap<String, Serializable>();
        inHeaders.put(MessageProducer.AMQ_SCHEDULED_DELAY, delay);
        MessageProducer rocketMessageProducer = new MessageProducer();
        message = rocketMessageProducer.handleDelay(message, inHeaders);
        assertEquals("Failed with time " + time + " and delay " + delay + ".", time + delay, message.getJMSTimestamp());
        //assertEquals("Failed with time " + time + " and delay " + delay + ".", time+delay, message.getLongProperty(MessageProducer.DELIVERY_TIMESTAMP));
    }

    @Test
    public void testHandleDelayWithoutDelayHeader() throws Exception {
        Message message = mock(MockJmsMessage.class);
        Map<String, Serializable> inHeaders = new HashMap<String, Serializable>();
        MessageProducer rocketMessageProducer = new MessageProducer();
        message = rocketMessageProducer.handleDelay(message, inHeaders);
        //verifyZeroInteractions(message);
        //TODO improve test
    }

    @Test
    public void testHandleDelayWithNullHeader() throws Exception {
        Message message = mock(MockJmsMessage.class);
        MessageProducer rocketMessageProducer = new MessageProducer();
        message = rocketMessageProducer.handleDelay(message, null);
        //verifyZeroInteractions(message);
        //TODO improve test
    }
}
