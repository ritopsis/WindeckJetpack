import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.ktor.http.isSuccess
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object ApiClientStyled {

    private const val SERVER_ADDRESS = "http://10.0.2.2:3000"
    // "https://wiry-alpine-harpymimus.glitch.me"
    // "http://10.0.2.2:3000"

    // Ktor HttpClient ohne ContentNegotiation, da wir JSON manuell erstellen
    val client = HttpClient(CIO) {
        // Optional: Standard-Timeout-Einstellungen etc. hinzufügen
    }

    var sessionId: String? = null
    var ownerUsername: String = ""

    private fun HttpRequestBuilder.applySessionCookie() {
        sessionId?.let { sidValue ->
            // Füge den Cookie-Header im Standardformat hinzu
            header(HttpHeaders.Cookie, "connect.sid=$sidValue")
            println("Session ID als Header gesetzt: connect.sid=$sidValue")
        }
    }

    // Helper zum Generieren des Datumsstrings im ISO 8601 Format (UTC)
    private fun getCurrentUtcDateTimeString(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .withZone(ZoneOffset.UTC)
        return formatter.format(Instant.now())
    }


    suspend fun getTopAchievementUsersRequest(): String? {
        return try {
            val response: HttpResponse = client.get("$SERVER_ADDRESS/topUserAchievements") {
                applySessionCookie()
            }
            println("GET /topUserAchievements - Response: ${response.status}")
            val body = response.bodyAsText()
            println("Body: $body")
            if (response.status.isSuccess()) body else null // Nur bei Erfolg Body zurückgeben
        } catch (e: Exception) {
            println("GET /topUserAchievements - Request error: ${e.message}")
            null
        }
    }

    suspend fun getLastUsedSubjectRequest(): String? {
        return try {
            val response: HttpResponse = client.get("$SERVER_ADDRESS/lastUsedSubject") {
                applySessionCookie()
            }
            println("GET /lastUsedSubject - Response: ${response.status}")
            val body = response.bodyAsText()
            println("Body: $body")
            if (response.status.isSuccess()) body else null
        } catch (e: Exception) {
            println("GET /lastUsedSubject - Request error: ${e.message}")
            null
        }
    }

    suspend fun getAllFinishedChaptersRequest(subject: String): String? {
        return try {
            val response: HttpResponse = client.get("$SERVER_ADDRESS/allfinishedchapters?subject=${subject.encodeURLParameter()}") {
                applySessionCookie()
            }
            println("GET /allfinishedchapters?subject=$subject - Response: ${response.status}")
            val body = response.bodyAsText()
            println("Body: $body")
            if (response.status.isSuccess()) body else null
        } catch (e: Exception) {
            println("GET /allfinishedchapters - Request error: ${e.message}")
            null
        }
    }

    suspend fun getAllAchievementsOfUserRequest(): String? {
        return try {
            val response: HttpResponse = client.get("$SERVER_ADDRESS/allUserAchievements") {
                applySessionCookie()
            }
            println("GET /allUserAchievements - Response: ${response.status}")
            val body = response.bodyAsText()
            println("Body: $body")
            if (response.status.isSuccess()) body else null
        } catch (e: Exception) {
            println("GET /allUserAchievements - Request error: ${e.message}")
            null
        }
    }


    suspend fun registerRequest(username: String, password: String): HttpResponse? {
        return try {
            ownerUsername = username.lowercase() // Setze globalen Username
            val date = getCurrentUtcDateTimeString()
            // Manuelles Erstellen des JSON-Strings
            val jsonBody = """{"username": "$username", "password": "$password", "date": "$date"}"""
            println("Register Payload: $jsonBody")

            val response: HttpResponse = client.post("$SERVER_ADDRESS/register") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
            }
            println("POST /register - Response: ${response.status}")

            // Extrahiere Session ID, wenn Registrierung erfolgreich war (z.B. OK oder Created)
            if (response.status.isSuccess()) {
                val setCookieHeader = response.headers[HttpHeaders.SetCookie]
                setCookieHeader?.let { headerValue ->
                    // Finde den Cookie-Wert für 'connect.sid'
                    val sidCookie = headerValue.split(';')
                        .firstOrNull { it.trim().startsWith("connect.sid=") }
                    if (sidCookie != null) {
                        sessionId = sidCookie.trim().substringAfter("connect.sid=")
                        println("Session ID gespeichert: $sessionId")
                    }
                }
                println("Body: ${response.bodyAsText()}") // Body loggen bei Erfolg
            }
            response // Gebe die gesamte Antwort zurück
        } catch (e: Exception) {
            println("POST /register - Request error: ${e.message}")
            null
        }
    }

    suspend fun loginRequest(username: String, password: String): HttpResponse? {
        return try {
            ownerUsername = username.lowercase()
            // Manuelles Erstellen des JSON-Strings
            val jsonBody = """{"username": "$username", "password": "$password"}"""
            println("Login Payload: $jsonBody")

            val response: HttpResponse = client.post("$SERVER_ADDRESS/login") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
            }
            println("POST /login - Response: ${response.status}")

            if (response.status == HttpStatusCode.OK) {
                ownerUsername = username.lowercase() // Nur bei OK setzen?
                val setCookieHeader = response.headers[HttpHeaders.SetCookie]
                setCookieHeader?.let { headerValue ->
                    val sidCookie = headerValue.split(';')
                        .firstOrNull { it.trim().startsWith("connect.sid=") }
                    if (sidCookie != null) {
                        sessionId = sidCookie.trim().substringAfter("connect.sid=")
                        println("Session ID gespeichert: $sessionId")
                    }
                }
                println("Body: ${response.bodyAsText()}")
            }
            response
        } catch (e: Exception) {
            println("POST /login - Request error: ${e.message}")
            null
        }
    }

    suspend fun kiPathRequest(questionAnswer: String): HttpResponse? {
        return try {
            val jsonBody = """{"text": "$questionAnswer"}""" // TODO: JSON Escaping für questionAnswer?
            println("KI Path Payload: $jsonBody")

            val response: HttpResponse = client.post("$SERVER_ADDRESS/kipath") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
                applySessionCookie()
            }
            println("POST /kipath - Response: ${response.status}")
            if (response.status.isSuccess()) {
                println("Body: ${response.bodyAsText()}")
            }
            response
        } catch (e: Exception) {
            println("POST /kipath - Request error: ${e.message}")
            null
        }
    }

    suspend fun kiFeedbackRequest(questionAnswer: String): HttpResponse? {
        return try {
            val jsonBody = """{"text": "$questionAnswer"}""" // TODO: JSON Escaping für questionAnswer?
            println("KI Feedback Payload: $jsonBody")

            val response: HttpResponse = client.post("$SERVER_ADDRESS/kifeedback") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
                applySessionCookie()
            }
            println("POST /kifeedback - Response: ${response.status}")
            if (response.status.isSuccess()) {
                println("Body: ${response.bodyAsText()}")
            }
            response
        } catch (e: Exception) {
            println("POST /kifeedback - Request error: ${e.message}")
            null
        }
    }

    suspend fun saveSubjectSequenceRequest(subject: String, sequence: String): HttpResponse? {
        return try {
            val date = getCurrentUtcDateTimeString()
            val jsonBody = """{"subject": "$subject", "sequence": "$sequence", "date": "$date"}"""
            println("Save Sequence Payload: $jsonBody")

            val response: HttpResponse = client.post("$SERVER_ADDRESS/subjectsequence") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
                applySessionCookie()
            }
            println("POST /subjectsequence - Response: ${response.status}")
            if (response.status.isSuccess()) {
                println("Body: ${response.bodyAsText()}")
            }
            response
        } catch (e: Exception) {
            println("POST /subjectsequence - Request error: ${e.message}")
            null
        }
    }

    suspend fun saveFinishedChapterRequest(subject: String, chapter: String): HttpResponse? {
        return try {
            val adjustedChapter = chapter.replace(',', '.')
            val date = getCurrentUtcDateTimeString()
            val jsonBody = """{"subject": "$subject", "chapter": "$adjustedChapter", "date": "$date"}"""
            println("Save Chapter Payload: $jsonBody")

            val response: HttpResponse = client.post("$SERVER_ADDRESS/addfinishedchapter") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
                applySessionCookie()
            }
            println("POST /addfinishedchapter - Response: ${response.status}")
            if (response.status.isSuccess()) {
                println("Body: ${response.bodyAsText()}")
            }
            response
        } catch (e: Exception) {
            println("POST /addfinishedchapter - Request error: ${e.message}")
            null
        }
    }

    // --- PUT-Anfragen (Gibt HttpResponse? zurück) ---

    suspend fun updateLastUsedOfSubjectRequest(subject: String): HttpResponse? {
        return try {
            val date = getCurrentUtcDateTimeString()
            val jsonBody = """{"subject": "$subject", "date": "$date"}"""
            println("Update Subject Payload: $jsonBody")

            val response: HttpResponse = client.put("$SERVER_ADDRESS/updatelastUpdatedSubject") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
                applySessionCookie()
            }
            println("PUT /updatelastUpdatedSubject - Response: ${response.status}")
            if (response.status.isSuccess()) {
                println("Body: ${response.bodyAsText()}")
            }
            response
        } catch (e: Exception) {
            println("PUT /updatelastUpdatedSubject - Request error: ${e.message}")
            null
        }
    }
}