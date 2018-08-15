--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.13
-- Dumped by pg_dump version 9.5.13

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: code; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.code (
    id bigint NOT NULL,
    name character varying(255),
    path character varying(255),
    dateupload timestamp without time zone,
    submission_id bigint
);


ALTER TABLE public.code OWNER TO juez;

--
-- Name: coder; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.coder (
    id bigint NOT NULL,
    ranking character varying(255),
    user_id bigint
);


ALTER TABLE public.coder OWNER TO juez;

--
-- Name: contest; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.contest (
    id bigint NOT NULL,
    name character varying(255),
    jhi_password character varying(255),
    startdate timestamp without time zone,
    enddate timestamp without time zone,
    jhi_type character varying(255),
    active boolean,
    creator_id bigint
);


ALTER TABLE public.contest OWNER TO juez;

--
-- Name: contest_coder; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.contest_coder (
    coders_id bigint NOT NULL,
    contests_id bigint NOT NULL
);


ALTER TABLE public.contest_coder OWNER TO juez;

--
-- Name: contest_problem; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.contest_problem (
    problems_id bigint NOT NULL,
    contests_id bigint NOT NULL
);


ALTER TABLE public.contest_problem OWNER TO juez;

--
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE public.databasechangelog OWNER TO juez;

--
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO juez;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: juez
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1000
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO juez;

--
-- Name: jhi_authority; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.jhi_authority (
    name character varying(50) NOT NULL
);


ALTER TABLE public.jhi_authority OWNER TO juez;

--
-- Name: jhi_persistent_audit_event; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.jhi_persistent_audit_event (
    event_id bigint NOT NULL,
    principal character varying(100) NOT NULL,
    event_date timestamp without time zone,
    event_type character varying(255)
);


ALTER TABLE public.jhi_persistent_audit_event OWNER TO juez;

--
-- Name: jhi_persistent_audit_evt_data; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.jhi_persistent_audit_evt_data (
    event_id bigint NOT NULL,
    name character varying(150) NOT NULL,
    value character varying(255)
);


ALTER TABLE public.jhi_persistent_audit_evt_data OWNER TO juez;

--
-- Name: jhi_social_user_connection; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.jhi_social_user_connection (
    id bigint NOT NULL,
    user_id character varying(255) NOT NULL,
    provider_id character varying(255) NOT NULL,
    provider_user_id character varying(255) NOT NULL,
    rank bigint NOT NULL,
    display_name character varying(255),
    profile_url character varying(255),
    image_url character varying(255),
    access_token character varying(255) NOT NULL,
    secret character varying(255),
    refresh_token character varying(255),
    expire_time bigint
);


ALTER TABLE public.jhi_social_user_connection OWNER TO juez;

--
-- Name: jhi_user; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.jhi_user (
    id bigint NOT NULL,
    login character varying(100) NOT NULL,
    password_hash character varying(60),
    first_name character varying(50),
    last_name character varying(50),
    email character varying(100),
    image_url character varying(256),
    activated boolean NOT NULL,
    lang_key character varying(5),
    activation_key character varying(20),
    reset_key character varying(20),
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone NOT NULL,
    reset_date timestamp without time zone,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone
);


ALTER TABLE public.jhi_user OWNER TO juez;

--
-- Name: jhi_user_authority; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.jhi_user_authority (
    user_id bigint NOT NULL,
    authority_name character varying(50) NOT NULL
);


ALTER TABLE public.jhi_user_authority OWNER TO juez;

--
-- Name: problem; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.problem (
    id bigint NOT NULL,
    name character varying(255),
    active boolean,
    timelimit integer,
    jhi_level integer,
    solution_id bigint,
    creator_id bigint
);


ALTER TABLE public.problem OWNER TO juez;

--
-- Name: submission; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.submission (
    id bigint NOT NULL,
    status character varying(255),
    runtime integer,
    language character varying(255),
    dateupload timestamp without time zone,
    submitter_id bigint,
    problem_id bigint
);


ALTER TABLE public.submission OWNER TO juez;

--
-- Name: test_case; Type: TABLE; Schema: public; Owner: juez
--

CREATE TABLE public.test_case (
    id bigint NOT NULL,
    inputfl character varying(3000),
    outputfl character varying(3000),
    jhi_show boolean,
    problem_id bigint
);


ALTER TABLE public.test_case OWNER TO juez;

--
-- Data for Name: code; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.code (id, name, path, dateupload, submission_id) FROM stdin;
1701	Main.java	/code/jorge/1401/java/2018_06_11_14_38_06/Main.java	2018-06-11 14:38:06.33	1751
1702	1401.cpp	/code/user1/1401/cpp/2018_06_11_14_41_11/1401.cpp	2018-06-11 14:41:11.027	1752
1703	1401.cpp	/code/user2/1401/cpp/2018_06_11_14_41_29/1401.cpp	2018-06-11 14:41:29.961	1753
1704	1401.cpp	/code/user5/1401/cpp/2018_06_11_14_42_37/1401.cpp	2018-06-11 14:42:37.035	1754
1705	1401.cpp	/code/user/1401/cpp/2018_06_11_14_45_05/1401.cpp	2018-06-11 14:45:05.906	1755
1706	1401.cpp	/code/user5/1401/cpp/2018_06_11_14_48_32/1401.cpp	2018-06-11 14:48:32.615	1756
1707	1401.cpp	/code/user/1401/cpp/2018_06_11_14_48_47/1401.cpp	2018-06-11 14:48:47.875	1757
1708	951.cpp	/code/user/951/cpp/2018_06_11_14_49_27/951.cpp	2018-06-11 14:49:27.377	1758
1709	951.cpp	/code/user/951/cpp/2018_06_11_14_49_27/951.cpp	2018-06-11 14:49:27.515	1759
1710	951.cpp	/code/jorge/951/cpp/2018_06_11_14_49_52/951.cpp	2018-06-11 14:49:52.283	1760
1711	Main.java	/code/user2/953/java/2018_06_11_14_51_20/Main.java	2018-06-11 14:51:20.36	1761
1712	953.cpp	/code/jorge/953/cpp/2018_06_11_14_51_50/953.cpp	2018-06-11 14:51:50.591	1762
1713	Main.java	/code/jorge/953/java/2018_06_11_14_52_03/Main.java	2018-06-11 14:52:03.477	1763
1714	Main.java	/code/jorge/953/java/2018_06_11_14_52_21/Main.java	2018-06-11 14:52:21.134	1764
1715	1401.cpp	/code/admin/1401/cpp/2018_06_11_14_52_56/1401.cpp	2018-06-11 14:52:56.678	1765
1716	1401.cpp	/code/user1/1401/cpp/2018_06_11_14_54_13/1401.cpp	2018-06-11 14:54:13.16	1766
1717	953.cpp	/code/user1/953/cpp/2018_06_11_14_55_04/953.cpp	2018-06-11 14:55:04.175	1767
1718	953.cpp	/code/user1/953/cpp/2018_06_11_14_55_30/953.cpp	2018-06-11 14:55:30.134	1768
1719	1801.cpp	/code/admin/1801/cpp/2018_06_11_15_09_29/1801.cpp	2018-06-11 15:09:29.265	1769
1720	1801.cpp	/code/admin/1801/cpp/2018_06_11_15_10_07/1801.cpp	2018-06-11 15:10:07.996	1770
1721	1801.cpp	/code/jorge/1801/cpp/2018_06_11_15_10_27/1801.cpp	2018-06-11 15:10:27.453	1771
1722	1801.cpp	/code/jorge/1801/cpp/2018_06_11_15_10_44/1801.cpp	2018-06-11 15:10:44.466	1772
1723	1801.cpp	/code/jorge/1801/cpp/2018_06_11_15_10_56/1801.cpp	2018-06-11 15:10:56.836	1773
1724	Main.java	/code/admin/1801/java/2018_06_11_15_11_25/Main.java	2018-06-11 15:11:25.273	1774
1725	Main.java	/code/admin/1801/java/2018_06_11_15_11_43/Main.java	2018-06-11 15:11:43.976	1775
1726	Main.java	/code/jorge/1801/java/2018_06_11_15_12_08/Main.java	2018-06-11 15:12:08.599	1776
\.


--
-- Data for Name: coder; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.coder (id, ranking, user_id) FROM stdin;
1201	\N	1101
1202	\N	1102
1203	\N	1103
1204	\N	3
1651	\N	4
1652	\N	1601
\.


--
-- Data for Name: contest; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.contest (id, name, jhi_password, startdate, enddate, jhi_type, active, creator_id) FROM stdin;
1052	borrar		2018-06-10 18:33:23.972	2018-06-10 18:33:23.972	publico	f	3
1502	Nuevo contes de prueba		2018-06-10 22:00:52.127	2018-06-11 19:25:52	publico	t	3
1503	Nueva prueba		2018-06-10 22:05:39.088	2018-06-11 11:00:39	publico	t	3
1513	pliease	123	2018-06-10 22:00:53.463	2018-06-10 22:13:53.463	privado	t	3
1505	Prueba 2 		2018-06-10 22:02:43.283	2018-06-10 22:02:43.283	publico	f	3
1506	prueba 3 		2018-06-11 08:00:54	2018-06-11 11:00:54	publico	f	3
1507	prueba 3 		2018-06-11 00:30:39	2018-06-11 05:30:39	publico	f	3
1508	prueba 5 		2018-06-10 21:40:11.042	2018-06-10 22:00:11.042	publico	f	3
1509	prueba 6		2018-06-11 07:12:06	2018-06-11 07:12:06	publico	f	3
1510	prueba 7		2018-06-10 22:15:50.664	2018-06-10 22:15:50.664	publico	f	3
1511	prueba 8 		2018-06-11 09:30:11.013	2018-06-11 09:30:11.013	publico	f	3
1501	Contest de prueba 	123	2018-06-10 21:30:16.727	2018-06-10 22:00:16.727	privado	t	3
1512	prueba 10		2018-06-10 22:25:25.788	2018-06-10 22:11:25.788	publico	f	3
1504	nueva prueba 		2018-06-11 14:10:36	2018-06-11 17:00:00	publico	t	3
\.


--
-- Data for Name: contest_coder; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.contest_coder (coders_id, contests_id) FROM stdin;
1201	1052
1202	1052
1203	1052
1204	1052
1204	1501
1204	1504
1202	1504
1651	1504
1203	1504
1652	1504
1201	1504
\.


--
-- Data for Name: contest_problem; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.contest_problem (problems_id, contests_id) FROM stdin;
951	1052
952	1052
953	1052
954	1052
956	1052
952	1501
1401	1501
953	1501
952	1502
953	1502
951	1503
953	1503
951	1504
1401	1504
952	1505
953	1505
951	1506
953	1506
1401	1506
952	1507
1401	1507
952	1508
953	1508
952	1509
1401	1509
952	1510
1401	1510
952	1511
1401	1511
953	1512
1401	1512
951	1513
952	1513
953	1504
\.


--
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) FROM stdin;
00000000000000	jhipster	config/liquibase/changelog/00000000000000_initial_schema.xml	2018-06-10 17:19:11.653788	1	EXECUTED	7:a6235f40597a13436aa36c6d61db2269	createSequence sequenceName=hibernate_sequence		\N	3.5.3	\N	\N	8665551362
00000000000001	jhipster	config/liquibase/changelog/00000000000000_initial_schema.xml	2018-06-10 17:19:12.433311	2	EXECUTED	7:280b048098ab3f023d431e10518ff9e2	createTable tableName=jhi_user; createIndex indexName=idx_user_login, tableName=jhi_user; createIndex indexName=idx_user_email, tableName=jhi_user; createTable tableName=jhi_authority; createTable tableName=jhi_user_authority; addPrimaryKey tableN...		\N	3.5.3	\N	\N	8665551362
20180406140137-1	jhipster	config/liquibase/changelog/20180406140137_added_entity_Contest.xml	2018-06-10 17:19:12.642481	3	EXECUTED	7:f620d7364b8fcde8f94a2e4caa424acf	createTable tableName=contest; dropDefaultValue columnName=startdate, tableName=contest; dropDefaultValue columnName=enddate, tableName=contest; createTable tableName=contest_problem; addPrimaryKey tableName=contest_problem; createTable tableName=...		\N	3.5.3	\N	\N	8665551362
20180406140138-1	jhipster	config/liquibase/changelog/20180406140138_added_entity_Problem.xml	2018-06-10 17:19:12.762307	4	EXECUTED	7:6400766c1c345e7566c016295ff39977	createTable tableName=problem		\N	3.5.3	\N	\N	8665551362
20180406140139-1	jhipster	config/liquibase/changelog/20180406140139_added_entity_Submission.xml	2018-06-10 17:19:12.873364	5	EXECUTED	7:23fe40e80c92f4f0360674c3d3c0955f	createTable tableName=submission; dropDefaultValue columnName=dateupload, tableName=submission		\N	3.5.3	\N	\N	8665551362
20180406140140-1	jhipster	config/liquibase/changelog/20180406140140_added_entity_Code.xml	2018-06-10 17:19:13.006977	6	EXECUTED	7:3a0ddb521198ee3b2a006834cd924e51	createTable tableName=code; dropDefaultValue columnName=dateupload, tableName=code		\N	3.5.3	\N	\N	8665551362
20180406140141-1	jhipster	config/liquibase/changelog/20180406140141_added_entity_TestCase.xml	2018-06-10 17:19:13.119871	7	EXECUTED	7:d42de38f03904ae036e60d409b6561e6	createTable tableName=test_case		\N	3.5.3	\N	\N	8665551362
20180406140142-1	jhipster	config/liquibase/changelog/20180406140142_added_entity_Coder.xml	2018-06-10 17:19:13.218275	8	EXECUTED	7:acddfd883ffe6ce4d10ce97da04d9ab2	createTable tableName=coder		\N	3.5.3	\N	\N	8665551362
20180406140137-2	jhipster	config/liquibase/changelog/20180406140137_added_entity_constraints_Contest.xml	2018-06-10 17:19:13.276599	9	EXECUTED	7:89d9db6fa5f2f60e42bee154f267b6c8	addForeignKeyConstraint baseTableName=contest, constraintName=fk_contest_creator_id, referencedTableName=jhi_user; addForeignKeyConstraint baseTableName=contest_problem, constraintName=fk_contest_problem_contests_id, referencedTableName=contest; a...		\N	3.5.3	\N	\N	8665551362
20180406140138-2	jhipster	config/liquibase/changelog/20180406140138_added_entity_constraints_Problem.xml	2018-06-10 17:19:13.297428	10	EXECUTED	7:193de15ee18435d9db1e221d6eb7c80f	addForeignKeyConstraint baseTableName=problem, constraintName=fk_problem_solution_id, referencedTableName=code; addForeignKeyConstraint baseTableName=problem, constraintName=fk_problem_creator_id, referencedTableName=jhi_user		\N	3.5.3	\N	\N	8665551362
20180406140140-2	jhipster	config/liquibase/changelog/20180406140140_added_entity_constraints_Code.xml	2018-06-10 17:19:13.319307	11	EXECUTED	7:98456495e131d9b606042c357cb0350c	addForeignKeyConstraint baseTableName=code, constraintName=fk_code_submission_id, referencedTableName=submission		\N	3.5.3	\N	\N	8665551362
20180406140142-2	jhipster	config/liquibase/changelog/20180406140142_added_entity_constraints_Coder.xml	2018-06-10 17:19:13.341273	12	EXECUTED	7:19ad25d84c70dea2d6fc94ed720a9c6d	addForeignKeyConstraint baseTableName=coder, constraintName=fk_coder_user_id, referencedTableName=jhi_user		\N	3.5.3	\N	\N	8665551362
20180406140139-2	jhipster	config/liquibase/changelog/20180406140139_added_entity_constraints_Submission.xml	2018-06-10 17:19:13.363471	13	EXECUTED	7:370a111d98a03013ad702de4961f21c2	addForeignKeyConstraint baseTableName=submission, constraintName=fk_submission_submitter_id, referencedTableName=jhi_user; addForeignKeyConstraint baseTableName=submission, constraintName=fk_submission_problem_id, referencedTableName=problem		\N	3.5.3	\N	\N	8665551362
20180406140141-2	jhipster	config/liquibase/changelog/20180406140141_added_entity_constraints_TestCase.xml	2018-06-10 17:19:13.385967	14	EXECUTED	7:904c9bb764a24eab902146110413ec43	addForeignKeyConstraint baseTableName=test_case, constraintName=fk_test_case_problem_id, referencedTableName=problem		\N	3.5.3	\N	\N	8665551362
\.


--
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.databasechangeloglock (id, locked, lockgranted, lockedby) FROM stdin;
1	f	\N	\N
\.


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: juez
--

SELECT pg_catalog.setval('public.hibernate_sequence', 2350, true);


--
-- Data for Name: jhi_authority; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.jhi_authority (name) FROM stdin;
ROLE_ADMIN
ROLE_USER
ROLE_TEACHER
\.


--
-- Data for Name: jhi_persistent_audit_event; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.jhi_persistent_audit_event (event_id, principal, event_date, event_type) FROM stdin;
1151	jorge	2018-06-10 18:11:39.312	AUTHENTICATION_SUCCESS
1152	user1	2018-06-10 18:11:48.737	AUTHENTICATION_SUCCESS
1153	user1	2018-06-10 18:12:53.855	AUTHENTICATION_SUCCESS
1154	user2	2018-06-10 18:13:43.165	AUTHENTICATION_SUCCESS
1155	admin	2018-06-10 18:18:14.68	AUTHENTICATION_SUCCESS
1251	admin	2018-06-10 20:35:01.003	AUTHENTICATION_SUCCESS
1551	admin	2018-06-11 14:30:37.98	AUTHENTICATION_SUCCESS
1552	jorge	2018-06-11 14:32:42.675	AUTHENTICATION_SUCCESS
1553	user1	2018-06-11 14:32:52.811	AUTHENTICATION_SUCCESS
1554	user2	2018-06-11 14:33:02.93	AUTHENTICATION_SUCCESS
1555	user3	2018-06-11 14:33:10.005	AUTHENTICATION_FAILURE
1556	user4	2018-06-11 14:33:31.272	AUTHENTICATION_FAILURE
1557	user5	2018-06-11 14:35:38.854	AUTHENTICATION_SUCCESS
1558	user	2018-06-11 14:35:51.942	AUTHENTICATION_SUCCESS
1901	admin	2018-06-11 18:53:19.426	AUTHENTICATION_SUCCESS
1902	admin	2018-06-11 19:26:21.302	AUTHENTICATION_SUCCESS
2051	admin	2018-07-09 23:05:09.946	AUTHENTICATION_SUCCESS
2101	admin	2018-08-04 18:14:05.234	AUTHENTICATION_SUCCESS
2151	admin	2018-08-05 10:28:15.812	AUTHENTICATION_SUCCESS
2201	user	2018-08-05 15:48:42.481	AUTHENTICATION_SUCCESS
2202	user	2018-08-05 15:48:42.482	AUTHENTICATION_SUCCESS
2251	user	2018-08-08 18:07:47.33	AUTHENTICATION_SUCCESS
2301	user	2018-08-11 11:18:03.839	AUTHENTICATION_SUCCESS
\.


--
-- Data for Name: jhi_persistent_audit_evt_data; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.jhi_persistent_audit_evt_data (event_id, name, value) FROM stdin;
1555	type	org.springframework.security.authentication.BadCredentialsException
1555	message	Bad credentials
1556	type	org.springframework.security.authentication.BadCredentialsException
1556	message	Bad credentials
\.


--
-- Data for Name: jhi_social_user_connection; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.jhi_social_user_connection (id, user_id, provider_id, provider_user_id, rank, display_name, profile_url, image_url, access_token, secret, refresh_token, expire_time) FROM stdin;
\.


--
-- Data for Name: jhi_user; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.jhi_user (id, login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created_date, reset_date, last_modified_by, last_modified_date) FROM stdin;
1	system	$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG	System	System	system@localhost		t	en	\N	\N	system	2018-06-10 17:19:11.694799	\N	system	\N
2	anonymoususer	$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO	Anonymous	User	anonymous@localhost		t	en	\N	\N	system	2018-06-10 17:19:11.694799	\N	system	\N
3	admin	$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC	Administrator	Administrator	admin@localhost		t	en	\N	\N	system	2018-06-10 17:19:11.694799	\N	system	\N
4	user	$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K	User	User	user@localhost		t	en	\N	\N	system	2018-06-10 17:19:11.694799	\N	system	\N
1101	jorge	$2a$10$EuuP47JVm.bFNEyuPSOPq.3q6JSWjSNWLr68gSGZHKxfDKg0qQaDW	jorge	jorge	jorge@gmail.com	/	t	en	70936636681315042101	\N	anonymousUser	2018-06-10 17:55:15.198	\N	admin	2018-06-10 18:11:31.361
1102	user1	$2a$10$vdjNh0Sc0BcSmvP6sdZtr.lI4EoMivNgPg24W0l9WHHtwwEfDVPz.	user	user	user@user.com	/	t	en	72751278211297836445	\N	anonymousUser	2018-06-10 17:55:52.971	\N	admin	2018-06-10 18:11:32.052
1103	user2	$2a$10$ESjiSvqxLmpcdUQuQFfasensXieyEiBXM461igTEnttnUvriMAcum	user2	user	user2@gmail.com	/	t	en	45684467688628472865	\N	anonymousUser	2018-06-10 17:56:29.007	\N	admin	2018-06-10 18:11:33.414
1601	user5	$2a$10$urGTEsmAFYv/ZUlOyi/Kv.rUNbarMu..NPR5IVi7QGNix8qGLRRCy	user5	user	user5@user5.com	/	t	en	15587248519555984462	\N	user2	2018-06-11 14:35:04.256	\N	admin	2018-06-11 14:35:30.087
\.


--
-- Data for Name: jhi_user_authority; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.jhi_user_authority (user_id, authority_name) FROM stdin;
1	ROLE_ADMIN
1	ROLE_USER
3	ROLE_ADMIN
3	ROLE_USER
4	ROLE_USER
1101	ROLE_USER
1102	ROLE_USER
1103	ROLE_USER
1601	ROLE_USER
\.


--
-- Data for Name: problem; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.problem (id, name, active, timelimit, jhi_level, solution_id, creator_id) FROM stdin;
951	Concatenando cadenas	t	6	1	\N	3
952	Izquierda o derecha	t	6	1	\N	3
953	El teatro cuadrado 	t	6	1	\N	3
956	pa borrar 2 	f	4	3	\N	3
954	pa probando update 	f	6	1	\N	3
1301	Nuevo	f	4	1	\N	3
1401	Viaje barato	t	1	2	\N	3
1801	Grafo bipartito	t	3	4	\N	3
1951	Nombre de prueba	t	5	2	\N	3
\.


--
-- Data for Name: submission; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.submission (id, status, runtime, language, dateupload, submitter_id, problem_id) FROM stdin;
1751	COMPILATION_ERROR	\N	JAVA	2018-06-11 14:38:07.725	1101	1401
1752	WRONG_ANSWER	\N	C	2018-06-11 14:41:12.606	1102	1401
1753	WRONG_ANSWER	\N	C	2018-06-11 14:41:30.805	1103	1401
1754	WRONG_ANSWER	\N	C	2018-06-11 14:42:37.287	1601	1401
1755	WRONG_ANSWER	\N	C	2018-06-11 14:45:06.098	4	1401
1756	ACCEPTED	\N	C	2018-06-11 14:48:34.248	1601	1401
1757	ACCEPTED	\N	C	2018-06-11 14:48:49.481	4	1401
1758	RUN_TIME_ERROR	\N	C	2018-06-11 14:49:30.108	4	951
1759	ACCEPTED	\N	C	2018-06-11 14:49:41.356	4	951
1760	ACCEPTED	\N	C	2018-06-11 14:50:05.098	1101	951
1761	ACCEPTED	\N	JAVA	2018-06-11 14:51:25.512	1103	953
1762	COMPILATION_ERROR	\N	C	2018-06-11 14:51:50.638	1101	953
1763	COMPILATION_ERROR	\N	JAVA	2018-06-11 14:52:04.317	1101	953
1764	ACCEPTED	\N	JAVA	2018-06-11 14:52:26.046	1101	953
1765	ACCEPTED	\N	C	2018-06-11 14:52:58.308	3	1401
1766	ACCEPTED	\N	C	2018-06-11 14:54:14.719	1102	1401
1767	COMPILATION_ERROR	\N	C	2018-06-11 14:55:04.239	1102	953
1768	ACCEPTED	\N	C	2018-06-11 14:55:40.1	1102	953
1769	ACCEPTED	\N	C	2018-06-11 15:09:32.133	3	1801
1770	COMPILATION_ERROR	\N	C	2018-06-11 15:10:08.213	3	1801
1771	RUN_TIME_ERROR	\N	C	2018-06-11 15:10:28.533	1101	1801
1772	TIME_LIMIT	\N	C	2018-06-11 15:10:47.889	1101	1801
1773	COMPILATION_ERROR	\N	C	2018-06-11 15:10:57.086	1101	1801
1774	ACCEPTED	\N	JAVA	2018-06-11 15:11:28.518	3	1801
1775	RUN_TIME_ERROR	\N	JAVA	2018-06-11 15:11:45.182	3	1801
1776	COMPILATION_ERROR	\N	JAVA	2018-06-11 15:12:09.128	1101	1801
\.


--
-- Data for Name: test_case; Type: TABLE DATA; Schema: public; Owner: juez
--

COPY public.test_case (id, inputfl, outputfl, jhi_show, problem_id) FROM stdin;
1001	input0.txt	output0.txt	t	951
1002	input1.txt	output1.txt	t	951
1003	input2.txt	output2.txt	t	951
1004	input3.txt	output3.txt	f	951
1005	input4.txt	output4.txt	f	951
1006	input0.txt	output0.txt	t	952
1007	input1.txt	output1.txt	f	952
1008	input2.txt	output2.txt	f	952
1009	input0.txt	output0.txt	t	953
1010	input1.txt	output1.txt	t	953
1011	input2.txt	output2.txt	f	953
1012	input3.txt	output3.txt	f	953
1013	input0.txt	output0.txt	t	954
1014	input1.txt	output1.txt	f	954
1015	input0.txt	output0.txt	t	956
1016	input1.txt	output1.txt	f	956
1351	input0.txt	output0.txt	f	1301
1352	input1.txt	output1.txt	t	1301
1451	input0.txt	output0.txt	t	1401
1452	input1.txt	output1.txt	f	1401
1851	input0.txt	output0.txt	t	1801
1852	input1.txt	output1.txt	f	1801
1853	input2.txt	output2.txt	f	1801
2001	input0.txt	output0.txt	t	1951
2002	input1.txt	output1.txt	f	1951
\.


--
-- Name: code_submission_id_key; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.code
    ADD CONSTRAINT code_submission_id_key UNIQUE (submission_id);


--
-- Name: coder_user_id_key; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.coder
    ADD CONSTRAINT coder_user_id_key UNIQUE (user_id);


--
-- Name: contest_coder_pkey; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.contest_coder
    ADD CONSTRAINT contest_coder_pkey PRIMARY KEY (contests_id, coders_id);


--
-- Name: contest_problem_pkey; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.contest_problem
    ADD CONSTRAINT contest_problem_pkey PRIMARY KEY (contests_id, problems_id);


--
-- Name: jhi_persistent_audit_evt_data_pkey; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_persistent_audit_evt_data
    ADD CONSTRAINT jhi_persistent_audit_evt_data_pkey PRIMARY KEY (event_id, name);


--
-- Name: jhi_social_user_connection_user_id_provider_id_provider_use_key; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_social_user_connection
    ADD CONSTRAINT jhi_social_user_connection_user_id_provider_id_provider_use_key UNIQUE (user_id, provider_id, provider_user_id);


--
-- Name: jhi_social_user_connection_user_id_provider_id_rank_key; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_social_user_connection
    ADD CONSTRAINT jhi_social_user_connection_user_id_provider_id_rank_key UNIQUE (user_id, provider_id, rank);


--
-- Name: jhi_user_authority_pkey; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT jhi_user_authority_pkey PRIMARY KEY (user_id, authority_name);


--
-- Name: jhi_user_email_key; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT jhi_user_email_key UNIQUE (email);


--
-- Name: jhi_user_login_key; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT jhi_user_login_key UNIQUE (login);


--
-- Name: pk_code; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.code
    ADD CONSTRAINT pk_code PRIMARY KEY (id);


--
-- Name: pk_coder; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.coder
    ADD CONSTRAINT pk_coder PRIMARY KEY (id);


--
-- Name: pk_contest; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.contest
    ADD CONSTRAINT pk_contest PRIMARY KEY (id);


--
-- Name: pk_databasechangeloglock; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT pk_databasechangeloglock PRIMARY KEY (id);


--
-- Name: pk_jhi_authority; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_authority
    ADD CONSTRAINT pk_jhi_authority PRIMARY KEY (name);


--
-- Name: pk_jhi_persistent_audit_event; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_persistent_audit_event
    ADD CONSTRAINT pk_jhi_persistent_audit_event PRIMARY KEY (event_id);


--
-- Name: pk_jhi_social_user_connection; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_social_user_connection
    ADD CONSTRAINT pk_jhi_social_user_connection PRIMARY KEY (id);


--
-- Name: pk_jhi_user; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT pk_jhi_user PRIMARY KEY (id);


--
-- Name: pk_problem; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.problem
    ADD CONSTRAINT pk_problem PRIMARY KEY (id);


--
-- Name: pk_submission; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.submission
    ADD CONSTRAINT pk_submission PRIMARY KEY (id);


--
-- Name: pk_test_case; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.test_case
    ADD CONSTRAINT pk_test_case PRIMARY KEY (id);


--
-- Name: problem_solution_id_key; Type: CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.problem
    ADD CONSTRAINT problem_solution_id_key UNIQUE (solution_id);


--
-- Name: idx_persistent_audit_event; Type: INDEX; Schema: public; Owner: juez
--

CREATE INDEX idx_persistent_audit_event ON public.jhi_persistent_audit_event USING btree (principal, event_date);


--
-- Name: idx_persistent_audit_evt_data; Type: INDEX; Schema: public; Owner: juez
--

CREATE INDEX idx_persistent_audit_evt_data ON public.jhi_persistent_audit_evt_data USING btree (event_id);


--
-- Name: idx_user_email; Type: INDEX; Schema: public; Owner: juez
--

CREATE UNIQUE INDEX idx_user_email ON public.jhi_user USING btree (email);


--
-- Name: idx_user_login; Type: INDEX; Schema: public; Owner: juez
--

CREATE UNIQUE INDEX idx_user_login ON public.jhi_user USING btree (login);


--
-- Name: fk_authority_name; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT fk_authority_name FOREIGN KEY (authority_name) REFERENCES public.jhi_authority(name);


--
-- Name: fk_code_submission_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.code
    ADD CONSTRAINT fk_code_submission_id FOREIGN KEY (submission_id) REFERENCES public.submission(id);


--
-- Name: fk_coder_user_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.coder
    ADD CONSTRAINT fk_coder_user_id FOREIGN KEY (user_id) REFERENCES public.jhi_user(id);


--
-- Name: fk_contest_coder_coders_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.contest_coder
    ADD CONSTRAINT fk_contest_coder_coders_id FOREIGN KEY (coders_id) REFERENCES public.coder(id);


--
-- Name: fk_contest_coder_contests_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.contest_coder
    ADD CONSTRAINT fk_contest_coder_contests_id FOREIGN KEY (contests_id) REFERENCES public.contest(id);


--
-- Name: fk_contest_creator_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.contest
    ADD CONSTRAINT fk_contest_creator_id FOREIGN KEY (creator_id) REFERENCES public.jhi_user(id);


--
-- Name: fk_contest_problem_contests_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.contest_problem
    ADD CONSTRAINT fk_contest_problem_contests_id FOREIGN KEY (contests_id) REFERENCES public.contest(id);


--
-- Name: fk_contest_problem_problems_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.contest_problem
    ADD CONSTRAINT fk_contest_problem_problems_id FOREIGN KEY (problems_id) REFERENCES public.problem(id);


--
-- Name: fk_evt_pers_audit_evt_data; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_persistent_audit_evt_data
    ADD CONSTRAINT fk_evt_pers_audit_evt_data FOREIGN KEY (event_id) REFERENCES public.jhi_persistent_audit_event(event_id);


--
-- Name: fk_problem_creator_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.problem
    ADD CONSTRAINT fk_problem_creator_id FOREIGN KEY (creator_id) REFERENCES public.jhi_user(id);


--
-- Name: fk_problem_solution_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.problem
    ADD CONSTRAINT fk_problem_solution_id FOREIGN KEY (solution_id) REFERENCES public.code(id);


--
-- Name: fk_submission_problem_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.submission
    ADD CONSTRAINT fk_submission_problem_id FOREIGN KEY (problem_id) REFERENCES public.problem(id);


--
-- Name: fk_submission_submitter_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.submission
    ADD CONSTRAINT fk_submission_submitter_id FOREIGN KEY (submitter_id) REFERENCES public.jhi_user(id);


--
-- Name: fk_test_case_problem_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.test_case
    ADD CONSTRAINT fk_test_case_problem_id FOREIGN KEY (problem_id) REFERENCES public.problem(id);


--
-- Name: fk_user_id; Type: FK CONSTRAINT; Schema: public; Owner: juez
--

ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.jhi_user(id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

