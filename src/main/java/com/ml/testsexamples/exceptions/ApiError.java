package com.ml.testsexamples.exceptions;

public record ApiError(String path, String message, int statusCode) {
}
