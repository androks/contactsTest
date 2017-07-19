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
    @NonNull
    private List<PhoneNumber> phones;
    @NonNull
    private List<Email> emails;
    @NonNull
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

    public void addEmails(List<Email> email) {
        emails = new ArrayList<>(email);
    }

    public void addPhoneNumbers(List<PhoneNumber> phoneNumber) {
        phones = new ArrayList<>(phoneNumber);
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

    public void setCreatedAt(String date) {
        if (TextUtils.isEmpty(date))
            createdAt = DateTimeUtils.dateToString(new Date());
        else
            createdAt = date;
    }

    public void setId(String id) {
        if (TextUtils.isEmpty(id))
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

    public static Integer compareName(Contact o1, Contact o2) {
        return (o1.getName() + o1.getSurname()).compareTo(o2.getName() + o2.getSurname());
    }

    public static Integer compareNewer(Contact o1, Contact o2) {
        Date d1 = DateTimeUtils.stringToDate(o1.getCreatedAt());
        Date d2 = DateTimeUtils.stringToDate(o2.getCreatedAt());
        if (d1 != null && d2 != null)
            return d2.compareTo(d1);
        else
            return (o2.getCreatedAt()).compareTo(o1.getCreatedAt());

    }

    public static Integer compareOlder(Contact o1, Contact o2) {
        Date d1 = DateTimeUtils.stringToDate(o1.getCreatedAt());
        Date d2 = DateTimeUtils.stringToDate(o2.getCreatedAt());
        if (d1 != null && d2 != null)
            return d1.compareTo(d2);
        else
            return (o1.getCreatedAt()).compareTo(o2.getCreatedAt());
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

        public Builder addPhone(PhoneNumber phoneNumber) {
            phones.add(phoneNumber);
            return this;
        }

        public Builder addEmail(Email email) {
            emails.add(email);
            return this;
        }


        public Builder addPhones(List<PhoneNumber> phoneNumber) {
            phones.addAll(phoneNumber);
            return this;
        }

        public Builder addEmails(List<Email> email) {
            emails.addAll(email);
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }
    }
}
