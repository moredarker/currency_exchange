CREATE TABLE currencies (
    id int NOT NULL AUTO_INCREMENT,
    code varchar(3),
    fullname varchar(40),
    sign varchar(5),
    PRIMARY KEY (id)
);

ALTER TABLE currencies ADD CONSTRAINT code UNIQUE KEY(code);

create table exchange_rates (
    id int NOT NULL auto_increment,
    base_currency_id int,
    target_currency_id int,
    rate decimal(12, 6),
    primary key (id),
    foreign key(base_currency_id) references my_db.currencies(id),
    foreign key(target_currency_id) references my_db.currencies(id),
    constraint rate unique(base_currency_id, target_currency_id)
)

