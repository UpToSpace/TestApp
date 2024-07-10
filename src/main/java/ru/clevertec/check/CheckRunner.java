package ru.clevertec.check;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CheckRunner {
    public static void main(String[] args) {
        String productsFileName = "./src/main/resources/products.csv";
        String discountCardsFileName = "./src/main/resources/discountCards.csv";
        String resultsFileName = "./src/result.csv";
        try {
            CheckParams checkParams = new CheckArguments().extractCheckParams(args);
            Check check = new Check();
            List<Product> products = check.readPurchasedProductsFromFile(productsFileName, checkParams);
            int discountPercentage = check.getDiscountPercentage(discountCardsFileName, checkParams.getDiscountCard());
            check.checkEnoughMoney(products, checkParams.getBalanceDebitCard(), discountPercentage);
            check.writeProductsToFile(resultsFileName, products, discountPercentage, checkParams.getDiscountCard());
        } catch (Exception e) {
            try (Writer writer = new FileWriter(resultsFileName)) {
                writer.write("ERROR\n");
                if (e instanceof CustomException) {
                    writer.write(((CustomException) e).getMessage() + "\n");
                } else {
                    writer.write(ExceptionMessages.INTERNAL_SERVER_ERROR + "\n");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}

