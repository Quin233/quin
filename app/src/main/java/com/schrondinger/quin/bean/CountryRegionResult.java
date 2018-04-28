package com.schrondinger.quin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HP on 2018/1/16.
 */

public class CountryRegionResult implements Serializable {

    private List<CountryRegionListBean> CountryRegionList;

    public List<CountryRegionListBean> getCountryRegionList() {
        return CountryRegionList;
    }

    public void setCountryRegionList(List<CountryRegionListBean> CountryRegionList) {
        this.CountryRegionList = CountryRegionList;
    }

    public static class CountryRegionListBean implements Serializable {
        /**
         * C_Name : 中国
         * E_Name : china
         * IDNA : CN
         * code : 86
         */

        private String C_Name;
        private String E_Name;
        private String IDNA;
        private String code;

        public CountryRegionListBean() {}

        public CountryRegionListBean(String e_Name, String c_Name, String IDNA, String code) {
            C_Name = c_Name;
            E_Name = e_Name;
            this.IDNA = IDNA;
            this.code = code;
        }

        public String getC_Name() {
            return C_Name;
        }

        public void setC_Name(String C_Name) {
            this.C_Name = C_Name;
        }

        public String getE_Name() {
            return E_Name;
        }

        public void setE_Name(String E_Name) {
            this.E_Name = E_Name;
        }

        public String getIDNA() {
            return IDNA;
        }

        public void setIDNA(String IDNA) {
            this.IDNA = IDNA;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
