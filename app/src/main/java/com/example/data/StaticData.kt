package com.example.data

data class Course(
    val id: String,
    val code: String,
    val name: String,
    val icon: String,
    val color: Long,
    val colorDark: Long,
    val colorLight: Long,
    val sks: Int,
    val modules: List<CourseModule>
)

data class CourseModule(
    val id: String,
    val courseId: String,
    val title: String,
    val materials: List<String>
)

object StaticData {
    val courses = listOf(
        Course(
            id = "kb", code = "EKMA4159", name = "Komunikasi Bisnis", icon = "💬", 
            color = 0xFF6366F1, colorDark = 0xFF4338CA, colorLight = 0xFFEEF2FF, sks = 3,
            modules = listOf(
                CourseModule("kb_1", "kb", "Konsep Dasar Komunikasi Bisnis", listOf("Peran Komunikator", "Efektivitas", "Daya Serap Informasi", "Model Lasswell", "Hambatan Komunikasi")),
                CourseModule("kb_2", "kb", "Pesan Komunikasi Bisnis", listOf("Pesan Tertulis", "Prinsip 7C", "Pendekatan Pesan", "Formula AIDA")),
                CourseModule("kb_3", "kb", "Komunikasi Antarbudaya dalam Bisnis", listOf("Kecerdasan Budaya", "Konteks Budaya", "Kajian Non-Verbal", "Etnosentrisme")),
                CourseModule("kb_4", "kb", "Korespondensi Bisnis", listOf("Memo", "Format Full Block", "Jenis Surat Bisnis", "Penulisan Angka")),
                CourseModule("kb_5", "kb", "Laporan dan Proposal Bisnis", listOf("Proposal", "Laporan Kelayakan", "Executive Summary"))
            )
        ),
        Course(
            id = "ab", code = "EKMA4315", name = "Akuntansi Biaya", icon = "📊", 
            color = 0xFF3B82F6, colorDark = 0xFF1D4ED8, colorLight = 0xFFEFF6FF, sks = 3,
            modules = listOf(
                CourseModule("ab_1", "ab", "Konsep Dasar Kos dan Akuntansi Kos", listOf("Kos vs Biaya", "Klasifikasi", "Biaya Overhead Pabrik", "Sistem Biaya Aktual")),
                CourseModule("ab_2", "ab", "Analisis Perilaku Kos Aktivitas", listOf("Fixed Cost", "Variable Cost", "Mixed Cost")),
                CourseModule("ab_3", "ab", "Siklus Akuntansi Kos dan Sistem Kos", listOf("Buku Besar", "Neraca Saldo")),
                CourseModule("ab_4", "ab", "Sistem Kos Pesanan (Job Order Costing)", listOf("Kartu Kos", "Work in Process")),
                CourseModule("ab_5", "ab", "Sistem Kos Proses (Process Costing)", listOf("Equivalent Units", "Weighted Average"))
            )
        ),
        Course(
            id = "po", code = "EKMA4158", name = "Perilaku Organisasi", icon = "🏢", 
            color = 0xFF8B5CF6, colorDark = 0xFF6D28D9, colorLight = 0xFFF5F3FF, sks = 3,
            modules = listOf(
                CourseModule("po_1", "po", "Esensi dan Ruang Lingkup Perilaku Organisasi", listOf("Pengenalan", "Ruang Lingkup")),
                CourseModule("po_2", "po", "Dasar-dasar Perilaku Individu", listOf("Sikap", "Kepuasan Kerja"))
            )
        )
    )
}
