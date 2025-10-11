package kuke.board.comment.api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import kuke.board.comment.service.response.CommentPageResponse;
import kuke.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CommentApiTest {
	RestClient restClient = RestClient.create("http://localhost:9001");

	@Test
	void create() {
		CommentResponse response1 = createComment(new CommentCreateRequest(1L, "my comment1", null, 1L));
		CommentResponse response2 = createComment(new CommentCreateRequest(1L, "my comment2", response1.getCommentId(), 1L));
		CommentResponse response3 = createComment(new CommentCreateRequest(1L, "my comment3", response1.getCommentId(), 1L));

		System.out.println("commentId=%s".formatted(response1.getCommentId()));
		System.out.println("\tcommentId=%s".formatted(response2.getCommentId()));
		System.out.println("\tcommentId=%s".formatted(response3.getCommentId()));
	}

	CommentResponse createComment(CommentCreateRequest request) {
		return restClient.post()
			.uri("/v1/comments")
			.body(request)
			.retrieve()
			.body(CommentResponse.class);
	}




	@Test
	void read() {
		CommentResponse response = restClient.get()
			.uri("/v1/comments/{commentId}", 235388879048081408L)
			.retrieve()
			.body(CommentResponse.class);

		System.out.println("response = " + response);
	}

	@Test
	void delete() {
		restClient.delete()
			.uri("/v1/comments/{commentId}", 235388879320711168L)
			.retrieve()
			.toBodilessEntity();
	}

	@Test
	void readAll() {
		CommentPageResponse response = restClient.get()
			.uri("/v1/comments?articleId=1&page=1&pageSize=10")
			.retrieve()
			.body(CommentPageResponse.class);
		System.out.println("response.getCommentCount() = " + response.getCommentCount());
		for (CommentResponse comment : response.getComments()) {
			if (comment.getCommentId().equals(comment.getParentCommentId())) {
				System.out.print("\t");
			}
			System.out.println("comment.getCommentId() = " + comment.getCommentId());
			
		}
	}

	/**
	 * 1번 수행결과
	 * 	response.getCommentCount() = 101
	 * 	comment.getCommentId() = 235050329906438144
	 * comment.getCommentId() = 235050330497835008
	 * comment.getCommentId() = 235050330569138176
	 * 	comment.getCommentId() = 235388332068896768
	 * comment.getCommentId() = 235388332458967040
	 * comment.getCommentId() = 235388332526075904
	 * 	comment.getCommentId() = 235390406980493312
	 * comment.getCommentId() = 235390407613833216
	 * comment.getCommentId() = 235390407668359168
	 * 	comment.getCommentId() = 235394353600888832
	 */

	@Test
	void readAllInfiniteScroll() {
		List<CommentResponse> response1 = restClient.get()
			.uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5")
			.retrieve()
			.body(new ParameterizedTypeReference<List<CommentResponse>>() {
			});

		System.out.println("firstPage");
		for (CommentResponse comment : response1) {
			if (!comment.getCommentId().equals(comment.getParentCommentId())) {
				System.out.print("\t");
			}
			System.out.println("comment.getCommentId() = " + comment.getCommentId());
		}

		Long lastParentCommentId = response1.getLast().getParentCommentId();
		Long lastCommentId = response1.getLast().getCommentId();

		List<CommentResponse> response2 = restClient.get()
			.uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5&lastParentCommentId=%s&lastCommentId=%s"
				.formatted(lastParentCommentId, lastCommentId))
			.retrieve()
			.body(new ParameterizedTypeReference<List<CommentResponse>>() {
			});

		System.out.println("secondPage");
		for (CommentResponse comment : response2) {
			if (!comment.getCommentId().equals(comment.getParentCommentId())) {
				System.out.print("\t");
			}
			System.out.println("comment.getCommentId() = " + comment.getCommentId());
		}
	}

	@Getter
	@AllArgsConstructor
	public static class CommentCreateRequest {
		private Long articleId;
		private String content;
		private Long parentCommentId;
		private Long writerId;

	}

}
