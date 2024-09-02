CREATE TABLE PRICE_HISTORY(
   id SERIAL PRIMARY KEY,
   post_id int references auto_post(id),
   before BIGINT not null,
   after BIGINT not null,
   created TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);