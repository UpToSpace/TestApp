package ru.clevertec.check;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Check {

    private static final long CENT_FACTOR = 100L;
    private static final Locale LOCALE = Locale.US;

    private long totalDiscount = 0L;
    private long totalPrice = 0L;

    public List<Product> readPurchasedProductsFromFile(String fileName, CheckParams checkParams) throws CustomException {
        List<Product> products = new ArrayList<>();
        Set<String> processedIds = new HashSet<>();
        try (CustomCSVReader reader = new CustomCSVReader(new FileReader(fileName), ";")) {
            List<String[]> lines = reader.readAll();
            for (int i = 1; i < lines.size(); i++) {
                String[] line = lines.get(i);
                Product product = new Product(line[0], line[1], Double.parseDouble(line[2]), Integer.parseInt(line[3]), Boolean.parseBoolean(line[4]));
                if (checkParams.getProducts().containsKey(product.getId())) {
                    int quantityToBuy = checkParams.getProducts().get(product.getId());
                    if (quantityToBuy > product.getQuantityInStock()) {
                        throw new CustomException(ExceptionMessages.BAD_REQUEST);
                    }
                    product.setQuantityToBuy(quantityToBuy);
                    products.add(product);
                    processedIds.add(product.getId());
                }
            }

            for (String id : checkParams.getProducts().keySet()) {
                if (!processedIds.contains(id)) {
                    throw new CustomException(ExceptionMessages.BAD_REQUEST);
                }
            }
        } catch (IOException e) {
            throw new CustomException(ExceptionMessages.INTERNAL_SERVER_ERROR);
        }
        return products;
    }

    public int getDiscountPercentage(String fileName, String discountCardParam) throws CustomException {
        try (CustomCSVReader reader = new CustomCSVReader(new FileReader(fileName), ";")) {
            List<String[]> lines = reader.readAll();
            for (int i = 1; i < lines.size(); i++) {
                String[] line = lines.get(i);
                DiscountCard discountCard = new DiscountCard(line[0], line[1], Integer.parseInt(line[2]));
                if (Objects.equals(discountCard.getNumber(), discountCardParam)) {
                    return discountCard.getDiscountAmount();
                }
            }
        } catch (IOException e) {
            throw new CustomException(ExceptionMessages.INTERNAL_SERVER_ERROR);
        }
        return 0;
    }

    private String formatMoney(long moneyInCents) {
        return String.format(LOCALE, "%.2f$", moneyInCents / 100.0);
    }

    public void checkEnoughMoney(List<Product> products, String balanceDebitCard, int discountPercentage) throws CustomException {
        for (Product product : products) {
            long productTotalCents = (long) (product.getPrice() * CENT_FACTOR * product.getQuantityToBuy());
            long productDiscountCents = calculateDiscount(product, discountPercentage, productTotalCents);

            totalPrice += productTotalCents;
            totalDiscount += productDiscountCents;
        }
        long balanceInCents = (long) (Double.parseDouble(balanceDebitCard) * CENT_FACTOR);
        if (balanceInCents < totalPrice - totalDiscount) {
            throw new CustomException(ExceptionMessages.NOT_ENOUGH_MONEY);
        }
    }

    public void writeProductsToFile(String fileName, List<Product> products, int discountPercentage, String discountCard) throws CustomException {
        try (CustomCSVWriter writer = new CustomCSVWriter(new FileWriter(fileName), ";")) {
            writeHeader(writer);
            writeProducts(writer, products, discountPercentage);
            writeSummary(writer, discountPercentage, discountCard);
        } catch (IOException e) {
            throw new CustomException(ExceptionMessages.INTERNAL_SERVER_ERROR);
        }
    }

    private void writeHeader(CustomCSVWriter writer) throws IOException {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        List<String[]> header = Arrays.asList(
                new String[]{"Date", "Time"},
                new String[]{dateTime.format(dateFormatter), dateTime.format(timeFormatter)},
                new String[]{},
                new String[]{"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"}
        );

        writer.writeAll(header);
    }

    private void writeProducts(CustomCSVWriter writer, List<Product> products, int discountPercentage) throws IOException {
        for (Product product : products) {
            long productTotalCents = (long) (product.getPrice() * CENT_FACTOR * product.getQuantityToBuy());
            long productDiscountCents = calculateDiscount(product, discountPercentage, productTotalCents);

            writer.writeNext(new String[]{
                    String.valueOf(product.getQuantityToBuy()),
                    product.getDescription(),
                    String.format(LOCALE, "%.2f$", product.getPrice()),
                    formatMoney(productDiscountCents),
                    formatMoney(productTotalCents)
            });
        }
        writer.writeNext(new String[]{});
    }

    private long calculateDiscount(Product product, int discountPercentage, long productTotalCents) {
        if (product.isWholesaleProduct() && product.getQuantityToBuy() >= 5) {
            return (long) (productTotalCents * product.getWHOLESALE_DISCOUNT() / 100);
        } else {
            return productTotalCents * discountPercentage / 100;
        }
    }

    private void writeSummary(CustomCSVWriter writer, int discountPercentage, String discountCard) throws IOException {
        if (!Objects.equals(discountCard, "")) {
            List<String[]> discountData = Arrays.asList(
                    new String[]{"DISCOUNT CARD", "DISCOUNT PERCENTAGE"},
                    new String[]{discountCard, discountPercentage + "%"},
                    new String[]{}
            );
            writer.writeAll(discountData);
        }

        List<String[]> summary = Arrays.asList(
                new String[]{"TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"},
                new String[]{formatMoney(totalPrice), formatMoney(totalDiscount), formatMoney(totalPrice - totalDiscount)}
        );

        writer.writeAll(summary);
    }
}