package controller;

import dal.filmDao;
import dtos.filmDtos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "categoryControl", urlPatterns = "/category")
public class categoryControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        filmDao fd = new filmDao();

        String pageStr = req.getParameter("page");
        int page = 1;
        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }

        int filmsPerPage = 6;
        List<filmDtos> films = fd.getFilmsPerPage(page, filmsPerPage);;
        int totalFilms = fd.getTotalFilms();
        int noOfPages = (int) Math.ceil(totalFilms * 1.0 / filmsPerPage);
        req.setAttribute("films", films);
        req.setAttribute("noOfPages", noOfPages);
        req.setAttribute("currentPage", page);
        req.getRequestDispatcher("Categories.jsp").forward(req, resp);
    }
}