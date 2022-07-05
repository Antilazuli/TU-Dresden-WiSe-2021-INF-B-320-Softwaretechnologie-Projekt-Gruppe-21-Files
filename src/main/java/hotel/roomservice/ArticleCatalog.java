package hotel.roomservice;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.util.Streamable;

import hotel.roomservice.Article.ArticleType;

public interface ArticleCatalog extends Catalog<Article> {

    public Streamable<Article> findByType(ArticleType type);
}
