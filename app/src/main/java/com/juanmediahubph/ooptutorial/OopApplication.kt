package com.juanmediahubph.ooptutorial

import android.app.Application
import androidx.room.Room
import com.juanmediahubph.ooptutorial.data.local.OopDatabase
import com.juanmediahubph.ooptutorial.data.repository.OopRepository

class OopApplication : Application() {
    lateinit var repository: OopRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(this, OopDatabase::class.java, "oop_tutorial.db")
            .build()
        repository = OopRepository(database)
    }
}
