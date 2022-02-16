package com.iccs.graduatoriaservice.controller;

import com.iccs.graduatoriaservice.service.GraduatoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graudatoria")
public class GraduatioriaController {

    private GraduatoriaService graduatoriaService;

//    public PostController(GraduatoriaService postService) {
//        this.graduatoriaService = postService;
//    }

//    // create a blog post
//    @PostMapping
//    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
//        return new ResponseEntity<>(graduatoriaService.create((postDto)), HttpStatus.CREATED);
//    }

}
