package com.jason.hdxw.bean;

import java.io.Serializable;

/**
 * 银行卡信息实体类
 * created by wang on 2018/11/19
 */
public class BankCardMsg implements Serializable {

    /**
     * status : y
     * msg :
     * bank : {"bankaddress":"工商银行","banknum":"555666655200","idcard":"王春雷"}
     */

    private String status;
    private String msg;
    private BankBean bank;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BankBean getBank() {
        return bank;
    }

    public void setBank(BankBean bank) {
        this.bank = bank;
    }

    public static class BankBean implements Serializable{
        /**
         * bankaddress : 工商银行
         * banknum : 555666655200
         * idcard : 王春雷
         */

        private String bankaddress;
        private String banknum;
        private String idcard;

        public String getBankaddress() {
            return bankaddress;
        }

        public void setBankaddress(String bankaddress) {
            this.bankaddress = bankaddress;
        }

        public String getBanknum() {
            return banknum;
        }

        public void setBanknum(String banknum) {
            this.banknum = banknum;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }
    }
}