package com.isegoria.server.image.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.isegoria.server.global.annotations.CurrentUser;
import com.isegoria.server.global.jwt.JwtPayload;
import com.isegoria.server.image.request.ImageRequest;
import com.isegoria.server.image.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("images")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;

  @PostMapping("upload")
  public List<String> uploadImages(@RequestParam("files") MultipartFile[] files) {
    List<String> response = imageService.uploadImages(files);
    return response;
  }

  @PostMapping("create")
  public List<String> createImages(@RequestBody ImageRequest request, @CurrentUser JwtPayload user) {
    List<String> response = imageService.createImage(request.getId(), request.getImages(), request.getEntity());
    return response;
  }
}