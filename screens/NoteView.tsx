import { SafeAreaView, StyleSheet, TextInput } from "react-native";
import { NativeStackScreenProps } from "@react-navigation/native-stack"
import CheckboxRow from "@/components/CheckboxRow";
import { useEffect, useState } from "react";
import NewItem from "@/components/NewItem";
import { RootStackParamList, Rows } from "@/constants/types";
import { getDBConnection, getRowsByNoteID, updateNotes } from "@/db_tools/notes";

type NoteViewProp = NativeStackScreenProps<RootStackParamList, "NoteView">;

export default function NoteView({ route, navigation }: NoteViewProp){
    const [title, setTitle] = useState(route.params.noteTitle);
    const [rows, setRows] = useState<Rows[]>(route.params.rows);
    const [settings, setSettings] = useState(route.params.settings);

    useEffect(() => {
        const fetchData = async () => {
            const db = await getDBConnection();
            const result = await getRowsByNoteID(db, route.params.noteID).then((value) => value)
            setRows(result);
        }

        fetchData();

    }, [rows]);

    // Workaround for updating header colors
    useEffect(() => {
        navigation.setOptions(settings.getHeaderOptions())
    }, [settings.settings])

    const updateTitle = async () => {
        const db = await getDBConnection();
        await updateNotes(db, title, route.params.noteID)
    }

    return (
        <SafeAreaView style={[styles.container, { backgroundColor: settings.getSettings().backgroundColor }]}>
            <TextInput
                style={[styles.titleInput, { color: settings.getSettings().textColor }]}
                value={title}
                onChangeText={setTitle}
                onEndEditing={updateTitle}
                placeholder="Title"
                placeholderTextColor={settings.getSettings().checkedTextColor}
            />
            {rows.map((row) => (
                row.checked ? null :
                    <CheckboxRow
                        rows={row}
                        settings={settings}
                        key={row.id}
                    />
            ))}
            <NewItem
                row={rows}
                noteId={route.params.noteID}
                settings={settings}
            />
            {rows.map((row) => (
                row.checked ?
                    <CheckboxRow
                        rows={row}
                        settings={settings}
                        key={row.id}
                    />
                : null
            ))}
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    titleInput: {
        fontSize: 22,
        paddingLeft: 15,
    },
    container: {
        flex: 1,
    }
})