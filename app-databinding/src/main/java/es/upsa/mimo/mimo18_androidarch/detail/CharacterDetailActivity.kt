package es.upsa.mimo.mimo18_androidarch.detail

import android.content.res.Resources
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import dagger.android.AndroidInjection
import es.upsa.mimo.mimo18_androidarch.R
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.character_detail_activity.*
import kotlinx.android.synthetic.main.view_recycler_view.*
import javax.inject.Inject


class CharacterDetailActivity : AppCompatActivity(), CharacterDetailContract.View {

    private lateinit var characterID: String

    private val sectionAdapter: SectionedRecyclerViewAdapter = SectionedRecyclerViewAdapter()

    @Inject
    lateinit var presenter: CharacterDetailPresenter

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var androidResources: Resources


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_detail_activity)
        injectDependencies()

        characterList.adapter = sectionAdapter

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        characterID = intent.getStringExtra(BUNDLE_CHARACTER_ID)
        presenter.start(
                view = this,
                charId = characterID
        )
    }

    override fun onStop() {
        super.onStop()
        presenter.end()
    }

    private fun injectDependencies() {
        AndroidInjection.inject(this)
    }

    override fun showLoadingIndicator() {

    }

    override fun hideLoadingIndicator() {

    }

    override fun showCharacterName(name: String) {
        toolbar_layout?.title = name
    }

    override fun showCharacterImage(imageUrl: String) {
        imageLoader.loadImageFittedToImageView(
                imageUrl = imageUrl,
                imageView = characterImage
        )
    }

    override fun showCharacterSeries(series: List<String>) {
        with(sectionAdapter) {
            addSection(
                    CharacterDetailSection(
                            title = androidResources.getString(R.string.character_detail_section_series),
                            items = series,
                            itemClick = { presenter.onSeriesItemClicked(series = it) }
                    )
            )
            notifyDataSetChanged()
        }
    }

    override fun showCharacterStories(stories: List<String>) {
        with(sectionAdapter) {
            addSection(
                    CharacterDetailSection(
                            title = androidResources.getString(R.string.character_detail_section_stories),
                            items = stories,
                            itemClick = { presenter.onStoryItemClicked(story = it) }
                    )
            )
            notifyDataSetChanged()
        }
    }

    override fun showCharacterComics(comics: List<String>) {
        with(sectionAdapter) {
            addSection(
                    CharacterDetailSection(
                            title = androidResources.getString(R.string.character_detail_section_comics),
                            items = comics,
                            itemClick = {
                                presenter.onComicItemClicked(comicName = it)
                            }
                    )
            )
            notifyDataSetChanged()
        }
    }

    override fun showCharacterLoadError(errorMessage: String) {
        Snackbar.make(
                characterList,
                errorMessage,
                Snackbar.LENGTH_LONG)
                .setAction(
                        "Retry",
                        View.OnClickListener { presenter.onRetryLoadButtonClicked() }
                )
                .show()
    }

    override fun showSnackbarWithText(snackbarText: String) {
        Snackbar.make(
                characterList,
                snackbarText,
                Snackbar.LENGTH_LONG)
                .show()
    }


    companion object {
        const val BUNDLE_CHARACTER_ID = "char_id"
    }

}
