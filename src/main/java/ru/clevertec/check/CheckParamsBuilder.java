package ru.clevertec.check;

import java.util.HashMap;

public class CheckParamsBuilder implements ICheckParamsBuilder {

    private CheckParams checkParams;

    public CheckParamsBuilder() {
        checkParams = new CheckParams();
    }

    public CheckParams getCheckParams() {
        return checkParams;
    }

    @Override
    public ICheckParamsBuilder setProducts(HashMap<String, Integer> products) {
        checkParams.setProducts(products);
        return this;
    }

    @Override
    public ICheckParamsBuilder setDiscountCard(String discountCard) {
        checkParams.setDiscountCard(discountCard);
        return this;
    }

    @Override
    public ICheckParamsBuilder setBalanceDebitCard(String balanceDebitCard) {
        checkParams.setBalanceDebitCard(balanceDebitCard);
        return this;
    }

    @Override
    public CheckParams build() {
        return checkParams;
    }
}
