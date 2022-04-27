package ca.bc.gov.open.jag.api.client;

import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientDetails;
import ca.bc.gov.open.jag.cccm.api.openapi.ClientsApi;
import ca.bc.gov.open.jag.cccm.api.openapi.model.ClientList;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.math.BigDecimal;

@ApplicationScoped
public class ClientsApiServiceImpl implements ClientsApi {

    @Override
    @Transactional
    public ClientDetails addClient(@Valid ClientDetails clientDetails) {
        return null;
    }

    @Override
    @Transactional
    public void deleteClient(BigDecimal clientId) {

    }

    @Override
    @Transactional
    public ClientDetails getClient(BigDecimal clientId) {
        return null;
    }

    @Override
    @Transactional
    public ClientList getClients() {
        return null;
    }

    @Override
    @Transactional
    public ClientDetails updateClient(@Valid ClientDetails clientDetails) {
        return null;
    }
}
