package br.edu.utfpr.cadastropessoascmp.data.local.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import androidx.room.RoomDatabase

private var appContext: Context? = null

fun initDatabase(context: Context) {
    appContext = context.applicationContext
}

internal class DatabaseContextProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        context?.applicationContext?.let { initDatabase(it) }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val context = requireNotNull(appContext) {
        "Context do Android não foi inicializado. Chame initDatabase(context) antes de acessar o banco."
    }
    val dbFile = context.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}