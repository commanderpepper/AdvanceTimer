package commanderpepper.advancetimer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AlarmTimerDAO::class], version = 1)
abstract class AlarmTimerDatabase : RoomDatabase() {
    abstract fun alarmTimerDAO(): AlarmTimerDAO

    companion object {
        @Volatile
        private var INSTANCE: AlarmTimerDatabase? = null

        fun getInstance(context: Context): AlarmTimerDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AlarmTimerDatabase::class.java,
                        "alarmtimer-db"
                    )
                        .build()
                }
                return instance
            }
        }
    }
}