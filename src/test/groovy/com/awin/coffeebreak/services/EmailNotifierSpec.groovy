package com.awin.coffeebreak.services

import com.awin.coffeebreak.entity.CoffeeBreakPreference
import com.awin.coffeebreak.entity.StaffMember
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class EmailNotifierSpec extends Specification {

    def "testStatusOfNotificationIsTrue"() {
        given:
        def staff = new StaffMember()
        staff.setEmail("ABC123@awin.com")
        def preference = new CoffeeBreakPreference("drink", "coffee", staff, null)

        def notificationService = new EmailNotifier()

        when:
        def status = notificationService.notifyStaffMember(staff, [preference])

        then:
        assertThat(status).isTrue()
    }

    def "testThrowsExceptionWhenCannotNotify"() {
        given:
        def staff = new StaffMember()
        def preference = new CoffeeBreakPreference("drink", "tea", staff, null)
        def notificationService = new EmailNotifier()

        when:
        def status = notificationService.notifyStaffMember(staff, [preference])

        then:
        thrown(RuntimeException.class)
    }
}
