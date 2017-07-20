package com.androks.contactstest.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androks.contactstest.R;
import com.androks.contactstest.data.entity.Contact;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by androks on 17.07.17.
 */

public class ContactsRecyclerViewAdapter
        extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder> {

    private View emptyView;
    private List<Contact> contacts;
    private ContactItemListener itemListener;

    public ContactsRecyclerViewAdapter(List<Contact> contacts, ContactItemListener itemListener) {
        this.contacts = contacts;
        this.itemListener = itemListener;
    }

    public void replaceData(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
        checkIfEmpty();
    }

    @Override
    public ContactsRecyclerViewAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.item.setOnClickListener(__ -> {
            itemListener.OnContactClick(contact);
        });
        holder.item.setOnLongClickListener(__ -> {
            itemListener.OnLongContactClick(contact);
            return true;
        });
        holder.name.setText(new StringBuilder()
                .append(contact.getName())
                .append(" ")
                .append(contact.getSurname())
        );
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private void checkIfEmpty() {
        if (emptyView != null) {
            emptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item) View item;
        @BindView(R.id.tv_name) TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
