package com.studentapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "NumberGuessServlet", urlPatterns = {"/guess"})
public class NumberGuessServlet extends HttpServlet {

    private int getOrCreateTarget(HttpSession session) {
        Object existing = session.getAttribute("target");
        if (existing instanceof Integer) {
            return (Integer) existing;
        }
        int t = new Random().nextInt(10) + 1; // 1..10
        session.setAttribute("target", t);
        return t;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        int target = getOrCreateTarget(session);

        String guessParam = request.getParameter("guess");
        String message;

        if (guessParam == null || guessParam.isBlank()) {
            message = "Type a number from 1 to 10 🙂";
        } else {
            try {
                int guess = Integer.parseInt(guessParam.trim());
                if (!NumberUtils.isValidGuess(guess)) {
                    message = "Only numbers 1 to 10 are allowed.";
                } else if (guess == target) {
                    message = "🎉 Correct! The number was " + target + ". I picked a new number; try again!";
                    // Reset for a new game
                    session.removeAttribute("target");
                } else if (guess < target) {
                    message = "Too small. Try a bigger number!";
                } else {
                    message = "Too big. Try a smaller number!";
                }
            } catch (NumberFormatException e) {
                message = "That wasn't a number. Please type digits like 3 or 7.";
            }
        }

        request.setAttribute("message", message);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
