package com.redmonkeysoftware.sitescraper.app.dao;

import com.redmonkeysoftware.sitescraper.logic.InnerLink;
import com.redmonkeysoftware.sitescraper.logic.Link;
import com.redmonkeysoftware.sitescraper.logic.MiniScrape;
import com.redmonkeysoftware.sitescraper.logic.Scrape;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class ScrapeDaoImpl extends BaseAbstractDao implements ScrapeDao {

    private final String selectMiniScrapesSql = "SELECT s.*,"
            + "(SELECT COUNT(l.*) FROM links l WHERE l.l_scrape_id = s.s_id) AS ms_link_count,"
            + "(SELECT COUNT(l.*) FROM links l WHERE l.l_scrape_id = s.s_id AND l.l_response BETWEEN '200' AND '299') AS ms_positives_count,"
            + "(SELECT COUNT(l.*) FROM links l WHERE l.l_scrape_id = s.s_id AND (l.l_response < '200' OR l.l_response > '299')) AS ms_negatives_count,"
            + "e.e_email "
            + "FROM scrapes s "
            + "LEFT JOIN links l ON s.s_id = l.l_scrape_id "
            + "LEFT JOIN emails e ON l.l_id = e.e_link_id";
    private final String selectScrapeSql = "SELECT s.*,l.*,e.*,il.* "
            + "FROM scrapes s "
            + "LEFT JOIN links l ON s.s_id = l.l_scrape_id "
            + "LEFT JOIN emails e ON l.l_id = e.e_link_id "
            + "LEFT JOIN inner_links il ON l.l_id = il.il_link_id "
            + "WHERE s.s_id = :id";
    private final String insertScrapeSql = "INSERT INTO scrapes "
            + "(s_id,s_name,s_started,s_finished,s_max_depth) "
            + "VALUES(:id,:name,:started,:finished,:maxDepth)";
    private final String updateScrapeSql = "UPDATE scrapes SET "
            + "s_name=:name,s_started=:started,s_finished=:finished,s_max_depth=:maxDepth "
            + "WHERE s_id=:id";
    private final String deleteScrapeSql = "DELETE FROM scrapes WHERE s_id = :id";

    private final String selectLinkSql = "SELECT l.*,e.*,il.* "
            + "FROM links l "
            + "LEFT JOIN emails e ON l.l_id = e.e_link_id "
            + "LEFT JOIN inner_links il ON l.l_id = il.il_link_id "
            + "WHERE l.l_id = :id";
    private final String insertLinkSql = "INSERT INTO links "
            + "(l_id,l_started,l_finished,l_resolves,l_response,l_url,l_scrape_id) "
            + "VALUES(:id,:started,:finished,:resolves,:response,:url,:scrapeId)";
    private final String updateLinkSql = "UPDATE links SET "
            + "l_started=:started,l_finished=:finished,l_resolves=:resolves,l_response=:response,l_url=:url,l_scrape_id=:scrapeId "
            + "WHERE l_id=:id";

    private final String updateScrapeFinishedSql = "UPDATE scrapes SET s_finished = (SELECT l_finished FROM links l WHERE l.l_scrape_id = :scrapeId ORDER BY l_finished DESC NULLS FIRST LIMIT 1) WHERE s_id = :scrapeId";

    private final String deleteEmailsSql = "DELETE FROM emails WHERE e_link_id = :linkId";
    private final String insertEmailSql = "INSERT INTO emails (e_link_id, e_email) VALUES(:linkId, :email)";

    private final String insertInnerLinkSql = "INSERT INTO inner_links "
            + "(il_id,il_started,il_finished,il_response,il_url,il_depth,il_link_id) "
            + "VALUES(:id,:started,:finished,:response,:url,:depth,:linkId)";
    private final String updateInnerLinkSql = "UPDATE inner_links SET "
            + "il_started=:started,il_finished=:finished,il_response=:response,il_url=:url,il_depth=:depth,il_link_id=:linkId "
            + "WHERE il_id = :id";

    @Override
    public List<MiniScrape> lookupMiniScrapes() {
        List<MiniScrape> results = this.getNamedParameterJdbcTemplate().query(selectMiniScrapesSql, new MiniScrapeResultSetExtractor());
        return results;
    }

    @Override
    public Scrape lookupScrape(Long id) {
        List<Scrape> results = this.getNamedParameterJdbcTemplate().query(selectScrapeSql, new MapSqlParameterSource("id", id), new ScrapeResultSetExtractor());
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public Link lookupLink(Long id) {
        List<Link> results = this.getNamedParameterJdbcTemplate().query(selectLinkSql, new MapSqlParameterSource("id", id), new LinkResultSetExtractor());
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public Scrape persistScrape(Scrape scrape) {
        String sql = updateScrapeSql;
        if (scrape.getId() == null) {
            sql = insertScrapeSql;
            scrape.setId(getNextId("scrapes_s_id_seq"));
            scrape.setStarted(LocalDateTime.now());
        }
        OverrideableBeanPropertySqlParameterSource params = new OverrideableBeanPropertySqlParameterSource(scrape);
        this.getNamedParameterJdbcTemplate().update(sql, params);
        for (Link link : scrape.getLinks()) {
            fastPersistLink(scrape.getId(), link);
        }
        return lookupScrape(scrape.getId());
    }

    @Override
    public Link persistLink(Long scrapeId, Link link) {
        return lookupLink(fastPersistLink(scrapeId, link));
    }

    @Override
    public Long fastPersistLink(Long scrapeId, Link link) {
        String sql = updateLinkSql;
        if (link.getId() == null) {
            sql = insertLinkSql;
            link.setId(getNextId("links_l_id_seq"));
        }
        OverrideableBeanPropertySqlParameterSource params = new OverrideableBeanPropertySqlParameterSource(link, "scrapeId", scrapeId);
        this.getNamedParameterJdbcTemplate().update(sql, params);
        for (InnerLink innerLink : link.getInnerLinks()) {
            persistInnerLink(link.getId(), innerLink);
        }
        persistEmails(link.getId(), link.getEmails());
        this.getNamedParameterJdbcTemplate().update(updateScrapeFinishedSql, new MapSqlParameterSource("scrapeId", scrapeId));
        return link.getId();
    }

    protected void persistInnerLink(Long linkId, InnerLink innerLink) {
        String sql = updateInnerLinkSql;
        if (innerLink.getId() == null) {
            sql = insertInnerLinkSql;
            innerLink.setId(getNextId("inner_links_il_id_seq"));
        }
        OverrideableBeanPropertySqlParameterSource params = new OverrideableBeanPropertySqlParameterSource(innerLink, "linkId", linkId);
        this.getNamedParameterJdbcTemplate().update(sql, params);
    }

    protected void persistEmails(Long linkId, Set<String> emails) {
        this.getNamedParameterJdbcTemplate().update(deleteEmailsSql, new MapSqlParameterSource("linkId", linkId));
        MapSqlParameterSource params = new MapSqlParameterSource("linkId", linkId);
        for (String email : emails) {
            if (StringUtils.isNotBlank(email)) {
                params.addValue("email", email);
                this.getNamedParameterJdbcTemplate().update(insertEmailSql, params);
            }
        }
    }

    @Override
    public void deleteScrape(Long id) {
        this.getNamedParameterJdbcTemplate().update(deleteScrapeSql, new MapSqlParameterSource("id", id));
    }

    private class ScrapeResultSetExtractor implements ResultSetExtractor<List<Scrape>> {

        @Override
        public List<Scrape> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Scrape> results = new ArrayList<>();
            while (rs.next()) {
                Scrape scrape = mapScrape(rs);
                if (results.contains(scrape)) {
                    Scrape original = results.get(results.indexOf(scrape));
                    for (Link link : scrape.getLinks()) {
                        if (original.getLinks().contains(link)) {
                            Link originalLink = original.getLinks().get(original.getLinks().indexOf(link));
                            Iterator<InnerLink> innerLinkIterator = link.getInnerLinks().iterator();
                            while (innerLinkIterator.hasNext()) {
                                InnerLink innerLink = innerLinkIterator.next();
                                if (!originalLink.getInnerLinks().contains(innerLink)) {
                                    originalLink.getInnerLinks().add(innerLink);
                                }
                            }
                            for (String email : link.getEmails()) {
                                originalLink.getEmails().add(email);
                            }
                        } else {
                            original.getLinks().add(link);
                        }
                    }
                } else {
                    results.add(scrape);
                }
            }
            return results;
        }
    }

    private class LinkResultSetExtractor implements ResultSetExtractor<List<Link>> {

        @Override
        public List<Link> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Link> results = new ArrayList<>();
            while (rs.next()) {
                Link link = mapLink(rs);
                if (results.contains(link)) {
                    Link originalLink = results.get(results.indexOf(link));
                    Iterator<InnerLink> innerLinkIterator = link.getInnerLinks().iterator();
                    while (innerLinkIterator.hasNext()) {
                        InnerLink innerLink = innerLinkIterator.next();
                        if (!originalLink.getInnerLinks().contains(innerLink)) {
                            originalLink.getInnerLinks().add(innerLink);
                        }
                    }
                    for (String email : link.getEmails()) {
                        originalLink.getEmails().add(email);
                    }
                } else {
                    results.add(link);
                }
            }
            return results;
        }
    }

    private class InnerLinkRowMapper implements RowMapper<InnerLink> {

        @Override
        public InnerLink mapRow(ResultSet rs, int i) throws SQLException {
            return mapInnerLink(rs);
        }
    }

    private class MiniScrapeResultSetExtractor implements ResultSetExtractor<List<MiniScrape>> {

        @Override
        public List<MiniScrape> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<MiniScrape> results = new ArrayList<>();
            while (rs.next()) {
                MiniScrape ms = mapMiniScrape(rs);
                if (results.contains(ms)) {
                    MiniScrape original = results.get(results.indexOf(ms));
                    original.getEmails().addAll(ms.getEmails());
                } else {
                    results.add(ms);
                }
            }
            return results;
        }
    }

    protected final MiniScrape mapMiniScrape(ResultSet rs) throws SQLException {
        MiniScrape ms = new MiniScrape();
        ms.setId(getLong(rs, "s_id"));
        ms.setName(rs.getString("s_name"));
        ms.setStarted(getLocalDateTime(rs, "s_started"));
        ms.setFinished(getLocalDateTime(rs, "s_finished"));
        ms.setMaxDepth(getInteger(rs, "s_max_depth"));
        ms.setLinkCount(getInteger(rs, "ms_link_count"));
        ms.setNegativesCount(getInteger(rs, "ms_negatives_count"));
        ms.setPositivesCount(getInteger(rs, "ms_positives_count"));
        if (StringUtils.isNotBlank(rs.getString("e_email"))) {
            ms.getEmails().add(rs.getString("e_email"));
        }
        return ms;
    }

    protected final Scrape mapScrape(ResultSet rs) throws SQLException {
        Scrape s = new Scrape();
        s.setId(getLong(rs, "s_id"));
        s.setName(rs.getString("s_name"));
        s.setStarted(getLocalDateTime(rs, "s_started"));
        s.setFinished(getLocalDateTime(rs, "s_finished"));
        s.setMaxDepth(getInteger(rs, "s_max_depth"));
        if (getLong(rs, "l_id") != null) {
            s.getLinks().add(mapLink(rs));
        }
        return s;
    }

    protected final Link mapLink(ResultSet rs) throws SQLException {
        Link l = new Link();
        l.setId(getLong(rs, "l_id"));
        l.setStarted(getLocalDateTime(rs, "l_started"));
        l.setFinished(getLocalDateTime(rs, "l_finished"));
        l.setResolves(getBoolean(rs, "l_resolves"));
        l.setResponse(getInteger(rs, "l_response"));
        l.setUrl(rs.getString("l_url"));
        if (getLong(rs, "il_id") != null) {
            l.getInnerLinks().add(mapInnerLink(rs));
        }
        if (getLong(rs, "e_link_id") != null) {
            l.getEmails().add(rs.getString("e_email"));
        }
        return l;
    }

    protected final InnerLink mapInnerLink(ResultSet rs) throws SQLException {
        InnerLink il = new InnerLink();
        il.setId(getLong(rs, "il_id"));
        il.setStarted(getLocalDateTime(rs, "il_started"));
        il.setFinished(getLocalDateTime(rs, "il_finished"));
        il.setDepth(getInteger(rs, "il_depth"));
        il.setResponse(getInteger(rs, "il_response"));
        il.setUrl(rs.getString("il_url"));
        return il;
    }
}
