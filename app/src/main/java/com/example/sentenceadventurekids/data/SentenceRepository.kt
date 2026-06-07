package com.example.sentenceadventurekids.data

import com.example.sentenceadventurekids.BuildConfig
import com.example.sentenceadventurekids.model.AnimationType
import com.example.sentenceadventurekids.model.Sentence
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SentenceRepository {
    private val apiKey = "Bearer ${BuildConfig.GROQ_API_KEY}"
    
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
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
        println("Groq: Fetching sentences for level $level")
        val prompt = when (level) {
            1 -> "Return exactly 3 very simple sentences for a toddler (3 words max). No numbers, no bullets, no periods. Example: 'I run', 'Cat is happy'. One sentence per line."
            2 -> "Return exactly 3 simple sentences for a kid (5 words max) with adjectives. No numbers, no bullets, no periods. Example: 'The big red dog runs'. One sentence per line."
            3 -> "Return exactly 3 sentences for a kid (8 words max) with conjunctions. No numbers, no bullets, no periods. Example: 'I like apples and bananas'. One sentence per line."
            else -> "Return exactly 3 simple sentences for kids. One sentence per line."
        }

        return try {
            val response = apiService.getChatCompletion(
                apiKey,
                GroqRequest(
                    model = "llama-3.1-8b-instant",
                    messages = listOf(Message("user", prompt))
                )
            )
            val content = response.choices.firstOrNull()?.message?.content ?: ""
            content.split("\n")
                .filter { it.isNotBlank() }
                .mapIndexed { index, text ->
                    val cleanedText = text.trim().removePrefix("- ").removePrefix("${index + 1}. ")
                    Sentence(
                        id = "groq_${level}_$index",
                        text = cleanedText,
                        animationType = mapTextToAnimation(cleanedText)
                    )
                }
        } catch (e: Exception) {
            // Fallback sentences
            listOf(
                Sentence("fallback_1", "I see you", animationType = AnimationType.SMILE),
                Sentence("fallback_2", "Sun is bright", animationType = AnimationType.HAPPY)
            )
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
            lowerText.contains("smile") || lowerText.contains("laugh") -> AnimationType.SMILE
            else -> AnimationType.NONE
        }
    }
}
