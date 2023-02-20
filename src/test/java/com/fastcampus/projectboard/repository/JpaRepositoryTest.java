package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentsRepository articleCommentsRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository,@Autowired ArticleCommentsRepository articleCommentsRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentsRepository = articleCommentsRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine(){
        //Given

        //When
        List<Article> articles = articleRepository.findAll();
        //Then
        assertThat(articles).isNotNull()
                .hasSize(900);
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine(){
        //Given
        long previousCount = articleRepository.count();
        Article article = Article.of("new article", "new content", "#spring");
        //When
        Article savedArticle = articleRepository.save(article);
        List<Article> articles = articleRepository.findAll();
        //Then
        assertThat(articleRepository.count()).isEqualTo(previousCount+1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine(){
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateHashtag = "#springboot";
        article.setHashtag(updateHashtag);
        //When
        Article savedArticle = articleRepository.save(article);
        //Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag",updateHashtag);
    }
    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine(){
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousCount = articleRepository.count();
        long previousArticleComment = articleCommentsRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();
        //When
        articleRepository.delete(article);
        //Then
        assertThat(articleRepository.count()).isEqualTo(previousCount-1);
        assertThat(articleCommentsRepository.count()).isEqualTo(previousArticleComment-deletedCommentsSize);
    }


}