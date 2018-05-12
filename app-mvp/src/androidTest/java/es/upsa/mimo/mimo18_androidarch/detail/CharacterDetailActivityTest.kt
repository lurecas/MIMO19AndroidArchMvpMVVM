package es.upsa.mimo.mimo18_androidarch.detail

import android.app.Activity
import android.content.Intent
import android.support.design.widget.CollapsingToolbarLayout
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.DispatchingAndroidInjector_Factory
import es.upsa.mimo.mimo18_androidarch.MarvelApplication
import es.upsa.mimo.mimo18_androidarch.espresso_utils.CollapsingToolbarViewMatcher
import es.upsa.mimo.mimo18_androidarch.marvel.MarvelApi
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.Character
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.CharacterDataContainer
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.CharactersResponse
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.Image
import es.upsa.mimo.mimo18_androidarch.util.HashGenerator
import es.upsa.mimo.mimo18_androidarch.util.ImageLoaderImpl
import es.upsa.mimo.mimo18_androidarch.util.TimestampProvider
import org.hamcrest.CoreMatchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.any
import org.mockito.Matchers.anyLong
import org.mockito.Matchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Provider


@RunWith(AndroidJUnit4::class)
class CharacterDetailActivityTest {

    fun createFakeCharacterListActivityInjector(block: CharacterDetailActivity.() -> Unit)
            : DispatchingAndroidInjector<Activity> {

        val injector = AndroidInjector<Activity> { instance ->
            if (instance is CharacterDetailActivity) {
                instance.block()
            }
        }

        val factory = AndroidInjector.Factory<Activity> { injector }

        val map = mapOf(Pair<Class<out Activity>,
                Provider<AndroidInjector.Factory<out Activity>>>(CharacterDetailActivity::class.java, Provider { factory }))

        return DispatchingAndroidInjector_Factory.newDispatchingAndroidInjector(map)
    }

    @get:Rule
    val activityTestRule = object : ActivityTestRule<CharacterDetailActivity>(
            CharacterDetailActivity::class.java,
            true,
            // false: do not launch the activity immediately
            false) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            val myApp = InstrumentationRegistry.getTargetContext().applicationContext as MarvelApplication
            myApp.dispatchingAndroidInjector = createFakeCharacterListActivityInjector {
                mockCharacterResponse()
                // Set up the stub we want to return in the mock
                presenter = mockedPresenter
                imageLoader = ImageLoaderImpl(this)
                androidResources = this.resources
            }
        }

    }

    private fun buildCharacterDetailStartIntent(): Intent {
        val context = InstrumentationRegistry.getTargetContext().applicationContext
        return Intent(context, CharacterDetailActivity::class.java).apply {
            putExtra(CharacterDetailActivity.BUNDLE_CHARACTER_ID, TEST_CHAR_ID)
        }
    }

    private fun mockHashGenerator(): HashGenerator {
        return mock(HashGenerator::class.java).apply {
            `when`(this.generate(
                    timestamp = anyLong(),
                    privateKey = anyString(),
                    publicKey = anyString())).thenReturn("")
        }
    }

    private fun buildPresenter(): CharacterDetailPresenter {
        return CharacterDetailPresenter(
                api = mockApi(),
                hashGenerator = mockHashGenerator(),
                timestampProvider = mock(TimestampProvider::class.java)
        )
    }

    var charactersResponse: CharactersResponse? = null

    var mockedPresenter: CharacterDetailPresenter = buildPresenter()

    private fun mockCharacterResponse() {
        charactersResponse = Character(
                id = TEST_CHAR_ID,
                name = TEST_CHARACTER_NAME,
                description = TEST_CHARACTER_DESCRIPTION,
                thumbnail = Image(path = TEST_CHARACTER_THUMBNAIL_PATH, extension = TEST_CHARACTER_THUMBNAIL_EXTENSION)
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

    private fun mockApi(): MarvelApi {

        val marvelApi = mock(MarvelApi::class.java)

        val mockedCall: Call<CharactersResponse> = mock(Call::class.java) as Call<CharactersResponse>

        `when`(marvelApi.getCharacterList(
                hash = anyString(),
                timestamp = anyLong(),
                apiKey = anyString())).thenReturn(mockedCall)

        `when`(marvelApi.getCharacterDetail(
                hash = anyString(),
                timestamp = anyLong(),
                apiKey = anyString(),
                characterId = anyString())).thenReturn(mockedCall)

        doAnswer(object : Answer<Any> {
            override fun answer(invocation: InvocationOnMock): Any? {
                val callback = invocation.getArgumentAt(0, Callback::class.java) as Callback<CharactersResponse>
                callback.onResponse(mockedCall, Response.success(charactersResponse))
                return null
            }
        }).`when`(mockedCall).enqueue(any(Callback::class.java) as Callback<CharactersResponse>?)

        return marvelApi

    }

    @Test
    fun characterTestIsAddedToTheScreen() {
        activityTestRule.launchActivity(buildCharacterDetailStartIntent())

        onView(isAssignableFrom(
                CollapsingToolbarLayout::class.java
        )).check(
                matches(CollapsingToolbarViewMatcher.withCollapsibleToolbarTitle(`is`(TEST_CHARACTER_NAME)))
        )

    }

    companion object {

        private val TEST_CHAR_ID = "1"
        private val TEST_CHARACTER_NAME = "Test Name"
        private val TEST_CHARACTER_DESCRIPTION = "Test Description"
        private val TEST_CHARACTER_THUMBNAIL_PATH = "https://ep01.epimg.net/internacional/imagenes/2018/05/04/actualidad/1525434241_941769_1525434417_noticia_normal_recorte1"
        private val TEST_CHARACTER_THUMBNAIL_EXTENSION = "jpg"
    }

}