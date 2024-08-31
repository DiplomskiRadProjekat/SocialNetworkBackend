package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.config.ApplicationConfig;
import com.internship.socialnetwork.dto.*;
import com.internship.socialnetwork.exception.BadRequestException;
import com.internship.socialnetwork.exception.FileUploadException;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.repository.PostRepository;
import com.internship.socialnetwork.service.PostService;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.internship.socialnetwork.dto.NewPostDTO.toPost;
import static com.internship.socialnetwork.dto.PostDTO.toPostDTO;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final String POST_NOT_FOUND_MESSAGE = "Post with id %s doesn't exist!";

    private final PostRepository postRepository;

    private final UserService userService;

    private final ApplicationConfig appConfig;

    @Override
    public PostDTO create(Long userId, NewPostDTO newPostDTO) {
        return toPostDTO(postRepository.save(toPost(userService.findById(userId), newPostDTO)));
    }

    @Override
    public List<PostDTO> getAllForUser(Long userId) {
        return postRepository.findAllByPostedById(userId)
                .stream()
                .map(PostDTO::toPostDTO)
                .toList();
    }

    @Override
    public PostDTO get(Long id) {
        return toPostDTO(findById(id));
    }

    @Override
    public PostDTO update(Long id, UpdatePostDTO updatedPost) {
        Post post = findById(id);
        post.setDescription(updatedPost.getDescription());
        return toPostDTO(postRepository.save(post));
    }

    @Override
    public void delete(Long id) {
        postRepository.delete(findById(id));
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(POST_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public List<CommentDTO> getAllCommentsForPost(Long id) {
        return findById(id).getComments().stream()
                .map(CommentDTO::toCommentDTO)
                .toList();
    }

    @Override
    public List<PostDTO> getAllFriendsPostsForUser(Long id) {
        return postRepository.findFriendsPostsByUserId(id).stream()
                .map(PostDTO::toPostDTO)
                .toList();
    }

    @Override
    public void setImageToPost(Long id, PostImageDTO postImage) {
        Post post = findById(id);
        MultipartFile file = postImage.getFile();
        if (file != null) {
            try {
                String filePath = appConfig.getFilesPath() + "posts/" + file.getOriginalFilename();
                file.transferTo(new File(filePath));
                post.getFilePaths().add(filePath);
                postRepository.save(post);
            } catch (IOException ex) {
                throw new FileUploadException("File upload failed: " + ex.getMessage());
            }
        }
    }

    @Override
    public byte[] downloadImage(String path) {
        if (path != null) {
            try {
                return Files.readAllBytes(new File(path).toPath());
            } catch (IOException ex) {
                throw new BadRequestException("Downloading image failed.");
            }
        }
        return null;
    }

}
