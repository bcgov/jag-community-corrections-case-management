package ca.bc.gov.open.jag.api.service;

import ca.bc.gov.open.jag.api.model.service.ClientAddressSearch;
import ca.bc.gov.open.jag.cccm.api.openapi.model.*;
import ca.bc.gov.open.jag.api.model.service.ClientSearch;

import java.math.BigDecimal;
import java.util.List;

public interface ClientDataService {

    List<Client> clientSearch(ClientSearch clientSearch);

    List<Client> clientAddressSearch(ClientAddressSearch clientAddressSearch);

    Client clientProfile(String clientNum, String user);

    Photo clientPhoto(String clientNum);

    FormSearchList clientFormSearch(String clientNum,  boolean currentPeriod,String formTypeCd);

    BigDecimal addClientForm(CreateFormInput createFormInput);

    String getClientFormJSON(BigDecimal clientFormId,String clientNumber,  boolean includeValues);
}
