package com.androks.contactstest.data;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.androks.contactstest.util.DateTimeUtils;

import java.util.Date;

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

    private String createdAt;

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
        setCreatedAt(builder.createdAt);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void setCreatedAt(String date){
        if(TextUtils.isEmpty(date))
            createdAt = DateTimeUtils.dateToString(new Date());
        else
            createdAt = date;
    }

    public String getCreatedAt() {
        return createdAt;
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
        private String createdAt;

        private Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder owner(String val) {
            owner = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder surname(String val) {
            surname = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder secondPhone(String val) {
            secondPhone = val;
            return this;
        }

        public Builder thirdPhone(String val) {
            thirdPhone = val;
            return this;
        }

        public Builder additionalEmail(String val) {
            additionalEmail = val;
            return this;
        }

        public Builder createAt(String val) {
            createdAt = val;
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }
    }
}
