package controller;

import dal.userDao;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Random;

@WebServlet(name = "otpConfirmControl", urlPatterns = "/confirm")
public class otpConfirmControl  extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userDao ud = new userDao();

        //input code of user from client
        String userCode = req.getParameter("code");
        String gmail = (String) req.getSession().getAttribute("userEmail");
        String password = (String)req.getSession().getAttribute("password");

        //code from session sent to user's email
        String savedCode = (String) req.getSession().getAttribute("verificationCode");
        Boolean isRegister = (Boolean) req.getSession().getAttribute("isRegister");

        //check input is not empty
        if (userCode.isBlank()) {
            req.setAttribute("errorMessage", "Please input to continue!");
            req.getRequestDispatcher("otpConfirmForm.jsp").forward(req, resp);
            return;
        }


        if (Boolean.TRUE.equals(isRegister)){
            if (userCode != null && userCode.equals(savedCode)) {
                boolean checked = ud.registerUser(new User(generateUsername(gmail), password, gmail, false));
                if (checked) {
                    req.setAttribute("failedLoginMessage", "Sign up successfully!");
                    req.getRequestDispatcher("SignIn.jsp").forward(req, resp);
                } else {
                    req.setAttribute("errorMessage", "Sign up unsuccessfully!");
                    req.getRequestDispatcher("SignUp.jsp").forward(req, resp);
                }
            } else {
                req.setAttribute("errorMessage", "Invalid verification code. Please try again.");
                req.getRequestDispatcher("otpConfirmForm.jsp").forward(req, resp);
            }
        }else {
            if (userCode != null && userCode.equals(savedCode)) {
                resp.sendRedirect("changePasswordForm.jsp");
            } else {
                req.setAttribute("errorMessage", "Invalid verification code. Please try again.");
                req.getRequestDispatcher("otpConfirmForm.jsp").forward(req, resp);
            }
        }
    }


    private static String generateUsername(String email) {
        String namePart = email.split("@")[0];

        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);

        String username = namePart + randomNumber;

        return username;
    }
}
