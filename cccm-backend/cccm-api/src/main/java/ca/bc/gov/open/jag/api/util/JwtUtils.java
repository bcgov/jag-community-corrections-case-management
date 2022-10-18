package ca.bc.gov.open.jag.api.util;

import ca.bc.gov.open.jag.api.error.CCCMErrorCode;
import ca.bc.gov.open.jag.api.error.CCCMException;
import org.apache.commons.lang3.StringUtils;

import java.util.logging.Logger;

import static ca.bc.gov.open.jag.api.Keys.USERNAME_PREFIX;

public class JwtUtils {

    private JwtUtils() {}

    private static final Logger logger = Logger.getLogger(String.valueOf(JwtUtils.class));

    public static String stripUserName(String username) {

        if (StringUtils.isBlank(username) || !username.contains(USERNAME_PREFIX)) {
            logger.severe("Invalid user access");
            throw new CCCMException("Invalid user", CCCMErrorCode.INVALIDUSER);
        }
        //Strip idir from username
        return username.replace(USERNAME_PREFIX, "");

    }

}
