package kuke.board.article.service.response;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ArticlePageResponse {
	private List<ArticleResponse> articles;
	private Long articleCount;

	// 팩토리 메서드
	public static ArticlePageResponse of(List<ArticleResponse> articles, Long articleCount) {
		ArticlePageResponse response = new ArticlePageResponse();
		response.articles = articles;
		response.articleCount = articleCount;
		return response;
	}
}
