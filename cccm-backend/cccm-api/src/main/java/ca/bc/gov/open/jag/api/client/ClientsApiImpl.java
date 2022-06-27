package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.api.model.Client;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ClientsApiImpl implements ClientsApi {

    private static final Logger logger = Logger.getLogger(String.valueOf(ClientsApiImpl.class));


    @Override
    @RolesAllowed("client-search")
    public List<Client> clientSearch(String name, Boolean soundex, Integer birthYear, Integer age, String address, String location, String gender, BigDecimal clientId) {

        logger.info("Client Search Request");

        return createClientResult();

    }

    private List<Client> createClientResult() {

        Client client = new Client();
        client.setACTIVE_YN("Y");
        client.setBIRTH_DT(LocalDate.of(1950,3,3));
        client.setCLIE_ID(BigDecimal.ONE);
        client.setCLIENT_AGE(33);
        client.setCLIENT_NM("Bob Ross");
        client.setCLTY_CD("TEST");
        client.setCS_NO(123);
        client.setFPS_NO(144);
        client.setCURRENT_NAME_YN("Y");
        client.setGIVEN1_CON("");
        client.setGIVEN1_NM("");
        client.setGIVEN2_CON("");
        client.setGIVEN2_NM("");
        client.setGIVEN3_CON("");
        client.setGIVEN3_NM("");
        client.setSEALED_YN("N");

        return Collections.singletonList(client);

    }

}
