package hotel.roomservice;

import java.util.EnumMap;
import java.util.Optional;
import java.util.stream.Collectors;

import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import hotel.booking.Booking;
import hotel.booking.BookingRepository;
import hotel.member.MemberRepository;
import hotel.member.guest.Guest;
import hotel.roomservice.Article.ArticleType;
import hotel.roomservice.Article.SortOption;

@PreAuthorize("hasRole('SERVICE')")
@Controller
@SessionAttributes("cart")
class RoomserviceController {

    public static final String CATALOG_ROUTE = "redirect:/roomservice";
    public static final String CART_ROUTE = "redirect:/roomservice/cart";

    private final ArticleCatalog catalog;
    private final UniqueInventory<UniqueInventoryItem> inventory;
    private final OrderManagement<Order> management;
    private final MemberRepository members;
    private final BookingRepository bookings;

    private ArticleType type;

    private EnumMap<SortOption, Boolean> sortOptionMap;
    private SortOption sortOption;

    RoomserviceController(ArticleCatalog catalog, UniqueInventory<UniqueInventoryItem> inventory,
            OrderManagement<Order> management,
            MemberRepository members, BookingRepository bookings) {

        Assert.notNull(management, "OrderManagement must not be null!");

        this.catalog = catalog;
        this.inventory = inventory;
        this.management = management;
        this.members = members;
        this.bookings = bookings;

        sortOptionMap = new EnumMap<>(SortOption.class);
    }

    @GetMapping("/roomservice")
    public String articles(Model model) {

        var articles = catalog.findAll().toList();

        if (type != null) {
            articles = catalog.findByType(type).toList();
        }

        if (sortOption != null) {

            var comparator = sortOption.getComparator();
            if (sortOptionMap.getOrDefault(sortOption, false).booleanValue()) {
                comparator = comparator.reversed();
            }

            articles = articles.stream().sorted(comparator).collect(Collectors.toList());
        }

        model.addAttribute("catalog", articles);

        return "guest/articles";
    }

    @GetMapping("/roomservice/{stringType}")
    public String articles(@PathVariable String stringType, Model model) {

        try {
            type = ArticleType.valueOf(stringType.toUpperCase());
        } catch (Exception e) {
            type = null;
        }

        return CATALOG_ROUTE;
    }

    @GetMapping("/roomservice/sort/{stringSort}")
    public String sort(@PathVariable String stringSort, Model model) {

        try {
            sortOption = SortOption.valueOf(stringSort.toUpperCase());

            if (sortOptionMap.containsKey(sortOption)) {
                sortOptionMap.put(sortOption, !sortOptionMap.get(sortOption));
            } else {
                sortOptionMap.put(sortOption, false);
            }
        } catch (Exception ex) {
            sortOption = null;
        }

        return CATALOG_ROUTE;
    }

    @ModelAttribute("cart")
    public Cart initializeCart() {
        return new Cart();
    }

    @PostMapping("/roomservice/addArticle")
    public String addArticle(@RequestParam("pid") Article article, @RequestParam("number") int number,
            @ModelAttribute Cart cart) {

        Quantity quantity = Quantity.of(number);

        var item = inventory.findByProduct(article);

        if (item.isPresent()) {
            inventory.save(item.get().increaseQuantity(quantity));
        }

        cart.addOrUpdateItem(article, Quantity.of(number));

        return CART_ROUTE;
    }

    @PostMapping("/roomservice/removeArticle")
    public String removeArticle(@RequestParam("id") String item, @ModelAttribute Cart cart,
            @LoggedIn Optional<UserAccount> userAccount) {

        return userAccount.map(account -> {

            cart.removeItem(item);

            return CART_ROUTE;

        }).orElse(CATALOG_ROUTE);
    }

    @PostMapping("/roomservice/removeAllArticle")
    public String removeAllArticle(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount) {

        return userAccount.map(account -> {

            cart.clear();

            return CART_ROUTE;

        }).orElse(CATALOG_ROUTE);
    }

    @GetMapping("/roomservice/cart")
    public String cart(@ModelAttribute Cart cart, Model model) {
        return "guest/roomserviceCart";
    }

    @PostMapping("/roomservice/checkout")
    public String checkout(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount) {

        if (userAccount.isPresent()) {

            Guest guest = (Guest) members.findByUserAccount(userAccount.get());

            for (Booking booking : bookings.findAllByGuest(guest)) {
                if (booking.isCheckedIn()) {

                    var order = new Order(guest.getUserAccount(), Cash.CASH);

                    cart.addItemsTo(order);

                    management.payOrder(order);
                    management.completeOrder(order);

                    booking.addOrder(order);
                    bookings.save(booking);

                    cart.clear();
                }
            }
        }

        return CATALOG_ROUTE;
    }
}
