package com.zybooks.inventorylist;

public class item {


        int id;
        String name, qty;

        public item(int id, String name, String qty){
            this.id = id;
            this.name = name;
            this.qty = qty;
        }

        public item(){

        }

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

        public String getQty() {
            return qty;
        }

        public void setQty(String age) {
            this.qty = age;
        }

}
