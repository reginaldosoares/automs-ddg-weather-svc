package app.automs.automation

import app.automs.sdk.StromAutomation
import app.automs.sdk.domain.AutomationRecipe
import app.automs.sdk.domain.http.*
import app.automs.sdk.domain.store.SessionFile
import app.automs.sdk.domain.store.StoreExtension
import com.google.gson.Gson
import kong.unirest.Unirest
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.apache.commons.lang3.StringUtils.stripAccents
import org.openqa.selenium.By
import org.openqa.selenium.By.*
import org.openqa.selenium.OutputType.BYTES
import org.openqa.selenium.WebElement
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("prototype")
class AutomationFunction(private val gson: Gson) : StromAutomation<AutomationOutput>() {

    @Value("\${geohash.api.url}")
    var geoHashApi: String = ""

    override fun before(recipe: AutomationRecipe): AutomationInput =
        run {
            val args = recipe.automationInput.inputParams
            val geoHash = requireNotNull(args["geoHash"]) { "missing required param [geoHash]" }

            AutomationInput.create(
                Promise.of(GlobalScope.async { apiCall(geoHash) }),
                args
            )
        }

    override fun process(input: AutomationInput): AutomationResponse<out ResponseOutput> =
        runBlocking {
            with(driver) {
                val args = input.inputParams
                val city = requireNotNull(args["city"]) { "missing required param [city]" }

                with(findElement(id("search_form_input_homepage"))) {
                    click()
                    clear()
                    sendKeys("weather in $city")
                    submit()
                }

                // check if the city weather box was found
                val (notFoundCityWeather, _) = xpath("$DIV_BOX/div") contains city

                if (notFoundCityWeather) {
                    AutomationResponse.withEmptyOutput().apply {
                        customResponse = "weather for $city not found"
                    }
                } else {
                    // choose Celsius
                    findElement(linkText("C")).click()

                    val weatherTopDialog = findElement(xpath(DIV_BOX))
                    val weatherFullDialog = findElement(xpath(DIV_BOX.removeSuffix("/div/div")))
                    val weatherInternalDialog = weatherTopDialog.findElement(xpath("div[2]/div")).text.split("•")

                    // set recaptcha hidden elements values
                    log.info("Await: I'm working in thread ${Thread.currentThread().name}")
                    val apiResponse = input.promise.await()

                    // adding automation user defined file
                    val sessionFile1 = SessionFile.create("sample text content".toByteArray())
                    val sessionFile2 = SessionFile.create(apiResponse.asJsonBytes(), StoreExtension.JSON)
                    val sessionFile3 = SessionFile.create(weatherFullDialog.getScreenshotAs(BYTES), StoreExtension.PNG)

                    val output = weatherTopDialog.getCaptureOutput(weatherInternalDialog, apiResponse)

                    AutomationResponse(output)
                        .apply {
                            customResponse = responseEntity.successMessage
                            sessionFiles = listOf(sessionFile1, sessionFile2, sessionFile3)
                        }
                }
            }
        }

    override fun validate(response: AutomationResponse<AutomationOutput>): AutomationValidation =
        with(response.responseEntity) {
            val validations =
                listOf(
                    if (temperature.isBlank()) "required temperature is blank" else null,
                    if (date.isBlank()) "required date is blank" else null,
                    if (temperature.removeSuffix("°").toInt() < 15) "temp must be greater than 15" else null
                )
            validations.filterNotNull().let { AutomationValidation(it.isEmpty(), it) }
        }

    override fun entryPointUrl() = "https://duckduckgo.com/"

    private fun apiCall(content: String) =
        log.info("Create: I'm working in thread ${Thread.currentThread().name}")
            .run { Unirest.get("$geoHashApi/$content").asString().body }

    private suspend fun Deferred<*>?.await() =
        requireNotNull(
            this?.await()?.let { geoInfo -> gson.fromJson(geoInfo as String, GeoHashResponse::class.java) }
        ) { "Invalid GeoHash information" }

    private fun GeoHashResponse.asJsonBytes() = gson.toJson(this).toByteArray()

    private infix fun By.contains(message: String) =
        if (driver.findElements(this).size > 0) {
            val siteContent = driver.findElement(this).text ?: ""
            if (stripAccents(siteContent).contains(message, ignoreCase = true)) (false to siteContent) else NOT_FOUND
        } else NOT_FOUND

    companion object {
        const val DIV_BOX = "//div[@id='links_wrapper']/div/div[3]/div/div/div/div"

        private val NOT_FOUND = true to ""

        private val AutomationOutput.successMessage
            get() = "[$temperature]C weather/geo at $date captured"

        private val AutomationInput.promise get() = completable?.value as? Deferred<*>

        private fun WebElement.getCaptureOutput(dialog: List<String>, apiResponse: GeoHashResponse): AutomationOutput =
            AutomationOutput(
                temperature = findElement(xpath("div[3]/div/div")).text.removeSuffix("°"),
                date = dialog[0].trim(),
                sky = dialog[1].trim(),
                wind = findElement(xpath("div[4]/div[2]")).text.removePrefix("Wind: "),
                humidity = findElement(xpath("div[4]/div[1]")).text.removePrefix("Humidity: "),
                coordinates = apiResponse.run { "$lat / $lon" }
            )
    }
}
