package com.example.windeck.ui.data

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

data class TopUser(val username: String, val total_points: Int)

data class SubjectInfo(val subject: String, val sequence: String)

data class Achievement(val name: String, val description: String, val points: Int)

data class FinishedChaptersResponse(val chapters: List<Double>)



class Repository {

    companion object {
        private val gson = Gson()

        suspend fun register(username: String, password: String): HttpResponse? =
            ApiClientStyled.registerRequest(username, password)

        suspend fun login(username: String, password: String): HttpResponse? =
            ApiClientStyled.loginRequest(username, password)

        suspend fun kiPath(questionAnswer: String): HttpResponse? =
            ApiClientStyled.kiPathRequest(questionAnswer)

        suspend fun kiFeedback(questionAnswer: String): String?
            {
                var response = ApiClientStyled.kiFeedbackRequest(questionAnswer)
                if(response != null)
                {
                    val complexRegex = Regex(""""text":\s*"((?:\\.|[^"\\])*)"""")
                    val complexMatchResult = complexRegex.find(response!!.bodyAsText())
                    val complexExtractedText = complexMatchResult?.groups?.get(1)?.value?.replace("\\\"", "\"")?.replace("\\n", "\n") // Manuelles Ersetzen von Escapes
                    println("Mit komplexerem Regex extrahiert: $complexExtractedText")
                    return complexExtractedText
                }
                else
                {
                    return null
                }

            }


        suspend fun saveSubjectSequence(subject: String, sequence: String): HttpResponse? =
            ApiClientStyled.saveSubjectSequenceRequest(subject, sequence)

        suspend fun saveFinishedChapter(subject: String, chapter: String): HttpResponse? =
            ApiClientStyled.saveFinishedChapterRequest(subject, chapter)

        suspend fun updateLastUsedSubject(subject: String): HttpResponse? =
            ApiClientStyled.updateLastUsedOfSubjectRequest(subject)

        suspend fun getTopAchievementUsers(): List<TopUser>? {
            val jsonString = ApiClientStyled.getTopAchievementUsersRequest() ?: return null
            return try {
                // Definiert den Typ List<TopUser> für Gson
                val listType = object : TypeToken<List<TopUser>>() {}.type
                gson.fromJson(jsonString, listType)
            } catch (e: JsonSyntaxException) {
                println("Error parsing TopUser JSON: ${e.message}")
                null // Fehler beim Parsen
            } catch (e: Exception) {
                println("An unexpected error occurred during TopUser parsing: ${e.message}")
                null
            }
        }

        suspend fun getLastUsedSubject(): SubjectInfo? {
            val jsonString = ApiClientStyled.getLastUsedSubjectRequest() ?: return null
            return try {
                // Erwartet ein einzelnes SubjectInfo-Objekt
                gson.fromJson(jsonString, SubjectInfo::class.java)
            } catch (e: JsonSyntaxException) {
                println("Error parsing SubjectInfo JSON: ${e.message}")
                null
            } catch (e: Exception) {
                println("An unexpected error occurred during SubjectInfo parsing: ${e.message}")
                null
            }
        }

        suspend fun getAllFinishedChapters(subject: String): List<Double>? {
            val jsonString = ApiClientStyled.getAllFinishedChaptersRequest(subject) ?: return null
            println(jsonString)
            return try {
                // Erwartet ein Objekt { chapters: ["...", "..."] }
                val responseType = object : TypeToken<FinishedChaptersResponse>() {}.type
                val parsedResponse: FinishedChaptersResponse? = gson.fromJson(jsonString, responseType)
                parsedResponse?.chapters // Gibt nur die Liste der Kapitel zurück
            } catch (e: JsonSyntaxException) {
                println("Error parsing FinishedChapters JSON: ${e.message}")
                null
            } catch (e: Exception) {
                println("An unexpected error occurred during FinishedChapters parsing: ${e.message}")
                null
            }
        }

        suspend fun getAllUserAchievements(): List<Achievement>? {
            val jsonString = ApiClientStyled.getAllAchievementsOfUserRequest() ?: return null
            return try {
                val listType = object : TypeToken<List<Achievement>>() {}.type
                gson.fromJson(jsonString, listType)
            } catch (e: JsonSyntaxException) {
                println("Error parsing Achievement JSON: ${e.message}")
                null
            } catch (e: Exception) {
                println("An unexpected error occurred during Achievement parsing: ${e.message}")
                null
            }
        }
    }
}