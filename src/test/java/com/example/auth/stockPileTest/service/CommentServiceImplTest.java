package com.example.auth.stockPileTest.service;

import com.example.auth.commons.exception.NotFoundException;
import com.example.auth.commons.helper.UserHelper;
import com.example.auth.helper.CategoryServiceImplGenerator;
import com.example.auth.stockPile.repository.CommentRepository;
import com.example.auth.stockPile.repository.PostRepository;
import com.example.auth.stockPile.service.CommentService;
import com.example.auth.stockPile.service.CommentServiceImpl;
import com.example.auth.stockPile.service.PostServiceImpl;
import com.example.auth.stockPile.service.UserDataServiceImpl;
import com.example.auth.stockPileTest.helper.CommentServiceImplTestGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.mockito.Mockito.*;

@SpringBootTest
 class CommentServiceImplTest {
    private static final String id= "id";
    private static final String post= "AAPL";
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final UserHelper userHelper=mock(UserHelper.class);
    private final UserDataServiceImpl userDataService=mock(UserDataServiceImpl.class);
    private final PostServiceImpl postService=mock(PostServiceImpl.class);
    private final PostRepository postRepository=mock(PostRepository.class);
    private final ModelMapper modelMapper = CommentServiceImplTestGenerator.getModelMapper();
  public CommentServiceImpl commentService= new CommentServiceImpl(commentRepository,userHelper,modelMapper,userDataService,postService,postRepository);


  @Test
     void testAddComment(){
      Date date = new Date();
      var userData  = CommentServiceImplTestGenerator.getMockUserData();
      var post = CommentServiceImplTestGenerator.getMockPost(date);

      var comment = CommentServiceImplTestGenerator.mockComment(date);

      var commentResponse = CommentServiceImplTestGenerator.getMockCommentResponse(date,null,null);

      var addComment= CommentServiceImplTestGenerator.getMockAddComment();

      when(userDataService.userById(addComment.getUserId())).thenReturn(userData);

      when(postService.getById(addComment.getPostId())).thenReturn(post);

      when(commentRepository.save(comment)).thenReturn(comment);

      when(postRepository.save(post)).thenReturn(post);

      var actualData= commentService.addComment(addComment);

      Assertions.assertEquals(commentResponse.getComment(),actualData.getComment());

  }

  @Test
    void testUpdateComment() throws NoSuchFieldException, IllegalAccessException {
      Date date = new Date();
      var comment= CommentServiceImplTestGenerator.mockComment(date);

      var commentAddRequest= CommentServiceImplTestGenerator.getMockCommentAddRequest();

      when(commentRepository.getByIdAndSoftDeleteIsFalse(id)).thenReturn(java.util.Optional.ofNullable(comment));

      commentService.updateComment(id, commentAddRequest);

      userHelper.difference(commentAddRequest, comment);

      verify(commentRepository,times(2)).getByIdAndSoftDeleteIsFalse(id);

  }


     @Test
     void testGetCommentById(){
         Date date = new Date();

         var comment= CommentServiceImplTestGenerator.mockComment(date);

         var commentResponse = CommentServiceImplTestGenerator.getMockCommentResponse(date,id,post);

         when(commentRepository.getByIdAndSoftDeleteIsFalse(id)).thenReturn(java.util.Optional.ofNullable(comment));

         var actualData= commentService.getCommentById(id);

         Assertions.assertEquals(commentResponse,actualData);
     }


     @Test
    void  testGetAllComment(){
         Date date = new Date();
         var comments= CommentServiceImplTestGenerator.getAllMockComment(date);

         var commentResponse= CommentServiceImplTestGenerator.getMockAllCommentResponse(date);

         when(commentRepository.findAllBySoftDeleteFalse()).thenReturn(comments);

         var actualData= commentService.getAllComment();

         Assertions.assertEquals(commentResponse,actualData);
     }

     @Test
    void  testDeleteCommentById(){
         Date date = new Date();

         var comment =CommentServiceImplTestGenerator.mockComment(date);

         when(commentRepository.getByIdAndSoftDeleteIsFalse(id)).thenReturn(java.util.Optional.ofNullable(comment));

         commentService.deleteCommentById(id);

    verify(commentRepository,times(1)).getByIdAndSoftDeleteIsFalse(id);
  }


  @Test
    void  testDeleteCommentByIdNotFound(){

         Date date = new Date();

         var comment =CommentServiceImplTestGenerator.mockComment(date);

         when(commentRepository.getByIdAndSoftDeleteIsFalse(id)).thenReturn(java.util.Optional.ofNullable(comment));

         Throwable exception = Assertions.assertThrows(NotFoundException.class, () -> commentService.deleteCommentById(null));

         Assertions.assertEquals("Id not found", exception.getMessage());
  }

}
