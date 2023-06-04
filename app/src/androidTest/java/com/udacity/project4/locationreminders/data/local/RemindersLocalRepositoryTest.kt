package com.udacity.project4.locationreminders.data.local

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private lateinit var remindersDAO: RemindersDao
    private lateinit var remindersDB: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository


    //init
    @Before
    fun setup() {
        remindersDB = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, RemindersDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        remindersDAO = remindersDB.reminderDao()
        repository = RemindersLocalRepository(remindersDAO)
    }

    @After
    fun closeDb() = remindersDB.close()

    @Test
    fun insertThreeRemindersTest_getAllThreeFromDatabase() = runBlocking {
        // given
        val rem1 = ReminderDTO(
            "title1",
            "description1",
            "somewhere1",
            11.0,
            11.0,
            "random1"
        )
        val rem2 = ReminderDTO(
            "title2",
            "descriptio2n",
            "somewhere2",
            12.0,
            12.0,
            "random2"
        )
        val rem3 = ReminderDTO(
            "title3",
            "description3",
            "somewhere3",
            13.0,
            13.0,
            "random3"
        )
        remindersDB.reminderDao().saveReminder(rem1)
        remindersDB.reminderDao().saveReminder(rem2)
        remindersDB.reminderDao().saveReminder(rem3)
        val remindersList = listOf(rem1, rem2, rem3).sortedBy { it.id }

        // when
        val loadedRemindersList = remindersDB.reminderDao().getReminders()
        val sortedLoadedRemindersList = loadedRemindersList.sortedBy { it.id }
        val reminder = repository.getReminder("fake") as Result.Error

        // then
        assertThat(reminder.message, `is`("Reminder not found!"))
        assertThat(sortedLoadedRemindersList[0].id, `is`(remindersList[0].id))
        assertThat(sortedLoadedRemindersList[1].id, `is`(remindersList[1].id))
        assertThat(sortedLoadedRemindersList[2].id, `is`(remindersList[2].id))
    }
}