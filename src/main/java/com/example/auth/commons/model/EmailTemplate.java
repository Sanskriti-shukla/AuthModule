package com.example.auth.commons.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplate {
    String from;
    String username;
    String password;
    String host;
    String port;
    Set<String> requiredEmailItems = getRequiredItems();



    private Set<String> getRequiredItems() {
        Set<String> requiredEmailItems = new HashSet<>();
        requiredEmailItems.add("@");
        return requiredEmailItems;
    }

    private Set<String> getExtensionsData() {
        Set<String> extensions = new HashSet<>();
        extensions.add("gmail.com");
        extensions.add("yahoo.com");
        return extensions;
    }

    private Set<String> getTechAdminEmails() {
        Set<String> emails = new HashSet<>();
        emails.add("sanskriti.s@techroversolutions.com");
        return emails;
    }


}
