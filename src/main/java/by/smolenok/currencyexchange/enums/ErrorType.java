package by.smolenok.currencyexchange.enums;

import lombok.Getter;

@Getter
public enum ErrorType {
    CURRENCY_NOT_FOUND_TEMPLATE("Currency with this code '%s' not found"),
    CURRENCY_CODE_REQUIRED("Currency code is required"),
    EXCHANGE_RATES_CODE_REQUIRED("Exchange rate code required"),
    EXCHANGE_RATES_NOT_FOUND_TEMPLATE("Exchange rate not found for the pair '%s' - '%s'"),
    CURRENCY_CODE_EXISTS_TEMPLATE("Currency with this code '%s' already exists"),
    EXCHANGE_RATES_EXISTS_TEMPLATE(" Exchange rates with this code '%s' - '%s' already exists"),
    VALIDATION_FAILED("Validation error"),
    SERVICE_UNAVAILABLE("Service temporarily unavailable"),
    INVALID_CURRENCY_CODE("Invalid currency code format, required format is three English letters"),
    INVALID_EXCHANGE_RATES_CODE("Invalid exchange rates code format, required format is six English letters"),
    INVALID_EXCHANGE_RATES_SIZE("Exchange rates code must be exactly 6 characters"),
    INVALID_CURRENCY_SIZE("Currency code must be exactly 3 characters"),
    INVALID_CREATE_CURRENCY_NO_ID("Creating currency failed, no ID obtained."),
    UNIQUE_FAILED("UNIQUE constraint failed"),
    SAVE_ERROR_CURRENCY("Error saving currency to database"),
    ERROR_RETRIEVING_CURRENCIES("Error retrieving currencies from the database"),
    ERROR_RETRIEVING_EXCHANGE_RATES("Error retrieving exchange rates from the database"),
    NO_EXCHANGE_RATES("There are no exchange rates"),
    NO_CURRENCY("There are no currency"),
    NO_CURRENCY_PAIR("Currency pair codes are missing in the address");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

}
