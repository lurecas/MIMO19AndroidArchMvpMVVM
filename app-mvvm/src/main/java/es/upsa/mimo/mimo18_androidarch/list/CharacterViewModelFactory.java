package es.upsa.mimo.mimo18_androidarch.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.util.ArrayMap;
import es.upsa.mimo.mimo18_androidarch.MarvelApplication;
import es.upsa.mimo.mimo18_androidarch.list.viewModel.CharacterListViewModel;
import es.upsa.mimo.mimo18_androidarch.marvel.repository.MarvelDataSource;
import java.util.Map;
import java.util.concurrent.Callable;

public class CharacterViewModelFactory implements ViewModelProvider.Factory {

    private final ArrayMap<Class, Callable<? extends ViewModel>> creators;

    public CharacterViewModelFactory(
            final MarvelDataSource marvelDataSource,
            final MarvelApplication application) {
        creators = new ArrayMap<>();

        // View models cannot be injected directly because they won't be bound to the owner's view model scope.
        creators.put(CharacterListViewModel.class,
                new Callable<ViewModel>() {
                    @Override
                    public ViewModel call() throws Exception {
                        return new CharacterListViewModel(application, marvelDataSource);
                    }
                });
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Callable<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class, Callable<? extends ViewModel>> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("Unknown model class " + modelClass);
        }
        try {
            return (T) creator.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}