package kuke.board.article.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import kuke.board.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ArticleApiTest {
	RestClient restClient = RestClient.create("http://localhost:9000");

	@Test
	void createTest() {
		ArticleResponse response = create(new ArticleCreateRequest(
			"hi", "my content", 1L, 1L
		));
		System.out.println("response = " + response);
	}

	@Test
	void readTest() {
		ArticleResponse response = read(233579563531513856L);
		System.out.println("response = " + response);
	}

	@Test
	void updateTest() {
		update(233579563531513856L);
		ArticleResponse response = read(233579563531513856L);
		System.out.println("response = " + response);

	}

	@Test
	void deleteTest() {
		restClient.delete()
			.uri("/v1/articles/{articleId}", 233579563531513856L)
			.retrieve()
			.toBodilessEntity();
	}

	void update(Long articleId) {
		restClient.put()
			.uri("/v1/articles/{articleId}", articleId)
			.body(new ArticleUpdateRequest("hi2", "my content 2"))
			.retrieve()
			.toEntity(ArticleResponse.class);
	}

	ArticleResponse read(Long articleId) {
		return restClient.get()
			.uri("/v1/articles/{articleId}", articleId)
			.retrieve()
			.body(ArticleResponse.class);
	}

	ArticleResponse create(ArticleCreateRequest request) {
		return restClient.post()
			.uri("/v1/articles")
			.body(request)
			.retrieve()
			.body(ArticleResponse.class);
	}

	@Getter
	@AllArgsConstructor
	static public class ArticleCreateRequest {
		private String title;
		private String content;
		private Long writerId;
		private Long boardId;
	}

	@Getter
	@AllArgsConstructor
	static public class ArticleUpdateRequest {
		private String title;
		private String content;
	}
}
