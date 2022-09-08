package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Client;
import ca.bc.gov.open.jag.cccm.api.openapi.model.Photo;

import java.util.List;

public interface ClientDataService {

    List<Client> clientSearch(ClientSearch clientSearch);

    List<Client> clientAddressSearch(ClientAddressSearch clientAddressSearch);

    Client clientProfile(String clientNum, String user);

    Photo clientPhoto(String clientNum);

}
