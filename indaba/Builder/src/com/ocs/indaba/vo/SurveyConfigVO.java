/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.common.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeff Jiang
 */
public class SurveyConfigVO {

    static public class OrgInfo {
        private String name;
        private boolean primary;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setPrimary(boolean p) {
            this.primary = p;
        }

        public boolean isPrimary() {
            return this.primary;
        }
    }

    static public class ProductInfo {
        private String name;
        private short mode;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setMode(short mode) {
            this.mode = mode;
        }

        public short getMode() {
            return this.mode;
        }
    }

    private int id;
    private String name;
    private int primaryOrgId;
    private List<OrgInfo> orgs = null;
    private List<ProductInfo> products = null;
    private int visibility;
    private boolean tsc;
    private boolean inUse;
    private boolean inActiveUse;
    private boolean owned;
    private boolean primaryOwner;
    private String orgNames;
    private String productNames;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<ProductInfo> getProducts() {
        return products;
    }

    public void addProduct(ProductInfo prod) {
        if (products == null) {
            products = new ArrayList<ProductInfo>();
        }
        products.add(prod);
    }

    public boolean isTsc() {
        return tsc;
    }

    public void setTsc(boolean tsc) {
        this.tsc = tsc;
    }

    public List<OrgInfo> getOrgs() {
        return orgs;
    }

    public void addOrg(OrgInfo org) {
        if (orgs == null) {
            orgs = new ArrayList<OrgInfo>();
        }
        orgs.add(org);
    }

    public boolean isInActiveUse() {
        return inActiveUse;
    }

    public void setInActiveUse(boolean used) {
        this.inActiveUse = used;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean used) {
        this.inUse = used;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public boolean isPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(boolean owned) {
        this.primaryOwner = owned;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getPrimaryOrgId() {
        return primaryOrgId;
    }

    public void setPrimaryOrgId(int orgId) {
        this.primaryOrgId = orgId;
    }

    public String getProductNames() {
        StringBuilder sb = new StringBuilder("");

        if (products != null && !products.isEmpty()) {
            boolean isFirst = true;
            for (ProductInfo p : products) {
                if (!isFirst) sb.append(", ");
                isFirst = false;

                sb.append(p.getName());
                switch(p.getMode()) {
                    case Constants.PRODUCT_MODE_CONFIG:
                        sb.append("[C]");
                        break;
                    case Constants.PRODUCT_MODE_TEST:
                        sb.append("[T]");
                        break;
                    default:
                        sb.append("[P]");
                }
            }
        }

        return sb.toString();
    }

    public String getOrgNames() {
        StringBuilder sb = new StringBuilder("");

        if (orgs != null && !orgs.isEmpty()) {
            boolean isFirst = true;
            for (OrgInfo org : orgs) {
                if (!isFirst) sb.append(", ");
                isFirst = false;

                sb.append(org.getName());
                if (org.isPrimary()) {
                    sb.append("[P]");
                }
            }
        }

        return sb.toString();
    }
}
