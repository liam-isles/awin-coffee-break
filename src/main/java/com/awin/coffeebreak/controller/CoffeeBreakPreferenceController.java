package com.awin.coffeebreak.controller;

import com.awin.coffeebreak.entity.CoffeeBreakPreference;
import com.awin.coffeebreak.entity.StaffMember;
import com.awin.coffeebreak.repository.CoffeeBreakPreferenceRepository;
import com.awin.coffeebreak.repository.StaffMemberRepository;
import com.awin.coffeebreak.services.EmailNotifier;

import java.util.*;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CoffeeBreakPreferenceController {

    public CoffeeBreakPreferenceRepository coffeeBreakPreferenceRepository;
    public StaffMemberRepository staffMemberRepository;

    public CoffeeBreakPreferenceController(
          CoffeeBreakPreferenceRepository coffeeBreakPreferenceRepository,
          StaffMemberRepository staffMemberRepository
    ) {
        this.coffeeBreakPreferenceRepository = coffeeBreakPreferenceRepository;
        this.staffMemberRepository = staffMemberRepository;

    }

    /**
     * Publishes the list of preferences in the requested format
     */
    @GetMapping(path = "/today")
    public ResponseEntity<?> today(@RequestParam("format") String format) {
        if (format == null) {
            format = "html";
        }

        List<CoffeeBreakPreference> t = coffeeBreakPreferenceRepository.getPreferencesForToday();

        String responseContent;
        String contentType = "text/html";

        switch (format) {
            case "json":
                responseContent = getJsonForResponse(t);
                contentType = "application/json";
                break;

            case "xml":
                responseContent = getXmlForResponse(t);
                contentType = "text/xml";
                break;

            default:
                String formattedPreferences = getHtmlForResponse(t);
                return ResponseEntity.ok().contentType(MediaType.valueOf(contentType))
                      .body(formattedPreferences);
        }

        return ResponseEntity.ok()
              .contentType(MediaType.valueOf(contentType))
              .body(responseContent);
    }

    @GetMapping("/notifyStaffMember")
    public ResponseEntity<Object> notifyStaffMember(@RequestParam("staffMemberId") int id) {
        Optional<StaffMember> staffMember = this.staffMemberRepository.findById(id);

        List<CoffeeBreakPreference> preferences = new ArrayList<>();

        EmailNotifier notifier = new EmailNotifier();
        boolean ok = notifier.notifyStaffMember(staffMember.get(), preferences);


        return ResponseEntity.ok(ok ? "OK" : "NOT OK");
    }

    @PostMapping("/addNewStaff")
    public ResponseEntity addNewStaffMember(
            @RequestBody StaffMember newStaffMember) {

        //request needs some usable data
        if(newStaffMember == null) {
            return ResponseEntity.badRequest()
                   .body("Staff information required to add new staff member");
        }

        //saving details to repo
        staffMemberRepository.save(newStaffMember);

        return ResponseEntity.status(201)
               .body("Staff member added with the following details: " +
                       "\n id: " + newStaffMember.getId() +
                       "\n name: " + newStaffMember.getName() +
                       "\n email: " + newStaffMember.getEmail());
    }

    @PostMapping("/addPreference")
    public ResponseEntity addPreference(
            @RequestBody CoffeeBreakPreference coffeeBreakPreference) {
        //doing a check on the ID to see if it already exists
        Optional<StaffMember> staffMember = staffMemberRepository.findById(coffeeBreakPreference.getId());

        //if it does not exist it is created as part of the process
        if(staffMember.isEmpty()) {
            StaffMember createNewStaff;
            createNewStaff = coffeeBreakPreference.getRequestedBy();
            staffMemberRepository.save(createNewStaff);
        }


        //setting details
        Map<String, String> details = new HashMap<>();
        details.put(coffeeBreakPreference.getId().toString(),coffeeBreakPreference.toString());
        coffeeBreakPreference.setDetails(details);

        //updating staff member list of preferences
        StaffMember existingStaff = coffeeBreakPreference.getRequestedBy();
        List<CoffeeBreakPreference> staffPreferences = existingStaff.getCoffeeBreakPreferences();
        staffPreferences.add(coffeeBreakPreference);
        existingStaff.setCoffeeBreakPreferences(staffPreferences);

        coffeeBreakPreferenceRepository.save(coffeeBreakPreference);


        return ResponseEntity.ok()
                .body("preference added: \n " + coffeeBreakPreference);
    }

    private String getJsonForResponse(final List<CoffeeBreakPreference> list) {
        String responseJson = "{\"preferences\":[";

        for (final CoffeeBreakPreference p : list) {
            responseJson += p.getAsJson();
        }

        return responseJson += "]}";
    }

    private String getXmlForResponse(List<CoffeeBreakPreference> list) {
        String responseJson = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        responseJson += "<Preferences>";

        for (final CoffeeBreakPreference p : list) {
            responseJson += p.getAsXml();
        }

        responseJson += "</Preferences>";

        return responseJson;
    }

    private String getHtmlForResponse(final List<CoffeeBreakPreference> list) {
        String responseJson = "<ul>";

        for (final CoffeeBreakPreference p : list) {
            responseJson += p.getAsListElement();
        }

        return responseJson + "</ul>";
    }
}
