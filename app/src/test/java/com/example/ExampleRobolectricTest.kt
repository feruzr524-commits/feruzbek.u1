package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read strings from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertEquals("Free UC Prank", context.getString(R.string.app_name))
        assertEquals("FREE UC PRANK 😂", context.getString(R.string.site_title))
        assertEquals("TEKIN UC OLISH 😈", context.getString(R.string.home_headline))
        assertEquals("UC hisoblanmoqda...", context.getString(R.string.loading_title))
        assertEquals("🎉 600 UC TUSHDI!", context.getString(R.string.uc_dropped_title))
        assertEquals("UC muvaffaqiyatli qo‘shildi!", context.getString(R.string.uc_dropped_subtitle))
        assertEquals("😂 ALDANDIMMM!", context.getString(R.string.prank_title))
        assertEquals(
            "Senga tekin UC beradigan jinni yo‘q edi 😂😂",
            context.getString(R.string.prank_subtitle_1)
        )
        assertEquals(
            "Bu shunchaki PRANK DEMO edi 😎",
            context.getString(R.string.prank_subtitle_2)
        )
        assertEquals(
            "QAYTADAN SINAB KO‘RISH 🔄",
            context.getString(R.string.retry_button)
        )
    }
}
