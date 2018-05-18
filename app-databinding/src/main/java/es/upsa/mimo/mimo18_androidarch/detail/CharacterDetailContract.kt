package es.upsa.mimo.mimo18_androidarch.detail

import es.upsa.mimo.mimo18_androidarch.base.BasePresenter
import es.upsa.mimo.mimo18_androidarch.base.BaseView
import es.upsa.mimo.mimo18_androidarch.marvel.bindingModel.CharacterBindingModel

/**
 * This specifies the contract between the view and the presenter.
 */
interface CharacterDetailContract {

    interface View : BaseView {

        fun showCharacter(character : CharacterBindingModel)

        fun showCharacterComics(comics: List<String>)

        fun showCharacterSeries(series: List<String>)

        fun showCharacterStories(stories: List<String>)

        fun showCharacterLoadError(errorMessage: String)

        fun showSnackbarWithText(snackbarText: String)

    }

    interface Presenter : BasePresenter<View> {

        fun start(view: View, charId: String)

        fun onRetryLoadButtonClicked()

        fun onComicItemClicked(comicName: String)

        fun onSeriesItemClicked(series: String)

        fun onStoryItemClicked(story: String)

    }
}