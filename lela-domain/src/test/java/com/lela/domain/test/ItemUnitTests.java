package com.lela.domain.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.lela.domain.document.Attribute;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Item;
import com.lela.domain.enums.IdentifierType;

public class ItemUnitTests {

	Item item = null;
	@Before
	public void beforeEach(){
		item = new Item();

		Item aisItem = new Item();
		
		List<Attribute> attributeList = new ArrayList<Attribute>();
		
		Attribute attributeAsin1 = new Attribute();
		attributeAsin1.setKy(IdentifierType.ASIN.getValue());
		attributeAsin1.setVl("asin1");
		
		Attribute attributeAsin2 = new Attribute();
		attributeAsin2.setKy(IdentifierType.ASIN.getValue());
		attributeAsin2.setVl("asin2");
		
		Attribute attributeAsin3 = new Attribute();
		attributeAsin3.setKy(IdentifierType.ASIN.getValue());
		attributeAsin3.setVl("asin3");		

		
		Attribute attributeMpn1 = new Attribute();
		attributeMpn1.setKy(IdentifierType.MPN.getValue());
		attributeMpn1.setVl("mpn1");
		
		Attribute attributeMpn2 = new Attribute();
		attributeMpn2.setKy(IdentifierType.MPN.getValue());
		attributeMpn2.setVl("mpn2");
		
		Attribute attributeMpn3 = new Attribute();
		attributeMpn3.setKy(IdentifierType.MPN.getValue());
		attributeMpn3.setVl("mpn3");

		
		Attribute attributeUpc1 = new Attribute();
		attributeUpc1.setKy(IdentifierType.UPC.getValue());
		attributeUpc1.setVl("upc1");
		
		Attribute attributeUpc2 = new Attribute();
		attributeUpc2.setKy(IdentifierType.UPC.getValue());
		attributeUpc2.setVl("upc2");
		
		Attribute attributeUpc3 = new Attribute();
		attributeUpc3.setKy(IdentifierType.UPC.getValue());
		attributeUpc3.setVl("upc3");
		
		
		attributeList.add(attributeAsin1);
		attributeList.add(attributeAsin2);
		attributeList.add(attributeAsin3);		
		attributeList.add(attributeMpn1);
		attributeList.add(attributeMpn2);
		attributeList.add(attributeMpn3);
		attributeList.add(attributeUpc1);
		attributeList.add(attributeUpc2);
		attributeList.add(attributeUpc3);
		aisItem.setSbttrs(attributeList);

		Item aisItem2 = new Item();
		
		Attribute attributeAsin1b = new Attribute();
		attributeAsin1b.setKy(IdentifierType.ASIN.getValue());
		attributeAsin1b.setVl("asin1b");
		
		Attribute attributeAsin2b = new Attribute();
		attributeAsin2b.setKy(IdentifierType.ASIN.getValue());
		attributeAsin2b.setVl("asin2b");
		
		Attribute attributeAsin3b = new Attribute();
		attributeAsin3b.setKy(IdentifierType.ASIN.getValue());
		attributeAsin3b.setVl("asin3b");		

		
		Attribute attributeMpn1b = new Attribute();
		attributeMpn1b.setKy(IdentifierType.MPN.getValue());
		attributeMpn1b.setVl("mpn1b");
		
		Attribute attributeMpn2b = new Attribute();
		attributeMpn2b.setKy(IdentifierType.MPN.getValue());
		attributeMpn2b.setVl("mpn2b");
		
		Attribute attributeMpn3b = new Attribute();
		attributeMpn3b.setKy(IdentifierType.MPN.getValue());
		attributeMpn3b.setVl("mpn3b");

		
		Attribute attributeUpc1b = new Attribute();
		attributeUpc1b.setKy(IdentifierType.UPC.getValue());
		attributeUpc1b.setVl("upc1b");
		
		Attribute attributeUpc2b = new Attribute();
		attributeUpc2b.setKy(IdentifierType.UPC.getValue());
		attributeUpc2b.setVl("upc2b");
		
		Attribute attributeUpc3b = new Attribute();
		attributeUpc3b.setKy(IdentifierType.UPC.getValue());
		attributeUpc3b.setVl("upc3b");
		
		
		attributeList.add(attributeAsin1b);
		attributeList.add(attributeAsin2b);
		attributeList.add(attributeAsin3b);		
		attributeList.add(attributeMpn1b);
		attributeList.add(attributeMpn2b);
		attributeList.add(attributeMpn3b);
		attributeList.add(attributeUpc1b);
		attributeList.add(attributeUpc2b);
		attributeList.add(attributeUpc3b);
		aisItem.setSbttrs(attributeList);	
		
		List<Item> itemList = new ArrayList<Item>();
		itemList.add(aisItem);
		itemList.add(aisItem2);
		
		AvailableInStore ais  = new AvailableInStore();
		ais.setTms(itemList);
		
		List<AvailableInStore> aisList  = new ArrayList<AvailableInStore>();
		aisList.add(ais);
		item.setStrs(aisList);
	}
	@Test
	public void testIdentifiers() {
		Set<String> asinSet = item.getASINsForItem();
		assertNotNull(asinSet);
		assertEquals(asinSet.size(), 6);
		
		Set<String> upcSet = item.getUPCsForItem();
		assertNotNull(upcSet);
		assertEquals(upcSet.size(), 6);
		
		Set<String> mpnSet = item.getMPNsForItem();
		assertNotNull(mpnSet);
		assertEquals(mpnSet.size(), 6);		
	}

}
