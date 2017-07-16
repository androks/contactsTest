package com.androks.contactstest.data.entity;

/**
 * Created by androks on 16.07.17.
 */

public final class Email {
    private String id;
    private String contactId;
    private String email;
    private String label;

    private Email(Builder builder) {
        id = builder.id;
        contactId = builder.contactId;
        email = builder.email;
        label = builder.label;
    }

    public String getId() {
        return id;
    }

    public String getContactId() {
        return contactId;
    }

    public String getEmail() {
        return email;
    }

    public String getLabel() {
        return label;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String id;
        private String contactId;
        private String email;
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

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder label(String val) {
            label = val;
            return this;
        }

        public Email build() {
            return new Email(this);
        }
    }
}
