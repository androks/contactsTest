package com.androks.contactstest.contacts;

import com.androks.contactstest.data.entity.Contact;

/**
 * Created by androks on 17.07.17.
 */

public interface ContactItemListener {

    void OnContactClick(Contact clickedContact);

    void OnLongContactClick(Contact longClickedContact);

}
