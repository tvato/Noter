import { NativeStackScreenProps } from "@react-navigation/native-stack"
import { SafeAreaView, ScrollView, StyleSheet } from "react-native";
// These can be deleted after database browsing is not needed anymore
import { useDrizzleStudio } from "expo-drizzle-studio-plugin";
import * as SQLite from "expo-sqlite"
// ******************************************************************
import { getDBConnection, getNotesAll } from "@/db_tools/notes";
import { useEffect, useState } from "react";
import Note from "@/components/Note";
import { Notes, RootStackParamList } from "@/constants/types";
import NewNote from "@/components/NewNote";
import Setting from "@/db_tools/Setting";

type HomeScreenNavigationProp = NativeStackScreenProps<RootStackParamList, "Home">

export default function HomeScreen({ route, navigation }: HomeScreenNavigationProp) {
    const [notes, setNotes] = useState<Notes[]>([])
    const [settings, setSettings] = useState<Setting>(route.params.settings);

    // Can be deleted if database browsing is not needed
    const drizzleDb = SQLite.openDatabaseSync("notes.db");
    useDrizzleStudio(drizzleDb);

    useEffect(() => {
        const retrieveNotes = async () => {
            const db = await getDBConnection();
            const allNotes = await getNotesAll(db);
            setNotes(allNotes);
        }
        
        retrieveNotes();
    }, [notes]);

    // Workaround for updating header colors
    useEffect(() => {
        navigation.setOptions(settings.getHeaderOptions())
    }, [settings.settings])

    return (
        <SafeAreaView style={[styles.container, { backgroundColor: settings.getSettings().backgroundColor }]}>
            <ScrollView>
                {notes.map((note) => (
                    <Note
                        key={note.id}
                        title={note.title}
                        id={note.id}
                        settings={settings}
                    />
                ))}
            </ScrollView>
            <NewNote settings={settings} />
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    }
})