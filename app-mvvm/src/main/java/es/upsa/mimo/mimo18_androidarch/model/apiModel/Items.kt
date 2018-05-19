package es.upsa.mimo.mimo18_androidarch.model.apiModel

import com.google.gson.annotations.SerializedName

data class Items(
        @SerializedName(value = "resourceURI") var resourceURI: String? = null,
        @SerializedName(value = "name") var name: String? = null
)