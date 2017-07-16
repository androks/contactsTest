package com.androks.contactstest.data.entity;

/**
 * Created by androks on 16.07.17.
 */

public final class PhoneNumber {
    private String id;
    private String contactId;
    private String phone;
    private String label;

    private PhoneNumber(Builder builder) {
        id = builder.id;
        contactId = builder.contactId;
        phone = builder.phone;
        label = builder.label;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public String getId() {
        return id;
    }

    public String getContactId() {
        return contactId;
    }

    public String getPhone() {
        return phone;
    }

    public String getLabel() {
        return label;
    }

    public static final class Builder {
        private String id;
        private String contactId;
        private String phone;
        private String label;

        private Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder contactId(String val) {
            contactId = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder label(String val) {
            label = val;
            return this;
        }

        public PhoneNumber build() {
            return new PhoneNumber(this);
        }
    }
}
