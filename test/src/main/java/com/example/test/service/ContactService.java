package com.example.test.service;

import com.example.test.entity.Contacts;
import com.example.test.repository.ContactsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactsRepository contactsRepository;

    public List<Contacts> getAllContacts() {
        return contactsRepository.findAll();
    }

    public Optional<Contacts> getContactById(Integer id) {
        return contactsRepository.findById(id);
    }

    public Contacts saveContact(Contacts contact) {
        return contactsRepository.save(contact);
    }

    public void deleteContact(Integer id) {
        contactsRepository.deleteById(id);
    }
}

