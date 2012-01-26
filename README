Scheduled Messaging Module
==========================

This module provides a few features to make life working with JMS and Spring a little easier:

* A message producer to make it easier to send JMS messages without faffing about.
* A drop in replacement for Spring's DefaultMessageListenerContainer which lets you use something similar to ActiveMQ 5.4's delayed message functionality without having to use that broker.

Maven Dependency
-----------------

You can use this library with maven, run:

	$ mvn clean install

to get this library into your local repo, then dd the following dependancy to your pom file.

	<dependency>
		<groupId>net.conor.lib</groupId>
		<artifactId>scheduled-messaging</artifactId>
		<version>1.0-SNAPSHOT</version>
	</dependency>

You will also need Spring JMS, and (if you are using Active MQ) some other libraries:
	
	<dependency>
		<groupId>javax.jms</groupId>
        	<artifactId>jms</artifactId>
        	<version>1.1</version>
	</dependency>
	<dependency>
        	<groupId>org.springframework</groupId>
        	<artifactId>spring-jms</artifactId>
        	<version>3.0.3.RELEASE</version>
	</dependency>
	<dependency>
        	<groupId>org.apache.activemq</groupId>
        	<artifactId>activemq-core</artifactId>
        	<version>5.3.0</version>
        	<scope>compile</scope>
       		<exclusions>
            		<exclusion>
                		<groupId>org.springframework</groupId>
                		<artifactId>spring-context</artifactId>
            		</exclusion>
        	</exclusions>
	</dependency>
	<dependency>
        	<groupId>org.apache.xbean</groupId>
        	<artifactId>xbean-spring</artifactId>
        <version>3.5</version>
	</dependency>


Usage
-----

### Sending Messages

You can send JMS messages from any POJO within your application, so no need to reinvent the wheel every time.

#### Spring Application context

To use the MessageProducer  you need to define one jms template and one or many message producers (one per queue/topic) in your spring application context.

	<beans>
		<bean id="connectionFactory" class="org.apache.activemq.ActiveMQConnectionFactory">
		<property name="brokerURL">
		<value>${jms.broker.url}</value>
     		</property>	
	</bean>

   	<amq:queue id="my.queue" physicalName="my.queue"/>

   	<bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
		<property name="connectionFactory">
			<ref bean="connectionFactory" />
		</property>
		<property name="sessionTransacted" value="false" />
        	<property name="messageTimestampEnabled" value="false" /> <!-- only needed if you are using delayed msg functionality and thus override timestamps -->
   	</bean>

   <bean id="messageProducer" name="messageProducer" class="net.conor.jms.MessageProducer">
	<property name="jmsTemplate" ref="jmsTemplate"/>
	<property name="destination" ref="my.queue" />
   </bean>
</beans>
{code}

### Java code

Call sendTextMessage() or sendObjectMessage() on the messageProducer to send the messages. eg:


public class Sender {

    @Autowired
    private MessageProducer messageProducer;

     public void sendMessage(String myMessage, String someHeader, String someOtherHeader) throws JMSException {
         Map<String, Serializable> headers = new HashMap<String, Serializable>();
         headers.put(MessageConstants.SOMETHING, someHeader);
         headers.put(MessageConstants.SOMETHING_ELSE, someOtherHeader);
         messageProducer.sendTextMessage(myMessage, headers);
    }

}


### Sending Delayed Messages


#### Spring Application context

As above, but also set up a listener-container and consumer (a POJO which implements MessageListener) - one per queue/topic.

    <jms:listener-container
            container-class="conor.net.jms.ScheduledMessageListenerContainer"
            cache="session">
        <jms:listener destination="my.queue" ref="myConsumer" method="onMessage" />
    </jms:listener-container>

#### Java code

When sending message you wish to delay set the header AMQ_SCHEDULED_DELAY with a long value of the number of millis you want your message delayed. eg:

static final String AMQ_SCHEDULED_DELAY = "AMQ_SCHEDULED_DELAY";

...

long delayMillis = 15*60*1000;
Map<String, Serializable> headers = new HashMap();
headers.put(AMQ_SCHEDULED_DELAY, delayMillis);
myMessageProducer.sendTextMessage("Hello World!", headers);

