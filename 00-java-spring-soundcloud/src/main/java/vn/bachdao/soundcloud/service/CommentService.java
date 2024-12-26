package vn.bachdao.soundcloud.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.bachdao.soundcloud.domain.Comment;
import vn.bachdao.soundcloud.repository.CommentRepository;
import vn.bachdao.soundcloud.service.dto.repsonse.ResultPaginationDTO;
import vn.bachdao.soundcloud.service.dto.repsonse.comment.ResCommentDTO;
import vn.bachdao.soundcloud.service.mapper.CommentMapper;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public ResCommentDTO handleCreateComment(Comment comment) {
        Comment savedComment = this.commentRepository.save(comment);
        ResCommentDTO commentDTO = this.commentMapper.commentToResCommentDTO(savedComment);
        return commentDTO;
    }

    public Optional<Comment> fetchCommentById(long id) {
        return this.commentRepository.findById(id);
    }

    public ResCommentDTO handleUpdateComment(Comment reqComment, Comment currComment) {
        currComment.setContent(reqComment.getContent());
        Comment updatedComment = this.commentRepository.save(currComment);
        ResCommentDTO commentDTO = this.commentMapper.commentToResCommentDTO(updatedComment);
        return commentDTO;
    }

    public ResultPaginationDTO handleGetAllComments(Specification<Comment> spec, Pageable pageable) {
        Page<Comment> commentPage = this.commentRepository.findAll(spec, pageable);

        ResultPaginationDTO res = new ResultPaginationDTO();

        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPageNumber(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPage(commentPage.getTotalPages());
        mt.setTotalElement(commentPage.getTotalElements());

        res.setMeta(mt);
        res.setData(this.commentMapper.commentToResCommentDTO(commentPage.getContent()));

        return res;
    }

    public ResCommentDTO handleDeleteComment(Comment comment) {
        comment.setDeleted(true);

        Comment updatedComment = this.commentRepository.save(comment);
        ResCommentDTO commentDTO = this.commentMapper.commentToResCommentDTO(updatedComment);
        return commentDTO;
    }
}
