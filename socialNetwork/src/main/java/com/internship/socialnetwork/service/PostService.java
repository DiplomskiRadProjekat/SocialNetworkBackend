package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.*;
import com.internship.socialnetwork.model.Post;

import java.util.List;

public interface PostService {

    PostDTO create(Long userId, NewPostDTO newPostDTO);

    List<PostDTO> getAllForUser(Long userId);

    PostDTO get(Long id);

    PostDTO update(Long id, UpdatePostDTO updatedPost);

    void delete(Long id);

    Post findById(Long id);

    List<CommentDTO> getAllCommentsForPost(Long id);

    List<PostDTO> getAllFriendsPostsForUser(Long id);

    void setImageToPost(Long id, PostImageDTO postImage);

    byte[] downloadImage(String path);

}
