package com.example.auth.controller;


import com.example.auth.commons.Access;
import com.example.auth.commons.constant.ResponseConstant;
import com.example.auth.commons.decorator.DataResponse;
import com.example.auth.commons.decorator.ListResponse;
import com.example.auth.commons.decorator.Response;
import com.example.auth.commons.enums.Role;
import com.example.auth.stockPile.decorator.AddComment;
import com.example.auth.stockPile.decorator.CommentAddRequest;
import com.example.auth.stockPile.decorator.CommentResponse;
import com.example.auth.stockPile.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping(name = "addComment" , value = "/add/comment")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<CommentResponse> addComment(@RequestBody AddComment addComment) {
        DataResponse<CommentResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(commentService.addComment(addComment));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.SAVED_SUCCESSFULLY));
        return dataResponse;
    }


    @PutMapping(name = "updateComment" , value = "/update/comment")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> updateComment(@RequestParam String id, @RequestBody CommentAddRequest commentAddRequest) throws NoSuchFieldException, IllegalAccessException {
        DataResponse<Object> dataResponse = new DataResponse<>();
        commentService.updateComment(id, commentAddRequest);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.UPDATED_SUCCESSFULLY));
        return dataResponse;
    }


    @GetMapping(name = "getCommentById" , value = "comment/{id}")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<CommentResponse> getCommentById(@PathVariable String id) {
        DataResponse<CommentResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(commentService.getCommentById(id));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return dataResponse;
    }



    @GetMapping(name = "getAllComment" , value = "/all/comment")
    @Access(levels = Role.ANONYMOUS)
    public ListResponse<CommentResponse> getAllComment() {
        ListResponse<CommentResponse> listResponse = new ListResponse<>();
        listResponse.setData(commentService.getAllComment());
        listResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return listResponse;
    }


    @DeleteMapping(name = "deleteCommentById", value = "/delete/{id}")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> deleteCommentById(@PathVariable String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
       commentService.deleteCommentById(id);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.DELETED_SUCCESSFULLY));
        return dataResponse;
    }


}
