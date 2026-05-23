package com.isegoria.server.image.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import com.isegoria.server.global.error.ErrorCode;
import com.isegoria.server.global.exception.ApiException;
import com.isegoria.server.image.request.ImageRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

  private final WebClient webClient;

  @Override
  public List<String> uploadImages(MultipartFile[] files) {
    log.info("=== Express 서버로 파일 업로드 시작 ===");
    log.info("업로드할 파일 개수: {}", files.length);

    MultipartBodyBuilder builder = new MultipartBodyBuilder();

    Arrays.stream(files).forEach(file -> {
      log.info("파일 정보: 이름={}, 크기={}, 타입={}",

          file.getOriginalFilename(), file.getSize(), file.getContentType());

      String filename = file.getOriginalFilename();
      if (filename == null || filename.isEmpty()) {
        filename = "file";
      }

      builder.part("files", file.getResource()).filename(filename);
    });

    try {
      var response = webClient.post()
          .uri("/images")
          .contentType(MediaType.MULTIPART_FORM_DATA)
          .body(BodyInserters.fromMultipartData(builder.build()))
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
          })
          .block();

      log.info("Express 서버 응답: {}", response);
      if (response != null) {
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.get("body");

        if (body != null) {
          @SuppressWarnings("unchecked")
          List<String> imageUrls = (List<String>) body.get("images");

          log.info("업로드된 이미지 URL들: {}", imageUrls);
          return imageUrls != null ? imageUrls : List.of();
        }
      }

      return List.of();

    } catch (Exception e) {
      log.error("Express 서버로 파일 업로드 실패: {}", e.getMessage(), e);
      throw new ApiException(ErrorCode.SAVE_IMAGE_FAILED);
    }
  }

  @Override
  public List<String> createImage(String id, List<String> images, String entity) {

    var response = webClient.post()
        .uri("/images/isegoria/create")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new ImageRequest(id, List.of(), images, entity))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();

    if (response == null) {
      throw new ApiException(ErrorCode.SAVE_IMAGE_FAILED);
    }

    if (response != null) {
      @SuppressWarnings("unchecked")
      Map<String, Object> body = (Map<String, Object>) response.get("body");

      if (body != null) {
        @SuppressWarnings("unchecked")
        List<String> imageUrls = (List<String>) body.get("images");

        log.info("업로드된 이미지 URL들: {}", imageUrls);
        return imageUrls != null ? imageUrls : List.of();
      }
    }

    return List.of();
  }

  @Override
  public List<String> updateImage(String id, List<String> images, List<String> existingImages, String entity) {
    log.info("=== 이미지 업데이트 시작 ===");
    log.info("Entity ID: {}, Type: {}", id, entity);
    log.info("새 이미지 개수: {}, 기존 이미지 개수: {}", images.size(), existingImages.size());

    try {
      var response = webClient.put()
          .uri("/images/isegoria/update")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(new ImageRequest(id, existingImages, images, entity))
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
          })
          .block();

      if (response == null) {
        throw new ApiException(ErrorCode.SAVE_IMAGE_FAILED);
      }

      log.info("Express 서버 응답: {}", response);
      log.info("새 이미지 {} 개 저장 완료", response.size());

      if (response != null) {
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.get("body");

        if (body != null) {
          @SuppressWarnings("unchecked")
          List<String> imageUrls = (List<String>) body.get("images");

          log.info("업로드된 이미지 URL들: {}", imageUrls);
          return imageUrls != null ? imageUrls : List.of();
        }
      }
      return List.of();

    } catch (Exception e) {
      log.error("이미지 업데이트 실패: {}", e.getMessage(), e);
      throw new ApiException(ErrorCode.SAVE_IMAGE_FAILED);
    }
  }

  @Override
  public void deleteImage(String id, String entity) {
    Map<String, Object> requestBody = Map.of(
        "id", id,
        "entity", entity);
    try {
      webClient.method(HttpMethod.DELETE)
          .uri("/images/isegoria/delete")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(requestBody)
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
          })
          .block();
    } catch (Exception e) {
      log.error("이미지 삭제 실패: {}", e.getMessage(), e);
      throw new ApiException(ErrorCode.SAVE_IMAGE_FAILED);
    }
  }
}
