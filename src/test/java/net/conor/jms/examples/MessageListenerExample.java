package net.conor.jms.examples;

import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

/**
 * This is an example of a message listener, in reality
 * you would not be checking for both text or object message,
 * you should know which type this queue uses.
 *
 * @author conor
 * @since 14-Jul-2010
 */
public class MessageListenerExample implements MessageListener {

    @Autowired
    private ExampleService exampleService;

    public void onMessage(Message message) {
        try {
            //do whatever you want here
            System.out.print("message received");
            if (message instanceof TextMessage) {
                onTextMessage(message);
            } else if (message instanceof ObjectMessage) {
                onObjectMessage(message);
            }
            //message.acknowledge();
        } catch (JMSException e) {
            //log.error("message",e);
            e.printStackTrace();
        }
    }

    private void onObjectMessage(Message rawMessage) throws JMSException {
        ObjectMessage message = (ObjectMessage) rawMessage;
        exampleService.persistObject((ExampleObject) message.getObject());
    }

    private void onTextMessage(Message rawMessage) throws JMSException {
        TextMessage message = (TextMessage) rawMessage;
        exampleService.persistString(message.getText());
    }

    public ExampleService getExampleService() {
        return exampleService;
    }
}