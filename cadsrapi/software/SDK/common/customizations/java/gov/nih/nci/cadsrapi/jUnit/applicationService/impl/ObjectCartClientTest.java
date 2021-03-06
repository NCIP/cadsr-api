/*L
 * Copyright Oracle Inc, SAIC-F.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-api/LICENSE.txt for details.
 */

package gov.nih.nci.cadsrapi.jUnit.applicationService.impl;

import java.util.Collection;
import java.util.HashSet;

import gov.nih.nci.cadsr.objectcart.domain.Cart;
import gov.nih.nci.cadsr.objectcart.domain.CartObject;
import gov.nih.nci.cadsrapi.client.ObjectCartClient;
import gov.nih.nci.cadsrapi.client.ObjectCartException;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ObjectCartClientTest extends TestCase{

	public ObjectCartClient cartManager = null;
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
	}

	
	
	public void testFindCart() {

		String name = "cdeCart";
		String userId = "guest";
		
		try {
			cartManager = new ObjectCartClient("FORMBUILDER", "FORMBUILDER");
		} catch (ObjectCartException e1) {
			e1.printStackTrace();
			fail("Exception creating cart manager "+e1.getMessage());
		}

		Cart cart = null;
		try {
			 cart = cartManager.retrieveCart(userId,name);
		} catch (Exception e) {
			fail("Exception creating cart: "+e.getMessage());
		}
		assertNotNull(cart);
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationDate());
		assertNotNull(cart.getLastWriteDate());
		assertNotNull(cart.getLastReadDate());
		printCart(cart);
	}
	
	public void testCreateCartWithCredentials() {

		String name = "cdeCart";
		String userId = "guest";

		try {
			cartManager = new ObjectCartClient("FORMBUILDER", "FORMBUILDER");
		} catch (ObjectCartException e1) {
			e1.printStackTrace();
			fail("Exception creating cart manager: "+e1.getMessage());
		}

		Cart cart = null;
		try {
			 cart = cartManager.createCart(userId,name);
		} catch (Exception e) {
			fail("Exception creating cart: "+e.getMessage());
		}
		assertNotNull(cart);
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationDate());
		assertNotNull(cart.getLastWriteDate());
		assertNotNull(cart.getLastReadDate());

		printCart(cart);
		Cart secondCart = null;

		try {
			secondCart = cartManager.retrieveCart(cart.getUserId(), cart.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception retrieving cart: "+e.getMessage());
		}

		assertNotNull(secondCart);
		assertEquals(name, secondCart.getName());
		assertEquals(userId, secondCart.getUserId());
		assertNotNull(secondCart.getId());
		assertNotNull(secondCart.getCreationDate());
		assertNotNull(secondCart.getLastWriteDate());
		assertNotNull(secondCart.getLastReadDate());
		assertEquals(secondCart.getId(), cart.getId());

		printCart(secondCart);
	}

	public void testCreateCartWithoutCredentials() {

		String name = "formCart";
		String userId = "guest";

		try {
			cartManager = new ObjectCartClient();
		} catch (ObjectCartException e1) {
			e1.printStackTrace();
			//fail("Exception creating cart manager "+e1.getMessage());
			assertNull(cartManager);
			return;
		}

		Cart cart = null;
		try {
			 cart = cartManager.createCart(userId,name);
		} catch (Exception e) {
			fail("Exception creating cart: "+e.getMessage());
		}
		assertNotNull(cart);
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationDate());
		assertNotNull(cart.getLastWriteDate());
		assertNotNull(cart.getLastReadDate());

		printCart(cart);
		Cart secondCart = null;

		try {
			secondCart = cartManager.retrieveCart(cart.getUserId(), cart.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception retrieving cart");
		}

		assertNotNull(secondCart);
		assertEquals(name, secondCart.getName());
		assertEquals(userId, secondCart.getUserId());
		assertNotNull(secondCart.getId());
		assertNotNull(secondCart.getCreationDate());
		assertNotNull(secondCart.getLastWriteDate());
		assertNotNull(secondCart.getLastReadDate());
		assertEquals(secondCart.getId(), cart.getId());

		printCart(secondCart);
	}
	
	public void testCreateCartWithWrongCredentials() {

		String name = "formCart";
		String userId = "guest";

		try {
			cartManager = new ObjectCartClient("FORMBUILDER", "FORMBUILDER1");
		} catch (ObjectCartException e1) {
			e1.printStackTrace();
			//fail("Exception creating cart manager" + e1.getMessage());
			assertNull(cartManager);
			return;
		}

		Cart cart = null;
		try {
			 cart = cartManager.createCart(userId,name);
		} catch (Exception e) {
			fail("Exception creating cart");
		}
		assertNotNull(cart);
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationDate());
		assertNotNull(cart.getLastWriteDate());
		assertNotNull(cart.getLastReadDate());

		printCart(cart);
		Cart secondCart = null;

		try {
			secondCart = cartManager.retrieveCart(cart.getUserId(), cart.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception retrieving cart");
		}

		assertNotNull(secondCart);
		assertEquals(name, secondCart.getName());
		assertEquals(userId, secondCart.getUserId());
		assertNotNull(secondCart.getId());
		assertNotNull(secondCart.getCreationDate());
		assertNotNull(secondCart.getLastWriteDate());
		assertNotNull(secondCart.getLastReadDate());
		assertEquals(secondCart.getId(), cart.getId());

		printCart(secondCart);
	}
	
	public void testCreateAndDeleteCart(){
		try {
			cartManager = new ObjectCartClient("FORMBUILDER", "FORMBUILDER");
		} catch (ObjectCartException e1) {
			e1.printStackTrace();
			fail("Exception creating cart manager");
		}
		String name = "formCart";
		String userId = "GUEST";

		Cart cart = null;
		try {
			 cart = cartManager.createCart(userId,name);
			 // cart = cartManager.retrieveCart(userId, name);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}
		assertNotNull(cart);
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationDate());
		assertNotNull(cart.getLastWriteDate());
		//assertNotNull(cart.getExpirationDate());

		printCart(cart);

		try {
			cartManager.deleteCart(cart);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception deleting cart");
		}
	}


	@Test
	public void testAddandRetrieveObjects(){

		String name = "cdeCart";
		String userId = "guest";

		String type = ":Test:CDE Cart type";
		String dispName = "This is my Name";
		String data = "Some data here";

		try {
			cartManager = new ObjectCartClient("FORMBUILDER", "FORMBUILDER");
		} catch (ObjectCartException e1) {
			e1.printStackTrace();
			fail("Exception creating cart manager");
		}

		Cart cart = null;
		try {
			 cart = cartManager.createCart(userId,name);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}
		assertNotNull(cart);
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationDate());
		assertNotNull(cart.getLastWriteDate());
		assertNotNull(cart.getLastReadDate());
		//assertNotNull(cart.getExpirationDate());

		printCart(cart);

		CartObject co = new CartObject();
		co.setData(data);
		co.setDisplayText(dispName);
		co.setType(type);
		co.setNativeId(Long.toString(System.currentTimeMillis()));

		try {
			 cart = cartManager.storeObject(cart, co);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}

		Cart secondCart = null;


		try {
			secondCart = cartManager.retrieveCart(cart.getUserId(), cart.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception retrieving cart");
		}

		assertNotNull(secondCart);
		assertEquals(name, secondCart.getName());
		assertEquals(userId, secondCart.getUserId());
		assertNotNull(secondCart.getId());
		assertNotNull(secondCart.getCreationDate());
		assertNotNull(secondCart.getLastWriteDate());
		assertNotNull(secondCart.getLastReadDate());
		//assertNotNull(secondCart.getExpirationDate());

		assertEquals(secondCart.getId(), cart.getId());

		printCart(secondCart);
	}

	public void testAddCollection(){

		String name = "cdeCart";
		String userId = "guest";

		try {
			cartManager = new ObjectCartClient("FORMBUILDER", "FORMBUILDER");
		} catch (ObjectCartException e1) {
			e1.printStackTrace();
			fail("Exception creating cart manager");
		}

		String type = ":Test:CDE Cart type";
		String dispName = "This is my Name";
		String data = "Some data here";

		Cart cart = null;
		try {
			 cart = cartManager.createCart(userId,name);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}
		assertNotNull(cart);
		assertEquals(name, cart.getName());
		assertEquals(userId, cart.getUserId());
		assertNotNull(cart.getId());
		assertNotNull(cart.getCreationDate());
		assertNotNull(cart.getLastWriteDate());
		assertNotNull(cart.getLastReadDate());
		//assertNotNull(cart.getExpirationDate());

		printCart(cart);

		CartObject co = new CartObject();
		co.setData(data);
		co.setDisplayText(dispName);
		co.setType(type);
		co.setNativeId(Long.toString(System.currentTimeMillis()));
		CartObject co2 = new CartObject();
		co2.setData(data+"2");
		co2.setDisplayText(dispName+"2");
		co2.setType(type);
		co.setNativeId(Long.toString(System.currentTimeMillis()));

		Collection<CartObject> col = new HashSet<CartObject>();
		col.add(co);
		col.add(co2);

		try {
			 cart = cartManager.storeObjectCollection(cart, col);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception creating cart");
		}

		Cart secondCart = null;


		try {
			secondCart = cartManager.retrieveCart(cart.getUserId(), cart.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception retrieving cart");
		}

		assertNotNull(secondCart);
		assertEquals(name, secondCart.getName());
		assertEquals(userId, secondCart.getUserId());
		assertNotNull(secondCart.getId());
		assertNotNull(secondCart.getCreationDate());
		assertNotNull(secondCart.getLastWriteDate());
		assertNotNull(secondCart.getLastReadDate());
		//assertNotNull(secondCart.getExpirationDate());

		assertEquals(secondCart.getId(), cart.getId());

		printCart(secondCart);
	}

	

	
	public static void printCart(Cart cart) {
		System.out.println("------------------------");
		System.out.print("Id:         ");
		System.out.println(cart.getId());
		System.out.print("User Id:    ");
		System.out.println(cart.getUserId());
		System.out.print("Name:       ");
		System.out.println(cart.getName());
		System.out.print("Created On: ");
		System.out.println(cart.getCreationDate());
		System.out.print("Write Active On:  ");
		System.out.println(cart.getLastWriteDate());
		System.out.print("Read Active On:  ");
		System.out.println(cart.getLastReadDate());
		System.out.print("Expires:    ");
		System.out.println(cart.getExpirationDate());
		/*System.out.print("Type:");
		System.out.println(cart.getType());*/
		System.out.print("Contents:   ");
		printContents(cart.getCartObjectCollection());
		System.out.println("______________________");
	}



	public static void printContents(Collection<CartObject> objects) {

		if (objects != null) {
			System.out.println("Collection Size:"+objects.size());
			for (CartObject c: objects) {
				System.out.println(c.getId());
				System.out.println(c.getType());
				System.out.println(c.getDisplayText());
				System.out.println(c.getNativeId());
				System.out.println(c.getData());
				System.out.println("*********************************");

			}
		}
	}
}