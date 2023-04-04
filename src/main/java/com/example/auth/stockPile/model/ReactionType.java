package com.example.auth.stockPile.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum ReactionType {
    UPVOTE("upVote"),
    DOWNVOTE("downVote");

      private String value;

    ReactionType(String value) {
        this.value = value;
    }

    public static ReactionType getOtherReactionType(ReactionType existingReactionType) {
        if (existingReactionType == UPVOTE) {
            return DOWNVOTE;
        } else if (existingReactionType == DOWNVOTE) {
            return UPVOTE;
        } else {
            throw new IllegalArgumentException("Invalid reaction type");
        }
    }

    }
