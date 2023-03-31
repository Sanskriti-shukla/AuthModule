package com.example.auth.stockPile.service;
import com.example.auth.commons.constant.MessageConstant;
import com.example.auth.commons.exception.NotFoundException;
import com.example.auth.commons.helper.UserHelper;
import com.example.auth.decorator.CommentsResponse;
import com.example.auth.decorator.pagination.FilterSortRequest;
import com.example.auth.stockPile.decorator.*;
import com.example.auth.stockPile.model.*;
import com.example.auth.stockPile.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final StockServiceImpl stockService;
    private final TopicServiceImpl topicService;
    private final TopicRepository topicRepository;
    private final UserDataServiceImpl userDataService;
    private final ModelMapper modelMapper;
    private final UserHelper userHelper;
    private final Reaction reaction;
    private final ReactionRepository reactionRepository;
    private final UserDataRepository userDataRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;


    public PostServiceImpl(PostRepository postRepository, StockServiceImpl stockService, TopicServiceImpl topicService, TopicRepository topicRepository, UserDataServiceImpl userDataService, ModelMapper modelMapper, UserHelper userHelper, Reaction reaction, ReactionRepository reactionRepository, UserDataRepository userDataRepository, CommentRepository commentRepository, CommentService commentService) {
        this.postRepository = postRepository;
        this.stockService = stockService;
        this.topicService = topicService;
        this.topicRepository = topicRepository;
        this.userDataService = userDataService;
        this.modelMapper = modelMapper;
        this.userHelper = userHelper;
        this.reaction = reaction;
        this.reactionRepository = reactionRepository;
        this.userDataRepository = userDataRepository;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }


    @Override
    public PostResponse addPost(PostAddParameter postAddParameter) {
        Stock stock = stockService.stockById(postAddParameter.getStockId());
        UserData userData = userDataService.userById(postAddParameter.getUserId());
        Topic topic = topicService.topicById(postAddParameter.getTopicId());
        Post post = modelMapper.map(postAddParameter.getPostAddRequest(), Post.class);
        post.setCreatedOn(new Date());
        post.setStockInfo(stock.getId());
        post.setTopicInfo(topic.getId());
        PostResponse postResponse = modelMapper.map(post, PostResponse.class);
        postResponse.setPostBy(userData);
        post.setPostBy(userData.getId());
        postRepository.save(post);
        return postResponse;
    }

    @Override
    public void updatePost(String id, PostAddRequest postAddRequest) throws NoSuchFieldException, IllegalAccessException {
        Post post = getById(id);
        update(postAddRequest, id);
        userHelper.difference(postAddRequest, post);
    }


    @Override
    public PostResponse getPostById(String id) {
        Post post = getById(id);
       return modelMapper.map(post, PostResponse.class);
    }

    @Override
    public List<PostResponse> getAllPost() {
        List<Post> posts = postRepository.findAllBySoftDeleteFalse();
        List<PostResponse> postResponses = new ArrayList<>();
        posts.forEach(post -> {
            PostResponse postResponse = modelMapper.map(post, PostResponse.class);
            postResponses.add(postResponse);
        });
        return postResponses;
    }

    @Override
    public void deletePostById(String id) {
        Post post = getById(id);
        removeReaction(id);
        commentService.removeComments(id);
        post.setSoftDelete(true);
        postRepository.save(post);

    }

    @Override
    public Page<PostResponse> getAllPostByPagination(PostFilter filter, FilterSortRequest.SortRequest<PostSortBy> sort, PageRequest pagination) {
        return postRepository.getAllPostByPagination(filter, sort, pagination);
    }

    @Override
    public List<Post> getAllPostByTopicId(String topicId) {
        List<PostResponse> postResponses = new ArrayList<>();
        boolean exist = postRepository.existsByTopicInfoAndSoftDeleteFalse(topicId);
        if (!exist) {
            throw new NotFoundException(MessageConstant.ID_NOT_FOUND);
        } else {
            List<Post> posts = postRepository.findByTopicInfoAndSoftDeleteIsFalse(topicId);
            return posts;
        }
    }


    @Override
    public List<ReactionResponse> getAllReactionByPostId(String postId) {
          Post post = getById(postId);
        List<Reaction> reactions = reactionRepository.findAllByPostIdAndSoftDeleteIsFalse(postId);
        List<ReactionResponse> reactionResponses = new ArrayList<>();
        for (Reaction reaction : reactions) {
            ReactionResponse reactionResponse = new ReactionResponse();
            UserData user =getUserById(reaction.getUserId());
            reactionResponse.setUserId(user.getId());
            reactionResponse.setName(user.getName());
            reactionResponse.setReaction(reaction.getReactionType());
            reactionResponses.add(reactionResponse);
        }
        return reactionResponses;
    }

    @Override
    public List<CommentsResponse> getAllCommentByPostId(String postId) {
        Post post = getById(postId);
        List<Comment> comments =commentRepository .findAllByPostAndSoftDeleteIsFalse(postId);
        List<CommentsResponse> commentsResponses = new ArrayList<>();
        for (Comment comment : comments) {
           CommentsResponse commentResponse= new CommentsResponse();
            UserData user =getUserById(comment.getCommentId());
            commentResponse.setUserId(user.getId());
            commentResponse.setName(user.getName());
            commentResponse.setComment(comment.getComment());
            commentResponse.setCreatedOn(comment.getCreatedOn());
            commentsResponses.add(commentResponse);

        }
        return commentsResponses;
    }
@Override
public void addReaction(ReactionType reactionType, ReactionAddRequest reactionAddRequest) {
    Post post = getById(reactionAddRequest.getPostId());
    UserData userData = userDataService.userById(reactionAddRequest.getUserId());
     checkReaction(reactionType,reactionAddRequest);
    // Add the new reaction
    Reaction newReaction = new Reaction();
    newReaction.setPostId(post.getId());
    newReaction.setUserId(userData.getId());
    newReaction.setReactionType(reactionType);
    newReaction.setSoftDelete(false);
    reactionRepository.save(newReaction);


}
//
//    public void checkReaction(ReactionType reactionType, ReactionAddRequest reactionAddRequest){
//        Post post = getById(reactionAddRequest.getPostId());
//        UserData userData = userDataService.userById(reactionAddRequest.getUserId());
//        Reaction existingReaction = reactionRepository.findByPostIdAndUserId(post.getId(), userData.getId());
//
//        if (existingReaction != null) {
//            ReactionType existingReactionType = existingReaction.getReactionType();
//            int existingCount = post.getReaction().getOrDefault(existingReactionType, 0);
//            if (existingCount > 0) {
//                existingCount--;
//                post.getReaction().put(existingReactionType, existingCount);
//            }
//            ReactionType otherReactionType = ReactionType.getOtherReactionType(existingReactionType);
//            post.getReaction().put(otherReactionType, 0);
//            reactionRepository.delete(existingReaction);
//        }
//        int count = post.getReaction().getOrDefault(reactionType, 0);
//        count++;
//        post.getReaction().put(reactionType, count);
//        postRepository.save(post);
//
//    }

    public void checkReaction(ReactionType reactionType, ReactionAddRequest reactionAddRequest){
        Post post = getById(reactionAddRequest.getPostId());
        UserData userData = userDataService.userById(reactionAddRequest.getUserId());
        Reaction existingReaction = reactionRepository.findByPostIdAndUserId(post.getId(), userData.getId());

        if (existingReaction != null) {
            ReactionType existingReactionType = existingReaction.getReactionType();
            int existingCount = post.getReaction().getOrDefault(existingReactionType, 0);
            if (existingCount > 0) {
                existingCount--;
                Map<ReactionType, Integer> newReactionMap = new HashMap<>(post.getReaction());
                newReactionMap.put(existingReactionType, existingCount);
                post.setReaction(newReactionMap);
            }
            ReactionType otherReactionType = ReactionType.getOtherReactionType(existingReactionType);
            Map<ReactionType, Integer> newReactionMap = new HashMap<>(post.getReaction());
            newReactionMap.put(otherReactionType, 0);
            post.setReaction(newReactionMap);
            reactionRepository.delete(existingReaction);
        }
        int count = post.getReaction().getOrDefault(reactionType, 0);
        count++;
        Map<ReactionType, Integer> newReactionMap = new HashMap<>(post.getReaction());
        newReactionMap.put(reactionType, count);
        post.setReaction(newReactionMap);
        postRepository.save(post);

    }
    @Override
    public void deleteReaction(ReactionAddRequest reactionAddRequest) {
        Post post = getById(reactionAddRequest.getPostId());
        UserData userData = userDataService.userById(reactionAddRequest.getUserId());
        Reaction existingReaction = reactionRepository.findByPostIdAndUserId(post.getId(), userData.getId());
        if (existingReaction != null) {
            ReactionType reactionType = existingReaction.getReactionType();
            int currentCount = post.getReaction().getOrDefault(reactionType, 0);
            if (currentCount > 0) {
                post.getReaction().put(reactionType, currentCount - 1);
                postRepository.save(post);
            }
            existingReaction.setSoftDelete(true);
            reactionRepository.save(existingReaction);
        }
    }

    private void update(PostAddRequest postAddRequest, String id) {
        Post post = getById(id);
        if (postAddRequest.getTemplateContent() != null) {
            post.setTemplateContent(postAddRequest.getTemplateContent());
        }
        postRepository.save(post);
    }

    public Post getById(String id) {
        return postRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.ID_NOT_FOUND));
    }
     public UserData getUserById(String userId){
        return  userDataRepository.findById(userId).orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
     }

    public void removeReaction(String id) {
        List<Reaction> reactions = reactionRepository.findAllByPostIdAndSoftDeleteIsFalse(id);

        if (!CollectionUtils.isEmpty(reactions)) {
            reactions.forEach(reaction -> {
                reaction.setSoftDelete(true);
            });
            reactionRepository.saveAll(reactions);
        }
    }
}
