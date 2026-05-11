# RMMC Student Task Hub

RMMC Student Task Hub is a Kotlin Android app for students to manage tasks, class schedules, deadlines, and reminders in one place.

## Tech Stack

- Language: Kotlin
- Architecture: MVVM
- Database: Room
- UI: Material Design (XML + ViewBinding)
- Notifications: AlarmManager + BroadcastReceiver
- Background restore: WorkManager
- Min SDK: 26 (Android 8.0)

## Features

- Dashboard with summary cards:
  - Total Tasks
  - Completed Tasks
  - Pending Tasks
  - Today tasks and next deadline
- Task Management (CRUD):
  - Title, subject, description, deadline, priority, status
  - Mark complete/incomplete
  - Search
  - Filter by status/priority
  - Sort by deadline/priority
- Schedule Manager (CRUD):
  - Subject, instructor, room, day, start/end time
- Notifications:
  - Task reminder before deadline (default: 1 hour before)
  - Class reminder 30 minutes before class starts (weekly repeating)
  - Reminder restoration after reboot
- Dark mode support via DayNight themes

## Project Structure

- `app/src/main/java/com/rmmc/studenttaskhub/data`
  - `model`: entities and enums
  - `local`: Room DAO and database
  - `repository`: repository classes
- `app/src/main/java/com/rmmc/studenttaskhub/ui`
  - `dashboard`, `tasks`, `schedule`: Fragments, adapters, ViewModels
  - `common`: App container and ViewModel factory
- `app/src/main/java/com/rmmc/studenttaskhub/notifications`
  - Reminder scheduler/receiver/boot handling

## Open in Android Studio

1. Open Android Studio.
2. Select **Open** and choose this project folder.
3. Let Gradle sync finish.
4. Run on emulator or physical device (Android 8.0+).

## Notes

- On Android 13+, the app requests notification permission.
- Exact alarm behavior may vary by OEM battery and alarm restrictions.
