package com.example.ArtistBackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * GlobalExceptionHandler — catches all unhandled exceptions across the app.
 *
 * Without this, Spring shows its default white-label error page with a raw stack trace.
 * This handler gives the user a clean, meaningful message and logs the error for debugging.
 *
 * WARNING #6 fix.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all generic runtime exceptions (e.g. "Artwork not found", null pointer, etc.)
     * Returns a user-friendly error page.
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException ex,
                                         Model model,
                                         HttpServletRequest request) {
        System.err.println("[ERROR] RuntimeException on " + request.getRequestURI() + ": " + ex.getMessage());
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorTitle", "Something went wrong");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    /**
     * Handles 403 Access Denied — e.g. a USER trying to hit /admin endpoints.
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException ex,
                                     Model model,
                                     HttpServletRequest request) {
        System.err.println("[WARN] Access denied on " + request.getRequestURI());
        model.addAttribute("errorCode", "403");
        model.addAttribute("errorTitle", "Access Denied");
        model.addAttribute("errorMessage", "You don't have permission to access this page.");
        return "error";
    }

    /**
     * Handles file upload size exceeded (configured in application.properties as 10MB).
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public String handleFileTooLarge(MaxUploadSizeExceededException ex,
                                     Model model,
                                     HttpServletRequest request) {
        System.err.println("[WARN] File too large on " + request.getRequestURI());
        model.addAttribute("errorCode", "413");
        model.addAttribute("errorTitle", "File Too Large");
        model.addAttribute("errorMessage", "Uploaded file exceeds the maximum allowed size of 10MB.");
        return "error";
    }

    /**
     * Handles IllegalArgumentException — e.g. invalid input data.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException ex,
                                        Model model,
                                        HttpServletRequest request) {
        System.err.println("[WARN] IllegalArgument on " + request.getRequestURI() + ": " + ex.getMessage());
        model.addAttribute("errorCode", "400");
        model.addAttribute("errorTitle", "Bad Request");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
