/**
 *
 *	MIT License
 *
 *	Copyright (c) 2023 Gautam Hazarika
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 *
 **/

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