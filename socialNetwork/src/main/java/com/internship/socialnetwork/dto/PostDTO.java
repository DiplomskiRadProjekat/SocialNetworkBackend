package com.internship.socialnetwork.dto;

import com.internship.socialnetwork.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Long id;

    private Long userId;

    private String description;

    private List<String> filePaths;

    private String postedAt;

    private List<CommentDTO> comments;

    public static PostDTO toPostDTO(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .userId(post.getPostedBy().getId())
                .description(post.getDescription())
                .filePaths(post.getFilePaths())
                .postedAt(post.getPostedAt().toString())
                .comments(post.getComments()
                        .stream()
                        .map(CommentDTO::toCommentDTO)
                        .toList())
                .build();
    }

}
