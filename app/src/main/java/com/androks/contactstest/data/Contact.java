package com.androks.contactstest.data;

import android.support.annotation.NonNull;

/**
 * Created by androks on 15.07.17.
 */

public final class Contact {
    @NonNull
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String email;
    @NonNull
    private String phone;

    private String secondPhone;
    private String thirdPhone;
    private String additionalEmail;

    private Contact(Builder builder) {
        id = builder.id;
        name = builder.name;
        surname = builder.surname;
        email = builder.email;
        additionalEmail = builder.additionalEmail;
        phone = builder.phone;
        secondPhone = builder.secondPhone;
        thirdPhone = builder.thirdPhone;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    /**
     * {@code Contact} builder static inner class.
     */
    public static final class Builder {
        private String id;
        private String name;
        private String surname;
        private String email;
        private String additionalEmail;
        private String phone;
        private String secondPhone;
        private String thirdPhone;

        private Builder() {
        }

        /**
         * Sets the {@code id} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code id} to set
         * @return a reference to this Builder
         */
        public Builder id(String val) {
            id = val;
            return this;
        }

        /**
         * Sets the {@code name} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code name} to set
         * @return a reference to this Builder
         */
        public Builder name(String val) {
            name = val;
            return this;
        }

        /**
         * Sets the {@code surname} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code surname} to set
         * @return a reference to this Builder
         */
        public Builder surname(String val) {
            surname = val;
            return this;
        }

        /**
         * Sets the {@code email} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code email} to set
         * @return a reference to this Builder
         */
        public Builder email(String val) {
            email = val;
            return this;
        }

        /**
         * Sets the {@code additionalEmail} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code additionalEmail} to set
         * @return a reference to this Builder
         */
        public Builder additionalEmail(String val) {
            additionalEmail = val;
            return this;
        }

        /**
         * Sets the {@code phone} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code phone} to set
         * @return a reference to this Builder
         */
        public Builder phone(String val) {
            phone = val;
            return this;
        }

        /**
         * Sets the {@code secondPhone} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code secondPhone} to set
         * @return a reference to this Builder
         */
        public Builder secondPhone(String val) {
            secondPhone = val;
            return this;
        }

        /**
         * Sets the {@code thirdPhone} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code thirdPhone} to set
         * @return a reference to this Builder
         */
        public Builder thirdPhone(String val) {
            thirdPhone = val;
            return this;
        }

        /**
         * Returns a {@code Contact} built from the parameters previously set.
         *
         * @return a {@code Contact} built with parameters of this {@code Contact.Builder}
         */
        public Contact build() {
            return new Contact(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (!id.equals(contact.id)) return false;
        if (!email.equals(contact.email)) return false;
        return phone.equals(contact.phone);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phone.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", secondPhone='" + secondPhone + '\'' +
                ", thirdPhone='" + thirdPhone + '\'' +
                ", additionalEmail='" + additionalEmail + '\'' +
                '}';
    }
}
