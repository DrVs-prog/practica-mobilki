/**package com.example.praktika.common


import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
//import io.github.jan.supabase.gotrue
import io.ktor.websocket.WebSocketDeflateExtension.Companion.install

/**
 * Конфигурация Supabase клиента
 * @author Студент
 * @date 02.03.2026
 */
object SupabaseConfig {

    // TODO: Заменить на свои данные из проекта Supabase
    const val SUPABASE_URL = "https://smcpuseutorhsrzgapil.supabase.co"
    const val SUPABASE_KEY = "sb_publishable_ZuKnplqj7jH0MMHOWW21sg_96lqouVp"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        //install(GoTrue)
        install(Postgrest)
    }
}
*/