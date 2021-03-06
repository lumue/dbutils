/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package io.github.lumue.dbutils

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

class App {
    val greeting: String
        get() {
            return "Hello world."
        }
}

fun main(args: Array<String>) {

    val connectionProps = Properties()
    connectionProps["user"] = "arteria"
    connectionProps["password"] = "arteria"
    try {

        Class.forName("com.mysql.jdbc.Driver")
                .getDeclaredConstructor()
                .newInstance()

        val  conn = DriverManager.getConnection(
                "jdbc:" + "postgresql" + "://" +
                        "127.0.0.1" +
                        ":" + "15432" + "/" +
                        "arteria",
                connectionProps)
    } catch (ex: SQLException) {
        // handle any errors
        ex.printStackTrace()
    } catch (ex: Exception) {
        // handle any errors
        ex.printStackTrace()
    }

    println(App().greeting)
}
