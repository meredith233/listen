package com.example.listen.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.listen.dao.WordDAO;
import com.example.listen.database.WordRoomDatabase;
import com.example.listen.entity.Word;

import java.util.List;

public class WordRepository {

    private WordDAO wordDAO;

    private LiveData<List<Word>> allWords;

    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        wordDAO = db.wordDAO();
        allWords = wordDAO.getAlphabetizedWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public void insert(Word word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
            wordDAO.insert(word);
        });
    }

    public void deleteAll() {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
            wordDAO.deleteAll();
        });
    }
}
