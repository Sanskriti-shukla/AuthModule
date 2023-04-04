package com.example.auth.stockPileTest.helper;

import com.example.auth.stockPile.decorator.AddComment;
import com.example.auth.stockPile.decorator.CommentAddRequest;
import com.example.auth.stockPile.decorator.CommentResponse;
import com.example.auth.stockPile.model.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CommentServiceImplTestGenerator {
    private final static String id="id";
    private final static String commentId="id";
    private final static String postId="id";
    private final static String userId="id";
    private final static Map<ReactionType, Integer> reactions = Collections.singletonMap(ReactionType.DOWNVOTE, 1);
    public static ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }


    public static Comment mockComment(Date date){
        return Comment.builder()
                .comment("very helpful")
                .commentedBy("sanskriti")
                .id(id)
                .createdOn(date)
                .post("AAPL")
                .commentId(commentId)
                .build();
    }


    public static AddComment getMockAddComment(){
        return AddComment.builder()
                .comment("very helpful")
                .postId(postId)
                .userId(userId)
                .build();
    }

    public static CommentResponse getMockCommentResponse(Date date,String id,String post){
        return  CommentResponse.builder()
                 .id(id)
                .comment("very helpful")
                .commentId(commentId)
                .commentedBy("sanskriti")
                .createdOn(date)
                .post(post)
                .build();

    }

    public static UserData getMockUserData(){
        return UserData.builder()
                .id(userId)
                .name("sanskriti")
                .userName("sans")
                .email("sans@gmail.com")
                .contact("6386580395")
                .subscribe(true)
                .build();

    }

       public static Post getMockPost(Date date){
        return Post.builder()
                .comments(2)
                .build();


    }
    public static CommentAddRequest getMockCommentAddRequest(){
        return CommentAddRequest.builder()
                .comment("very helpful")
                .build();
    }


    public static List<CommentResponse> getMockAllCommentResponse(Date date){
        return List.of( CommentResponse.builder()
                .id(id)
                .comment("very helpful")
                .commentId(commentId)
                .commentedBy("sanskriti")
                .createdOn(date)
                .post("AAPL")
                .build());
    }

    public static    List<Comment> getAllMockComment(Date date){
        return List.of(Comment.builder()
                .comment("very helpful")
                .commentedBy("sanskriti")
                .id(id)
                .createdOn(date)
                .post("AAPL")
                .commentId(commentId)
                .build());
    }


}
