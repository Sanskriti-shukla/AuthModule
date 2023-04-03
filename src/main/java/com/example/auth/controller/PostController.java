package com.example.auth.controller;
import com.example.auth.commons.Access;
import com.example.auth.commons.constant.ResponseConstant;
import com.example.auth.commons.decorator.DataResponse;
import com.example.auth.commons.decorator.GeneralHelper;
import com.example.auth.commons.decorator.ListResponse;
import com.example.auth.commons.decorator.Response;
import com.example.auth.commons.enums.Role;
import com.example.auth.decorator.CommentsResponse;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.decorator.pagination.PageResponse;
import com.example.auth.stockPile.decorator.*;
import com.example.auth.stockPile.model.Post;
import com.example.auth.stockPile.model.ReactionType;
import com.example.auth.stockPile.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import javax.mail.Multipart;
import java.util.List;

@RestController
@RequestMapping("post")
public class PostController {
    private final PostService postService;
    private final GeneralHelper generalHelper;


    public PostController(PostService postService, GeneralHelper generalHelper) {
        this.postService = postService;
        this.generalHelper = generalHelper;
    }

//    @RequestMapping(name = "addPost",value = "/add",method = RequestMethod.POST)
    @PostMapping(name = "addPost" ,value = "/add")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<PostResponse> addPost(@RequestBody PostAddParameter postAddParameter){
        DataResponse<PostResponse> dataResponse= new DataResponse<>();
        dataResponse.setData(postService.addPost(postAddParameter));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.SAVED_SUCCESSFULLY));
        return dataResponse;
    }
//    @RequestMapping(name = "updatePost",value = "/update/post",method = RequestMethod.POST)
    @PutMapping(name = "updatePost" , value = "/update/post")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> updatePost(@RequestParam String id, @RequestBody PostAddRequest postAddRequest) throws NoSuchFieldException, IllegalAccessException {
        DataResponse<Object> dataResponse= new DataResponse<>();
        postService.updatePost(id,postAddRequest);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.UPDATED_SUCCESSFULLY));
        return dataResponse;
    }
//    @RequestMapping(name = "getPostById",value = "/{id}",method = RequestMethod.GET)
    @GetMapping(name = "getPostById" , value = "/{id}")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<PostResponse> getPostById( @PathVariable String id){
        DataResponse<PostResponse> dataResponse= new DataResponse<>();
        dataResponse.setData(postService.getPostById(id));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return dataResponse;
    }
//    @RequestMapping(name = "getAllPost",value = "/all/post",method = RequestMethod.GET)
    @GetMapping(name = "getAllPost", value = "/all/post")
    @Access(levels = Role.ANONYMOUS)
    public ListResponse<PostResponse> getAllPost(){
        ListResponse<PostResponse> listResponse= new ListResponse<>();
        listResponse.setData(postService.getAllPost());
        listResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return listResponse;
    }

//    @RequestMapping(name = "deletePostById",value = "/delete/{id}",method = RequestMethod.POST)
    @DeleteMapping(name = "deletePostById" ,value = "delete/{id}")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> deletePostById( @PathVariable String id){
        DataResponse<Object> dataResponse= new DataResponse<>();
        postService.deletePostById(id);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.DELETED_SUCCESSFULLY));
        return dataResponse;
    }


//   @RequestMapping(name = "addReaction",value = "/add/reaction",method = RequestMethod.POST)
    @PostMapping(name = "addReation", value = "/add/reaction")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> addReaction(@RequestParam ReactionType reactionType, @RequestBody  ReactionAddRequest reactionAddRequest){
        DataResponse<Object> dataResponse= new DataResponse<>();
       postService.addReaction(reactionType,reactionAddRequest);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.REACTION_ADDED_SUCCESSFULLY));
        return dataResponse;
    }


//    @RequestMapping(name = "getAllReactionByPostId",value = "/all/reaction",method = RequestMethod.POST)
    @GetMapping(name = "getAllReactionByPostId" , value = "/all/reaction")
    @Access(levels = Role.ANONYMOUS)
    public ListResponse<ReactionResponse> getAllReactionByPostId(@RequestParam String postId){
        ListResponse<ReactionResponse> dataResponse= new ListResponse<>();
        dataResponse.setData(postService.getAllReactionByPostId(postId));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return dataResponse;
    }

//    @RequestMapping(name = "getAllCommentByPostId",value = "/all/comment",method = RequestMethod.POST)
    @PostMapping(name = "getAllCommentByPostId" , value = "/all/comment")
    @Access(levels = Role.ANONYMOUS)
    public ListResponse<CommentsResponse> getAllCommentByPostId(@RequestParam String postId){
        ListResponse<CommentsResponse> dataResponse= new ListResponse<>();
        dataResponse.setData(postService.getAllCommentByPostId(postId));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return dataResponse;
    }



//    @RequestMapping(name = "getAllPostByPagination",value = "/all/pagination",method = RequestMethod.POST)
    @PostMapping(name = "getAllPostByPagination" , value = "/all/pagination")
    @Access(levels = Role.ANONYMOUS)
    public PageResponse<PostResponse> getAllPostByPagination(@RequestBody FilterSortRequest<PostFilter, PostSortBy> filterSortRequest){
        PageResponse<PostResponse> pageResponse= new PageResponse<>();
        Page<PostResponse> page= postService.getAllPostByPagination(filterSortRequest.getFilter(),filterSortRequest.getSort()
                ,generalHelper.getPagination(filterSortRequest.getPagination().getPage(),filterSortRequest.getPagination().getLimit()));
        pageResponse.setData(page);
        pageResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return  pageResponse;

    }



//    @RequestMapping(name = "getAllPostByTopicId",value = "/all/topicId",method = RequestMethod.GET)
    @GetMapping(name = "getAllPostByTopicId"  ,value = "/all/topicId")
    @Access(levels = Role.ANONYMOUS)
    public ListResponse<PostResponse> getAllPostByTopicId(@RequestParam String topicId){
        ListResponse<PostResponse> listResponse= new ListResponse<>();
        listResponse.setData(postService.getAllPostByTopicId(topicId));
        listResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return listResponse;
    }

//    @RequestMapping(name = "deleteReaction",value = "deleteReaction/id",method = RequestMethod.POST)
    @DeleteMapping(name = "deleteReaction" , value = "/deleteReaction/id")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> deleteReaction(@RequestBody ReactionAddRequest reactionAddRequest){
        DataResponse<Object> dataResponse= new DataResponse<>();
        postService.deleteReaction(reactionAddRequest);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.REACTION_DELETED_SUCCESSFULLY));
        return dataResponse;
    }



}
