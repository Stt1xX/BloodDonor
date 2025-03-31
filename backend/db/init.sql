SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE FUNCTION public.update_blood_reserves() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO blood_reserves (bank_id, blood_group, rh_factor, total_quantity)
        VALUES (NEW.blood_bank_id, NEW.blood_group, NEW.rh_factor, NEW.volume)
        ON CONFLICT (bank_id, blood_group, rh_factor)
        DO UPDATE SET total_quantity = blood_reserves.total_quantity + NEW.volume;

ELSIF TG_OP = 'DELETE' THEN
UPDATE blood_reserves
SET total_quantity = GREATEST(0, total_quantity - OLD.volume)
WHERE bank_id = OLD.blood_bank_id
  AND blood_group = OLD.blood_group
  AND rh_factor = OLD.rh_factor;

ELSIF TG_OP = 'UPDATE' THEN
UPDATE blood_reserves
SET total_quantity = GREATEST(0, total_quantity - OLD.volume + NEW.volume)
WHERE bank_id = OLD.blood_bank_id
  AND blood_group = OLD.blood_group
  AND rh_factor = OLD.rh_factor;
END IF;

RETURN NEW;
END;
$$;


ALTER FUNCTION public.update_blood_reserves() OWNER TO admin;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: blood_requests; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.blood_requests (
                                       is_emergency boolean,
                                       volume_needed double precision,
                                       created_at timestamp(6) without time zone,
                                       creator_id bigint,
                                       id serial NOT NULL,
                                       medical_institution_id bigint,
                                       updated_at timestamp(6) without time zone,
                                       blood_group character varying(255),
                                       description character varying(255),
                                       rh_factor character varying(255),
                                       CONSTRAINT blood_requests_blood_group_check CHECK (((blood_group)::text = ANY ((ARRAY['A'::character varying, 'B'::character varying, 'AB'::character varying, 'O'::character varying])::text[]))),
    CONSTRAINT blood_requests_rh_factor_check CHECK (((rh_factor)::text = ANY ((ARRAY['POSITIVE'::character varying, 'NEGATIVE'::character varying])::text[])))
);


ALTER TABLE public.blood_requests OWNER TO admin;


--
-- Name: blood_reserves; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.blood_reserves (
                                       total_quantity double precision,
                                       bank_id bigint NOT NULL,
                                       blood_group character varying(255) NOT NULL,
                                       rh_factor character varying(255) NOT NULL
);


ALTER TABLE public.blood_reserves OWNER TO admin;

--
-- Name: blood_units; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.blood_units (
                                    created_at date,
                                    expiration_date date,
                                    volume double precision,
                                    blood_bank_id bigint,
                                    id serial NOT NULL,
                                    blood_group character varying(255),
                                    rh_factor character varying(255)
);


ALTER TABLE public.blood_units OWNER TO admin;


--
-- Name: organizations; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.organizations (
                                      hours_from integer,
                                      hours_to integer,
                                      minutes_from integer,
                                      minutes_to integer,
                                      id serial NOT NULL,
                                      address character varying(255),
                                      name character varying(255),
                                      phone character varying(255),
                                      type character varying(255),
                                      CONSTRAINT organizations_type_check CHECK (((type)::text = ANY ((ARRAY['BLOOD_BANK'::character varying, 'MEDICAL_INSTITUTION'::character varying])::text[])))
);


ALTER TABLE public.organizations OWNER TO admin;

--
-- Name: registration_requests; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.registration_requests (
                                              created_at timestamp(6) without time zone,
                                              id serial NOT NULL,
                                              organization_id bigint,
                                              email character varying(255),
                                              name character varying(255),
                                              password character varying(255),
                                              post character varying(255),
                                              role character varying(255),
                                              surname character varying(255),
                                              CONSTRAINT registration_requests_role_check CHECK (((role)::text = ANY ((ARRAY['ADMIN'::character varying, 'MEDICAL_EMPLOYEE'::character varying, 'BANK_EMPLOYEE'::character varying])::text[])))
);


ALTER TABLE public.registration_requests OWNER TO admin;

--
-- Name: requests_to_bank; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.requests_to_bank (
                                         banker_id bigint,
                                         blood_bank_id bigint,
                                         created_at timestamp(6) without time zone,
                                         id serial NOT NULL,
                                         request_id bigint,
                                         updated_at timestamp(6) without time zone,
                                         rejection_reason character varying(255),
                                         status character varying(255),
                                         CONSTRAINT requests_to_bank_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'REJECTED'::character varying, 'DELETED'::character varying, 'COMPLETED'::character varying])::text[])))
);


ALTER TABLE public.requests_to_bank OWNER TO admin;

--
-- Name: users; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.users (
                              is_deleted boolean,
                              id serial NOT NULL,
                              organization_id bigint,
                              email character varying(255),
                              name character varying(255),
                              password character varying(255),
                              post character varying(255),
                              role character varying(255),
                              surname character varying(255),
                              CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['ADMIN'::character varying, 'MEDICAL_EMPLOYEE'::character varying, 'BANK_EMPLOYEE'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO admin;

--
-- Name: blood_requests blood_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.blood_requests
    ADD CONSTRAINT blood_requests_pkey PRIMARY KEY (id);


--
-- Name: blood_reserves blood_reserves_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.blood_reserves
    ADD CONSTRAINT blood_reserves_pkey PRIMARY KEY (bank_id, blood_group, rh_factor);


--
-- Name: blood_units blood_units_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.blood_units
    ADD CONSTRAINT blood_units_pkey PRIMARY KEY (id);


--
-- Name: organizations organizations_name_key; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.organizations
    ADD CONSTRAINT organizations_name_key UNIQUE (name);


--
-- Name: organizations organizations_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.organizations
    ADD CONSTRAINT organizations_pkey PRIMARY KEY (id);


--
-- Name: registration_requests registration_requests_organization_id_key; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.registration_requests
    ADD CONSTRAINT registration_requests_organization_id_key UNIQUE (organization_id);


--
-- Name: registration_requests registration_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.registration_requests
    ADD CONSTRAINT registration_requests_pkey PRIMARY KEY (id);


--
-- Name: requests_to_bank requests_to_bank_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.requests_to_bank
    ADD CONSTRAINT requests_to_bank_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: blood_units trg_update_inventory; Type: TRIGGER; Schema: public; Owner: admin
--

CREATE TRIGGER trg_update_inventory AFTER INSERT OR DELETE OR UPDATE ON public.blood_units FOR EACH ROW EXECUTE FUNCTION public.update_blood_reserves();

--
-- Name: requests_to_bank fk67u2lfufbnnmo9yb7h6yjfn5s; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.requests_to_bank
    ADD CONSTRAINT fk67u2lfufbnnmo9yb7h6yjfn5s FOREIGN KEY (request_id) REFERENCES public.blood_requests(id) ON DELETE CASCADE;
--
-- Name: requests_to_bank fk92br4kkab8o284q1jhjpshly1; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.requests_to_bank
    ADD CONSTRAINT fk92br4kkab8o284q1jhjpshly1 FOREIGN KEY (banker_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: blood_requests fkebryw2k7798gm0inexkpvdw8c; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.blood_requests
    ADD CONSTRAINT fkebryw2k7798gm0inexkpvdw8c FOREIGN KEY (creator_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: blood_units fkfph9xans6yjs63tvnaheti1kp; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.blood_units
    ADD CONSTRAINT fkfph9xans6yjs63tvnaheti1kp FOREIGN KEY (blood_bank_id) REFERENCES public.organizations(id) ON DELETE CASCADE;


--
-- Name: blood_reserves fkg9b5mjn6unu2iwqrvn2bq77l8; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.blood_reserves
    ADD CONSTRAINT fkg9b5mjn6unu2iwqrvn2bq77l8 FOREIGN KEY (bank_id) REFERENCES public.organizations(id);


--
-- Name: registration_requests fkgnays5txa30yj4pktfrt0w3v7; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.registration_requests
    ADD CONSTRAINT fkgnays5txa30yj4pktfrt0w3v7 FOREIGN KEY (organization_id) REFERENCES public.organizations(id) ON DELETE CASCADE;


--
-- Name: requests_to_bank fkhli9dhn14ndirl8vrrytira7a; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.requests_to_bank
    ADD CONSTRAINT fkhli9dhn14ndirl8vrrytira7a FOREIGN KEY (blood_bank_id) REFERENCES public.organizations(id) ON DELETE CASCADE;


--
-- Name: users fkqpugllwvyv37klq7ft9m8aqxk; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkqpugllwvyv37klq7ft9m8aqxk FOREIGN KEY (organization_id) REFERENCES public.organizations(id) ON DELETE CASCADE;


--
-- Name: blood_requests fkrf2xsbnrkpnmkjk48cxxaa0kq; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.blood_requests
    ADD CONSTRAINT fkrf2xsbnrkpnmkjk48cxxaa0kq FOREIGN KEY (medical_institution_id) REFERENCES public.organizations(id) ON DELETE CASCADE;



ALTER TABLE public.blood_requests OWNER TO admin;

--
-- PostgreSQL database dump complete
--

CREATE TABLE public.notifications (
                               id SERIAL PRIMARY KEY,
                               user_id BIGINT,
                               is_read BOOLEAN NOT NULL DEFAULT FALSE,
                               notification TEXT NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_notifications_user FOREIGN KEY (user_id)
                                   REFERENCES public.users (id) ON DELETE CASCADE
);

-- INDEXES

CREATE INDEX idx_blood_group_rh_total
    ON public.blood_reserves (blood_group, rh_factor, total_quantity);

CREATE INDEX idx_notifications_user_read
    ON public.notifications (user_id, is_read);


