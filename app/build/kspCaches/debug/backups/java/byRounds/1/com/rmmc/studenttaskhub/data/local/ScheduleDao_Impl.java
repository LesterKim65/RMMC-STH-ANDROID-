package com.rmmc.studenttaskhub.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.rmmc.studenttaskhub.data.model.AlarmFrequency;
import com.rmmc.studenttaskhub.data.model.Schedule;
import com.rmmc.studenttaskhub.data.model.WeekDay;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ScheduleDao_Impl implements ScheduleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Schedule> __insertionAdapterOfSchedule;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Schedule> __deletionAdapterOfSchedule;

  private final EntityDeletionOrUpdateAdapter<Schedule> __updateAdapterOfSchedule;

  public ScheduleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSchedule = new EntityInsertionAdapter<Schedule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `schedules` (`id`,`subject`,`instructor`,`room`,`day`,`startTimeMillis`,`endTimeMillis`,`classStartReminderMillis`,`alarmFrequency`,`alarmSound`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Schedule entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSubject());
        statement.bindString(3, entity.getInstructor());
        statement.bindString(4, entity.getRoom());
        final String _tmp = __converters.fromWeekDay(entity.getDay());
        statement.bindString(5, _tmp);
        statement.bindLong(6, entity.getStartTimeMillis());
        statement.bindLong(7, entity.getEndTimeMillis());
        if (entity.getClassStartReminderMillis() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getClassStartReminderMillis());
        }
        final String _tmp_1 = __converters.fromAlarmFrequency(entity.getAlarmFrequency());
        statement.bindString(9, _tmp_1);
        if (entity.getAlarmSound() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getAlarmSound());
        }
      }
    };
    this.__deletionAdapterOfSchedule = new EntityDeletionOrUpdateAdapter<Schedule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `schedules` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Schedule entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSchedule = new EntityDeletionOrUpdateAdapter<Schedule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `schedules` SET `id` = ?,`subject` = ?,`instructor` = ?,`room` = ?,`day` = ?,`startTimeMillis` = ?,`endTimeMillis` = ?,`classStartReminderMillis` = ?,`alarmFrequency` = ?,`alarmSound` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Schedule entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSubject());
        statement.bindString(3, entity.getInstructor());
        statement.bindString(4, entity.getRoom());
        final String _tmp = __converters.fromWeekDay(entity.getDay());
        statement.bindString(5, _tmp);
        statement.bindLong(6, entity.getStartTimeMillis());
        statement.bindLong(7, entity.getEndTimeMillis());
        if (entity.getClassStartReminderMillis() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getClassStartReminderMillis());
        }
        final String _tmp_1 = __converters.fromAlarmFrequency(entity.getAlarmFrequency());
        statement.bindString(9, _tmp_1);
        if (entity.getAlarmSound() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getAlarmSound());
        }
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public Object insertSchedule(final Schedule schedule,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSchedule.insertAndReturnId(schedule);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSchedule(final Schedule schedule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSchedule.handle(schedule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSchedule(final Schedule schedule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSchedule.handle(schedule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<Schedule>> getAllSchedules() {
    final String _sql = "SELECT * FROM schedules ORDER BY day ASC, startTimeMillis ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"schedules"}, false, new Callable<List<Schedule>>() {
      @Override
      @Nullable
      public List<Schedule> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfInstructor = CursorUtil.getColumnIndexOrThrow(_cursor, "instructor");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "day");
          final int _cursorIndexOfStartTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMillis");
          final int _cursorIndexOfEndTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMillis");
          final int _cursorIndexOfClassStartReminderMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "classStartReminderMillis");
          final int _cursorIndexOfAlarmFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmFrequency");
          final int _cursorIndexOfAlarmSound = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmSound");
          final List<Schedule> _result = new ArrayList<Schedule>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Schedule _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpInstructor;
            _tmpInstructor = _cursor.getString(_cursorIndexOfInstructor);
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final WeekDay _tmpDay;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfDay);
            _tmpDay = __converters.toWeekDay(_tmp);
            final long _tmpStartTimeMillis;
            _tmpStartTimeMillis = _cursor.getLong(_cursorIndexOfStartTimeMillis);
            final long _tmpEndTimeMillis;
            _tmpEndTimeMillis = _cursor.getLong(_cursorIndexOfEndTimeMillis);
            final Long _tmpClassStartReminderMillis;
            if (_cursor.isNull(_cursorIndexOfClassStartReminderMillis)) {
              _tmpClassStartReminderMillis = null;
            } else {
              _tmpClassStartReminderMillis = _cursor.getLong(_cursorIndexOfClassStartReminderMillis);
            }
            final AlarmFrequency _tmpAlarmFrequency;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfAlarmFrequency);
            _tmpAlarmFrequency = __converters.toAlarmFrequency(_tmp_1);
            final String _tmpAlarmSound;
            if (_cursor.isNull(_cursorIndexOfAlarmSound)) {
              _tmpAlarmSound = null;
            } else {
              _tmpAlarmSound = _cursor.getString(_cursorIndexOfAlarmSound);
            }
            _item = new Schedule(_tmpId,_tmpSubject,_tmpInstructor,_tmpRoom,_tmpDay,_tmpStartTimeMillis,_tmpEndTimeMillis,_tmpClassStartReminderMillis,_tmpAlarmFrequency,_tmpAlarmSound);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getSchedulesWithReminders(final Continuation<? super List<Schedule>> $completion) {
    final String _sql = "SELECT * FROM schedules WHERE classStartReminderMillis IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Schedule>>() {
      @Override
      @NonNull
      public List<Schedule> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfInstructor = CursorUtil.getColumnIndexOrThrow(_cursor, "instructor");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "day");
          final int _cursorIndexOfStartTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMillis");
          final int _cursorIndexOfEndTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMillis");
          final int _cursorIndexOfClassStartReminderMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "classStartReminderMillis");
          final int _cursorIndexOfAlarmFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmFrequency");
          final int _cursorIndexOfAlarmSound = CursorUtil.getColumnIndexOrThrow(_cursor, "alarmSound");
          final List<Schedule> _result = new ArrayList<Schedule>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Schedule _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpInstructor;
            _tmpInstructor = _cursor.getString(_cursorIndexOfInstructor);
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final WeekDay _tmpDay;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfDay);
            _tmpDay = __converters.toWeekDay(_tmp);
            final long _tmpStartTimeMillis;
            _tmpStartTimeMillis = _cursor.getLong(_cursorIndexOfStartTimeMillis);
            final long _tmpEndTimeMillis;
            _tmpEndTimeMillis = _cursor.getLong(_cursorIndexOfEndTimeMillis);
            final Long _tmpClassStartReminderMillis;
            if (_cursor.isNull(_cursorIndexOfClassStartReminderMillis)) {
              _tmpClassStartReminderMillis = null;
            } else {
              _tmpClassStartReminderMillis = _cursor.getLong(_cursorIndexOfClassStartReminderMillis);
            }
            final AlarmFrequency _tmpAlarmFrequency;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfAlarmFrequency);
            _tmpAlarmFrequency = __converters.toAlarmFrequency(_tmp_1);
            final String _tmpAlarmSound;
            if (_cursor.isNull(_cursorIndexOfAlarmSound)) {
              _tmpAlarmSound = null;
            } else {
              _tmpAlarmSound = _cursor.getString(_cursorIndexOfAlarmSound);
            }
            _item = new Schedule(_tmpId,_tmpSubject,_tmpInstructor,_tmpRoom,_tmpDay,_tmpStartTimeMillis,_tmpEndTimeMillis,_tmpClassStartReminderMillis,_tmpAlarmFrequency,_tmpAlarmSound);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
