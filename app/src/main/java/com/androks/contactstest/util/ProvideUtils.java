package com.androks.contactstest.util;

import android.content.Context;

import com.androks.contactstest.data.source.ContactsRepository;
import com.androks.contactstest.data.source.local.ContactsLocalDataSource;
import com.androks.contactstest.util.schedulers.BaseSchedulerProvider;
import com.androks.contactstest.util.schedulers.SchedulerProvider;

import io.reactivex.annotations.NonNull;

/**
 * Created by androks on 17.07.17.
 */

public class ProvideUtils {

    public static ContactsRepository provideContactsRepository(@NonNull Context context){
        return ContactsRepository.getInstance(
                ContactsLocalDataSource.getInstance(context, provideScheduleProvider())
        );
    }

    public static BaseSchedulerProvider provideScheduleProvider(){
        return SchedulerProvider.getInstance();
    }
}
