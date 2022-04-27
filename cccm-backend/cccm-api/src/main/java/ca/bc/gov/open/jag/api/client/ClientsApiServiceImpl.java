package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.ClientsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientList;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

import java.math.BigDecimal;

@ApplicationScoped
public class ClientsApiServiceImpl implements ClientsApi {

    @Override
    public ClientDetails addClient(@Valid ClientDetails clientDetails) {
        return null;
    }

    @Override
    public void deleteClient(BigDecimal clientId) {

    }

    @Override
    public ClientDetails getClient(BigDecimal clientId) {
        return null;
    }

    @Override
    public ClientList getClients() {
        return null;
    }

    @Override
    public ClientDetails updateClient(@Valid ClientDetails clientDetails) {
        return null;
    }
}
