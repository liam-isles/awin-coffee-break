package com.awin.coffeebreak.services;

import com.awin.coffeebreak.entity.CoffeeBreakPreference;
import com.awin.coffeebreak.entity.StaffMember;
import java.util.List;

public class EmailNotifier {

    /**
        Method to notify staff member via email of their coffee preference.
     */
    public boolean notifyStaffMember(final StaffMember staffMember, final List<CoffeeBreakPreference> preferences) {

        if (staffMember.getEmail().isEmpty()) {
            throw new RuntimeException();
        }

        return true;
    }

}
