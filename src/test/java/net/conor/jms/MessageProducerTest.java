package net.conor.jms;

import net.conor.jms.examples.ExampleObject;
import net.conor.jms.examples.MessageExampleConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author conor
 * @since 14-Jul-2010
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/test-spring-beans.xml"
})
public class MessageProducerTest {

    @Autowired
    private MessageProducer exampleMessageProducer;

    @Test
    public void testSendTextMessageWithHeaders() throws Exception {
        String aTextMessage = "hello world!";
        String anImportantHeader = "the world is big";
        Map<String, Serializable> headers = new HashMap<String, Serializable>();
        headers.put(MessageExampleConstants.IMPORTANT_HEADER, anImportantHeader);

        exampleMessageProducer.sendTextMessage(aTextMessage, headers);

        while (true){
            Message msg = exampleMessageProducer.receive();
            if (msg instanceof TextMessage) {
                    assertEquals(aTextMessage, ((TextMessage) msg).getText());
                    assertEquals(anImportantHeader, ((TextMessage) msg).getStringProperty(MessageExampleConstants.IMPORTANT_HEADER));
                    break;
                }
        }
    }

    @Test
    public void testSendTextMessage() throws Exception {
        String aTextMessage = "hello world!";

        exampleMessageProducer.sendTextMessage(aTextMessage);

        while (true){
            Message msg = exampleMessageProducer.receive();
            if (msg instanceof TextMessage) {
                    assertEquals(aTextMessage, ((TextMessage) msg).getText());
                    break;
                }
        }
    }

    @Test
    public void testSendObjectMessageWithHeaders() throws Exception {
        ExampleObject obj = new ExampleObject();
        String anImportantHeader = "the world is big";
        Map<String, Serializable> headers = new HashMap<String, Serializable>();
        headers.put(MessageExampleConstants.IMPORTANT_HEADER, anImportantHeader);

        exampleMessageProducer.sendObjectMessage(obj, headers);

        while (true){
            Message msg = exampleMessageProducer.receive();
            if (msg instanceof ObjectMessage) {
                    assertTrue(((ObjectMessage)msg).getObject() instanceof ExampleObject);
                    assertEquals(anImportantHeader, ((ObjectMessage) msg).getStringProperty(MessageExampleConstants.IMPORTANT_HEADER));
                    break;
                }
        }

    }

    @Test
    public void testSendObjectMessage() throws Exception {
        ExampleObject obj = new ExampleObject();
        exampleMessageProducer.sendObjectMessage(obj);
        while (true){
            Message msg = exampleMessageProducer.receive();
            if (msg instanceof ObjectMessage) {
                    assertTrue(((ObjectMessage)msg).getObject() instanceof ExampleObject);
                    break;
                }
        }

    }

    @Test
    public void testAddHeaders() throws Exception {
        Message message = new MockJmsMessage();

        String testAction = "foo";
        int testInteger = 55;
        long testLong = 99l;
        boolean testBoolean = true;
        short testShort = 10;
        float testFloat = 33.3f;
        double testDouble = 1.1d;
        byte testByte = 3;
        TestClass testClass = new TestClass();

        Map<String, Serializable> inHeaders = new HashMap<String, Serializable>();
        inHeaders.put("action", testAction);
        inHeaders.put("integer", testInteger);
        inHeaders.put("long", testLong);
        inHeaders.put("bool", testBoolean);
        inHeaders.put("short", testShort);
        inHeaders.put("float", testFloat);
        inHeaders.put("double", testDouble);
        inHeaders.put("byte", testByte);
        inHeaders.put("object", testClass);

        MessageProducer rocketMessageProducer = new MessageProducer();
        rocketMessageProducer.addHeaders(message, inHeaders);

        assertEquals(testAction, message.getStringProperty("action"));
        assertEquals(testInteger, message.getIntProperty("integer"));
        assertEquals(testLong, message.getLongProperty("long"));
        assertEquals(testBoolean, message.getBooleanProperty("bool"));
        assertEquals(testShort, message.getShortProperty("short"));
        assertEquals(testFloat, message.getFloatProperty("float"), 0);
        assertEquals(testDouble, message.getDoubleProperty("double"), 0);
        assertEquals(testByte, message.getByteProperty("byte"));
        //assertEquals(testClass, message.getObjectProperty("object"));
    }

    @Test
    public void testAddHeadersWithNull() throws Exception {
        Message message = mock(MockJmsMessage.class);
        MessageProducer rocketMessageProducer = new MessageProducer();
        rocketMessageProducer.addHeaders(message, null);
        verifyZeroInteractions(message);
    }
}
