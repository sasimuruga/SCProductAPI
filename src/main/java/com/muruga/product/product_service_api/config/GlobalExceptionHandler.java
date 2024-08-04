package com.muruga.product.product_service_api.config;

import com.muruga.product.product_service_api.exception.ProductAlreadyExistsException;
import com.muruga.product.product_service_api.exception.ProductNotFoundException;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ProblemDetail handleUniqueProductException(ProductAlreadyExistsException ex, WebRequest request){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("http://example.com/errors/product-exists"));
        problemDetail.setTitle("Product already exists");
        return problemDetail;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFoundException(ProductNotFoundException ex, WebRequest request){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("http://example.com/errors/product-not-found"));
        problemDetail.setTitle("Product not found");
        return problemDetail;
    }

}
