package ru.clevertec.check;

import java.util.HashMap;

public interface ICheckParamsBuilder {
    ICheckParamsBuilder setProducts(HashMap<String, Integer> products);
    ICheckParamsBuilder setDiscountCard(String discountCard);
    ICheckParamsBuilder setBalanceDebitCard(String balanceDebitCard);
    CheckParams build();
}
