package ru.clevertec.check;

public class Product {
    private String id;
    private String description;
    private double price;
    private int quantityInStock;
    private boolean wholesaleProduct;
    private int quantityToBuy;
    private final int WHOLESALE_DISCOUNT = 10;

    public Product() {}

    public Product(String id, String description, double price, int quantityInStock, boolean wholesaleProduct) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.wholesaleProduct = wholesaleProduct;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public boolean isWholesaleProduct() {
        return wholesaleProduct;
    }

    public void setWholesaleProduct(boolean wholesaleProduct) {
        this.wholesaleProduct = wholesaleProduct;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantityInStock=" + quantityInStock +
                ", wholesaleProduct=" + wholesaleProduct +
                '}';
    }

    public int getQuantityToBuy() {
        return quantityToBuy;
    }

    public void setQuantityToBuy(int quantityToBuy) {
        this.quantityToBuy = quantityToBuy;
    }

    public double getWHOLESALE_DISCOUNT() {
        return WHOLESALE_DISCOUNT;
    }
}
