package net.conor.jms.examples;

import java.io.Serializable;

/**
 * @author conor
 * @since 14-Jul-2010
 */
public class ExampleObject implements Serializable{

    private String messageDump;


    public void persistString(String text) {
        System.out.println("persist:"+text);
        this.messageDump = text;
    }

    public void persistObject(ExampleObject exampleObject) {
        System.out.println("persist:"+exampleObject.toString());
        this.messageDump = exampleObject.toString();
    }

    public String getMessageDump() {
        return messageDump;
    }

}
