package com.example.taskpro

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://tlrpzkzpaxhgvqlgncds.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRscnB6a3pwYXhoZ3ZxbGduY2RzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzg3ODUxMTksImV4cCI6MjA5NDM2MTExOX0.308Vn5enUiuVddonVCVCgDVHRXOt7cC5hvDf0ZrSemo"
    ) {
        install(Auth.Companion)
        install(Postgrest.Companion)
        install(Storage)
    }

}