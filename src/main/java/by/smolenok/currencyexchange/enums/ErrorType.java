package by.smolenok.currencyexchange.enums;

import lombok.Getter;

@Getter
public enum ErrorType {
    CURRENCY_NOT_FOUND("Currency not found"),
    EXCHANGE_RATES_NOT_FOUND("Exchange rate not found for the pair"),
    CURRENCY_CODE_EXISTS_TEMPLATE("Currency with this code '%s' already exists"),
    VALIDATION_FAILED("Validation error"),
    SERVICE_UNAVAILABLE("Service temporarily unavailable"),
    INVALID_CURRENCY_CODE("Invalid currency code format, required format is three English letters"),
    INVALID_CREATE_CURRENCY_NO_ID("Creating currency failed, no ID obtained."),
    UNIQUE_FAILED("UNIQUE constraint failed"),
    SAVE_ERROR_CURRENCY("Error saving currency to database"),
    ERROR_RETRIEVING_CURRENCIES("Error retrieving currencies from the database"),
    ERROR_RETRIEVING_EXCHANGE_RATES("Error retrieving exchange rates from the database"),
    NO_EXCHANGE_RATES("There are no exchange rates"),
    NO_CURRENCY_PAIR("Currency pair codes are missing in the address");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

}
