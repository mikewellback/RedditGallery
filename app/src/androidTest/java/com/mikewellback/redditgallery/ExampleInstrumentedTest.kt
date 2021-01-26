package com.mikewellback.redditgallery

import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mikewellback.redditgallery.api.RedditRetrofit

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.mikewellback.redditgallery", appContext.packageName)
    }

    @Test
    fun testAPI() {
        val posts = RedditRetrofit.service.getTopPosts("cats").execute()
        assert(posts.isSuccessful)
        val body = posts.body()
        assertNotNull(body)
        body!!.data.children.forEach { sp ->
            assertTrue(sp.data.author.isNotBlank())
            assertTrue(sp.data.title.isNotBlank())
            assertTrue(sp.data.permalink.isNotBlank())
            assertTrue(sp.data.subreddit.isNotBlank())
            Log.d("API", "Sub: ${sp.data.subreddit}, Link: https://reddit.com${sp.data.permalink}")
            Log.d("API", "Content: ${HtmlCompat.fromHtml(sp.data.selftext_html ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)}")
            Log.d("API", "Time diff: ${sp.data.created_utc - sp.data.created}, UTC: ${sp.data.created_utc}, Local: ${sp.data.created}")
            assertEquals(sp.data.score, sp.data.ups - sp.data.downs)
            Log.d("API", "Vote ratio: ${sp.data.upvote_ratio}, U/D: ${sp.data.ups}/${sp.data.downs}, Score: ${sp.data.score}")
            //assertFalse(it.data.is_gallery ?: false)
            //assertFalse(it.data.is_video ?: false)
            assertTrue(sp.data.num_comments >= 0)
            if (sp.data.preview != null && sp.data.preview!!.images.isNotEmpty()) {
                sp.data.preview!!.images.forEach { pi ->
                    assertTrue(pi.source.url.isNotBlank())
                    assertTrue(pi.source.width > 0)
                    assertTrue(pi.source.height > 0)
                }
            }
        }
    }
}