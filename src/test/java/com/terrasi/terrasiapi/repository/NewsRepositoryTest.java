package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.News;
import com.terrasi.terrasiapi.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class NewsRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    UserRepository userRepository;

    private News prepareNews(){
        User user = userRepository.getOne(1L);

        News news = new News();
        news.setTitle("test");
        news.setUser(user);
        news.setShortContent("test news");
        news.setReadTime(5);
        news.setImgNews("img/news/");
        news.setImgNewsMobile("img/news/mobile");
        news.setImgThumbnail("img/thumb");
        news.setImgThumbnailMobile("img/thumb/mobile");
        news.setCreateDate(LocalDateTime.now());
        return news;
    }

    @Test
    public void should_save_news(){

        //given
        News news = prepareNews();

        //then
        assertEquals(entityManager.persistAndFlush(news), newsRepository.findById(news.getId()).get());
    }

    @Test
    public void should_not_save_news_bad_read_time(){
        //given
        News news = prepareNews();
        news.setReadTime(0);

        //then
        assertThrows(ConstraintViolationException.class,() -> entityManager.persistAndFlush(news));
    }

    @Test
    public void should_not_save_news_bad_create_date(){
        //given
        News news = prepareNews();
        news.setCreateDate(LocalDateTime.of(2100, 3, 3, 10, 10));

        //then
        assertThrows(ConstraintViolationException.class,() -> entityManager.persistAndFlush(news));
    }

}
