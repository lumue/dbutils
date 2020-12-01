package io.github.lumue.dbutils

import java.sql.Connection

import java.util.HashSet


import java.sql.DatabaseMetaData

import java.sql.SQLException

data class Table(val name: String, val references: Set<String>, val referencedBy: Set<String>)


public fun Set<Table>.sortTablesByDependencies() {
    return this.toMutableList().sortWith(
            Comparator { table1, table2 ->
                if (table1.references.contains(table2.name)) 1
                else if (table1.referencedBy.contains(table2.name)) -1
                else 0
            }
    )
}

@Throws(SQLException::class)
public fun Connection.listTables(): Set<Table> {
    val dbSchema="public"
    val tables: MutableSet<Table> = HashSet()
    val databaseMetaData: DatabaseMetaData = metaData
    val tablesResultSet = databaseMetaData.getTables(null, dbSchema, null, arrayOf("TABLE"))
    while (tablesResultSet.next()) {

        val tableName = tablesResultSet.getString("TABLE_NAME")

        val references: MutableSet<String> = HashSet()
        val exportedKeys = databaseMetaData.getExportedKeys(null, dbSchema, tableName)
        while (exportedKeys.next()) {
            references.add(exportedKeys.getString("FKTABLE_NAME"))
        }
        exportedKeys.close()

        val referencedBy: MutableSet<String> = HashSet()
        val importedKeys = databaseMetaData.getImportedKeys(null, dbSchema, tableName)
        while (importedKeys.next()) {
            referencedBy.add(importedKeys.getString("PKTABLE_NAME"))
        }
        importedKeys.close()

        tables.add(Table(tableName, references, referencedBy))
    }
    tablesResultSet.close()
    return tables
}

