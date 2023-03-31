package com.example.auth.stockPileTest.helper;

import com.example.auth.decorator.CommentsResponse;
import com.example.auth.stockPile.decorator.*;
import com.example.auth.stockPile.model.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PostServiceImplTestGenerator {
    private final static String id= "id";
    private final static String userId="id";
    private final static String stockId="id";
    private final static String topicId="id";
    private final static String reactionId="id";
    private final static String commentId="id";
    private final static String postId="id";
    private final static ReactionType reactionType=ReactionType.UPVOTE;

    private final static List<String> subscribers= Collections.singletonList("23232");
    private final static Map<ReactionType, Integer> reactions = Collections.singletonMap(ReactionType.DOWNVOTE, 1);
    public static ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    public static PostResponse mockPostResponse(Date date,String id, int comment,Map<ReactionType, Integer> reactions){
        return PostResponse.builder()
                .postBy(mockUserData())
                .createdOn(date)
                .comments(comment)
                .stockInfo(stockId)
                .topicInfo(topicId)
                .reaction(reactions)
                .id(id)
                .createdOn(date)
                .build();

    }
    public static UserData mockUserData(){
        return UserData.builder()
                .subscribe(true)
                .id(userId)
                .email("sans@gmail.com")
                .userName("sans")
                .name("sans")
                .contact("6385254154")
                .build();
    }

    public static PostAddParameter mockPostAddParameter(){
        return PostAddParameter.builder()
                .postAddRequest(mockPostAddRequest())
                .stockId(stockId)
                .topicId(topicId)
                .userId(userId)
                .build();


    }
    public static PostAddRequest mockPostAddRequest(){
       return PostAddRequest.builder()
                .title("hello")
                .templateContent("hello")
                .build();

    }
    public static Stock mockStock(){
        return Stock.builder()
                .id(stockId)
                .symbol("AAPL")
                .description("AAPL")
                .name("Apple Inc")
                .subscribers(subscribers)
                .build();
    }
    public static Topic mockTopic(Date date){
        return Topic.builder()
                .id(topicId)
                .title("hello")
                .description("hello")
                .createdBy(mockUserData().getId())
                .createdOn(date)
                .stockId(stockId)
                .stockSymbol("AAPL")
                .stockName("AAPL")
                .build();
    }

    public static Post mockPost(Date date){
        return Post.builder()
                .id(postId)
                .stockInfo(stockId)
                .reaction(reactions)
                .postBy(mockUserData().getId())
                .createdOn(date)
                .topicInfo(topicId)
                .build();
    }

    public static List<ReactionResponse> mockReactionResponse(){
        return List.of(ReactionResponse.builder()
                .userId(userId)
                .name("sans")
                .reaction(reactionType)
                .build());
    }

    public static  List<Reaction> mockReactions(){
        return List.of(Reaction.builder()
                .id(reactionId)
                .userId(userId)
                .postId(postId)
                .reactionType(reactionType)
                .build());
    }

    public static Reaction mockReaction(){
     return    Reaction.builder()
                .id(reactionId)
                .userId(userId)
                .postId(postId)
                .reactionType(reactionType)
                .build();
    }
    public static  List<CommentsResponse> mockcommentsResponse(Date date){
        return List.of(CommentsResponse.builder()
                .userId(userId)
                .name("sans")
                .comment("very helpful")
                .createdOn(date)
                .build());
    }

    public static List<Comment> mockComment(Date date){
        return List.of(Comment.builder()
                .id(commentId)
                .commentId(userId)
                .commentedBy("sans")
                .post("hello")
                .comment("very helpful")
                .createdOn(date)
                .build());

    }
    public static Comment mockGetComment(Date date){
       return Comment.builder()
                .id(commentId)
                .commentId(userId)
                .commentedBy("sans")
                .post("hello")
                .comment("very helpful")
                .createdOn(date)
                .build();
    }



    public static CommentsResponse mockCommentResponse(Date date){
        return CommentsResponse.builder()
                .userId(userId)
                .name("sans")
                .comment("very helpful")
                .createdOn(date)
                .build();
    }

    public static ReactionAddRequest mockReactionAddRequest(){
        return ReactionAddRequest.builder()
                .userId(userId)
                .postId(postId)
                .build();
    }

    public static List<Post> mockAllPost(Date date){
        return List.of(Post.builder()
                .stockInfo(stockId)
                .postBy(mockUserData().getId())
                .createdOn(date)
                .topicInfo(topicId)
                .build());
    }
    public static List<PostResponse> mockAllPostResponse(Date date){
        return List.of( PostResponse.builder()
                .postBy(mockUserData())
                .createdOn(date)
                .stockInfo(stockId)
                .topicInfo(topicId)
                .createdOn(date)
                .build());
    }

}
