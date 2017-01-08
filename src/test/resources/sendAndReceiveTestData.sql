INSERT INTO jhi_user (id, login, password_hash , first_name, last_name ,email ,activated ,lang_key, activation_key, created_by, created_date)
VALUES('7','from','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','','User','from@localhost',true,'EN','', 'system', CURRENT_DATE);

INSERT INTO jhi_user (id, login, password_hash , first_name, last_name ,email ,activated ,lang_key, activation_key, created_by, created_date)
VALUES('8','to','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','','User','to@localhost',true,'EN','', 'system', CURRENT_DATE);

INSERT INTO totem (id, creation_latitude, creation_longitude, creation_date, created_by_id) VALUES(1, 2.0, 2.0, CURRENT_DATE, 7);
COMMIT;

