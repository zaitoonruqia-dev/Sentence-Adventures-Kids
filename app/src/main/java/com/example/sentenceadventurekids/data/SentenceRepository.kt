package com.ruqiazaitoon.sentenceadventurekids.data

import com.ruqiazaitoon.sentenceadventurekids.BuildConfig
import com.ruqiazaitoon.sentenceadventurekids.model.AnimationType
import com.ruqiazaitoon.sentenceadventurekids.model.Sentence
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SentenceRepository {
    private val groqApiKey = BuildConfig.GROQ_API_KEY.trim()
    private val authorizationHeader = "Bearer $groqApiKey"
    
    private val logging = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
    }
    
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.groq.com/openai/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val apiService = retrofit.create(GroqApiService::class.java)

    suspend fun fetchSentencesForLevel(level: Int): List<Sentence> {
        if (groqApiKey.isBlank()) return fallbackSentences(level)

        return try {
            val response = apiService.getChatCompletion(
                authorizationHeader,
                GroqRequest(
                    messages = listOf(
                        Message("system", "You write safe, cheerful, age-appropriate reading practice for children. Avoid scary, violent, branded, political, medical, or adult topics."),
                        Message("user", levelPrompt(level))
                    )
                )
            )
            val content = response.choices.firstOrNull()?.message?.content ?: ""
            val parsed = parseGroqSentences(content, level)
            parsed.ifEmpty { fallbackSentences(level) }
        } catch (e: Exception) {
            fallbackSentences(level)
        }
    }

    private fun levelPrompt(level: Int): String {
        val guide = when (level) {
            1 -> "Level 1: exactly 3 tiny sentences, 2 to 3 words each, simple nouns and verbs."
            2 -> "Level 2: exactly 3 playful sentences, 4 to 5 words each, include one describing word."
            3 -> "Level 3: exactly 3 kid-friendly sentences, 6 to 8 words each, include and/or/because once."
            else -> "Level 4: exactly 3 connected story sentences, 7 to 10 words each, about one gentle adventure."
        }
        return """
            $guide
            Return only JSON in this exact shape:
            {"sentences":["sentence one","sentence two","sentence three"]}
            Rules:
            - No numbering, bullets, markdown, quotes inside sentences, emojis, or ending punctuation.
            - Use concrete action words such as run, jump, smile, eat, read, play, help.
            - Keep vocabulary familiar for early readers.
        """.trimIndent()
    }

    private fun parseGroqSentences(content: String, level: Int): List<Sentence> {
        val jsonStart = content.indexOf('{')
        val jsonEnd = content.lastIndexOf('}')
        val fromJson = if (jsonStart >= 0 && jsonEnd > jsonStart) {
            runCatching {
                JsonParser.parseString(content.substring(jsonStart, jsonEnd + 1))
                    .asJsonObject
                    .getAsJsonArray("sentences")
                    .mapNotNull { it.asString.cleanSentenceText() }
            }.getOrDefault(emptyList())
        } else {
            emptyList()
        }

        val candidates = fromJson.ifEmpty {
            content.lines().mapNotNull { line ->
                line.cleanSentenceText()
            }
        }

        return candidates
            .distinct()
            .take(3)
            .mapIndexed { index, text ->
                Sentence(
                    id = "groq_${level}_$index",
                    text = text,
                    animationType = mapTextToAnimation(text)
                )
            }
    }

    private fun String.cleanSentenceText(): String? {
        val cleaned = trim()
            .removePrefix("-")
            .replace(Regex("^\\d+[.)]\\s*"), "")
            .trim()
            .trim('"', '\'', '.', '!', '?')
            .replace(Regex("\\s+"), " ")

        val words = cleaned.split(" ").filter { it.isNotBlank() }
        return cleaned.takeIf {
            it.isNotBlank() &&
                words.size in 2..10 &&
                it.all { char -> char.isLetter() || char.isWhitespace() || char == '\'' }
        }
    }

    private fun fallbackSentences(level: Int): List<Sentence> {
        val sentences = when (level) {
            1 -> listOf("I jump", "We smile", "Birds sing")
            2 -> listOf("The red kite flies", "A happy frog hops", "My tiny boat floats")
            3 -> listOf("I read and my friend smiles", "The puppy runs because bells ring", "We jump and clap together")
            else -> listOf("Mia opens a map and grins", "Her little boat sails past flowers", "Friends cheer because the story shines")
        }
        return sentences.mapIndexed { index, text ->
            Sentence("fallback_${level}_$index", text, animationType = mapTextToAnimation(text))
        }
    }

    private fun mapTextToAnimation(text: String): AnimationType {
        val lowerText = text.lowercase()
        return when {
            lowerText.contains("run") -> AnimationType.RUN
            lowerText.contains("jump") -> AnimationType.JUMP
            lowerText.contains("sleep") -> AnimationType.SLEEP
            lowerText.contains("happy") || lowerText.contains("glad") -> AnimationType.HAPPY
            lowerText.contains("eat") -> AnimationType.EAT
            lowerText.contains("smile") || lowerText.contains("laugh") || lowerText.contains("cheer") || lowerText.contains("grin") -> AnimationType.SMILE
            else -> AnimationType.NONE
        }
    }
}
