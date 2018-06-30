package com.appirio.service.member.util;

import java.util.List;
import com.appirio.tech.core.auth.AuthUser;
import com.appirio.supply.SupplyException;

public class Helper {

    /**
     * Check if the user has roles.
     * @param user the user
     * @throws SupplyException if the user does not have any role in roles
     */
    public static void checkRole(AuthUser user, String[] roles) throws SupplyException {
        if (user != null) {
            List<String> userRoles = user.getRoles();
            for (String role : userRoles) {
                for (String r : roles) {
                    if (r.equalsIgnoreCase(role)) {
                        return;
                    }
                }
            }
        }
        throw new SupplyException("You don't have permission to perform this operation", 403);
    }
}
