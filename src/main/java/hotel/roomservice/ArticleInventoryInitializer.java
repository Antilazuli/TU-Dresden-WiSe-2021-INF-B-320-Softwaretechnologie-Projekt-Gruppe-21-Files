package hotel.roomservice;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(30)
class ArticleInventoryInitializer implements DataInitializer {

    private final UniqueInventory<UniqueInventoryItem> inventory;
    private final ArticleCatalog catalog;

    ArticleInventoryInitializer(UniqueInventory<UniqueInventoryItem> inventory, ArticleCatalog catalog) {

        Assert.notNull(inventory, "ArticleInventory must not be null!");
        Assert.notNull(catalog, "ArticleCatalog must not be null!");

        this.inventory = inventory;
        this.catalog = catalog;
    }

    @Override
    public void initialize() {
        catalog.findAll().forEach(article -> inventory.save(new UniqueInventoryItem(article, Quantity.of(1))));
    }
}
