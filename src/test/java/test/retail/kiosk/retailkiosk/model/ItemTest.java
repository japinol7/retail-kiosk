package test.retail.kiosk.retailkiosk.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import retail.kiosk.retailkiosk.model.*;

import static org.junit.jupiter.api.Assertions.*;


public class ItemTest {

	private Item item;

	@BeforeEach
    void setUp() throws ItemException {
		item = new ActionFigure();
    }

	@Test
	public void testSetStock() throws ItemException {
		int expected_result;

		// test setting stock to a positive number:  5 units
		item.setStock(5);
		expected_result = 5;
		assertEquals(expected_result, item.getStock());

		// test setting stock to 0 units
		item.setStock(0);
		expected_result = 0;
		assertEquals(expected_result, item.getStock());
	}

	@Test
	public void testSetStockException() {
		String expected_result;
		// test setting stock to a negative number: -1 units
		Throwable throwable = assertThrows(ItemException.class, () -> item.setStock(-1));
		expected_result = "Stock cannot be a negative value!!";
		assertEquals(expected_result, throwable.getMessage());
	}

	@Test
	public void testSetId() throws ItemException {
		// test setting id to 0. That means that it has no id.
		item.setId(0);
		assertEquals(0, item.getId());
		
		// test setting id to a non 0 number:  15
		item.setId(15);
		assertEquals(15, item.getId());
	}

	@Test
	public void testSetIdException() throws ItemException {
		String expected_result;
		// test setting id to 20 when it has been set previously to 15
		item.setId(15);
		Throwable throwable = assertThrows(ItemException.class, () -> item.setId(20));
		expected_result = "The id can only be set once. Its current value is: 15!!";
		assertEquals(expected_result, throwable.getMessage());
	}

}
