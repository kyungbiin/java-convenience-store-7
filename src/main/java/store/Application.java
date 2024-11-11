package store;

import java.util.HashMap;
import java.util.Map;

class Product {
    private String name;
    private int price;
    private int stock;
    private String promotion;

    public Product(String name, int price, int stock, String promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void reduceStock(int quantity) {
        this.stock -= quantity;
    }

    public String getPromotion() {
        return promotion;
    }

    @Override
    public String toString() {
        return String.format("- %s %d원 %d개 %s", name, price, stock, promotion == null ? "" : promotion);
    }
}

public class Application {
    private static Map<String, Product> productCatalog = new HashMap<>();

    public static void main(String[] args) {
        initializeCatalog();

        // Display products (for example purposes, showing without user interaction)
        for (Product product : productCatalog.values()) {
            System.out.println(product);
        }

        // Sample purchase
        try {
            purchaseProduct("비타민워터", 3);
            purchaseProduct("물", 2);
            purchaseProduct("정식도시락", 2);
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void initializeCatalog() {
        productCatalog.put("콜라", new Product("콜라", 1000, 10, "탄산2+1"));
        productCatalog.put("사이다", new Product("사이다", 1000, 8, "탄산2+1"));
        productCatalog.put("오렌지주스", new Product("오렌지주스", 1800, 9, "MD추천상품"));
        productCatalog.put("탄산수", new Product("탄산수", 1200, 5, "탄산2+1"));
        productCatalog.put("물", new Product("물", 500, 10, null));
        productCatalog.put("비타민워터", new Product("비타민워터", 1500, 6, null));
        productCatalog.put("감자칩", new Product("감자칩", 1500, 5, "반짝할인"));
        productCatalog.put("초코바", new Product("초코바", 1200, 5, "MD추천상품"));
        productCatalog.put("에너지바", new Product("에너지바", 2000, 5, null));
        productCatalog.put("정식도시락", new Product("정식도시락", 6400, 8, null));
        productCatalog.put("컵라면", new Product("컵라면", 1700, 10, "MD추천상품"));
    }

    private static void purchaseProduct(String productName, int quantity) throws Exception {
        Product product = productCatalog.get(productName);
        if (product == null) {
            throw new Exception("상품을 찾을 수 없습니다.");
        }
        if (product.getStock() < quantity) {
            throw new Exception("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
        product.reduceStock(quantity);
        System.out.printf("내실돈 %d원%n", product.getPrice() * quantity);
    }
}
