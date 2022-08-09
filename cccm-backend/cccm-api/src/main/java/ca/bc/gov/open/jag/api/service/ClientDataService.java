package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;

import java.math.BigDecimal;
import java.util.List;

public interface ClientDataService {

    List<Client> clientSearch(ClientSearch clientSearch);

    Client clientProfile(String clientNum);

    byte[] clientPhoto(BigDecimal clientId);

}
