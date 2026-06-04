package com.nvtt.formatters;

import com.nvtt.pojo.Role;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public class RoleFormatter implements Formatter<Role> {

    @Override
    public String print(Role role, Locale locale) {
        return String.valueOf(role.getId());
    }

    @Override
    public Role parse(String roleId, Locale locale) throws ParseException {
        Role role = new Role();
        role.setId(Long.valueOf(roleId));

        return role;
    }
}
