package es.upsa.mimo.mimo18_androidarch.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import dagger.android.AndroidInjection
import es.upsa.mimo.mimo18_androidarch.R
import es.upsa.mimo.mimo18_androidarch.detail.CharacterDetailActivity
import es.upsa.mimo.mimo18_androidarch.marvel.bindingModel.CharacterBindingModel
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

        viewModel.getCharacterList().observe(this, Observer<List<CharacterBindingModel>> {

            it?.let {
                listAdapter?.characters(it)
            }


        })

//        // Update the list when the data changes
//        viewModel.getProjectListObservable().observe(this, object : Observer<List<Project>> {
//            override fun onChanged(projects: List<Project>?) {
//                if (projects != null) {
//                    binding.setIsLoading(false)
//                    projectAdapter.setProjectList(projects)
//                }
//            }
//        })
    }


//    private fun fetchMarvelCharacters(adapter: CharacterListAdapter) {
//
//        val timestamp = timestampProvider.getTimestamp()
//
//        api.getCharacterList(
//                hash = hashGenerator.generate(
//                        timestamp = timestamp,
//                        privateKey = MarvelApiConstants.PRIVATE_API_KEY,
//                        publicKey = MarvelApiConstants.PUBLIC_API_KEY)!!,
//                timestamp = timestamp,
//                apiKey = MarvelApiConstants.PUBLIC_API_KEY
//        ).enqueue(object : Callback<CharactersResponse> {
//            override fun onResponse(call: Call<CharactersResponse>,
//                                    response: Response<CharactersResponse>) {
//
//                if (response.isSuccessful) {
//                    val characterList = response.body()!!.data!!.results!!.toList()
//                    adapter.characters(characterList)
//
//                } else {
//                    Snackbar.make(
//                            characterList,
//                            "Error obtaining characters",
//                            Snackbar.LENGTH_LONG
//                    ).show()
//                }
//
//            }
//
//            override fun onFailure(call: Call<CharactersResponse>, t: Throwable) {
//                Log.e(TAG, "Error fetching cat images", t)
//            }
//        })
//    }

    companion object {
        private val TAG = CharacterDetailActivity::class.java.canonicalName
    }

}
