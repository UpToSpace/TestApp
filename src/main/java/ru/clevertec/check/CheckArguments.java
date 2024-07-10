package ru.clevertec.check;

import java.util.HashMap;

public class CheckArguments {
    private CheckParamsBuilder checkParamsBuilder;

    public CheckArguments() {
        checkParamsBuilder = new CheckParamsBuilder();
    }

    private String getKey(String arg) {
        if (arg.contains("=")) {
            return arg.substring(0, arg.indexOf('='));
        }
        return "";
    }

    private String getValue(String arg) {
        if (arg.contains("=")) {
            return arg.substring(arg.indexOf('=') + 1);
        }
        return "";
    }

    private boolean checkCheckParams(String[] args) throws CustomException {
        if (args.length < 4) {
            return false;
        }

        boolean hasDebitCard = false;
        boolean hasOneProduct = false;

        for (String arg: args) {
            if (arg.matches("\\d+-\\d+")) {
                hasOneProduct = true;
            } else {
                String key = getKey(arg);
                String value = getValue(arg);

                switch (key) {
                    case "balanceDebitCard":
                        if (!value.matches("\\d+(\\.\\d{2})?")) {
                            return false;
                        }
                        hasDebitCard = true;
                        break;
                    case "discountCard":
                        break;
                    default:
                        throw new CustomException(ExceptionMessages.BAD_REQUEST);
                }
            }
        }
        return hasOneProduct && hasDebitCard;
    }

    public CheckParams extractCheckParams(String[] args) throws CustomException {
        if (!checkCheckParams(args)) {
            throw new CustomException(ExceptionMessages.BAD_REQUEST);
        }
        for (String arg : args) {
            if (arg.matches("\\d+-\\d+")) {
                String id = arg.substring(0, arg.indexOf('-'));
                int quantity = Integer.parseInt(arg.substring(arg.indexOf('-') + 1));
                HashMap<String, Integer> products = checkParamsBuilder.getCheckParams().getProducts();
                if (products.containsKey(id)) {
                    products.replace(id, products.get(id) + quantity);
                } else {
                    products.put(id, quantity);
                }
            } else {
                String key = getKey(arg);
                String value = getValue(arg);

                switch (key) {
                    case "discountCard":
                        checkParamsBuilder.setDiscountCard(value);
                        break;
                    case "balanceDebitCard":
                        if (Double.parseDouble(value) <= 0) {
                            throw new CustomException(ExceptionMessages.NOT_ENOUGH_MONEY);
                        }
                        checkParamsBuilder.setBalanceDebitCard(value);
                        break;
                    default:
                        break;
                }
            }
        }
        return checkParamsBuilder.build();
    }
}
