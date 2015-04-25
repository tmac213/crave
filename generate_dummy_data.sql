USE restaurantsApp;

INSERT INTO dishes (name)
VALUES	("hamburger"),
		("cheeseburger"),
		("cheesesteak"),
		("spaghetti"),
		("chicken taco"),
		("aztec burrito");

INSERT INTO restaurants (name, address)
VALUES	("Bob's Diner", "1681 Sunset Blvd."),
		("McDonald's", "1872 Broadway Ave."),
		("Mario's Italian Cuisine", "5577 Omega Dr."),
		("La Casita", "89 Pacific Blvd.");

INSERT INTO types (name)
VALUES	("burger"),
		("sandwich"),
		("pasta"),
		("taco"),
		("burrito");

INSERT INTO regions (name)
VALUES	("North Side"),
		("Coventry"),
		("Little Italy");

INSERT INTO origins (name)
VALUES	("American"),
		("Italian"),
		("Mexican");
		
INSERT INTO serves (rID, dID, price, description)
VALUES	(1, 1, 2.75, "a hamburger"),
		(1, 2, 3.50, "a cheeseburger"),
		(1, 3, 4.50, "our famous cheesesteak"),
		(2, 1, 1.00, "McBurger"),
		(2, 2, 1.50, "McDouble"),
		(3, 4, 12.00, "succulent homemade spaghetti"),
		(4, 5, 1.50, "taco de pollo"),
		(4, 6, 4.00, "nuestro burrito de pollo con salsa verde");

INSERT INTO dishOrigin (dID, oID)
VALUES	(1, 1),
		(2, 1),
		(3, 1),
		(4, 2),
		(5, 3),
		(6, 3);

INSERT INTO dishType (dID, tID)
VALUES	(1, 1),
		(2, 1),
		(3, 2),
		(4, 3),
		(5, 4),
		(6, 5);

INSERT INTO restaurantOrigin (rID, oID)
VALUES	(1, 1),
		(2, 1),
		(3, 2),
		(4, 3);

INSERT INTO locatedIn (resID, regID)
VALUES	(1, 1),
		(2, 2),
		(3, 3),
		(4, 1);


