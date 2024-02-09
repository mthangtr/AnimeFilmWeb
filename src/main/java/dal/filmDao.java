package dal;

import dtos.filmDtos;
import entity.Category;
import entity.Tag;
import entity.Season;
import entity.Episode;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class filmDao extends DBContext {

    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<filmDtos> getAllFilms() {
        List<filmDtos> films = new ArrayList<>();
        String sql = "WITH RatingAverage AS (\n" +
                "    SELECT\n" +
                "        filmID,\n" +
                "        AVG(ratingValue) AS averageRating\n" +
                "    FROM\n" +
                "        Ratings\n" +
                "    GROUP BY\n" +
                "        filmID\n" +
                ")\n" +
                "SELECT\n" +
                "    f.*,\n" +
                "    ra.averageRating\n" +
                "FROM\n" +
                "    Film f\n" +
                "LEFT JOIN RatingAverage ra ON f.filmID = ra.filmID\n" +
                "ORDER BY f.filmID DESC;\n"; // Câu lệnh đã được chỉnh sửa để sắp xếp theo filmID giảm dần
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                filmDtos film = new filmDtos();
                film.setFilmID(rs.getInt("filmID"));
                film.setFilmName(rs.getString("filmName"));
                film.setDescription(rs.getString("description"));
                film.setImageLink(rs.getString("imageLink"));
                film.setTrailerLink(rs.getString("trailerLink"));
                film.setViewCount(rs.getLong("viewCount"));
                film.setRatingValue(rs.getFloat("averageRating"));

                film.setCategories(getCategoriesForFilm(film.getFilmID()));
                film.setTags(getTagsForFilm(film.getFilmID()));
                film.setSeasons(getSeasonsForFilm(film.getFilmID()));
                film.setEpisodes(getEpisodesForFilm(film.getFilmID()));

                films.add(film);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return films;
    }

    public List<filmDtos> getNewFilms() {
        List<filmDtos> newFilms = new ArrayList<>();
        String sql = "WITH RatingAverage AS (\n" +
                "    SELECT\n" +
                "        filmID,\n" +
                "        AVG(ratingValue) AS averageRating\n" +
                "    FROM\n" +
                "        Ratings\n" +
                "    GROUP BY\n" +
                "        filmID\n" +
                ")\n" +
                "SELECT TOP (5) f.*, ra.averageRating\n" +
                "FROM Film f\n" +
                "LEFT JOIN RatingAverage ra ON f.filmID = ra.filmID\n" +
                "ORDER BY f.filmID DESC;";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                filmDtos film = new filmDtos();
                // Thiết lập thông tin film
                film.setFilmID(rs.getInt("filmID"));
                film.setFilmName(rs.getString("filmName"));
                film.setDescription(rs.getString("description"));
                film.setImageLink(rs.getString("imageLink"));
                film.setTrailerLink(rs.getString("trailerLink"));
                film.setViewCount(rs.getLong("viewCount"));
                film.setRatingValue(rs.getFloat("averageRating"));

                // Lấy thông tin liên quan
                film.setCategories(getCategoriesForFilm(film.getFilmID()));
                film.setTags(getTagsForFilm(film.getFilmID()));
                film.setSeasons(getSeasonsForFilm(film.getFilmID()));
                film.setEpisodes(getEpisodesForFilm(film.getFilmID()));

                newFilms.add(film);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFilms;
    }


    private List<Category> getCategoriesForFilm(int filmID) {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT c.CategoryName FROM Category c JOIN FilmCategory fc ON c.CategoryID = fc.CategoryID WHERE fc.filmID = ?";
        ResultSet rsLocal = null;
        try (PreparedStatement psLocal = getConnection().prepareStatement(sql)) {
            psLocal.setInt(1, filmID);
            rsLocal = psLocal.executeQuery();
            while (rsLocal.next()) {
                Category category = new Category();
                category.setCategoryName(rsLocal.getString("CategoryName"));
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rsLocal != null) {
                try {
                    rsLocal.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return categories;
    }
    private List<Tag> getTagsForFilm(int filmID) {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT t.tagName FROM Tag t JOIN FilmTag ft ON t.tagID = ft.tagID WHERE ft.filmID = ?";
        try (PreparedStatement psLocal = conn.prepareStatement(sql)) {
            psLocal.setInt(1, filmID);
            try (ResultSet rsLocal = psLocal.executeQuery()) {
                while (rsLocal.next()) {
                    Tag tag = new Tag();
                    tag.setTagName(rsLocal.getString("tagName"));
                    tags.add(tag);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tags;
    }

    private List<Season> getSeasonsForFilm(int filmID) {
        List<Season> seasons = new ArrayList<>();
        String sql = "SELECT s.seasonName FROM Season s WHERE s.filmID = ?";
        try (PreparedStatement psLocal = conn.prepareStatement(sql)) {
            psLocal.setInt(1, filmID);
            try (ResultSet rsLocal = psLocal.executeQuery()) {
                while (rsLocal.next()) {
                    Season season = new Season();
                    season.setSeasonName(rsLocal.getString("seasonName"));
                    seasons.add(season);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seasons;
    }

    private List<Episode> getEpisodesForFilm(int filmID) {
        List<Episode> episodes = new ArrayList<>();
        String sql = "SELECT e.episodeID, e.title, e.episodeLink, e.releaseDate FROM Episode e JOIN Season s ON e.seasonID = s.seasonID JOIN Film f ON s.filmID = f.filmID WHERE f.filmID = ?";
        try (PreparedStatement psLocal = conn.prepareStatement(sql)) {
            psLocal.setInt(1, filmID);
            try (ResultSet rsLocal = psLocal.executeQuery()) {
                while (rsLocal.next()) {
                    Episode episode = new Episode();
                    episode.setEpId(rsLocal.getInt("episodeID"));
                    episode.setEpTittle(rsLocal.getString("title"));
                    episode.setEpLink(rsLocal.getString("episodeLink"));
                    episode.setEpDate(rsLocal.getDate("releaseDate"));
                    episodes.add(episode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return episodes;
    }


    public filmDtos getFilmById(int filmId) {
        filmDtos film = null;
        String sql = "WITH RatingAverage AS (\n" +
                "    SELECT\n" +
                "        filmID,\n" +
                "        AVG(ratingValue) AS averageRating\n" +
                "    FROM\n" +
                "        Ratings\n" +
                "    GROUP BY\n" +
                "        filmID\n" +
                ")\n" +
                "SELECT f.*, ra.averageRating\n" +
                "FROM Film f\n" +
                "LEFT JOIN RatingAverage ra ON f.filmID = ra.filmID\n" +
                "WHERE f.filmID = ?;";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, filmId);
            rs = ps.executeQuery();
            if (rs.next()) {
                film = new filmDtos();
                // Set film details
                film.setFilmID(rs.getInt("filmID"));
                film.setFilmName(rs.getString("filmName"));
                film.setDescription(rs.getString("description"));
                film.setImageLink(rs.getString("imageLink"));
                film.setTrailerLink(rs.getString("trailerLink"));
                film.setViewCount(rs.getLong("viewCount"));
                film.setRatingValue(rs.getFloat("averageRating"));

                // Get related data
                film.setCategories(getCategoriesForFilm(film.getFilmID()));
                film.setTags(getTagsForFilm(film.getFilmID()));
                film.setSeasons(getSeasonsForFilm(film.getFilmID()));
                film.setEpisodes(getEpisodesForFilm(film.getFilmID()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return film;
    }

    public filmDtos getFilmByName(String filmName) {
        filmDtos film = null;
        String sql = "WITH RatingAverage AS (\n" +
                "    SELECT\n" +
                "        filmID,\n" +
                "        AVG(ratingValue) AS averageRating\n" +
                "    FROM\n" +
                "        Ratings\n" +
                "    GROUP BY\n" +
                "        filmID\n" +
                ")\n" +
                "SELECT f.*, ra.averageRating\n" +
                "FROM Film f\n" +
                "LEFT JOIN RatingAverage ra ON f.filmID = ra.filmID\n" +
                "WHERE f.filmName = ?;";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, filmName);
            rs = ps.executeQuery();
            if (rs.next()) {
                film = new filmDtos();
                // Set film details
                film.setFilmID(rs.getInt("filmID"));
                film.setFilmName(rs.getString("filmName"));
                film.setDescription(rs.getString("description"));
                film.setImageLink(rs.getString("imageLink"));
                film.setTrailerLink(rs.getString("trailerLink"));
                film.setViewCount(rs.getLong("viewCount"));
                film.setRatingValue(rs.getFloat("averageRating"));

                // Get related data
                film.setCategories(getCategoriesForFilm(film.getFilmID()));
                film.setTags(getTagsForFilm(film.getFilmID()));
                film.setSeasons(getSeasonsForFilm(film.getFilmID()));
                film.setEpisodes(getEpisodesForFilm(film.getFilmID()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return film;
    }

    public List<filmDtos> getFilmWithHighestViewCount() {
        filmDtos film = null;
        List<filmDtos> filmDtosList = new ArrayList<>();
        String sql = "WITH RatingAverage AS (\n" +
                "    SELECT\n" +
                "        filmID,\n" +
                "        AVG(ratingValue) AS averageRating\n" +
                "    FROM\n" +
                "        Ratings\n" +
                "    GROUP BY\n" +
                "        filmID\n" +
                "),\n" +
                "HighestViewCount AS (\n" +
                "    SELECT TOP 8\n" +
                "        f.*,\n" +
                "        ra.averageRating\n" +
                "    FROM Film f\n" +
                "    LEFT JOIN RatingAverage ra ON f.filmID = ra.filmID\n" +
                "    ORDER BY f.viewCount DESC\n" +
                ")\n" +
                "SELECT *\n" +
                "FROM HighestViewCount;\n";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                film = new filmDtos();
                film.setFilmID(rs.getInt("filmID"));
                film.setFilmName(rs.getString("filmName"));
                film.setDescription(rs.getString("description"));
                film.setImageLink(rs.getString("imageLink"));
                film.setTrailerLink(rs.getString("trailerLink"));
                film.setViewCount(rs.getLong("viewCount"));
                film.setRatingValue(rs.getFloat("averageRating"));

                // Get related data
                film.setCategories(getCategoriesForFilm(film.getFilmID()));
                film.setTags(getTagsForFilm(film.getFilmID()));
                film.setSeasons(getSeasonsForFilm(film.getFilmID()));
                film.setEpisodes(getEpisodesForFilm(film.getFilmID()));
                filmDtosList.add(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return filmDtosList;
    }

    public int getTotalFilms(){
        String query = "select COUNT(*) from Film";
        try{
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                return rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public List<filmDtos> getFilmsPerPage(int currentPage, int filmsPerPage) {
        List<filmDtos> films = new ArrayList<>();
        // Cập nhật câu truy vấn cho SQL Server
        String query = "SELECT * FROM Film ORDER BY filmID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            int offset = (currentPage - 1) * filmsPerPage;
            ps.setInt(1, offset); // Đặt OFFSET
            ps.setInt(2, filmsPerPage); // Đặt số lượng ROWS để FETCH
            rs = ps.executeQuery();
            while (rs.next()) {
                filmDtos film = new filmDtos();
                film.setFilmID(rs.getInt("filmID"));
                film.setFilmName(rs.getString("filmName"));
                film.setDescription(rs.getString("description"));
                film.setImageLink(rs.getString("imageLink"));
                film.setTrailerLink(rs.getString("trailerLink"));
                film.setViewCount(rs.getLong("viewCount"));

                // Get related data
                film.setCategories(getCategoriesForFilm(film.getFilmID()));
                film.setTags(getTagsForFilm(film.getFilmID()));
                film.setSeasons(getSeasonsForFilm(film.getFilmID()));
                film.setEpisodes(getEpisodesForFilm(film.getFilmID()));
                films.add(film);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return films;
    }

//    public List<filmDtos> getFilmsByCategory(String categoryName) {
//        List<filmDtos> films = new ArrayList<>();
//        String sql = "SELECT f.* FROM Film f "
//                + "INNER JOIN FilmCategory fc ON f.filmID = fc.filmID "
//                + "INNER JOIN Category c ON fc.CategoryID = c.CategoryID "
//                + "WHERE c.CategoryName = ?";
//        try {
//            conn = new DBContext().getConnection();
//            ps = conn.prepareStatement(sql);
//            ps.setString(1, categoryName);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                filmDtos film = new filmDtos();
//                film.setFilmID(rs.getInt("filmID"));
//                film.setFilmName(rs.getString("filmName"));
//                film.setDescription(rs.getString("description"));
//                film.setImageLink(rs.getString("imageLink"));
//                film.setTrailerLink(rs.getString("trailerLink"));
//                film.setViewCount(rs.getLong("viewCount"));
//                // Lấy thông tin liên quan như thể loại, tag, mùa, tập,...
//                film.setCategories(getCategoriesForFilm(film.getFilmID()));
//                film.setTags(getTagsForFilm(film.getFilmID()));
//                film.setSeasons(getSeasonsForFilm(film.getFilmID()));
//                film.setEpisodes(getEpisodesForFilm(film.getFilmID()));
//
//                films.add(film);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // Close resources
//            try {
//                if (rs != null) rs.close();
//                if (ps != null) ps.close();
//                if (conn != null) conn.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return films;
//    }

    public int getTotalFilmsByCategory(String category) {
        String query = "SELECT COUNT(*) FROM Film f JOIN FilmCategory fc ON f.filmID = fc.filmID JOIN Category c ON fc.CategoryID = c.CategoryID WHERE c.CategoryName = ?";
        try {
             conn = new DBContext().getConnection();
             ps = conn.prepareStatement(query);
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<filmDtos> getFilmsByCategory(String categoryName, int page, int filmsPerPage) {
        List<filmDtos> films = new ArrayList<>();
        String query = "WITH Film_CTE AS (" +
                "SELECT ROW_NUMBER() OVER (ORDER BY f.filmID DESC) AS RowNum, f.* " +
                "FROM Film f JOIN FilmCategory fc ON f.filmID = fc.filmID " +
                "JOIN Category c ON fc.CategoryID = c.CategoryID " +
                "WHERE c.CategoryName = ?" +
                ")" +
                "SELECT * FROM Film_CTE " +
                "WHERE RowNum BETWEEN ? AND ?";
        try {
            int startRow = (page - 1) * filmsPerPage + 1;
            int endRow = startRow + filmsPerPage - 1;
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, categoryName);
            ps.setInt(2, startRow);
            ps.setInt(3, endRow);
            rs = ps.executeQuery();
            while (rs.next()) {
                filmDtos film = new filmDtos();
                film.setFilmID(rs.getInt("filmID"));
                film.setFilmName(rs.getString("filmName"));
                film.setDescription(rs.getString("description"));
                film.setImageLink(rs.getString("imageLink"));
                film.setTrailerLink(rs.getString("trailerLink"));
                film.setViewCount(rs.getLong("viewCount"));

                film.setCategories(getCategoriesForFilm(film.getFilmID()));
                film.setTags(getTagsForFilm(film.getFilmID()));
                film.setSeasons(getSeasonsForFilm(film.getFilmID()));
                film.setEpisodes(getEpisodesForFilm(film.getFilmID()));
                films.add(film);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return films;
    }
    public static void main(String[] args) {
        filmDao fd = new filmDao();
//        List<filmDtos> films = fd.getFilmsByCategory("Action/Adventure", 1,6);
//        for (filmDtos a : films){
//            System.out.println(a.getFilmName());
//        }
        int total = fd.getTotalFilmsByCategory("Action/Adventure");
        System.out.println(total);
    }

}
