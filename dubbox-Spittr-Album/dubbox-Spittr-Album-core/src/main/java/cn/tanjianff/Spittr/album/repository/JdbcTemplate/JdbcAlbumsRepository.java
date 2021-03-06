package cn.tanjianff.Spittr.album.repository.JdbcTemplate;

import cn.tanjianff.Spittr.album.domain.Album;
import cn.tanjianff.Spittr.album.repository.AlbumRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by tanjian on 2016/12/31.
 * 基于JdbcTemplate实现Album专辑存储接口
 */
public class JdbcAlbumsRepository implements AlbumRepository {
    private static final String INSERT_ALBUM="INSERT INTO S_albums" +
            "(s_aid,s_singerid,s_atitle,s_apubTime,s_adescp,s_avisRec,s_aCoverUrl) VALUES (?,?,?,?,?,?,?);";
    private static final String COUNT="SELECT s_aid,s_singerid,s_atitle,s_apubTime,s_adescp,s_avisRec,s_aCoverUrl FROM S_albums";
    private static final String SELECT_BY_ID="SELECT s_aid,s_singerid,s_atitle,s_apubTime,s_adescp,s_avisRec,s_aCoverUrl FROM S_albums WHERE s_aid=?;";
    private static final String SELECT_BY_SINGER_ID="SELECT s_aid,s_singerid,s_atitle,s_apubTime,s_adescp,s_avisRec,s_aCoverUrl FROM S_albums WHERE s_singerid=?;";
    private static final String ORDER_BY_PUBTIME="SELECT s_aid,s_singerid,s_atitle,s_apubTime,s_adescp,s_avisRec,s_aCoverUrl FROM S_albums ORDER BY s_apubTime DESC LIMIT 12;";
    private static final String SELECT_ALL="SELECT s_aid,s_singerid,s_atitle,s_apubTime,s_adescp,s_avisRec,s_aCoverUrl FROM S_albums;";
    private static final String SELECT_BY_VIS="SELECT s_aid,s_singerid,s_atitle,s_apubTime,s_adescp,s_avisRec,s_aCoverUrl FROM S_albums ORDER BY s_avisRec DESC;";
    private static final String SELECT_BY_TITLE_REG="SELECT s_aid,s_singerid,s_atitle,s_apubTime,s_adescp,s_avisRec,s_aCoverUrl FROM S_albums WHERE s_atitle REGEXP ?;";
    private static final String DELETE_BY_ID="DELETE * FROM S_albums WHERE s_aid=?";
    private static final String FIND_BY_VISITED="SELECT s_aid,s_singerid,s_atitle,s_apubTime,s_adescp,s_avisRec,s_aCoverUrl FROM S_albums ORDER BY s_avisRec DESC LIMIT ?";

    private JdbcTemplate jdbcTemplate;

    public JdbcAlbumsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

    public boolean save(Album album) {
        return jdbcTemplate.update(INSERT_ALBUM,album.getId(),album.getSingerid()
                                    ,album.getTitle(),album.getPubTime(),album.getDescp()
                                    ,album.getVisRec(),album.getCoverUrl())>0;
    }

    /*Return the maximum number of rows specified for this JdbcTemplate.*/
    public int count() {
        return jdbcTemplate.getMaxRows();
    }

    @Override
    public int updateOne(Album album) {
        return 0;
    }

    public Album findById(String id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID,new AlbumRowMapper(),id);
    }

    public List<Album> findBySingerId(String id) {
        return jdbcTemplate.query(SELECT_BY_SINGER_ID,new AlbumRowMapper(),id);
    }

    public List<Album> orderByPubTime() {
        return jdbcTemplate.query(ORDER_BY_PUBTIME,new AlbumRowMapper());
    }

    public List<Album> findAll() {
        return jdbcTemplate.query(SELECT_ALL,new AlbumRowMapper());
    }

    public List<Album> ListByVis() {
        return jdbcTemplate.query(SELECT_BY_VIS,new AlbumRowMapper());
    }

    public List<Album> findByTitle(String title) {
        return jdbcTemplate.query(SELECT_BY_TITLE_REG,new AlbumRowMapper(),title);
    }

    @Override
    public List<Album> findByVisited(int lmt) {
        return jdbcTemplate.query(FIND_BY_VISITED,new AlbumRowMapper(),lmt);
    }

    private static class AlbumRowMapper implements RowMapper<Album>{
        public Album mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Album(resultSet.getString("s_aid")
                    ,resultSet.getString("s_singerid")
                    ,resultSet.getString("s_atitle")
                    ,resultSet.getDate("s_apubTime")
                    ,resultSet.getString("s_adescp")
                    ,resultSet.getInt("s_avisRec")
                    ,resultSet.getString("s_aCoverUrl"));
        }
    }
}
