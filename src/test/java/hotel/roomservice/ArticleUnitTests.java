package hotel.roomservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.core.Currencies;

import hotel.roomservice.Article.ArticleType;
import hotel.roomservice.Article.SortOption;

public class ArticleUnitTests {

    @Test
    void article() {

        Article article = new Article("Essen", Money.of(5, Currencies.EURO), ArticleType.FOOD);

        assertEquals("Essen", article.getName());
        assertEquals(Money.of(5, Currencies.EURO), article.getPrice());
        assertEquals(ArticleType.FOOD, article.getType());
    }

    @Test
    void articleOf() {

        Article article = Article.of("Trinken", Money.of(3, Currencies.EURO), ArticleType.DRINK);

        assertEquals("Trinken", article.getName());
        assertEquals(Money.of(3, Currencies.EURO), article.getPrice());
        assertEquals(ArticleType.DRINK, article.getType());
    }

    @Test
    void articleType() {

        assertEquals("Essen", ArticleType.FOOD.toString());
        assertEquals("Getränk", ArticleType.DRINK.toString());
        assertEquals("Film", ArticleType.MOVIE.toString());
        assertEquals("Spiel", ArticleType.GAME.toString());
    }

    @Test
    void sortOption() {

        Article article1 = new Article("Film1", Money.of(7, Currencies.EURO), ArticleType.MOVIE);
        Article article2 = new Article("Spiel1", Money.of(11, Currencies.EURO), ArticleType.GAME);

        var articleList = List.of(article1, article2);

        articleList = articleList.stream().sorted(SortOption.TYPE.getComparator()).collect(Collectors.toList());
        assertEquals(List.of(article1, article2), articleList);

        articleList = articleList.stream().sorted(SortOption.NAME.getComparator()).collect(Collectors.toList());
        assertEquals(List.of(article1, article2), articleList);

        articleList = articleList.stream().sorted(SortOption.PRICE.getComparator()).collect(Collectors.toList());
        assertEquals(List.of(article2, article1), articleList);
    }
}
