package hotel.roomservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import java.util.stream.Collectors;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.core.Currencies;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import hotel.AbstractIntegrationTests;
import hotel.member.MemberRepository;
import hotel.member.guest.Guest;
import hotel.roomservice.Article.ArticleType;

public class RoomserviceControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    RoomserviceController controller;

    @Autowired
    ArticleCatalog catalog;

    @Autowired
    UniqueInventory<UniqueInventoryItem> inventory;

    @Autowired
    MemberRepository members;

    @Test
    @WithMockUser(roles = "SERVICE")
    void articles() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.articles(model);

        assertEquals("guest/articles", returnedView);

        Object catalogObject = model.asMap().get("catalog");
        assertEquals(catalog.findAll().toList(), catalogObject);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void articlesTypeAll() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.articles("all", model);

        assertEquals("redirect:/roomservice", returnedView);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void articlesTypeGame() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.articles("game", model);

        assertEquals("redirect:/roomservice", returnedView);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void articlesGame() {

        Model model = new ExtendedModelMap();

        controller.articles("game", model);
        controller.sort("default", model); // wird benötigt damit Test 'articlesSortPrice' diesen nicht verfälscht
        String returnedView = controller.articles(model);

        assertEquals("guest/articles", returnedView);

        Object catalogObject = model.asMap().get("catalog");
        assertEquals(catalog.findByType(ArticleType.GAME).toList(), catalogObject);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void sort() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.sort("", model);

        assertEquals("redirect:/roomservice", returnedView);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void sortPrice() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.sort("price", model);

        assertEquals("redirect:/roomservice", returnedView);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void articlesSortPrice() {

        Model model = new ExtendedModelMap();

        // einmal sortiert
        controller.sort("price", model);
        String returnedView = controller.articles(model);

        assertEquals("guest/articles", returnedView);

        Object catalogObject = model.asMap().get("catalog");
        assertEquals(
                catalog.findAll().stream().sorted(new Article.SortByPrice().reversed()).collect(Collectors.toList()),
                catalogObject);

        // Sortierung umkehren
        controller.sort("price", model);
        returnedView = controller.articles(model);

        assertEquals("guest/articles", returnedView);

        catalogObject = model.asMap().get("catalog");
        assertEquals(catalog.findAll().stream().sorted(new Article.SortByPrice()).collect(Collectors.toList()),
                catalogObject);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void CartInit() {

        Cart returnedView = controller.initializeCart();

        assertNotNull(returnedView);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void Cart() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.cart(mock(Cart.class), model);

        assertEquals("guest/roomserviceCart", returnedView);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void addArticle() {

        Article article = catalog.findByName("Wasser").toList().get(0);

        String returnedView = controller.addArticle(article, 2,
                controller.initializeCart());

        assertEquals("redirect:/roomservice/cart", returnedView);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void addArticleFalse() {

        Article article = new Article("test", Money.of(5, Currencies.EURO), ArticleType.MOVIE);

        String returnedView = controller.addArticle(article, 2,
                controller.initializeCart());

        assertEquals("redirect:/roomservice/cart", returnedView);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void removeArticle() {

        Article article = catalog.findByName("Wasser").toList().get(0);

        UserAccount userAccount = mock(UserAccount.class);

        Cart cart = controller.initializeCart();
        controller.addArticle(article, 2, cart);

        var item = inventory.findByProduct(article);

        String returnedView = controller.removeArticle(item.get().getId().toString(), cart, Optional.of(userAccount));

        assertEquals("redirect:/roomservice/cart", returnedView);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void removeArticleError() {

        Article article = catalog.findByName("Wasser").toList().get(0);

        Cart cart = controller.initializeCart();
        controller.addArticle(article, 2, cart);

        var item = inventory.findByProduct(article);

        String returnedView = controller.removeArticle(item.get().getId().toString(), cart, Optional.empty());

        assertEquals("redirect:/roomservice", returnedView);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void removeAllArticle() {

        Article article = catalog.findByName("Wasser").toList().get(0);

        UserAccount userAccount = mock(UserAccount.class);

        Cart cart = controller.initializeCart();
        controller.addArticle(article, 2, cart);

        String returnedView = controller.removeAllArticle(cart, Optional.of(userAccount));

        assertEquals("redirect:/roomservice/cart", returnedView);
    }

    @Test
    @WithMockUser(roles = "SERVICE")
    void removeAllArticleError() {

        Article article = catalog.findByName("Wasser").toList().get(0);

        Cart cart = controller.initializeCart();
        controller.addArticle(article, 2, cart);

        String returnedView = controller.removeAllArticle(cart, Optional.empty());

        assertEquals("redirect:/roomservice", returnedView);
    }

    @Test
    @WithMockUser(username = "johannes@hotel.de", roles = { "SERVICE", "GUEST" })
    void checkout() {

        Cart cart = controller.initializeCart();

        String returnedView = controller.checkout(cart, Optional.empty());

        assertEquals("redirect:/roomservice", returnedView);
    }

    @Test
    @WithMockUser(username = "johannes@hotel.de", roles = { "SERVICE", "GUEST" })
    void checkoutCheckedInTrue() {

        Cart cart = controller.initializeCart();
        Article article = catalog.findByName("Wasser").toList().get(0);
        controller.addArticle(article, 2, cart);

        Guest guest = (Guest) members.findByEmail("johannes@hotel.de").get();

        String returnedView = controller.checkout(cart, Optional.of(guest.getUserAccount()));

        assertEquals("redirect:/roomservice", returnedView);
    }

    @Test
    @WithMockUser(username = "johannes@hotel.de", roles = { "SERVICE", "GUEST" })
    void checkoutCheckedInFalse() {

        Cart cart = controller.initializeCart();

        Guest guest = (Guest) members.findByEmail("mika@hotel.de").get();

        String returnedView = controller.checkout(cart, Optional.of(guest.getUserAccount()));

        assertEquals("redirect:/roomservice", returnedView);
    }

    @Test
    @WithMockUser(username = "johannes@hotel.de", roles = { "SERVICE", "GUEST" })
    void checkoutNoBookings() {

        Cart cart = controller.initializeCart();

        Guest guest = (Guest) members.findByEmail("eric@hotel.de").get();

        String returnedView = controller.checkout(cart, Optional.of(guest.getUserAccount()));

        assertEquals("redirect:/roomservice", returnedView);
    }
}
