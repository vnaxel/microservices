package com.clientui.clientui.beans;

public class CardNumberBean {
    private long cardNumber;


    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return "CardNumberBean{" +
                "cardNumber=" + cardNumber +
                '}';
    }
}
