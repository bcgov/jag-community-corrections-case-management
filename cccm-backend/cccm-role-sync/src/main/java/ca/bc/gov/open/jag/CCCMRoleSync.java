package ca.bc.gov.open.jag;

import ca.bc.gov.open.jag.service.RoleSyncService;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.Quarkus;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;

@QuarkusMain  
public class CCCMRoleSync {


    public static void main(String ... args) {

        Quarkus.run(MyApp.class, args);

    }

    public static class MyApp implements QuarkusApplication {

        @Inject
        RoleSyncService roleSyncService;

        @Override
        @ActivateRequestContext
        public int run(String... args) throws Exception {
            System.out.println("Running role sync");

            roleSyncService.syncRoles();

            System.out.println("Role sync complete");
            System.exit(0);
            Quarkus.waitForExit();
            return 0;
        }
    }
}
