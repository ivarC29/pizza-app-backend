CREATE OR REPLACE PROCEDURE take_random_pizza_order(
    IN id_customer VARCHAR(15),
    IN method CHAR,
    OUT order_taken BOOLEAN
)
AS
$$
DECLARE
    id_random_pizza INT;
    price_random_pizza DECIMAL(5,2);
    price_with_discount DECIMAL(5,2);
    last_order_id INT;
    WITH_ERRORS BOOLEAN DEFAULT FALSE;
BEGIN
    BEGIN
        SELECT id_pizza, price
        INTO id_random_pizza, price_random_pizza
        FROM pizza
        WHERE available = 1
        ORDER BY RANDOM()
        LIMIT 1;

        price_with_discount := price_random_pizza - (price_random_pizza * 0.20);

		BEGIN
			INSERT INTO pizza_order (id_customer, date, total, method, additional_notes)
			VALUES (id_customer, CURRENT_TIMESTAMP, price_with_discount, method, '20% OFF PIZZA RANDOM PROMOTION')
			RETURNING id_order INTO last_order_id;

			INSERT INTO order_item (id_item, id_order, id_pizza, quantity, price)
			VALUES (1, last_order_id, id_random_pizza, 1, price_random_pizza);
		END;
    EXCEPTION
        WHEN OTHERS THEN
            WITH_ERRORS := TRUE;
    END;

    IF WITH_ERRORS THEN
        order_taken := FALSE;
    ELSE
        order_taken := TRUE;
    END IF;
END;
$$
LANGUAGE plpgsql;