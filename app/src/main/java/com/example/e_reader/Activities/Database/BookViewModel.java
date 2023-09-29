package com.example.e_reader.Activities.Database;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookRepository repository;
    private LiveData<List<BookTable>> allBooks;

    public BookViewModel(Application application) {
        super(application);
        repository = new BookRepository(application);
        allBooks = repository.getAllBooks();
    }

    public void insert(BookTable bookTable) {
        repository.insert(bookTable);
    }

    public void update(BookTable bookTable) {
        repository.update(bookTable);
    }

    public void delete(BookTable bookTable) {
        repository.delete(bookTable);
    }

    public void deleteAllBooks() {
        repository.deleteAllBooks();
    }

    public LiveData<List<BookTable>> getAllBooks() {
        return allBooks;
    }
}