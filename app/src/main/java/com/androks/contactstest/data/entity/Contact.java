package com.androks.contactstest.data.entity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.androks.contactstest.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    private List<PhoneNumber> phones = new ArrayList<>();
    private List<Email> emails = new ArrayList<>();

    private String createdAt;

    private Contact(Builder builder) {
        setId(builder.id);
        owner = builder.owner;
        name = builder.name;
        surname = builder.surname;
        setCreatedAt(builder.createdAt);
        phones = new ArrayList<>(builder.phones);
        emails = new ArrayList<>(builder.emails);
        setCreatedAt(builder.createdAt);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void addEmail(Email email){
        emails.add(email);
    }

    public void addPhoneNumber(PhoneNumber phoneNumber){
        phones.add(phoneNumber);
    }

    public void setPhones(List<PhoneNumber> phones) {
        this.phones = phones;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
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

    public List<PhoneNumber> getPhones() {
        return phones;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String date){
        if(TextUtils.isEmpty(date))
            createdAt = DateTimeUtils.dateToString(new Date());
        else
            createdAt = date;
    }

    public void setId(String id){
        if(TextUtils.isEmpty(id))
            this.id = UUID.randomUUID().toString();
        else
            this.id = id;
    }
    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + emails.get(0).hashCode();
        result = 31 * result + phones.get(0).hashCode();
        return result;
    }


    public static final class Builder {
        private String id;
        private String owner;
        private String name;
        private String surname;
        private String createdAt;
        private List<Email> emails = new ArrayList<>();
        private List<PhoneNumber> phones = new ArrayList<>();

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

        public Builder createdAt(String val) {
            createdAt = val;
            return this;
        }

        public Builder addPhone(PhoneNumber phoneNumber){
            phones.add(phoneNumber);
            return this;
        }

        public Builder addEmail(Email email){
            emails.add(email);
            return this;
        }


        public Builder addPhones(List<PhoneNumber> phoneNumber){
            phones.addAll(phoneNumber);
            return this;
        }

        public Builder addEmails(List<Email> email){
            emails.addAll(email);
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }
    }
}
