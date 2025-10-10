package kuke.board.comment.service.response;

import java.time.LocalDateTime;

import kuke.board.comment.enttiy.Comment;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommentResponse {
	private Long commentId;
	private String content;
	private Long parentCommentId;
	private Long articleId;
	private Long writerId;
	private Boolean deleted;
	private LocalDateTime createAt;

	public static CommentResponse from(Comment comment) {
		CommentResponse commentResponse = new CommentResponse();
		commentResponse.commentId = comment.getCommentId();
		commentResponse.content = comment.getContent();
		commentResponse.parentCommentId = comment.getParentCommentId();
		commentResponse.articleId = comment.getArticleId();
		commentResponse.writerId = comment.getWriterId();
		commentResponse.deleted = comment.getDeleted();
		commentResponse.createAt = comment.getCreatedAt();
		return commentResponse;
	}
}
