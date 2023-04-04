package com.example.auth.stockPile.service;

import com.example.auth.stockPile.decorator.AddComment;
import com.example.auth.stockPile.decorator.CommentAddRequest;
import com.example.auth.stockPile.decorator.CommentResponse;

import java.util.List;

public interface CommentService {

    void deleteCommentById(String id);

    void updateComment(String id, CommentAddRequest commentAddRequest) throws NoSuchFieldException, IllegalAccessException;

    CommentResponse getCommentById(String id);

    List<CommentResponse> getAllComment();

    CommentResponse addComment(AddComment addComment);


    void removeComments(String id);
}



