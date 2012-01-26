package net.conor.jms;

import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * Spring MessageListenerContainer, extending DefaultMessageListenerContainer,
 * which only consumes messages which were sent with JMSTimestamp values of
 * before now (System.currentTimeMillis()).
 *
 * We can stop using this when Active MQ 5.4 is available.
 *
 *
 * @author conor
 * @since 09-Aug-2010
 */
public class ScheduledMessageListenerContainer extends DefaultMessageListenerContainer {

    @Override
    public String getMessageSelector() {
        long now = Time.now();
        String defaultMessageSelector = super.getMessageSelector();
        StringBuilder sb = new StringBuilder();
        if (defaultMessageSelector != null && defaultMessageSelector.length() != 0) {
            sb.append("(");
            sb.append(defaultMessageSelector);
            sb.append(") AND ");
        }
        sb.append("(" + MessageProducer.DELIVERY_TIMESTAMP + " BETWEEN 0 AND ");
        //sb.append("(JMSTimestamp BETWEEN 0 AND ");
        sb.append(now);
        sb.append(")");

        String messageSelector = sb.toString();

        if (logger.isTraceEnabled()) {
            logger.trace("Using JMS scheduled message selector : '" + messageSelector + "'");
        }

        return messageSelector;
    }

}