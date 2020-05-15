CREATE TABLE Users (
  user_id SERIAL NOT NULL PRIMARY KEY,
  Username VARCHAR(25) NOT NULL,
  Password VARCHAR(50) NOT NULL,
  type INTEGER NOT NULL
);

CREATE TABLE Customers
(
  customer_id    SERIAL NOT NULL PRIMARY KEY,
  name      VARCHAR(50),
  surname   VARCHAR(50),
  email     VARCHAR(255),
  phone_number varchar(15),
  id VARCHAR(8)
);

CREATE TABLE Cars
(
  car_id SERIAL NOT NULL PRIMARY KEY,
  model VARCHAR(20),
  brand VARCHAR(25),
  vin VARCHAR(30),
  fuel VARCHAR(15),
  owner_id INTEGER REFERENCES Customers (customer_id)
);

CREATE TABLE Mechanics
(
  mechanic_id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(255),
  surname VARCHAR(255),
  user_id INTEGER REFERENCES Users (user_id)
);

CREATE TABLE Rewards
(
  reward_id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(255),
  surname VARCHAR(255),
  amount INTEGER,
  reason VARCHAR(255),
  mechanic_id INTEGER REFERENCES Mechanics (mechanic_id)
);

CREATE TABLE Payouts
(
  payout_id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(255),
  surname VARCHAR(255),
  amount INTEGER,
  mechanic_id INTEGER REFERENCES Mechanics (mechanic_id)
);

CREATE TABLE Repairs
(
  repair_id SERIAL NOT NULL PRIMARY KEY,
  cost numeric(1000,10),
  repair VARCHAR(255),
  start_day DATE,
  end_day DATE,
  mechanic_id INTEGER REFERENCES Mechanics (mechanic_id),
  car_id INTEGER REFERENCES Cars (car_id)
);

CREATE TABLE Components
(
  component_id SERIAL NOT NULL PRIMARY KEY,
  cost numeric(1000,10),
  car_type VARCHAR(25),
  name VARCHAR(25),
  amount INTEGER
);

CREATE TABLE Component
(
  usedcomponent_id SERIAL NOT NULL PRIMARY KEY,
  components_id INTEGER REFERENCES Components(component_id),
  repair_id INTEGER REFERENCES Repairs(repair_id)
);

INSERT INTO Users (USERNAME,Password,type) VALUES
('admin','admin',1);

SELECT username,Password FROM Users WHERE username = 'admin' AND password ='admin';


SELECT * FROM Users;

SELECT * FROM Mechanics;

SELECT * FROM Cars;

SELECT * FROM Repairs;

SELECT * FROM Customers ORDER BY 1;

SELECT * FROM Components;

delete from Repairs where repair_id = 2;
delete from Component where repair_id = 2;

SELECT name,surname,id,customer_id FROM Customers WHERE name like '%' AND surname like '%';

SELECT model,brand,vin FROM Cars WHERE model like '%' AND brand like'%' AND vin like '%%';

DELETE FROM Cars;

SELECT * FROM Repairs;

------fake data

insert into Customers (
    name,surname,email,phone_number,id
)
select
    left(md5(i::name), 10),
    left(md5(i::name), 10),
    md5(random()::name),
    (random() * 10)::int,
    left(md5(random()::varchar), 8)
from generate_series(1, 10000000) s(i);

insert into Cars (
    model,brand,vin,fuel,owner_id
)
select
    left(md5(i::name), 20),
    left(md5(i::name), 25),
    left(md5(i::name),30),
    left(md5(i::name),15),
    ((random() * 100000)+9)::int
from generate_series(1, 100000) s(i);

DROP TABLE Users,Components,Cars,Customers,Payouts,Rewards,Mechanics,Repairs,Component cascade