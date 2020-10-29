package app.automs.automation

import app.automs.sdk.domain.http.AutomationRequest
import app.automs.sdk.domain.http.AutomationResponse
import com.google.gson.Gson
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AutomationFunctionTests {
    @Autowired
    private val mvc: MockMvc? = null

    @MockBean
    private val sampleRecipeAutomation: AutomationFunction? = null

    private val gson = Gson()

    @Test
    @Disabled("test refactoring needed")
    fun automationRunTest() {
        `when`(sampleRecipeAutomation!!.run(any()))
            .thenReturn(automationResponse)

        mvc?.perform(post("/api/automation/run/")
            .contentType(APPLICATION_JSON)
            .content(gson.toJson(automationRequest))
        )
            ?.andExpect(status().isOk)
            ?.andExpect(content().contentType(APPLICATION_JSON_VALUE))
    }

    private val automationResponse
        get() =
            AutomationResponse(
                AutomationOutput(
                    temperature = "13",
                    date = "05/06",
                    sky = "Clean",
                    wind = "North",
                    humidity = "55%",
                    coordinates = "12Lat 13Lon")
            ).apply { customResponse = "OK Message" }


    private val automationRequest
        get() = AutomationRequest().apply {
            orderId = "1234"
            requestId = "4321"
            automationResourceId = "publisher/sample-automation"
            inputParams = mapOf("cnpj" to "42266122000174")
        }

}
