
import androidx.room.*


@Entity(tableName = "Schedule")
data class Schedule(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "block1") val block1: String?,
    @ColumnInfo(name = "block2") val block2: String?,
    @ColumnInfo(name = "block3") val block3: String?,
    @ColumnInfo(name = "block4") val block4: String?,
    @ColumnInfo(name = "block5") val block5: String?,
    @ColumnInfo(name = "block6") val block6: String?,
    @ColumnInfo(name = "block7") val block7: String?,
    @ColumnInfo(name = "block8") val block8: String?,
)


@Dao
interface ScheduleDao {
    @Query("SELECT * FROM Schedule")
    fun getAll(): List<Schedule>

    @Insert
    fun insertAll(vararg schedules: Schedule)

    @Delete
    fun delete(schedule: Schedule)
}

@Database(entities = [Schedule::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}
/*
val db = Room.databaseBuilder(
    applicationContext,
    AppDatabase::class.java, "database-name"
).build()
*/


