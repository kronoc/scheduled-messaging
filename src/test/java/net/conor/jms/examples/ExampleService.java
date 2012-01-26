package net.conor.jms.examples;

/**
 * @author conor
 * @since 14-Jul-2010
 */
//@Component
public class ExampleService {

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