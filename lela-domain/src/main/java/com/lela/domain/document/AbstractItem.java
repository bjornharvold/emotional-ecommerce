/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.index.Indexed;

import com.lela.domain.enums.IdentifierType;
import com.lela.domain.enums.ItemType;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 9/7/11
 * Time: 9:28 AM
 * Responsibility:
 */
public abstract class AbstractItem extends AbstractDocument {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractItem.class);
	
    /** Field description */
    private Map<String, Integer> mtvtrs = new ConcurrentHashMap<String, Integer>();

    /** Attributes */
    private List<Attribute> attrs;

    /** Colors */
    private List<Color> clrs;

    /** Images */
    private List<ProductImage> imgs;

    /** Category */
    private Category ctgry;

    /** locale */
    private String lcl;

    /** Field description */
    private List<Merchant> mrchnts;

    /** Name */
    @NotNull
    private String nm;

    /** URL name */
    @Indexed( unique = true )
    private String rlnm;

    /** SEO URL name */
    private String srlnm;

    /** Manufacturer's color name */
    private String clrNm;

    /** Primary color name */
    private String clrPrmryNm;

    /** Hex color value*/
    private String clrHx;

    /** Subset of all the attributes */
    private List<Attribute> sbttrs;

    /** Type */
    private ItemType tp;

    /** Owner */
    private Owner wnr;

    /** Online stores this item is available in */
    private List<AvailableInStore> strs;

    /** UPC */
    private String pc;
    
    /** Lela UPC */
    private String llpc;

    /** Search Tags **/
    private List<Tag> tgs;

    /** Available */
    private Boolean vlbl;
    
    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     */
    public AbstractItem() {}

    /**
     * Constructs ...
     *
     *
     * @param item item
     */
    public AbstractItem(AbstractItem item) {
        super(item);
        this.attrs  = item.getAttrs();
        this.ctgry  = item.getCtgry();
        this.lcl    = item.getLcl();
        this.mtvtrs = item.getMtvtrs();
        this.nm     = item.getNm();
        this.rlnm   = item.getRlnm();
        this.srlnm   = item.getSrlnm();
        this.sbttrs = item.getSbttrs();
        this.tp     = item.getTp();
        this.clrs   = item.getClrs();
        this.imgs   = item.getImgs();
        this.wnr    = item.getWnr();
        this.mrchnts = item.getMrchnts();
        this.strs = item.getStrs();
        this.pc = item.getPc();
        this.llpc = item.getLlpc();
        this.tgs = item.getTgs();
        this.vlbl = item.getVlbl();
    }

    /**
     * Returns a multiMap keyed off the {@link IdentifierType}
     * @return
     */
    public MultiValueMap getIdentifierMultiMap(){

    	//This impl is rich and stable and we know that it will not change. So use it instead of an interface
    	MultiValueMap identifierMultiMap = new MultiValueMap();
    	if (this.getStrs() != null){
    		for (AvailableInStore availableInStore : this.getStrs()) {
				if (availableInStore.getTms() != null) {
					for (Item item : availableInStore.getTms()) {
						if (item.getSbttrs() != null) {
							for (Attribute attribute : item.getSbttrs()) {
								if (StringUtils.equalsIgnoreCase(attribute.getKy(), IdentifierType.UPC.getValue())){
									identifierMultiMap.put(IdentifierType.UPC, attribute.getVl());
								} else if (StringUtils.equalsIgnoreCase(attribute.getKy(), IdentifierType.MPN.getValue())){
									identifierMultiMap.put(IdentifierType.MPN, attribute.getVl());
								} else if (StringUtils.equalsIgnoreCase(attribute.getKy(), IdentifierType.ASIN.getValue())) {
									identifierMultiMap.put(IdentifierType.ASIN, attribute.getVl());
								} else {
									//Ignore
									//LOG.warn(String.format("Invalid identifier type %s for item %s", attribute.getKy(), this.getRlnm() ));
								}
							}
						}
					}
				}
			}
    	}
    	return identifierMultiMap;
    }
    
    @SuppressWarnings("unchecked")
    public Set<String> getUPCsForItem() { 
    	List<String> ls = (List<String>)this.getIdentifierMultiMap().getCollection(IdentifierType.UPC);
    	if (ls != null){
    		return new HashSet<String>(ls);
    	} else {
    		return null;
    	}
    }
    
    @SuppressWarnings("unchecked")
	public Set<String> getMPNsForItem() {
    	List<String> ls = (List<String>)this.getIdentifierMultiMap().getCollection(IdentifierType.MPN);
    	if (ls != null){    	
    		return new HashSet<String>(ls);
		} else {
			return null;
		}
    }
    
    @SuppressWarnings("unchecked")
    public Set<String> getASINsForItem() {
    	List<String> ls =(List<String>)this.getIdentifierMultiMap().getCollection(IdentifierType.ASIN);
    	if (ls != null){    
    		return new HashSet<String>(ls);
		} else {
			return null;
		}
    }

    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = null;

        if (attrs != null) {
            attributes = new HashMap<String, Object>();

            for (Attribute attr : attrs) {
                attributes.put(attr.getKy(), attr.getVl());
            }
        }

        return attributes;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public List<Attribute> getAttrs() {
        return attrs;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public List<Color> getClrs() {
        return clrs;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public List<ProductImage> getImgs() {
        return imgs;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Category getCtgry() {
        return ctgry;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getLcl() {
        return lcl;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public List<Merchant> getMrchnts() {
        return mrchnts;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Map<String, Integer> getMtvtrs() {
        return mtvtrs;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getNm() {
        return nm;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getRlnm() {
        return rlnm;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public List<Attribute> getSbttrs() {
        return sbttrs;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Map<String, Object> getSubAttributes() {
        Map<String, Object> result = null;

        if ((sbttrs != null) && !sbttrs.isEmpty()) {
            result = new HashMap<String, Object>();

            for (Attribute attr : sbttrs) {
                result.put(attr.getKy(), attr.getVl());
            }
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public ItemType getTp() {
        return tp;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Owner getWnr() {
        return wnr;
    }

    /**
     * Get search tags
     * @return List of tags
     */
    public List<Tag> getTgs() {
        return tgs;
    }

    /**
     * Method description
     *
     *
     * @param attrs attrs
     */
    public void setAttrs(List<Attribute> attrs) {
        this.attrs = attrs;
    }

    /**
     * Method description
     *
     *
     * @param clrs clrs
     */
    public void setClrs(List<Color> clrs) {
        this.clrs = clrs;
    }

    /**
     * Method description
     *
     *
     * @param imgs imgs
     */
    public void setImgs(List<ProductImage> imgs) {
        this.imgs = imgs;
    }

    /**
     * Method description
     *
     *
     * @param ctgry category
     */
    public void setCtgry(Category ctgry) {
        this.ctgry = ctgry;
    }

    /**
     * Method description
     *
     *
     * @param lcl lcl
     */
    public void setLcl(String lcl) {
        this.lcl = lcl;
    }

    /**
     * Method description
     *
     *
     * @param mrchnts mrchnts
     */
    public void setMrchnts(List<Merchant> mrchnts) {
        this.mrchnts = mrchnts;
    }

    /**
     * Method description
     *
     *
     * @param mtvtrs mtvtrs
     */
    public void setMtvtrs(Map<String, Integer> mtvtrs) {
        this.mtvtrs = mtvtrs;
    }

    /**
     * Method description
     *
     *
     * @param nm nm
     */
    public void setNm(String nm) {
        this.nm = nm;
    }

    /**
     * Method description
     *
     *
     * @param rlnm rlnm
     */
    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    /**
     * Method description
     *
     *
     * @param sbttrs sbttrs
     */
    public void setSbttrs(List<Attribute> sbttrs) {
        this.sbttrs = sbttrs;
    }

    /**
     * Method description
     *
     *
     * @param tp tp
     */
    public void setTp(ItemType tp) {
        this.tp = tp;
    }

    /**
     * Method description
     *
     *
     * @param wnr wnr
     */
    public void setWnr(Owner wnr) {
        this.wnr = wnr;
    }

    public List<AvailableInStore> getStrs() {
        return strs;
    }

    public void setStrs(List<AvailableInStore> strs) {
        this.strs = strs;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getLlpc() {
        return llpc;
    }

    public void setLlpc(String llpc) {
        this.llpc = llpc;
    }

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public String getClrNm() {
        return clrNm;
    }

    public void setClrNm(String clrNm) {
        this.clrNm = clrNm;
    }

    public String getClrPrmryNm() {
        return clrPrmryNm;
    }

    public void setClrPrmryNm(String clrPrmryNm) {
        this.clrPrmryNm = clrPrmryNm;
    }

    public String getClrHx() {
        return clrHx;
    }

    public void setClrHx(String clrHx) {
        this.clrHx = clrHx;
    }

    public void setTgs(List<Tag> tgs) {
        this.tgs = tgs;
    }

    public Boolean getVlbl() {
        return vlbl;
    }

    public void setVlbl(Boolean vlbl) {
        this.vlbl = vlbl;
    }

    public boolean isAvailableInStore(String storeUrlName) {
        boolean result = false;

        if (this.strs != null && !this.strs.isEmpty()) {
            for (AvailableInStore str : this.strs) {
                if (StringUtils.equals(str.getRlnm(), storeUrlName)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public Map<String, AvailableInStore> getStores() {
        Map<String, AvailableInStore> result = null;

        if (this.strs != null && !this.strs.isEmpty()) {
            result = new HashMap<String, AvailableInStore>(this.strs.size());

            for (AvailableInStore str : this.strs) {
                result.put(str.getRl(), str);
            }
        }

        return result;
    }
}
