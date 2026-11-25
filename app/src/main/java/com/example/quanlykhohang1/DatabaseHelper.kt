package com.example.quanlykhohang1
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "warehouse.db"
        const val DATABASE_VERSION = 1

        // Bảng users
        const val TABLE_USERS = "users"
        const val COL_USER_ID = "id"
        const val COL_USERNAME = "username"
        const val COL_PASSWORD = "password"

        // Bảng products
        const val TABLE_PRODUCTS = "products"
        const val COL_PROD_ID = "id"
        const val COL_PROD_NAME = "name"
        const val COL_PROD_PRICE = "price"
        const val COL_PROD_QUANTITY = "quantity"
        const val COL_PROD_CATEGORY = "category"
        const val COL_PROD_ACTIVE = "active"
        const val COL_PROD_DESC = "description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USERNAME TEXT NOT NULL UNIQUE,
                $COL_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()

        val createProductsTable = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COL_PROD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PROD_NAME TEXT NOT NULL,
                $COL_PROD_PRICE INTEGER NOT NULL,
                $COL_PROD_QUANTITY INTEGER NOT NULL,
                $COL_PROD_CATEGORY TEXT NOT NULL,
                $COL_PROD_ACTIVE INTEGER NOT NULL,
                $COL_PROD_DESC TEXT
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createProductsTable)

        // Chèn tài khoản mẫu
        insertUserInternal(db, "admin", "123456")

        // Chèn 3 sản phẩm mẫu
        insertProductInternal(
            db,
            Product(
                name = "Chuột không dây",
                price = 150000,
                quantity = 20,
                category = "Điện tử",
                active = 1,
                description = "Chuột không dây kết nối USB 2.4GHz, pin bền"
            )
        )
        insertProductInternal(
            db,
            Product(
                name = "Bàn phím cơ",
                price = 450000,
                quantity = 3,
                category = "Điện tử",
                active = 1,
                description = "Bàn phím cơ switch xanh, có đèn nền"
            )
        )
        insertProductInternal(
            db,
            Product(
                name = "Sổ tay A5",
                price = 25000,
                quantity = 40,
                category = "Văn phòng phẩm",
                active = 1,
                description = "Sổ tay bìa cứng 200 trang giấy kẻ ngang"
            )
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    private fun insertUserInternal(db: SQLiteDatabase, username: String, password: String): Long {
        val values = ContentValues().apply {
            put(COL_USERNAME, username)
            put(COL_PASSWORD, password)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun insertUser(username: String, password: String): Long {
        return insertUserInternal(writableDatabase, username, password)
    }

    fun checkLogin(username: String, password: String): Boolean {
        val query = "SELECT $COL_USER_ID FROM $TABLE_USERS WHERE $COL_USERNAME = ? AND $COL_PASSWORD = ?"
        readableDatabase.rawQuery(query, arrayOf(username, password)).use { cursor ->
            return cursor.count > 0
        }
    }

    private fun insertProductInternal(db: SQLiteDatabase, p: Product): Long {
        val values = ContentValues().apply {
            put(COL_PROD_NAME, p.name)
            put(COL_PROD_PRICE, p.price)
            put(COL_PROD_QUANTITY, p.quantity)
            put(COL_PROD_CATEGORY, p.category)
            put(COL_PROD_ACTIVE, p.active)
            put(COL_PROD_DESC, p.description)
        }
        return db.insert(TABLE_PRODUCTS, null, values)
    }

    fun insertProduct(p: Product): Long {
        return insertProductInternal(writableDatabase, p)
    }

    fun getAllProducts(): ArrayList<Product> {
        val list = ArrayList<Product>()
        val query = "SELECT * FROM $TABLE_PRODUCTS ORDER BY $COL_PROD_ID ASC"
        readableDatabase.rawQuery(query, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    list.add(
                        Product(
                            id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROD_ID)),
                            name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_NAME)),
                            price = cursor.getLong(cursor.getColumnIndexOrThrow(COL_PROD_PRICE)),
                            quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROD_QUANTITY)),
                            category = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_CATEGORY)),
                            active = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROD_ACTIVE)),
                            description = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_DESC)) ?: ""
                        )
                    )
                } while (cursor.moveToNext())
            }
        }
        return list
    }

    fun getProductById(id: Int): Product? {
        val query = "SELECT * FROM $TABLE_PRODUCTS WHERE $COL_PROD_ID = ?"
        readableDatabase.rawQuery(query, arrayOf(id.toString())).use { cursor ->
            if (cursor.moveToFirst()) {
                return Product(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROD_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_NAME)),
                    price = cursor.getLong(cursor.getColumnIndexOrThrow(COL_PROD_PRICE)),
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROD_QUANTITY)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_CATEGORY)),
                    active = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROD_ACTIVE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COL_PROD_DESC)) ?: ""
                )
            }
        }
        return null
    }

    fun updateQuantity(id: Int, quantity: Int): Int {
        val values = ContentValues().apply {
            put(COL_PROD_QUANTITY, quantity)
        }
        return writableDatabase.update(
            TABLE_PRODUCTS,
            values,
            "$COL_PROD_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun deleteProduct(id: Int): Int {
        return writableDatabase.delete(
            TABLE_PRODUCTS,
            "$COL_PROD_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun getTotalValue(): Long {
        val query = "SELECT SUM($COL_PROD_PRICE * $COL_PROD_QUANTITY) FROM $TABLE_PRODUCTS"
        readableDatabase.rawQuery(query, null).use { cursor ->
            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                return cursor.getLong(0)
            }
        }
        return 0L
    }

    fun countLowStock(): Int {
        val query = "SELECT COUNT(*) FROM $TABLE_PRODUCTS WHERE $COL_PROD_QUANTITY < ?"
        readableDatabase.rawQuery(query, arrayOf("5")).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getInt(0)
            }
        }
        return 0
    }
}