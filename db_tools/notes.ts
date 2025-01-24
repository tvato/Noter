import { Notes, Rows, Settings } from "@/constants/types";
import * as SQLite from "expo-sqlite";

// Needed
export const getDBConnection = async () => {
    return await SQLite.openDatabaseAsync("notes.db")
}

// Needed
export const createTables = async (db: SQLite.SQLiteDatabase) => {
    const query = `CREATE TABLE IF NOT EXISTS notes(
        id INTEGER NOT NULL PRIMARY KEY,
        title TEXT
    );

    CREATE TABLE IF NOT EXISTS noterows(
        id INTEGER NOT NULL PRIMARY KEY,
        content TEXT,
        note INTEGER,
        checked BOOLEAN,
        FOREIGN KEY (note)
            REFERENCES notes(id)
    );
    
    CREATE TABLE IF NOT EXISTS settings(
        id INTEGER NOT NULL PRIMARY KEY,
        backgroundColor TEXT,
        textColor TEXT,
        checkedTextColor TEXT,
        borderColorNote TEXT,
        borderColorNewNote TEXT,
        backgroundColorNewNote TEXT
    );`;

    return await db.execAsync(query);
}



// Needed
export const insertIntoNotes = async (db: SQLite.SQLiteDatabase, title: string) => {
    const insertQuery = `INSERT INTO notes (title) VALUES ("${title}");`;

    return await db.runAsync(insertQuery);
}

// Needed
export const insertIntoNoterow = async (db: SQLite.SQLiteDatabase, content: string, noteId: number, checked: boolean) => {
    const checkedAsNumber = (checked == true) ? 1 : 0;  // Converts JS boolean to integer to match SQL
    const insertQuery = `INSERT INTO noterows (content, note, checked) VALUES ("${content}", ${noteId}, ${checkedAsNumber});`;

    return await db.runAsync(insertQuery);
}

// May not be needed
export const getNotesByID = async (db: SQLite.SQLiteDatabase, id: number) => {
    const query = `SELECT * FROM notes WHERE id = ${id};`;

    const results = await db.getAllAsync(query);

    return results;
}

// Needed
export const getRowsByNoteID = async (db: SQLite.SQLiteDatabase, id: number) => {
    const query = `SELECT * FROM noterows WHERE note = ${id}`;

    const result = await db.getAllAsync<Rows>(query);
    
    // To convert SQLite boolean (number) to javascript boolean
    for(let i=0; i < result.length; i++){
        result[i].checked = (result[i].checked == true) ? true : false;
    }
    
    return result;
}

// Needed
export const getNotesAll = async (db: SQLite.SQLiteDatabase) => {
    const result = await db.getAllAsync<Notes>("SELECT * FROM notes;");
    return result
}

// May not be needed
export const getNoterowsAll = async (db: SQLite.SQLiteDatabase) => {
    const result = await db.getAllAsync("SELECT * FROM noterows");
    return result;
}

// Needed
export const deleteFromRows = async (db: SQLite.SQLiteDatabase, id: number) => {
    await db.runAsync(`DELETE FROM noterows WHERE id = ${id};`)
}

// Needed
export const updateRowContent = async (db: SQLite.SQLiteDatabase, newContent: string, id: number) => {
    await db.runAsync(`UPDATE noterows SET content = "${newContent}" WHERE id = ${id}`)
}

// Needed
export const updateNotes = async (db: SQLite.SQLiteDatabase, newTitle: string, id: number) => {
    await db.runAsync(`UPDATE notes SET title = "${newTitle}" WHERE id = ${id}`)
}

// Needed
export const updateRowChecked = async (db: SQLite.SQLiteDatabase, newChecked: boolean, id: number) => {
    await db.runAsync(`UPDATE noterows SET checked = ${newChecked} WHERE id = ${id}`)
}

// Needed
export const getAllSettings = async (db: SQLite.SQLiteDatabase) => {
    const results = await db.getFirstAsync<Settings>("SELECT * FROM settings;");
    return results
}

// Needed
export const updateSettings = async (db: SQLite.SQLiteDatabase, settings: Settings) => {
    const query = `UPDATE settings SET
        backgroundColor = "${settings.backgroundColor}",
        textColor = "${settings.textColor}",
        checkedTextColor = "${settings.checkedTextColor}",
        borderColorNote = "${settings.borderColorNote}",
        borderColorNewNote = "${settings.borderColorNewNote}",
        backgroundColorNewNote = "${settings.backgroundColorNewNote}";`

    await db.runAsync(query);
}

export const addToSettings = async (db: SQLite.SQLiteDatabase, settings: Settings) => {
    const query = `INSERT OR IGNORE INTO settings (
        id,
        backgroundColor,
        textColor,
        checkedTextColor,
        borderColorNote,
        borderColorNewNote,
        backgroundColorNewNote
    ) VALUES (
        1,
        "${settings.backgroundColor}",
        "${settings.textColor}",
        "${settings.checkedTextColor}",
        "${settings.borderColorNote}",
        "${settings.borderColorNewNote}",
        "${settings.backgroundColorNewNote}"
    );`

    await db.runAsync(query)
}

// Needed
export const deleteFromNotes = async (db: SQLite.SQLiteDatabase, id: number) => {
    await db.runAsync(`DELETE FROM notes WHERE id = ${id};`);
}