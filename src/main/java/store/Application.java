package store;

import java.util.*;

class Product {
    String name;
    int price;
    int stock;
    String promotionType;

    public Product(String name, int price, int stock, String promotionType) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotionType = promotionType;
    }

    // 기타 getter 및 setter 메서드
}

class Store {
    List<Product> products = new ArrayList<>();
    final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    final int MEMBERSHIP_DISCOUNT_LIMIT = 8000;

    public Store() {
        loadProducts();
    }

    // 상품과 프로모션 목록 불러오기
    private void loadProducts() {
        // 예시 상품 추가 (파일 입출력으로 대체 가능)
        products.add(new Product("콜라", 1000, 10, "2+1"));
        products.add(new Product("사이다", 1000, 8, "2+1"));
        products.add(new Product("오렌지주스", 1800, 0, "추천"));
    }

    public void printWelcomeMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        for (Product product : products) {
            System.out.printf("- %s %d원 %d개 %s%n",
                    product.name, product.price, product.stock,
                    product.promotionType != null ? product.promotionType : "");
        }
    }

    public void processPurchase() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printWelcomeMessage();
            System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
            String input = scanner.nextLine();

            List<Product> purchasedProducts = parsePurchaseInput(input);

            // 예외 상황 처리 예시
            if (purchasedProducts.isEmpty()) {
                System.out.println("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
                continue;
            }

            int totalAmount = calculateTotalAmount(purchasedProducts);
            int discount = applyPromotions(purchasedProducts);
            int membershipDiscount = applyMembershipDiscount(totalAmount - discount);
            int finalAmount = totalAmount - discount - membershipDiscount;

            printReceipt(purchasedProducts, discount, membershipDiscount, finalAmount);

            System.out.println("구매하고 싶은 다른 상품이 있나요? (Y/N)");
            if (!scanner.nextLine().equalsIgnoreCase("Y")) {
                break;
            }
        }

        scanner.close();
    }

    private List<Product> parsePurchaseInput(String input) {
        List<Product> purchasedProducts = new ArrayList<>();
        String[] items = input.split(",");

        for (String item : items) {
            String[] parts = item.replaceAll("[\\[\\]]", "").split("-");
            if (parts.length != 2) continue;

            String name = parts[0];
            int quantity = Integer.parseInt(parts[1]);

            Product product = findProductByName(name);
            if (product != null && product.stock >= quantity) {
                product.stock -= quantity;
                purchasedProducts.add(new Product(name, product.price, quantity, product.promotionType));
            } else {
                System.out.println("[ERROR] 존재하지 않는 상품이거나 재고가 부족합니다. 다시 입력해 주세요.");
            }
        }

        return purchasedProducts;
    }

    private Product findProductByName(String name) {
        return products.stream()
                .filter(product -> product.name.equals(name))
                .findFirst().orElse(null);
    }

    private int calculateTotalAmount(List<Product> purchasedProducts) {
        int total = 0;
        for (Product product : purchasedProducts) {
            total += product.price * product.stock;
        }
        return total;
    }

    private int applyPromotions(List<Product> purchasedProducts) {
        int discount = 0;

        for (Product product : purchasedProducts) {
            if (product.promotionType != null && product.promotionType.equals("2+1")) {
                int freeItems = product.stock / 2;
                discount += freeItems * product.price;
            }
        }

        return discount;
    }

    private int applyMembershipDiscount(int amount) {
        int discount = (int) (amount * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discount, MEMBERSHIP_DISCOUNT_LIMIT);
    }

    private void printReceipt(List<Product> purchasedProducts, int promotionDiscount, int membershipDiscount, int finalAmount) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");
        for (Product product : purchasedProducts) {
            System.out.printf("%s\t\t%d\t%d%n", product.name, product.stock, product.price * product.stock);
        }
        System.out.println("=============증정품===============");
        for (Product product : purchasedProducts) {
            if (product.promotionType != null && product.promotionType.equals("2+1")) {
                int freeItems = product.stock / 2;
                System.out.printf("%s\t\t%d%n", product.name, freeItems);
            }
        }
        System.out.println("====================================");
        System.out.printf("총구매액\t\t%d%n", calculateTotalAmount(purchasedProducts));
        System.out.printf("행사할인\t\t-%d%n", promotionDiscount);
        System.out.printf("멤버십할인\t\t-%d%n", membershipDiscount);
        System.out.printf("내실돈\t\t %d%n", finalAmount);
    }
}

public class Application {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Store store = new Store();
        store.processPurchase();

        // 입력이 있을 때마다 반복
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            // 입력값이 숫자만 포함하는지 확인
            if (isNumeric(input)) {
                System.out.println("입력한 숫자는: " + input);
                // 추가적인 처리 로직 추가
            } else {
                System.out.println("잘못된 입력입니다. 숫자만 입력해주세요.");
            }
        }

        scanner.close();
    }

    // 숫자만 포함하는지 확인하는 메서드
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str); // 숫자로 변환 시도
            return true; // 변환이 성공하면 숫자
        } catch (NumberFormatException e) {
            return false; // 변환이 실패하면 숫자가 아님
        }
    }
    }


