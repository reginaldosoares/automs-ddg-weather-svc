package app.automs.automation

import app.automs.sdk.domain.AutomationRecipe
import app.automs.sdk.domain.http.AutomationRequest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/automation")
class AutomationApi(val automation: AutomationFunction) {

    @ResponseBody
    @PostMapping(value = ["/run"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun runAutomation(@RequestBody automationRequest: AutomationRequest): ResponseEntity<AutomationRecipe> {
        val recipe = AutomationRecipe.createFrom(automationRequest)
        recipe.response = automation.run(AutomationRecipe.createFrom(automationRequest))
        return automation.withResponseEntity(recipe)
    }

}
