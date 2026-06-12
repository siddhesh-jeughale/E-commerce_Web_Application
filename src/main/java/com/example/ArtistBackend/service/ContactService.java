package com.example.ArtistBackend.service;

import com.example.ArtistBackend.model.Contact;
import com.example.ArtistBackend.repository.ContactFormRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactFormRepo contactFormRepo;

    public void  saveContact(Contact contact) {
        contactFormRepo.save(contact);
    }
    public List<Contact> findAllContacts() {
        return contactFormRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    public long getMessageCount() {
        return contactFormRepo.count();
    }
}
