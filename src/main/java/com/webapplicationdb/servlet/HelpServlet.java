package com.webapplicationdb.servlet;

import com.webapplicationdb.dao.HelpMessageDAO;
import com.webapplicationdb.model.HelpMessage;
import com.webapplicationdb.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/HelpServlet")
public class HelpServlet extends HttpServlet {
    private HelpMessageDAO helpMessageDAO;

    @Override
    public void init() throws ServletException {
        helpMessageDAO = new HelpMessageDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = SessionUtil.getUser(request);
        if (userName == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String message = request.getParameter("message");
        if (message == null || message.trim().isEmpty()) {
            SessionUtil.setErrorMessage(request, "Message cannot be empty");
            response.sendRedirect("help.jsp");
            return;
        }

        HelpMessage helpMessage = new HelpMessage(userName, message);
        if (helpMessageDAO.createMessage(helpMessage)) {
            SessionUtil.setSuccessMessage(request, "Message sent successfully");
        } else {
            SessionUtil.setErrorMessage(request, "Error sending message");
        }
        
        response.sendRedirect("help.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("help.jsp");
    }
}
