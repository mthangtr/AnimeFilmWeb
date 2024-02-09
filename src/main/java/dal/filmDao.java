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

    public static void main(String[] args) {
        filmDao fd = new filmDao();
        List<filmDtos> f = fd.getFilmWithHighestViewCount();
        for (filmDtos fdt : f){
            System.out.println(fdt.toString());
        }
    }

}
