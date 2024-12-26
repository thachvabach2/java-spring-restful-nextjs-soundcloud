package vn.bachdao.soundcloud.web.rest;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.bachdao.soundcloud.domain.Comment;
import vn.bachdao.soundcloud.domain.Track;
import vn.bachdao.soundcloud.domain.User;
import vn.bachdao.soundcloud.security.SecurityUtils;
import vn.bachdao.soundcloud.service.CommentService;
import vn.bachdao.soundcloud.service.TrackService;
import vn.bachdao.soundcloud.service.UserService;
import vn.bachdao.soundcloud.service.dto.repsonse.ResultPaginationDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.comment.ResCommentDTO;
import vn.bachdao.soundcloud.service.util.annotation.ApiMessage;
import vn.bachdao.soundcloud.web.rest.errors.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CommentResource {

    private final CommentService commentService;
    private final UserService userService;
    private final TrackService trackService;

    public CommentResource(
            CommentService commentService,
            UserService userService,
            TrackService trackService) {
        this.commentService = commentService;
        this.userService = userService;
        this.trackService = trackService;
    }

    @PostMapping("/comments")
    @ApiMessage("Create a comment")
    public ResponseEntity<ResCommentDTO> createComment(@Valid @RequestBody Comment comment) throws IdInvalidException {
        // check track exist
        Optional<Track> trackOptional = this.trackService.fetchTrackById(comment.getTrack().getId());
        if (trackOptional.isEmpty()) {
            throw new IdInvalidException("Track với id = " + comment.getTrack().getId() + " không tồn tại");
        }

        // add user logged
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("Hãy đăng nhập");
        }
        User currentUser = this.userService.getUserByEmail(email);

        comment.setTrack(trackOptional.get());
        comment.setUser(currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.commentService.handleCreateComment(comment));
    }

    @PutMapping("/comments")
    @ApiMessage("Update a comment by id")
    public ResponseEntity<ResCommentDTO> updateComment(@RequestBody Comment reqComment) throws IdInvalidException {
        // check id
        Optional<Comment> commentOptional = this.commentService.fetchCommentById(reqComment.getId());
        if (commentOptional.isEmpty()) {
            throw new IdInvalidException("Comment với id = " + reqComment.getId() + " không tồn tại");
        }

        return ResponseEntity.ok(this.commentService.handleUpdateComment(reqComment, commentOptional.get()));
    }

    @GetMapping("/comments")
    @ApiMessage("Fetch all comments with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllComments(@Filter Specification<Comment> spec, Pageable pageable) {
        return ResponseEntity.ok(this.commentService.handleGetAllComments(spec, pageable));
    }

    @DeleteMapping("/comments/{id}")
    @ApiMessage("Delete a comment by id")
    public ResponseEntity<ResCommentDTO> deleteComment(@PathVariable("id") long id) throws IdInvalidException {
        // check id
        Optional<Comment> commentOptional = this.commentService.fetchCommentById(id);
        if (commentOptional.isEmpty()) {
            throw new IdInvalidException("Comment với id = " + id + " không tồn tại");
        }

        return ResponseEntity.ok(this.commentService.handleDeleteComment(commentOptional.get()));
    }
}
