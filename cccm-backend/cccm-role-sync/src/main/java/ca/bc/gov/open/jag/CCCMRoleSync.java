package ca.bc.gov.open.jag;

import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.Quarkus;

import java.util.concurrent.TimeUnit;

@QuarkusMain  
public class CCCMRoleSync {

    public static void main(String ... args) throws InterruptedException {
        System.out.println("Running role sync");

        TimeUnit.SECONDS.sleep(30);

        System.out.println("Role sync done after 30 seconds");

        System.exit(0);

    }
}
