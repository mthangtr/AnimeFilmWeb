package controller;

import dal.commentDao;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(urlPatterns = "/deleteComment")
public class deleteCommentControl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int commentID = Integer.parseInt(req.getParameter("commentID"));
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("userSession");
        commentDao cmd = new commentDao();
        String filmName = req.getParameter("filmName");

            boolean success = cmd.deleteComment(commentID);
            if (success) {
                resp.sendRedirect("detail?filmName=" + URLEncoder.encode(filmName, "UTF-8"));
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to delete comment.");
            }
    }

}
