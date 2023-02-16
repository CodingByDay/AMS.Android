package com.example.uhf.mvvm.Model;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ItemDAO_Impl implements ItemDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Item> __insertionAdapterOfItem;

  private final EntityDeletionOrUpdateAdapter<Item> __deletionAdapterOfItem;

  private final EntityDeletionOrUpdateAdapter<Item> __updateAdapterOfItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllItems;

  public ItemDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfItem = new EntityInsertionAdapter<Item>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `item` (`ecd`,`ident`,`name`,`location`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Item value) {
        if (value.getEcd() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getEcd());
        }
        if (value.getIdent() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getIdent());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        stmt.bindLong(4, value.getLocation());
      }
    };
    this.__deletionAdapterOfItem = new EntityDeletionOrUpdateAdapter<Item>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `item` WHERE `ecd` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Item value) {
        if (value.getEcd() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getEcd());
        }
      }
    };
    this.__updateAdapterOfItem = new EntityDeletionOrUpdateAdapter<Item>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `item` SET `ecd` = ?,`ident` = ?,`name` = ?,`location` = ? WHERE `ecd` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Item value) {
        if (value.getEcd() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getEcd());
        }
        if (value.getIdent() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getIdent());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        stmt.bindLong(4, value.getLocation());
        if (value.getEcd() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getEcd());
        }
      }
    };
    this.__preparedStmtOfDeleteAllItems = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM item";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Item item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfItem.insert(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Item item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfItem.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Item item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfItem.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllItems() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllItems.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllItems.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Item>> getAllItems() {
    final String _sql = "SELECT * FROM item";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"item"}, false, new Callable<List<Item>>() {
      @Override
      public List<Item> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEcd = CursorUtil.getColumnIndexOrThrow(_cursor, "ecd");
          final int _cursorIndexOfIdent = CursorUtil.getColumnIndexOrThrow(_cursor, "ident");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final List<Item> _result = new ArrayList<Item>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Item _item;
            final String _tmpEcd;
            if (_cursor.isNull(_cursorIndexOfEcd)) {
              _tmpEcd = null;
            } else {
              _tmpEcd = _cursor.getString(_cursorIndexOfEcd);
            }
            final String _tmpIdent;
            if (_cursor.isNull(_cursorIndexOfIdent)) {
              _tmpIdent = null;
            } else {
              _tmpIdent = _cursor.getString(_cursorIndexOfIdent);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpLocation;
            _tmpLocation = _cursor.getInt(_cursorIndexOfLocation);
            _item = new Item(_tmpEcd,_tmpIdent,_tmpName,_tmpLocation);
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

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
