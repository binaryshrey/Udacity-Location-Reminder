package com.udacity.project4.locationreminders.savereminder

import android.app.Application
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.BaseRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.hamcrest.core.Is.`is`

@Config(sdk = [Build.VERSION_CODES.P])
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    // rule-1
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // rule-2
    @get:Rule
    var baseRule = BaseRule()


    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private lateinit var remindersLocalRepository: FakeDataSource

    @Before
    fun setupViewModel() {
        // Initialise empty reminders repository
        remindersLocalRepository = FakeDataSource()
        val appContext = ApplicationProvider.getApplicationContext() as Application
        saveReminderViewModel = SaveReminderViewModel(appContext, remindersLocalRepository)
    }

    @Test
    fun whenIncompleteInfoTest_returnsNull() {
        // given - incomplete reminder fields, when title is null
        saveReminderViewModel.onClear()
        saveReminderViewModel.reminderTitle.value = null
        saveReminderViewModel.reminderSelectedLocationStr.value = null
        saveReminderViewModel.longitude.value = 10.0
        saveReminderViewModel.reminderDescription.value = "description"
        saveReminderViewModel.latitude.value = 10.0

        // when
        val result = saveReminderViewModel.validateEnteredData(
            ReminderDataItem(
                saveReminderViewModel.reminderTitle.value,
                saveReminderViewModel.reminderDescription.value,
                saveReminderViewModel.reminderSelectedLocationStr.value,
                saveReminderViewModel.longitude.value,
                saveReminderViewModel.latitude.value,
                "id"
            )
        )

        // then - result is false
        assertThat(result, `is`(false))

    }
}