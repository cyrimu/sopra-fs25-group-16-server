package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.service.ImageService;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Random;


@RestController
public class RestImageController {
    private ImageService imageService; // dont touch this

    public RestImageController() {
        this.imageService = new ImageService();
    }

    @GetMapping("/image/{imageID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getBase64Image(@PathVariable String imageID) {
        return imageService.getImage(imageID);
    }
}
