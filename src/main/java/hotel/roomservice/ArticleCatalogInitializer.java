package hotel.roomservice;

import static org.salespointframework.core.Currencies.EURO;

import java.util.List;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import hotel.roomservice.Article.ArticleType;

@Component
@Order(20)
class CatalogServiceInitializer implements DataInitializer {

    private final ArticleCatalog catalog;

    CatalogServiceInitializer(ArticleCatalog catalog) {

        Assert.notNull(catalog, "ArticleCatalog must not be null!");

        this.catalog = catalog;
    }

    @Override
    public void initialize() {

        List.of(Article.of("Wasser", Money.of(3, EURO), ArticleType.DRINK),
                Article.of("Cola", Money.of(5, EURO), ArticleType.DRINK),
                Article.of("Apfelsaft", Money.of(4, EURO), ArticleType.DRINK),
                Article.of("Obstteller", Money.of(4, EURO), ArticleType.FOOD),
                Article.of("Belegtes Brot", Money.of(3, EURO), ArticleType.FOOD),
                Article.of("Vergoldetes Steak", Money.of(1000, EURO), ArticleType.FOOD),
                Article.of("Monopoly", Money.of(7, EURO), ArticleType.GAME),
                Article.of("Uno", Money.of(1, EURO), ArticleType.GAME),
                Article.of("Mensch ärger' dich nicht", Money.of(666, EURO), ArticleType.GAME),
                Article.of("Pulp Fiction", Money.of(1337, EURO), ArticleType.MOVIE),
                Article.of("Marvel's Endgame", Money.of(3000, EURO), ArticleType.MOVIE),
                Article.of("Frozen 2", Money.of(2, EURO), ArticleType.MOVIE)).forEach(catalog::save);
    }
}
