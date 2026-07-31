    package com.example.cookbook.database

    import android.content.Context
    import androidx.room.Database
    import androidx.room.Room
    import androidx.room.RoomDatabase
    import com.example.cookbook.dao.FavoriteMealDao
    import com.example.cookbook.dao.UserDao
    import com.example.cookbook.model.FavoriteMeal
    import com.example.cookbook.model.User

    @Database(entities = [User::class, FavoriteMeal::class], version = 2)
    abstract class CookDatabase: RoomDatabase() {
        abstract fun userDao(): UserDao
        abstract fun favoriteMealDao(): FavoriteMealDao

        companion object {
            @Volatile
            private var INSTANCE: CookDatabase? = null

            fun createDatabase(context: Context): CookDatabase {

                return INSTANCE ?: synchronized(this) {

                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CookDatabase::class.java,
                        "user_db"
                    )
                        .fallbackToDestructiveMigration(true)
                        .build()

                    INSTANCE = instance
                    instance
                }
            }
        }
    }