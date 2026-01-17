package by.smolenok.currencyexchange.enums;

import lombok.Getter;

@Getter
public enum ErrorType {
    UNEXPECTED_ERROR("An unexpected error occurred"),
    INVALID_CURRENCY_SIGN_LENGTH_TEMPLATE("The currency sign must not be exactly 1 character (Your sign: '%s')"),

    CURRENCY_NOT_FOUND_TEMPLATE("Currency with this code '%s' not found"),
    CURRENCY_ALREADY_EXISTS_TEMPLATE("Currency with this code '%s' already exists"),
    INVALID_CURRENCY_CODE_TEMPLATE("Currency code must contain 3 uppercase Latin letters: '%s'"),
    INVALID_CURRENCY_CODE_LENGTH_TEMPLATE("The currency code must be exactly 3 characters long (Your code: '%s')"),
    INVALID_CURRENCY_NAME_LENGTH("Currency naming should not be longer than 35 characters"),
    SAME_CURRENCY_PAIRS_TEMPLATE("Currency pair codes must not be the same. (Your code: '%s' - '%s')"),
    CURRENCY_CODE_MISSING("Currency code is required"),
    CURRENCY_NAME_MISSING("Currency name is required"),
    INVALID_CREATE_CURRENCY_NO_ID("Creating currency failed, no ID obtained."),
    CURRENCY_SAVE_FAILED("Error saving currency to database"),
    CURRENCIES_RETRIEVAL_FAILED("Error retrieving currencies from the database"),
    NO_CURRENCY("There are no currency"),
    NO_CURRENCY_PAIR("Currency pair codes are missing in the address"),
    CURRENCY_CODE_DUPLICATE("A currency with this code already exists."),

    EXCHANGE_RATE_NOT_FOUND_TEMPLATE("Exchange rate not found for the pair '%s' - '%s'"),
    EXCHANGE_RATE_ALREADY_EXISTS("Exchange rates with this code '%s' - '%s' already exists"),
    EXCHANGE_RATE_PAIR_LENGTH_INVALID("Currency pair must be 6 Latin letters (e.g. USDRUB), got: '%s'"),
    INCORRECT_EXCHANGE_RATE_NO_ID("Creating exchange rate failed, no ID obtained."),
    EXCHANGE_RATE_SAVE_FAILED("Error saving exchange rates to database"),
    ERROR_RETRIEVING_EXCHANGE_RATES("Error retrieving exchange rates from the database"),
    NO_EXCHANGE_RATES("There are no exchange rates"),
    EXCHANGE_RATE_PAIR_MISSING("Exchange rate code pair required"),
    EXCHANGE_RATE_NOT_EMPTY("Exchange rate cannot be empty"),
    EXCHANGE_RATE_BELOW_ZERO("Exchange rate cannot be less than 0"),
    EXCHANGE_RATE_MUST_BE_NUMBER("Exchange rate must be a number"),

    PARAMETER_REQUIRED_TEMPLATE("Parameter '%s' is required"),


    DATABASE_ERROR("Service temporarily unavailable"),
    DUPLICATE_RECORD("UNIQUE constraint failed"),

    FIELD_REQUIRED_TEMPLATE("Field '%s' is required"),
    FIELD_MUST_BE_NUMBER_TEMPLATE("Field '%s' must be a number"),
    FIELD_MUST_BE_POSITIVE_TEMPLATE("Field '%s' must be greater than zero");
    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

}
