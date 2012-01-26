package net.conor.jms;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author conor
 * @since 14-Jul-2010
 */
public class MessageProducer {

    public static final String AMQ_SCHEDULED_DELAY = "AMQ_SCHEDULED_DELAY";
    public static final String DELIVERY_TIMESTAMP = "DELIVERY_TIMESTAMP";
    public static final String DELIVERY_DATE = "DELIVERY_DATE";
    public static final String DISPATCH_DATE = "DISPATCH_DATE";

    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static Log log = LogFactory.getLog(MessageProducer.class);

    private JmsTemplate jmsTemplate;
    private Destination destination;
    public static final String DISPATCH_TIMESTAMP = "DISPATCH_TIMESTAMP";


    /**
     * @param messageBody a String, which is used to set the message body of a JMS TextMessage
     * @param headers     Map, use to set appropriate JMS Message Properties on the message
     */
    public void sendTextMessage(final String messageBody, final Map<String, Serializable> headers) {
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(final Session session) throws JMSException {
                TextMessage message = session.createTextMessage(messageBody);
                handleDelay(message, headers);
                addHeaders(message, headers);
                return message;
            }
        });
    }


    /**
     * For JMS messages with no message properties, otherwise use sendTextMessage(final String messageBody, final Map<String, Serializable> headers)
     *
     * @param messageBody a String, which is used to set the message body of a JMS TextMessage
     */
    public void sendTextMessage(final String messageBody) {
        sendTextMessage(messageBody, Collections.<String, Serializable>emptyMap());
    }

    /**
     * @param serializableObject an Object implementing Serializable, which is to be sent via JMS
     * @param headers            Map, use to set appropriate JMS Message Properties on the message
     */
    public void sendObjectMessage(final Serializable serializableObject, final Map<String, Serializable> headers) {
        if (log.isDebugEnabled()) {
            log.debug("sending object " + serializableObject.toString());
        }

        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(final Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage(serializableObject);
                handleDelay(message, headers);
                addHeaders(message, headers);
                if (log.isDebugEnabled()) {
                    log.debug("sending headers " + headers);
                    log.debug("sending message " + message);
                }
                return message;
            }
        });

    }

    /**
     * For JMS messages with no message properties, otherwise use sendTextMessage(final String messageBody, final Map<String, Serializable> headers)
     *
     * @param serializableObject an Object implementing Serializable, which is to be sent via JMS
     */
    public void sendObjectMessage(final Serializable serializableObject) {
        sendObjectMessage(serializableObject, Collections.<String, Serializable>emptyMap());
    }


    protected Message handleDelay(Message message, Map<String, Serializable> headers) throws JMSException {
        long now = Time.now();
        if (headers != null && headers.get(AMQ_SCHEDULED_DELAY) != null) {
            long delay = (Long) headers.get(AMQ_SCHEDULED_DELAY);
            long deliveryTime = now + delay;
            try {
                message.setLongProperty(DELIVERY_TIMESTAMP, deliveryTime);
                message.setLongProperty(DISPATCH_TIMESTAMP, now);
                DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
                message.setStringProperty(DISPATCH_DATE, formatter.format(new Date(now)));
                message.setStringProperty(DELIVERY_DATE, formatter.format(new Date(deliveryTime)));
                message.setJMSTimestamp(deliveryTime);
            } catch (JMSException e) {
                log.error("error setting message headers", e);
                throw e;
            }
        } else {
            message.setJMSTimestamp(now);
        }
        return message;
    }

    protected Message addHeaders(Message message, Map<String, Serializable> headers) throws JMSException {
        if (headers != null) {
            for (String key : headers.keySet()) {
                if (headers.get(key) instanceof String) {
                    message.setStringProperty(key, (String) headers.get(key));
                } else if (headers.get(key) instanceof Integer) {
                    message.setIntProperty(key, (Integer) headers.get(key));
                } else if (headers.get(key) instanceof Boolean) {
                    message.setBooleanProperty(key, (Boolean) headers.get(key));
                } else if (headers.get(key) instanceof Long) {
                    message.setLongProperty(key, (Long) headers.get(key));
                } else if (headers.get(key) instanceof Float) {
                    message.setFloatProperty(key, (Float) headers.get(key));
                } else if (headers.get(key) instanceof Double) {
                    message.setDoubleProperty(key, (Double) headers.get(key));
                } else if (headers.get(key) instanceof Byte) {
                    message.setByteProperty(key, (Byte) headers.get(key));
                } else if (headers.get(key) instanceof Short) {
                    message.setShortProperty(key, (Short) headers.get(key));
                } else if ((headers.get(key) instanceof Map) || (headers.get(key) instanceof List)) {
                    //setObjectProperty only takes objectified primitive objects,
                    // String, Map and List.
                    message.setObjectProperty(key, headers.get(key));
                }
            }
        }
        return message;
    }

    /**
     * Sets the template.
     *
     * @param jmsTemplate the new template
     */
    public void setJmsTemplate(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }


    /**
     * Sets the destination.
     *
     * @param destination the new destination
     */
    public void setDestination(final Destination destination) {
        this.destination = destination;
    }

    /**
     * Convenience method to receive messages from the queue which are being sent to.
     * <p/>
     * Typically for use in a unit test (not application code!!)
     * <p/>
     * For real situations use a MessageListener, @see MessageListenerExample.
     *
     * @return Message jmsMessage
     */
    protected Message receive() {
        return this.jmsTemplate.receive(destination);
    }

}
