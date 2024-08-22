DROP TABLE IF EXISTS theatre_show;
DROP TABLE IF EXISTS seat;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS customer;

CREATE TABLE theatre_show (
	id varchar(256),
	name varchar(256),
	venue varchar(256),
	start_time datetime,
	duration_in_mins int,
	PRIMARY KEY(id)
);

CREATE TABLE customer (
	id varchar(256),
	name varchar(256),
	email varchar(256),
	tier int,
	PRIMARY KEY(id)
);

CREATE TABLE reservation (
	id varchar(256),
	customer_id varchar(256),
	total_price decimal(10,2),
	reservation_time datetime,
	PRIMARY KEY(id),
	FOREIGN KEY(customer_id) REFERENCES customer(id)
);

CREATE TABLE seat (
	show_id varchar(256),
	seat_id varchar(256),
	region varchar(256),
	status varchar(256),
	price decimal(6,2),
	reservation_id varchar(256),
	PRIMARY KEY(show_id, seat_id),
	FOREIGN KEY(reservation_id) REFERENCES reservation(id)
);

