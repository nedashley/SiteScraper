--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.7
-- Dumped by pg_dump version 9.5.0

-- Started on 2016-04-12 09:44:05 BST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 179 (class 3079 OID 12776)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2957 (class 0 OID 0)
-- Dependencies: 179
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 16386)
-- Name: emails; Type: TABLE; Schema: public; Owner: sitescraper
--

CREATE TABLE emails (
    e_link_id bigint NOT NULL,
    e_email character varying(256) NOT NULL
);


ALTER TABLE emails OWNER TO sitescraper;

--
-- TOC entry 173 (class 1259 OID 16389)
-- Name: inner_links; Type: TABLE; Schema: public; Owner: sitescraper
--

CREATE TABLE inner_links (
    il_id bigint NOT NULL,
    il_started timestamp without time zone,
    il_finished timestamp without time zone,
    il_response integer,
    il_url character varying(256),
    il_depth integer,
    il_link_id bigint NOT NULL
);


ALTER TABLE inner_links OWNER TO sitescraper;

--
-- TOC entry 174 (class 1259 OID 16392)
-- Name: inner_links_il_id_seq; Type: SEQUENCE; Schema: public; Owner: sitescraper
--

CREATE SEQUENCE inner_links_il_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE inner_links_il_id_seq OWNER TO sitescraper;

--
-- TOC entry 2958 (class 0 OID 0)
-- Dependencies: 174
-- Name: inner_links_il_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sitescraper
--

ALTER SEQUENCE inner_links_il_id_seq OWNED BY inner_links.il_id;


--
-- TOC entry 175 (class 1259 OID 16394)
-- Name: links; Type: TABLE; Schema: public; Owner: sitescraper
--

CREATE TABLE links (
    l_id bigint NOT NULL,
    l_scrape_id bigint NOT NULL,
    l_started timestamp without time zone,
    l_finished timestamp without time zone,
    l_resolves boolean,
    l_response integer,
    l_url character varying(256)
);


ALTER TABLE links OWNER TO sitescraper;

--
-- TOC entry 176 (class 1259 OID 16397)
-- Name: links_l_id_seq; Type: SEQUENCE; Schema: public; Owner: sitescraper
--

CREATE SEQUENCE links_l_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE links_l_id_seq OWNER TO sitescraper;

--
-- TOC entry 2959 (class 0 OID 0)
-- Dependencies: 176
-- Name: links_l_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sitescraper
--

ALTER SEQUENCE links_l_id_seq OWNED BY links.l_id;


--
-- TOC entry 177 (class 1259 OID 16399)
-- Name: scrapes; Type: TABLE; Schema: public; Owner: sitescraper
--

CREATE TABLE scrapes (
    s_id bigint NOT NULL,
    s_started timestamp without time zone,
    s_finished timestamp without time zone,
    s_name character varying,
    s_max_depth integer DEFAULT 1 NOT NULL
);


ALTER TABLE scrapes OWNER TO sitescraper;

--
-- TOC entry 178 (class 1259 OID 16406)
-- Name: scrapes_s_id_seq; Type: SEQUENCE; Schema: public; Owner: sitescraper
--

CREATE SEQUENCE scrapes_s_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE scrapes_s_id_seq OWNER TO sitescraper;

--
-- TOC entry 2960 (class 0 OID 0)
-- Dependencies: 178
-- Name: scrapes_s_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sitescraper
--

ALTER SEQUENCE scrapes_s_id_seq OWNED BY scrapes.s_id;


--
-- TOC entry 2817 (class 2604 OID 16408)
-- Name: il_id; Type: DEFAULT; Schema: public; Owner: sitescraper
--

ALTER TABLE ONLY inner_links ALTER COLUMN il_id SET DEFAULT nextval('inner_links_il_id_seq'::regclass);


--
-- TOC entry 2818 (class 2604 OID 16409)
-- Name: l_id; Type: DEFAULT; Schema: public; Owner: sitescraper
--

ALTER TABLE ONLY links ALTER COLUMN l_id SET DEFAULT nextval('links_l_id_seq'::regclass);


--
-- TOC entry 2820 (class 2604 OID 16410)
-- Name: s_id; Type: DEFAULT; Schema: public; Owner: sitescraper
--

ALTER TABLE ONLY scrapes ALTER COLUMN s_id SET DEFAULT nextval('scrapes_s_id_seq'::regclass);


--
-- TOC entry 2822 (class 2606 OID 16412)
-- Name: emails_pk; Type: CONSTRAINT; Schema: public; Owner: sitescraper
--

ALTER TABLE ONLY emails
    ADD CONSTRAINT emails_pk PRIMARY KEY (e_link_id, e_email);


--
-- TOC entry 2824 (class 2606 OID 16414)
-- Name: inner_links_pk; Type: CONSTRAINT; Schema: public; Owner: sitescraper
--

ALTER TABLE ONLY inner_links
    ADD CONSTRAINT inner_links_pk PRIMARY KEY (il_id);


--
-- TOC entry 2828 (class 2606 OID 16416)
-- Name: links_pk; Type: CONSTRAINT; Schema: public; Owner: sitescraper
--

ALTER TABLE ONLY links
    ADD CONSTRAINT links_pk PRIMARY KEY (l_id);


--
-- TOC entry 2830 (class 2606 OID 16418)
-- Name: scrapes_pk; Type: CONSTRAINT; Schema: public; Owner: sitescraper
--

ALTER TABLE ONLY scrapes
    ADD CONSTRAINT scrapes_pk PRIMARY KEY (s_id);


--
-- TOC entry 2825 (class 1259 OID 16449)
-- Name: links_l_response_idx; Type: INDEX; Schema: public; Owner: sitescraper
--

CREATE INDEX links_l_response_idx ON links USING btree (l_response);


--
-- TOC entry 2826 (class 1259 OID 16448)
-- Name: links_l_scrape_id_l_finished_idx; Type: INDEX; Schema: public; Owner: sitescraper
--

CREATE INDEX links_l_scrape_id_l_finished_idx ON links USING btree (l_scrape_id, l_finished NULLS FIRST);


--
-- TOC entry 2831 (class 2606 OID 16419)
-- Name: emails_links_fk; Type: FK CONSTRAINT; Schema: public; Owner: sitescraper
--

ALTER TABLE ONLY emails
    ADD CONSTRAINT emails_links_fk FOREIGN KEY (e_link_id) REFERENCES links(l_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2832 (class 2606 OID 16424)
-- Name: inner_links_links_fk; Type: FK CONSTRAINT; Schema: public; Owner: sitescraper
--

ALTER TABLE ONLY inner_links
    ADD CONSTRAINT inner_links_links_fk FOREIGN KEY (il_link_id) REFERENCES links(l_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2833 (class 2606 OID 16429)
-- Name: links_scrapes_fk; Type: FK CONSTRAINT; Schema: public; Owner: sitescraper
--

ALTER TABLE ONLY links
    ADD CONSTRAINT links_scrapes_fk FOREIGN KEY (l_scrape_id) REFERENCES scrapes(s_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2956 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-04-12 09:44:09 BST

--
-- PostgreSQL database dump complete
--

