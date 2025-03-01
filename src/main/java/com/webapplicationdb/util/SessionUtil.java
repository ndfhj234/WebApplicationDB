package com.webapplicationdb.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {
    public static final String USER_SESSION_KEY = "user";
    public static final String USER_ROLE_SESSION_KEY = "userRole";
    public static final String ERROR_MESSAGE_KEY = "errorMessage";
    public static final String SUCCESS_MESSAGE_KEY = "successMessage";

    public static void setUser(HttpServletRequest request, String userName, String userRole) {
        HttpSession session = request.getSession();
        session.setAttribute(USER_SESSION_KEY, userName);
        session.setAttribute(USER_ROLE_SESSION_KEY, userRole);
    }

    public static String getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (String) session.getAttribute(USER_SESSION_KEY) : null;
    }

    public static String getUserRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (String) session.getAttribute(USER_ROLE_SESSION_KEY) : null;
    }

    public static void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static void setErrorMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute(ERROR_MESSAGE_KEY, message);
    }

    public static String getErrorMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String message = (String) session.getAttribute(ERROR_MESSAGE_KEY);
            session.removeAttribute(ERROR_MESSAGE_KEY);
            return message;
        }
        return null;
    }

    public static void setSuccessMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute(SUCCESS_MESSAGE_KEY, message);
    }

    public static String getSuccessMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String message = (String) session.getAttribute(SUCCESS_MESSAGE_KEY);
            session.removeAttribute(SUCCESS_MESSAGE_KEY);
            return message;
        }
        return null;
    }
}
