package com.example.pfa.services;

import com.example.pfa.entities.Contact;
import java.util.List;

public interface IContactService {
    Contact saveContact(Contact contact);
    List<Contact> getAllContacts();
    Contact getContactById(Long id);
    void deleteContact(Long id);
}
