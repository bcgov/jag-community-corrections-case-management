package ca.bc.gov.open.jag.service;

import ca.bc.gov.open.jag.model.IdirUser;

public interface LdapService {

    IdirUser getUserByUsername(String username);

}
