package net.conor.jms.examples;

import net.conor.jms.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * an example message sender, you would probably want to send
 * only strings or only objects, not both.
 *
 * @author conor
 * @since 14-Jul-2010
 */
//@Component
public class MessageSenderExample {

    @Autowired
    private MessageProducer exampleMessageProducer;


    public void sendMessage(String body, String important) throws JMSException {
         Map<String, Serializable> headers = new HashMap<String, Serializable>();
         headers.put(MessageExampleConstants.IMPORTANT_HEADER,important);
         exampleMessageProducer.sendTextMessage(body, headers);
    }

    public void sendMessage(ExampleObject someObject, String important) throws JMSException {
         Map<String, Serializable> headers = new HashMap<String, Serializable>();
         headers.put(MessageExampleConstants.IMPORTANT_HEADER,important);
         exampleMessageProducer.sendObjectMessage(someObject, headers);
    }







}
