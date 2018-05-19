package es.upsa.mimo.mimo18_androidarch.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import es.upsa.mimo.mimo18_androidarch.R
import es.upsa.mimo.mimo18_androidarch.detail.CharacterDetailActivity
import es.upsa.mimo.mimo18_androidarch.list.viewModel.CharacterListViewModel
import es.upsa.mimo.mimo18_androidarch.list.model.CharacterListBindingModel
import es.upsa.mimo.mimo18_androidarch.util.ActivityNavigator
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader
import kotlinx.android.synthetic.main.character_list_activity.*
import kotlinx.android.synthetic.main.view_recycler_view.*
import javax.inject.Inject

class CharacterListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var navigator: ActivityNavigator

    private var listAdapter: CharacterListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_list_activity)

        setSupportActionBar(toolbar)

        injectDependencies()

        listAdapter = CharacterListAdapter(
                itemListener = object : CharacterListAdapter.OnItemClickListener {

                    override fun onItemClicked(charId: String) {
                        navigator.openCharacterActivity(charId = charId)
                    }

                })
        characterList.adapter = listAdapter


        val viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(CharacterListViewModel::class.java)

        observeViewModel(viewModel)
    }

    private fun injectDependencies() {
        AndroidInjection.inject(this)
    }


    private fun observeViewModel(viewModel: CharacterListViewModel) {

        viewModel.getCharacterList().observe(
                this,
                Observer<List<CharacterListBindingModel>?> {

                    it?.let {
                        listAdapter?.characters(it)
                    }

                })

    }

    companion object {
        private val TAG = CharacterDetailActivity::class.java.canonicalName
    }

}
