package com.example.auth.stockPile.service;


import com.example.auth.commons.constant.MessageConstant;
import com.example.auth.commons.exception.NotFoundException;
import com.example.auth.commons.helper.UserHelper;
import com.example.auth.stockPile.decorator.AddComment;
import com.example.auth.stockPile.decorator.CommentAddRequest;
import com.example.auth.stockPile.decorator.CommentResponse;
import com.example.auth.stockPile.model.Comment;
import com.example.auth.stockPile.model.Post;
import com.example.auth.stockPile.model.UserData;
import com.example.auth.stockPile.repository.CommentRepository;
import com.example.auth.stockPile.repository.PostRepository;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserHelper userHelper;
    private final ModelMapper modelMapper;
    private final UserDataServiceImpl userDataService;
    private final PostServiceImpl postService;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserHelper userHelper, ModelMapper modelMapper, UserDataServiceImpl userDataService, @Lazy PostServiceImpl postService, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userHelper = userHelper;
        this.modelMapper = modelMapper;
        this.userDataService = userDataService;
        this.postService = postService;
        this.postRepository = postRepository;
    }


    @Override
    public CommentResponse addComment(AddComment addComment) {
        UserData userData = userDataService.userById(addComment.getUserId());
        Post post = postService.getById(addComment.getPostId());
        Comment comment = modelMapper.map(addComment, Comment.class);
        comment.setCommentId(userData.getId());
        comment.setCommentedBy(userData.getName());
        comment.setCreatedOn(currentDate());
        comment.setPost(post.getId());
        commentRepository.save(comment);
        int commentCount = post.getComments() == 0 ? 1 : post.getComments() + 1;
        post.setComments(commentCount);
        postRepository.save(post);
        CommentResponse commentResponse = modelMapper.map(comment, CommentResponse.class);
        return commentResponse;

    }
    @VisibleForTesting
    Date currentDate() {
        return new Date();
    }


    @Override
    public void updateComment(String id, CommentAddRequest commentAddRequest) throws NoSuchFieldException, IllegalAccessException {
        Comment comment = getById(id);
        update(commentAddRequest, id);
        userHelper.difference(commentAddRequest, comment);
    }


    @Override
    public CommentResponse getCommentById(String id) {
        Comment comment = getById(id);
        return modelMapper.map(comment, CommentResponse.class);

    }

    @Override
    public List<CommentResponse> getAllComment() {
        List<Comment> comments = commentRepository.findAllBySoftDeleteFalse();
        List<CommentResponse> commentResponses = new ArrayList<>();
        comments.forEach(comment -> {
            CommentResponse commentResponse = modelMapper.map(comment, CommentResponse.class);
            commentResponses.add(commentResponse);
        });
        return commentResponses;
    }


    @Override
    public void deleteCommentById(String id) {
        Comment comment = getById(id);
        comment.setSoftDelete(true);
        commentRepository.save(comment);
    }

    private void update(CommentAddRequest commentAddRequest, String id) {

        Comment comment = getById(id);
        if (commentAddRequest.getComment() != null) {
            comment.setComment(commentAddRequest.getComment());
        }
        commentRepository.save(comment);
    }


    @Override
    public void removeComments(String id) {
        List<Comment> comments = commentRepository.findAllByPostAndSoftDeleteIsFalse(id);

        if (!CollectionUtils.isEmpty(comments)) {
            comments.forEach(comment -> {
                comment.setSoftDelete(true);
            });
            commentRepository.saveAll(comments);
        }
    }


    public Comment getById(String id) {
        return commentRepository.getByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.ID_NOT_FOUND));
    }
}
