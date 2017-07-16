package com.androks.contactstest.data;

import android.support.annotation.NonNull;

/**
 * Created by androks on 15.07.17.
 */

public final class Contact {
    @NonNull
    private String id;
    @NonNull
    private String owner;
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
        owner = builder.owner;
        name = builder.name;
        surname = builder.surname;
        email = builder.email;
        phone = builder.phone;
        secondPhone = builder.secondPhone;
        thirdPhone = builder.thirdPhone;
        additionalEmail = builder.additionalEmail;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getOwner() {
        return owner;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getSurname() {
        return surname;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    public String getSecondPhone() {
        return secondPhone;
    }

    public String getThirdPhone() {
        return thirdPhone;
    }

    public String getAdditionalEmail() {
        return additionalEmail;
    }

    public static Builder newBuilder() {
        return new Builder();
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

    /**
     * {@code Contact} builder static inner class.
     */
    public static final class Builder {
        private String id;
        private String owner;
        private String name;
        private String surname;
        private String email;
        private String phone;
        private String secondPhone;
        private String thirdPhone;
        private String additionalEmail;

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
         * Sets the {@code owner} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code owner} to set
         * @return a reference to this Builder
         */
        public Builder owner(String val) {
            owner = val;
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
         * Returns a {@code Contact} built from the parameters previously set.
         *
         * @return a {@code Contact} built with parameters of this {@code Contact.Builder}
         */
        public Contact build() {
            return new Contact(this);
        }
    }
}
