package ca.bc.gov.open.jag.api.util;

import static ca.bc.gov.open.jag.api.Keys.USERNAME_PREFIX;

public class JwtUtils {

    private JwtUtils() {}

    public static String stripUserName(String username) {

        //Strip idir from username
        return username.replace(USERNAME_PREFIX, "");

    }

}
