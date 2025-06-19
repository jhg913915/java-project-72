package hexlet.code.repository;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
                url.setCreatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
            } else {
                throw new SQLException("DB have not returned an id or createdAt after saving an entity");
            }
        }
    }

    public static Optional<Url> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var resultName = resultSet.getString("name");
                var resultCreatedAt = resultSet.getTimestamp("created_at");
                var resultId = resultSet.getLong("id");
                Url resultUrl = new Url(resultName);
                resultUrl.setId(resultId);
                resultUrl.setCreatedAt(resultCreatedAt.toLocalDateTime());
                List<UrlCheck> checks = UrlCheckRepository.findByUrlId(resultId);
                resultUrl.setChecks(checks);
                return Optional.of(resultUrl);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var resultName = resultSet.getString("name");
                var resultId = resultSet.getLong("id");
                var resultCreatedAt = resultSet.getTimestamp("created_at");
                Url resultUrl = new Url(resultName);
                resultUrl.setId(resultId);
                resultUrl.setCreatedAt(resultCreatedAt.toLocalDateTime());
                return Optional.of(resultUrl);
            }
            return Optional.empty();
        }
    }

    public static List<Url> getEntities() throws SQLException {
        String sql = "SELECT * FROM urls ORDER BY created_at ASC";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();
            var result = new ArrayList<Url>();
            while (resultSet.next()) {
                var name = resultSet.getString("name");
                var url = new Url(name);
                url.setId(resultSet.getLong("id"));
                url.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                result.add(url);
            }
            return result;
        }
    }
}
