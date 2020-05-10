insert into book (id, title) values (1, 'A year in Provence') ON CONFLICT DO NOTHING;
insert into book (id, title) values (2, 'The Magic Mountain') ON CONFLICT DO NOTHING;