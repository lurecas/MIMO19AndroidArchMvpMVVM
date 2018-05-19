package es.upsa.mimo.mimo18_androidarch.list.viewModel

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.spy
import es.upsa.mimo.mimo18_androidarch.list.model.CharacterListBindingModel
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.Character
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.CharacterDataContainer
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.CharactersResponse
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.Image
import es.upsa.mimo.mimo18_androidarch.marvel.repository.MarvelDataSource
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class CharacterListViewModelTest {

    inline fun <reified T> lambdaMock(): T = mock(T::class.java)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    var repo: MarvelRepoTestDouble = spy(MarvelRepoTestDouble())

    lateinit var viewModel: CharacterListViewModel

    @Before
    fun setUp() {
        viewModel = CharacterListViewModel(
                mock(Application::class.java),
                repo,
                mock(ImageLoader::class.java))
    }

    @Test
    fun viewModelTransformationWorks() {

        val character = mockCharacterResponse().data?.results?.first()

        repo.mockedCharReturn = character

        val lifecycleOwner = createAndStartLifecycleOwner()

        val observerFunction = lambdaMock<(List<CharacterListBindingModel>?) -> Unit>()
        val observer = Observer<List<CharacterListBindingModel>?> {
            observerFunction.invoke(it)
        }

        viewModel
                .getCharacterList()
                .observe(lifecycleOwner, observer)

        val argumentCaptor = argumentCaptor<List<CharacterListBindingModel>>()

        verify(observerFunction).invoke(argumentCaptor.capture())
        verify(repo).getCharacterList()
        assertThat(argumentCaptor.firstValue[0].name, `is`(TEST_CHARACTER_NAME))

    }


    private fun createAndStartLifecycleOwner(): LifecycleOwner {
        val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        return LifecycleOwner {
            lifecycle
        }
    }

    private fun mockCharacterResponse(): CharactersResponse {
        return Character(
                id = TEST_CHAR_ID,
                name = TEST_CHARACTER_NAME,
                description = TEST_CHARACTER_DESCRIPTION,
                thumbnail = Image(
                        path = TEST_CHARACTER_THUMBNAIL_PATH,
                        extension = TEST_CHARACTER_THUMBNAIL_EXTENSION)
        ).let { character ->
            CharacterDataContainer(
                    count = 1,
                    results = arrayOf(character)
            )
        }.let { dataContainer ->
            CharactersResponse(
                    code = 200,
                    data = dataContainer
            )
        }
    }

    companion object {
        private const val TEST_CHAR_ID = "1"
        private const val TEST_CHARACTER_NAME = "Test Name"
        private const val TEST_CHARACTER_DESCRIPTION = "Test Description"
        private const val TEST_CHARACTER_THUMBNAIL_PATH = "https://ep01.epimg.net/internacional/imagenes/2018/05/04/actualidad/1525434241_941769_1525434417_noticia_normal_recorte1"
        private const val TEST_CHARACTER_THUMBNAIL_EXTENSION = "jpg"
    }

}

open class MarvelRepoTestDouble : MarvelDataSource {

    var mockedCharReturn: Character? = null

    override fun getCharacterList(): LiveData<List<Character>?> {
        return MutableLiveData<List<Character>?>().apply {
            mockedCharReturn?.let {
                this.value = listOf(it)
            }
        }
    }

    override fun getCharacterDetail(characterID: String): LiveData<Character?> {
        return MutableLiveData<Character?>().apply {
            this.value = mockedCharReturn
        }
    }

    override fun searchCharacters(name: String): LiveData<List<Character>?> {
        return MutableLiveData<List<Character>?>().apply {
            mockedCharReturn?.let {
                this.value = listOf(it)
            }
        }
    }

}