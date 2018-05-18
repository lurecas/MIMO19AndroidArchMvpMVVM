package es.upsa.mimo.mimo18_androidarch.marvel.bindingModel

import android.databinding.BindingAdapter
import android.widget.ImageView
import es.upsa.mimo.mimo18_androidarch.R
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader


data class CharacterBindingModel(
        val id: String,
        val name: String,
        val description: String? = null,
        val imageUrl: String? = null,
        val series: List<String> = emptyList(),
        val stories: List<String> = emptyList(),
        val comics: List<String> = emptyList(),
        val imageLoader: ImageLoader
) {

    fun isAnAvenger(): Boolean = AVENGER_LIST.contains(name)

    companion object {
        private val AVENGER_LIST = listOf(
                "Iron Man",
                "3-D Man",
                "Captain America",
                "Thor",
                "Hulk",
                "Black Widow",
                "Hawkeye")

        @BindingAdapter(value = ["imageUrl", "imageLoader"])
        @JvmStatic
        fun loadImage(
                imageView: ImageView,
                imageUrl: String?,
                imageLoader: ImageLoader?
        ) {
            if (imageUrl != null && imageLoader != null) {
                imageLoader.loadImageFittedToImageView(
                        imageUrl = imageUrl,
                        imageView = imageView
                )
            } else{
                imageView.setImageResource(R.mipmap.avengers_logo)
            }
        }

    }

}