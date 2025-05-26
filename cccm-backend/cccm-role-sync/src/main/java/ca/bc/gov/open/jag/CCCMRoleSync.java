package ca.bc.gov.open.jag;

import ca.bc.gov.open.jag.service.RoleSyncService;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusMain  
public class CCCMRoleSync {


    public static void main(String ... args) {

        Quarkus.run(RoleSyncApplication.class, args);

    }

    public static class RoleSyncApplication implements QuarkusApplication {

        @Inject
        RoleSyncService roleSyncService;

        private static final Logger logger = LoggerFactory.getLogger(RoleSyncApplication.class);

        @Override
        @ActivateRequestContext
        public int run(String... args) throws Exception {
            logger.info("Running role sync");

            roleSyncService.syncRoles();

            logger.info("Role sync complete application is shutting down");
            System.exit(0);
            Quarkus.waitForExit();
            return 0;
        }
    }
}
