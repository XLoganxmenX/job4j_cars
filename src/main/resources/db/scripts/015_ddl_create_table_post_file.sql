CREATE TABLE post_file (
   id serial PRIMARY KEY,
   post_id int not null REFERENCES auto_post(id),
   file_id int not null REFERENCES files(id),
   UNIQUE (post_id, file_id)
);