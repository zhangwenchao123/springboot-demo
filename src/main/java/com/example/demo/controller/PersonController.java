package com.example.demo.controller;

import com.example.demo.model.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final List<Person> persons = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private static final String UPLOAD_DIR = "uploads/avatars/";

    @GetMapping
    public List<Person> list() {
        return persons;
    }

    @PostMapping
    public ResponseEntity<Person> add(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) throws IOException {

        Person person = new Person();
        person.setId(idCounter.getAndIncrement());
        person.setName(name);
        person.setEmail(email);
        person.setPhone(phone);

        if (avatar != null && !avatar.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);
            String filename = UUID.randomUUID() + "_" + avatar.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, avatar.getBytes());
            person.setAvatarUrl("/uploads/avatars/" + filename);
        }

        persons.add(person);
        return ResponseEntity.ok(person);
    }
}
