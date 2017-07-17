package com.androks.contactstest.addeditcontact;

import java.util.List;

/**
 * Created by androks on 17.07.17.
 */

public class AddEditContactPresenter implements AddEditContactContract.Presenter {
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void saveContact(String name,
                            String surname,
                            List<MultiImputedViewsViewHolder> emails,
                            List<MultiImputedViewsViewHolder> phones) {
    }

    @Override
    public void populateTask() {

    }

    @Override
    public boolean isDataMissing() {
        return true;
    }
}
