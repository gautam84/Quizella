package com.example.quizella.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.quizella.domain.repository.ScoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ScoreRepositoryImpl(context: Context) : ScoreRepository {

    private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
        name = POINTS
    )

    private val dataStore = context.userPreferencesDataStore

    override suspend fun setPoints(points: Int) {
        dataStore.edit { preference ->
            preference[POINTS_KEY] = points
        }
    }

    override val getPoints: Flow<Int>
        get() = dataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preference ->
            preference[POINTS_KEY] ?: 0
        }

    override val getTempPoints: Flow<Int>
        get() = dataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preference ->
            preference[TEMP_POINTS_KEY] ?: 0
        }

    override suspend fun setTempPoints(points: Int) {
        dataStore.edit { preference ->
            preference[TEMP_POINTS_KEY] = points
        }
    }

    override suspend fun clearTempPoints() {
        dataStore.edit { preference ->
            preference[TEMP_POINTS_KEY] = 0
        }
    }


    companion object {
        const val POINTS = "user_login_state"
        private val POINTS_KEY = intPreferencesKey("points")
        private val TEMP_POINTS_KEY = intPreferencesKey("temporary_points")

    }

}