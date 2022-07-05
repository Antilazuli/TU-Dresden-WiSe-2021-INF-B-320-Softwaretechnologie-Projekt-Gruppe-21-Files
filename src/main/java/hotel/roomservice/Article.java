package hotel.roomservice;

import java.util.Comparator;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.salespointframework.catalog.Product;

@Entity
public class Article extends Product {

	static class SortByType implements Comparator<Article> {

		@Override
		public int compare(Article o1, Article o2) {
			return o1.getType().compareTo(o2.getType());
		}
	}

	static class SortByName implements Comparator<Article> {

		@Override
		public int compare(Article o1, Article o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}

	static class SortByPrice implements Comparator<Article> {

		@Override
		public int compare(Article o1, Article o2) {
			return o1.getPrice().compareTo(o2.getPrice());
		}
	}

	public enum SortOption {
		TYPE {
			@Override
			public Comparator<Article> getComparator() {
				return new SortByType();
			}
		},
		NAME {
			@Override
			public Comparator<Article> getComparator() {
				return new SortByName();
			}
		},
		PRICE {
			@Override
			public Comparator<Article> getComparator() {
				return new SortByPrice().reversed();
			}
		};

		public abstract Comparator<Article> getComparator();
	}

	public enum ArticleType {
		FOOD {
			@Override
			public String toString() {
				return "Essen";
			}
		},
		DRINK {
			@Override
			public String toString() {
				return "Getränk";
			}
		},
		MOVIE {
			@Override
			public String toString() {
				return "Film";
			}
		},
		GAME {
			@Override
			public String toString() {
				return "Spiel";
			}
		};
	}

	private ArticleType type;

	@SuppressWarnings({ "unused", "deprecation" })
	private Article() {
	}

	public Article(String name, MonetaryAmount price, ArticleType type) {

		super(name, price);

		this.type = type;
	}

	public static Article of(String name, MonetaryAmount price, ArticleType type) {
		return new Article(name, price, type);
	}

	public ArticleType getType() {
		return type;
	}
}
