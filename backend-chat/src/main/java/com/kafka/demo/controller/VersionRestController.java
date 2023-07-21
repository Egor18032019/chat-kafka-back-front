package com.kafka.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RestController
@RequestMapping("/version")
public class VersionRestController {
    private static final Logger log = LoggerFactory.getLogger(VersionRestController.class);

    @GetMapping
    public String getVersion() throws NoSuchFileException {
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> textStream = Files.lines(Paths.get("backend-chat/pom.xml"))) {
            textStream.forEach(stringBuilder::append);
        } catch (IOException e) {
            log.warn("File pom.xml not found ");
            throw new NoSuchFileException("File pom.xml not found");
        }
        final Pattern pattern = Pattern.compile("<version>(.+?)</version>",
                Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(stringBuilder.toString());
        matcher.find();
        matcher.find();
        // первым find он версию спринга находит
        return matcher.group(1);
    }
}