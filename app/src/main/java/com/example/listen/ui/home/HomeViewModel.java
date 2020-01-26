package com.example.listen.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.listen.entity.Word;
import com.example.listen.repository.WordRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    private WordRepository wordRepository;

    private LiveData<List<Word>> allWords;

    public HomeViewModel(Application application) {
        super(application);
        wordRepository = new WordRepository(application);
        allWords = wordRepository.getAllWords();

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public void insert(Word word) {
        wordRepository.insert(word);
    }

    public void deleteAll() {
        wordRepository.deleteAll();
    }

}