package com.cloudbanter.rxinstantsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;

import com.cloudbanter.rxinstantsearch.adapter.ContactsAdapter;
import com.cloudbanter.rxinstantsearch.model.Contact;
import com.cloudbanter.rxinstantsearch.network.ApiClient;
import com.cloudbanter.rxinstantsearch.network.ApiService;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RemoteSearchActivity extends AppCompatActivity implements ContactsAdapter.ContactsAdapterListener{

    @BindView(R.id.input_search)
    EditText inputSearch;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private CompositeDisposable disposable = new CompositeDisposable();
    private PublishSubject<String> publishSubject = PublishSubject.create();
    private ApiService apiService;
    private ContactsAdapter mAdapter;
    private List<Contact> contactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_search);
        ButterKnife.bind(this);

        mAdapter = new ContactsAdapter(this, contactsList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        apiService = ApiClient.getRetrofit().create(ApiService.class);

        /*disposable.add(publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .switchMapSingle(new Function<String, SingleSource<List<Contact>>>() {
                    @Override
                    public SingleSource<List<Contact>> apply(String s) throws Exception {
                        return apiService.getContacts(null, s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                }).subscribeWith(getSearchObserver()));
        publishSubject.onNext("");

        disposable.add(RxTextView.textChangeEvents(inputSearch)
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(searchContactsTextWatcher()));*/



        Observable<String> observable= RxSearchObservable.fromView(inputSearch);

        observable
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .switchMapSingle(new Function<String, SingleSource<List<Contact>>>() {
                    @Override
                    public SingleSource<List<Contact>> apply(String s) throws Exception {
                        return apiService.getContacts("", s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                }).subscribeWith(new DisposableObserver<List<Contact>>() {
            @Override
            public void onNext(List<Contact> contacts) {
                contactsList.clear();
                contactsList.addAll(contacts);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }



   /* private DisposableObserver<List<Contact>> getSearchObserver(){
        return new DisposableObserver<List<Contact>>() {
            @Override
            public void onNext(List<Contact> contacts) {
                contactsList.clear();
                contactsList.addAll(contacts);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private DisposableObserver<TextViewTextChangeEvent> searchContactsTextWatcher(){
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                publishSubject.onNext(textViewTextChangeEvent.text().toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

    }*/
    @Override
    public void onContactSelected(Contact contact) {

    }
}
