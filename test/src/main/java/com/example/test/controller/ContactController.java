package com.example.test.controller;

import com.example.test.entity.Contacts;
import com.example.test.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public List<Contacts> getAllContacts() {
        return contactService.getAllContacts();
    }

    @GetMapping("/{id}")

    public ResponseEntity<Contacts> getContactById(@PathVariable Integer id) {
        Optional<Contacts> contact = contactService.getContactById(id);
        return contact.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Contacts> createContact(@RequestBody Contacts contact) {
        Contacts savedContact = contactService.saveContact(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contacts> updateContact(@PathVariable Integer id, @RequestBody Contacts contact) {
        if (contactService.getContactById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        contact.setId(id);
        Contacts updatedContact = contactService.saveContact(contact);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Integer id) {
        if (contactService.getContactById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> sayHelloToAdmin() {
        return ResponseEntity.ok("Hello Admin");
    }

    @GetMapping("/user")
    public ResponseEntity<String> sayHelloToUser() {
        return ResponseEntity.ok("Hello User");
    }
}
