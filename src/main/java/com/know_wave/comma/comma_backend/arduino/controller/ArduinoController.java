package com.know_wave.comma.comma_backend.arduino.controller;

import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoDetailResponse;
import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoReplyCommentResponse;
import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoResponse;
import com.know_wave.comma.comma_backend.arduino.dto.category.CategoryDto;
import com.know_wave.comma.comma_backend.arduino.dto.comment.CommentRequest;
import com.know_wave.comma.comma_backend.arduino.dto.comment.ReplyCommentRequest;
import com.know_wave.comma.comma_backend.arduino.service.normal.ArduinoCommunityService;
import com.know_wave.comma.comma_backend.arduino.service.normal.ArduinoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arduinos")
public class ArduinoController {

    private final ArduinoService arduinoService;
    private final ArduinoCommunityService arduinoCommunityService;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int defaultPageSize;

    public ArduinoController(ArduinoService arduinoService, ArduinoCommunityService arduinoCommunityService) {
        this.arduinoService = arduinoService;
        this.arduinoCommunityService = arduinoCommunityService;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return arduinoService.getAllCategories();
    }

    @GetMapping("/{arduinoId}")
    public ArduinoDetailResponse getArduino(@PathVariable("arduinoId") Long id) {
        return arduinoService.getArduinoDetails(id);
    }

    @GetMapping("/{arduinoId}/comments")
    public ArduinoDetailResponse getArduinoComments(@PathVariable("arduinoId") Long id, @PageableDefault(size = 10) Pageable pageable) {
        return arduinoService.getArduinoDetails(id, pageable);
    }

    @GetMapping
    public ResponseEntity<Page<ArduinoResponse>> getArduinoListPaging(Pageable pageable) {
        var result = arduinoService.getPage(PageRequest.of(pageable.getPageNumber(), defaultPageSize, pageable.getSort()));

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{arduinoId}/comment")
    public String addComment(@PathVariable("arduinoId") Long id, @Valid @RequestBody CommentRequest request) {
        arduinoCommunityService.submitComment(id, request);
        return "Submitted comment";
    }

    @PatchMapping("/{arduinoId}/comments/{commentId}")
    public String updateComment(@PathVariable("arduinoId") Long arduinoId, @PathVariable("commentId") Long commentId,
                                @RequestBody CommentRequest request) {
        arduinoCommunityService.updateComment(arduinoId, commentId, request);
        return "Updated comment";
    }

    @DeleteMapping("/{arduinoId}/comments/{commentId}")
    public String deleteComment(@PathVariable("arduinoId") Long arduinoId, @PathVariable("commentId") Long commentId) {
        arduinoCommunityService.deleteComment(arduinoId, commentId);
        return "Deleted comment";
    }

    @GetMapping("/{arduinoId}/comments/{commentId}/reply")
    public List<ArduinoReplyCommentResponse> getReplyComment(@PathVariable("arduinoId") Long arduinoId,
                                                             @PathVariable("commentId") Long commentId,
                                                             @PageableDefault(size = 10) Pageable pageable) {
        return arduinoCommunityService.getReplyComment(arduinoId, commentId, pageable);
    }

    @PostMapping("/{arduinoId}/comments/{commentId}/reply")
    public String addReplyComment(@PathVariable("arduinoId") Long arduinoId, @PathVariable("commentId") Long commentId,
                                  @Valid @RequestBody ReplyCommentRequest request) {
        arduinoCommunityService.submitReplyComment(arduinoId, commentId, request);
        return "Submitted reply comment";
    }


    @PatchMapping("/{arduinoId}/comments/{commentId}/reply/{replyCommentId}")
    public String updateReplyComment(@PathVariable("arduinoId") Long arduinoId, @PathVariable("commentId") Long commentId,
                                     @PathVariable("replyCommentId") Long replyCommentId, @Valid @RequestBody ReplyCommentRequest request) {
        arduinoCommunityService.updateReplyComment(arduinoId, commentId, replyCommentId, request);
        return "Updated reply comment";
    }

    @DeleteMapping("/{arduinoId}/comments/{commentId}/reply/{replyCommentId}")
    public String deleteReplyComment(@PathVariable("arduinoId") Long arduinoId, @PathVariable("commentId") Long commentId,
                                     @PathVariable("replyCommentId") Long replyCommentId) {
        arduinoCommunityService.deleteReplyComment(arduinoId, commentId, replyCommentId);
        return "Deleted reply comment";
    }

    @PostMapping("/{arduinoId}/like")
    public String likeArduino(@PathVariable("arduinoId") Long arduinoId) {
        arduinoCommunityService.likeArduino(arduinoId);
        return "Liked arduino";
    }

    @DeleteMapping("/{arduinoId}/like")
    public String unlikeArduino(@PathVariable("arduinoId") Long arduinoId) {
        arduinoCommunityService.unlikeArduino(arduinoId);
        return "Unliked arduino";
    }

}
