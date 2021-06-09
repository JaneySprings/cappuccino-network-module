package com.springsoftware.network.coverartarchive

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class CoverService {
    suspend fun executeRequests(artist: String, title: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val meta = requestMeta(arrayOf(artist, title)) ?: return@withContext null
                var mbid = ""

                for (i in 0 until (meta.recordings?.size ?: 0)) {
                    if (similarRatio(title, meta.recordings!![i].releases[0].title) +
                            similarRatio(artist, meta.recordings[i].artistCredit[0].name) > 1.2f) {
                        mbid = meta.recordings[i].releases[0].id
                        break
                    }
                }

                if (mbid.isEmpty()) return@withContext null

                val cover = requestCover(mbid) ?: return@withContext null
                return@withContext cover.images[0].thumbnails.large
            } catch (e: Exception) { return@withContext null }
        }
    }

    private fun requestMeta(params: Array<String>): MusicBrainzResponse? {
        val requestModel = Request.Builder()
                .url(URL("${MUSIC_BRAINZ_BASE_URL}artist:${params[0]}+recording:${params[1]}&${MUSIC_BRAINZ_RET_ARGS}"))
                .get()
                .addHeader("User-Agent", MUSIC_BRAINZ_CREDENTIALS)
                .build()
        val responseModel = OkHttpClient().newCall(requestModel).execute().body!!.string()

        return try {
            Gson().fromJson(responseModel, MusicBrainzResponse::class.java)
        } catch (e: Exception) { null }
    }
    private fun requestCover(id: String): CoverArtResponse? {
        val requestCover = Request.Builder()
                .url(URL("${COVER_ART_ARCHIVE_BASE_URL}/${id}"))
                .get()
                .addHeader("User-Agent", MUSIC_BRAINZ_CREDENTIALS)
                .build()
        val responseCover = OkHttpClient().newCall(requestCover).execute().body!!.string()
        return try {
            Gson().fromJson(responseCover, CoverArtResponse::class.java)
        } catch (e: Exception) { null }
    }

    private fun similarRatio(example: String, compare: String): Float {
        val tokens = compare.split(" ", "\'")
        var current = 0f

        for(i in tokens.indices)
            if (example.contains(tokens[i], true)) current++

        return current / tokens.size
    }

    companion object {
        private const val MUSIC_BRAINZ_CREDENTIALS = "Cappuccino/1.0.0 ( alis228300@yandex.ru )"
        private const val MUSIC_BRAINZ_BASE_URL = "https://musicbrainz.org/ws/2/recording?query="
        private const val MUSIC_BRAINZ_RET_ARGS = "limit=10&fmt=json"

        private const val COVER_ART_ARCHIVE_BASE_URL = "https://coverartarchive.org/release"

    }
}