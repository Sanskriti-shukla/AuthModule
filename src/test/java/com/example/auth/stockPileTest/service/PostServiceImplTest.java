package com.example.auth.stockPileTest.service;

import com.example.auth.commons.helper.UserHelper;
import com.example.auth.stockPile.model.Reaction;
import com.example.auth.stockPile.model.ReactionType;
import com.example.auth.stockPile.repository.*;
import com.example.auth.stockPile.service.*;
import com.example.auth.stockPileTest.helper.PostServiceImplTestGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
 class PostServiceImplTest {
    private final static Map<ReactionType, Integer> reactions = Collections.singletonMap(ReactionType.DOWNVOTE, 1);
    private final static String id = "id";
    private final static String topicId = "id";
    private final static String postId = "id";
    private final static ReactionType reactionType=ReactionType.UPVOTE;
    private final PostRepository postRepository= mock(PostRepository.class);
    private final StockServiceImpl stockService = mock(StockServiceImpl.class);
    private final TopicServiceImpl topicService = mock(TopicServiceImpl.class);
    private final UserDataServiceImpl userDataService = mock(UserDataServiceImpl.class);
    private final ModelMapper modelMapper = PostServiceImplTestGenerator.getModelMapper();
    private final UserHelper userHelper = mock(UserHelper.class);
    private final ReactionRepository reactionRepository = mock(ReactionRepository.class);
    private final UserDataRepository userDataRepository = mock(UserDataRepository.class);
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final CommentService commentService = mock(CommentService.class);

    public PostService postService = new PostServiceImpl(postRepository,stockService,topicService,
            userDataService,modelMapper,userHelper,reactionRepository,userDataRepository,commentRepository,commentService);


    @Test
    void  testAddPost(){
        Date date= new Date();
        var stock= PostServiceImplTestGenerator.mockStock();

        var userData = PostServiceImplTestGenerator.mockUserData();
        var topic = PostServiceImplTestGenerator.mockTopic(date);
        var post= PostServiceImplTestGenerator.mockPost(date);

        var postResponse = PostServiceImplTestGenerator.mockPostResponse(date,null,0,null);
        var postAddParameter = PostServiceImplTestGenerator.mockPostAddParameter();

        when(stockService.stockById(postAddParameter.getStockId())).thenReturn(stock);

        when(userDataService.userById(postAddParameter.getUserId())).thenReturn(userData);
        when(topicService.topicById(postAddParameter.getTopicId())).thenReturn(topic);

        when(postRepository.save(post)).thenReturn(post);
        var actualData = postService.addPost(postAddParameter);
        Assertions.assertEquals(postResponse.getPostBy(),actualData.getPostBy());

    }

    @Test
    void testUpdatePost() throws NoSuchFieldException, IllegalAccessException {
        Date date= new Date();
        var post= PostServiceImplTestGenerator.mockPost(date);

        var postAddRequest = PostServiceImplTestGenerator.mockPostAddRequest();
        when(postRepository.findByIdAndSoftDeleteIsFalse("id")).thenReturn(java.util.Optional.ofNullable(post));

        userHelper.difference(postAddRequest,post);
        postService.updatePost("id", postAddRequest);

        verify(postRepository, times(2)).findByIdAndSoftDeleteIsFalse("id");

    }

    @Test
    void testGetPostById(){
        Date date= new Date();
        var post =PostServiceImplTestGenerator.mockPost(date);

        var postResponse = PostServiceImplTestGenerator.mockPostResponse(date,id,0,reactions);

        when(postRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(java.util.Optional.ofNullable(post));

        var actualData = postService.getPostById(id);

        Assertions.assertEquals(postResponse,actualData);

    }

    @Test
    void testDeletePostById(){
        Date date= new Date();
        var post =PostServiceImplTestGenerator.mockPost(date);
        commentService.removeComments(id);

        when(postRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(java.util.Optional.ofNullable(post));

        postService.deletePostById(id);

        verify(postRepository, times(1)).findByIdAndSoftDeleteIsFalse(id);
    }

    @Test
    void testGetAllPost(){
        Date date= new Date();
        var posts= PostServiceImplTestGenerator.mockAllPost(date);
        var allPostResponse = PostServiceImplTestGenerator.mockAllPostResponse(date);

        when(postRepository.findAllBySoftDeleteFalse()).thenReturn(posts);
        modelMapper.map(posts,allPostResponse);
      var actualData = postService.getAllPost();

        Assertions.assertEquals(allPostResponse,actualData);

    }

    @Test
    void testGetAllPostByTopicId(){
        Date date= new Date();
        var posts= PostServiceImplTestGenerator.mockAllPost(date);
        var allPostResponse = PostServiceImplTestGenerator.mockAllPostResponse(date);

        when(postRepository.existsByTopicInfoAndSoftDeleteFalse(topicId)).thenReturn(true);

        when(postRepository.findByTopicInfoAndSoftDeleteIsFalse(topicId)).thenReturn(posts);

        var actualData = postService.getAllPostByTopicId(topicId);

        Assertions.assertEquals(posts,actualData);
    }

    @Test

    void testGetAllReactionByPostId(){
        Date date= new Date();
        var reactionResponse = PostServiceImplTestGenerator.mockReactionResponse();

        var userData = PostServiceImplTestGenerator.mockUserData();

        var post = PostServiceImplTestGenerator.mockPost(date);

        var reactions = PostServiceImplTestGenerator.mockReactions();

        var reaction = PostServiceImplTestGenerator.mockReaction();
        when(postRepository.findByIdAndSoftDeleteIsFalse(postId)).thenReturn(java.util.Optional.ofNullable(post));

        when(reactionRepository.findAllByPostIdAndSoftDeleteIsFalse(postId)).thenReturn(reactions);

        when(userDataRepository.findById(reaction.getUserId())).thenReturn(java.util.Optional.ofNullable(userData));

        var actualData = postService.getAllReactionByPostId(postId);

        Assertions.assertEquals(reactionResponse,actualData);


    }

    @Test

    void testGetAllCommentByPostId(){
        Date date= new Date();

        var post = PostServiceImplTestGenerator.mockPost(date);

        var comments = PostServiceImplTestGenerator.mockComment(date);

        var comment = PostServiceImplTestGenerator.mockGetComment(date);

        var commentsResponse = PostServiceImplTestGenerator.mockcommentsResponse(date);

        var userData = PostServiceImplTestGenerator.mockUserData();

        when(postRepository.findByIdAndSoftDeleteIsFalse(postId)).thenReturn(java.util.Optional.ofNullable(post));

        when(commentRepository.findAllByPostAndSoftDeleteIsFalse(postId)).thenReturn(comments);

        when(userDataRepository.findById(comment.getCommentId())).thenReturn(java.util.Optional.ofNullable(userData));

        var actualData = postService.getAllCommentByPostId(postId);

        Assertions.assertEquals(commentsResponse,actualData);
    }

    @Test
    void testAddReaction(){

        Date date= new Date();

        var post = PostServiceImplTestGenerator.mockPost(date);

        var userData = PostServiceImplTestGenerator.mockUserData();

        var reaction  = PostServiceImplTestGenerator.mockReaction();

        var reactionAddRequest = PostServiceImplTestGenerator.mockReactionAddRequest();

        when(postRepository.findByIdAndSoftDeleteIsFalse(reactionAddRequest.getPostId())).thenReturn(java.util.Optional.ofNullable(post));

        when(userDataService.userById(reactionAddRequest.getUserId())).thenReturn(userData);

        when(reactionRepository.findByPostIdAndUserId(post.getId(),userData.getId())).thenReturn(reaction);

          when(postRepository.save(post)).thenReturn(post);

          postService.addReaction(reactionType,reactionAddRequest);

        when(reactionRepository.save(reaction)).thenReturn(reaction);

    }

    @Test
    void testDeleteReaction(){
        Date date= new Date();

        var post = PostServiceImplTestGenerator.mockPost(date);

        var userData = PostServiceImplTestGenerator.mockUserData();

        var reaction  = PostServiceImplTestGenerator.mockReaction();

        var reactionAddRequest = PostServiceImplTestGenerator.mockReactionAddRequest();

        when(postRepository.findByIdAndSoftDeleteIsFalse(reactionAddRequest.getPostId())).thenReturn(java.util.Optional.ofNullable(post));

        when(userDataService.userById(reactionAddRequest.getUserId())).thenReturn(userData);

        when(reactionRepository.findByPostIdAndUserId(post.getId(),userData.getId())).thenReturn(reaction);

        when(postRepository.save(post)).thenReturn(post);

        when(reactionRepository.save(reaction)).thenReturn(reaction);

        postService.deleteReaction(reactionAddRequest);

        verify(reactionRepository,times(1)).save(reaction);

    }





}
