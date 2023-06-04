package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.BaseRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    // rule - 1
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // rule - 1
    @get:Rule
    var baseRule = BaseRule()

    // Subject under test
    private lateinit var remindersListViewModel: RemindersListViewModel
    private lateinit var remindersRepository: FakeDataSource

    @Before
    fun setupViewModel() {
        stopKoin()
        // Initialise empty reminders repository
        remindersRepository = FakeDataSource()
        remindersListViewModel = RemindersListViewModel(
            ApplicationProvider.getApplicationContext(), remindersRepository
        )
    }

    @Test
    fun loadTestRemindersWhenUnavailable_causesError() {
        // given
        remindersRepository.setShouldReturnError(true)

        // when
        remindersListViewModel.loadReminders()

        // then
        assertThat(remindersListViewModel.showSnackBar.getOrAwaitValue(), not(nullValue()))
    }
    @Test
    fun loadTestReminders_loading() {
        // given
        baseRule.pauseDispatcher()
        remindersListViewModel.loadReminders()

        // when
        assertThat(remindersListViewModel.showLoading.getOrAwaitValue(), `is`(true))
        baseRule.resumeDispatcher()

        // then
        assertThat(remindersListViewModel.showLoading.getOrAwaitValue(), `is`(false))
    }


}