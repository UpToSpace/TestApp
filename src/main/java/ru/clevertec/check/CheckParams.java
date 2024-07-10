package ru.clevertec.check;

import java.util.HashMap;

public class CheckParams {
    private HashMap<String, Integer> products;
    private String discountCard;
    private String balanceDebitCard;

    public CheckParams() {
        products = new HashMap<>();
        discountCard = "";
        balanceDebitCard = "";
    }

    public HashMap<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<String, Integer> products) {
        this.products = products;
    }

    public String getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(String discountCard) {
        this.discountCard = discountCard;
    }

    public String getBalanceDebitCard() {
        return balanceDebitCard;
    }

    public void setBalanceDebitCard(String balanceDebitCard) {
        this.balanceDebitCard = balanceDebitCard;
    }
}
