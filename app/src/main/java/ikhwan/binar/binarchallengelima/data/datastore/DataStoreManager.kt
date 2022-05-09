package ikhwan.binar.binarchallengelima.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    suspend fun setUser(email : String){
        context.userDataStore.edit {
            it[USER_KEY] = email
        }
    }

    suspend fun setViewHome(boolean: Boolean){
        context.viewDataStore.edit {
            it[VIEW_KEY] = boolean
        }
    }

    suspend fun setImageCamera(img : String){
        context.imageDataStore.edit {
            it[IMAGE_KEY] = img
        }
    }

    suspend fun setImageGallery(img: String){
        context.galleryDataStore.edit {
            it[GALLERY_KEY] = img
        }
    }

    fun getUser() : Flow<String>{
        return context.userDataStore.data.map {
            it[USER_KEY] ?: ""
        }
    }

    fun getBoolean() : Flow<Boolean>{
        return context.viewDataStore.data.map {
            it[VIEW_KEY] ?: false
        }
    }

    fun getImage() : Flow<String>{
        return context.imageDataStore.data.map {
            it[IMAGE_KEY] ?: ""
        }
    }

    fun getImageGallery() : Flow<String>{
        return context.galleryDataStore.data.map {
            it[GALLERY_KEY] ?: ""
        }
    }

    companion object {
        private const val USERDATA_NAME = "user_preferences"
        private const val VIEWDATA_NAME = "view_preferences"
        private const val IMAGEDATA_NAME = "camera_preferences"
        private const val GALLERYDATA_NAME = "gallery_preferences"


        private val USER_KEY = stringPreferencesKey("user_key")
        private val VIEW_KEY = booleanPreferencesKey("view_key")
        private val IMAGE_KEY = stringPreferencesKey("camera_key")
        private val GALLERY_KEY = stringPreferencesKey("gallery_key")

        private val Context.userDataStore by preferencesDataStore(
            name = USERDATA_NAME
        )

        private val Context.viewDataStore by preferencesDataStore(
            name = VIEWDATA_NAME
        )

        private val Context.imageDataStore by preferencesDataStore(
            name = IMAGEDATA_NAME
        )

        private val Context.galleryDataStore by preferencesDataStore(
            name = GALLERYDATA_NAME
        )
    }
}