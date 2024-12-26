package vn.bachdao.soundcloud.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import vn.bachdao.soundcloud.domain.Comment;
import vn.bachdao.soundcloud.service.dto.repsonse.comment.ResCommentDTO;

@Mapper(componentModel = "spring", uses = {})
public interface CommentMapper {

    ResCommentDTO commentToResCommentDTO(Comment comment);

    List<ResCommentDTO> commentToResCommentDTO(List<Comment> comments);
}
