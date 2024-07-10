package ru.clevertec.check;

public class DiscountCard {
    private String id;
    private String number;
    private int discountAmount;

    public DiscountCard(String id, String number, int discountAmount) {
        this.id = id;
        this.number = number;
        this.discountAmount = discountAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public String getNumber() {
        return number;
    }
}
