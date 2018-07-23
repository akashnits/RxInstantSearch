package com.cloudbanter.rxinstantsearch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudbanter.rxinstantsearch.R;
import com.cloudbanter.rxinstantsearch.model.Contact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsFilterableAdapter extends RecyclerView.Adapter<ContactsFilterableAdapter.ContactsViewHolder>
                                            implements Filterable{

    private Context context;
    private List<Contact> contactList;
    private List<Contact> contactListFiltered;
    private ContactsAdapterListener listener;

    public interface ContactsAdapterListener {
        void onContactSelected(Contact contact);
    }

    public ContactsFilterableAdapter(Context context, List<Contact> contactList,
                                     List<Contact> contactListFiltered, ContactsAdapterListener listener) {
        this.context = context;
        this.contactList = contactList;
        this.contactListFiltered = contactListFiltered;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row_item, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {

        final Contact contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhone());

        Glide.with(context)
                .load(contact.getProfileImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if(!TextUtils.isEmpty(constraint)){
                    List<Contact> filteredList= new ArrayList<>();
                    for(Contact contact: contactList){
                        if(contact.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                        {
                            filteredList.add(contact);
                        }
                        contactListFiltered= filteredList;
                    }
                }else {
                    contactListFiltered= contactList;
                }

                FilterResults filterResults= new FilterResults();
                filterResults.count= contactListFiltered.size();
                filterResults.values= contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactListFiltered= (ArrayList<Contact>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail)
        ImageView thumbnail;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.phone)
        TextView phone;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
