package by.smolenok.currencyexchange.dto.response;

import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.exeptions.UniqueDataException;
import by.smolenok.currencyexchange.exeptions.validation.ValidationException;
import jakarta.servlet.http.HttpServletResponse;

public class ErrorResponseFactory {
    public ErrorResponse from( Exception e){
        return switch (e){
            case ValidationException ex -> new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            case ModelNotFoundException ex -> new ErrorResponse(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
            case UniqueDataException ex -> new ErrorResponse(HttpServletResponse.SC_CONFLICT, ex.getMessage());
            case DataAccessException ex -> new ErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorType.DATABASE_ERROR.getMessage());
            default -> new ErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorType.UNEXPECTED_ERROR.getMessage());
        };
    }
}
