package app.automs.automation

import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class DefaultControllerIT {

    @Test
    fun respondsToHttpRequest() {
        var port = System.getenv("PORT")
        if (port == null || port === "") {
            port = "8080"
        }
        var url = System.getenv("SERVICE_URL")
        if (url == null || url === "") {
            url = "http://localhost:$port"
        }
        val httpClient =
            OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build()

        val request = Request.Builder().url("$url/").get().build()
        val expected = "Not Found"
        val response = httpClient.newCall(request).execute()


        assertAll(
            "expected responses",
            { assertTrue(response.body()?.string()?.contains(expected) ?: false) },
            { assertEquals(404, response.code()) }
        )
    }
}
