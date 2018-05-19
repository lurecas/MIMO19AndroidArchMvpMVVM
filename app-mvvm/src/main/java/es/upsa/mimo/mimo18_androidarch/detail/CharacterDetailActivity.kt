package es.upsa.mimo.mimo18_androidarch.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Resources
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import dagger.android.AndroidInjection
import es.upsa.mimo.mimo18_androidarch.R
import es.upsa.mimo.mimo18_androidarch.databinding.CharacterDetailActivityBinding
import es.upsa.mimo.mimo18_androidarch.detail.model.CharacterBindingModel
import es.upsa.mimo.mimo18_androidarch.detail.viewModel.CharacterDetailViewModel
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.character_detail_activity.*
import javax.inject.Inject


class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var characterID: String

    private val sectionAdapter: SectionedRecyclerViewAdapter = SectionedRecyclerViewAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var androidResources: Resources

    private var binding: CharacterDetailActivityBinding? = null
    private var viewModel: CharacterDetailViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.character_detail_activity)

        injectDependencies()
        (characterList as RecyclerView).adapter = sectionAdapter

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        characterID = intent.getStringExtra(BUNDLE_CHARACTER_ID)

        viewModel= ViewModelProviders
                .of(this, viewModelFactory)
                .get(CharacterDetailViewModel::class.java)

        loadCharacterDetail(viewModel = viewModel!!)
    }


    private fun injectDependencies() {
        AndroidInjection.inject(this)
    }

    private fun loadCharacterDetail(viewModel: CharacterDetailViewModel) {

        viewModel.getCharacterDetail(charId = characterID)
                .observe(
                        this,
                        Observer<CharacterBindingModel> {

                            if (it != null) {
                                showCharacter(it)
                            } else {
                                showCharacterLoadError("Error obtaining character $characterID detail")
                            }

                        })

    }

    private fun showCharacter(character: CharacterBindingModel) {
        binding?.run {
            this.character = character
            this.imageLoader = character.imageLoader
        }
        showCharacterSeries(character.series)
        showCharacterStories(character.stories)
        showCharacterComics(character.comics)
    }

    private fun showCharacterSeries(series: List<String>) {
        with(sectionAdapter) {
            addSection(
                    CharacterDetailSection(
                            title = androidResources.getString(R.string.character_detail_section_series),
                            items = series,
                            itemClick = {
                                showSnackbarWithText("Don't miss out the $series series")
                            }
                    )
            )
            notifyDataSetChanged()
        }
    }

    private fun showCharacterStories(stories: List<String>) {
        with(sectionAdapter) {
            addSection(
                    CharacterDetailSection(
                            title = androidResources.getString(R.string.character_detail_section_stories),
                            items = stories,
                            itemClick = {
                                showSnackbarWithText("You can find more related stories on $it")
                            }
                    )
            )
            notifyDataSetChanged()
        }
    }

    private fun showCharacterComics(comics: List<String>) {
        with(sectionAdapter) {
            addSection(
                    CharacterDetailSection(
                            title = androidResources.getString(R.string.character_detail_section_comics),
                            items = comics,
                            itemClick = {
                                showSnackbarWithText("You should read : $it !")
                            }
                    )
            )
            notifyDataSetChanged()
        }
    }

    private fun showCharacterLoadError(errorMessage: String) {
        Snackbar.make(
                characterList,
                errorMessage,
                Snackbar.LENGTH_LONG)
                .setAction(
                        "Retry",
                        View.OnClickListener {
                            loadCharacterDetail(viewModel = viewModel!!)
                        }
                )
                .show()
    }

    private fun showSnackbarWithText(snackbarText: String) {
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
