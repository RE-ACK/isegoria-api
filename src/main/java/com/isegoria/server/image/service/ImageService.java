package com.isegoria.server.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

  List<String> uploadImages(MultipartFile[] files);

  List<String> createImage(String id, List<String> images, String entity);

  List<String> updateImage(String id, List<String> images, List<String> existingImages, String entity);

  void deleteImage(String id, String entity);

}
