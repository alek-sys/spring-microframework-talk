create table if not exists book (id serial primary key, title varchar);

truncate table book;

insert into book (title) values ('A year in Provence');
insert into book (title) values ('The Magic Mountain');