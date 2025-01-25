import { deleteFromRows, getDBConnection, insertIntoNoterow, updateRowChecked, updateRowContent } from "@/db_tools/notes";
import { useEffect, useState } from "react";
import { Image, Pressable, SafeAreaView, StyleSheet, Text, TextInput } from "react-native"
import Checkbox from "expo-checkbox"
import Setting from "@/db_tools/Setting";
import { Rows } from "@/constants/types";

type Props = {
    noteID: number;
    rows: Rows;
    settings: Setting;
}

export default function CheckboxRow(props: Props){
    const [text, setText] = useState(props.rows.content);
    const [checked, setChecked] = useState(props.rows.checked);
    const [settings, setSettings] = useState<Setting>(props.settings);
    const [isFocused, setIsFocused] = useState(false);

    const removeRow = async () => {
        const db = await getDBConnection();
        await deleteFromRows(db, props.rows.id);
    }
    const addToDatabase = async () => {
        const db = await getDBConnection();
        await updateRowContent(db, text, props.rows.id);
    }
    const spawnNew = async () => {
        const db = await getDBConnection();
        await insertIntoNoterow(db, "", props.noteID, false);
    }
    useEffect(() => {
        const handleCheckbox = async () => {
            const db = await getDBConnection();
            await updateRowChecked(db, checked, props.rows.id);
        }
        
        handleCheckbox();
    },[checked])
    
    return (
        <SafeAreaView style={styles.noteArea}>
            <Checkbox
                value={checked}
                onValueChange={setChecked}
                style={styles.checkbox}
                color={checked ? settings.getSettings().checkedTextColor : settings.getSettings().textColor}
            />
            {// If item is checked it needs to be <Text> element, since <TextInput> cannot update textDecorationLine and would just throw an error
            checked ? <Text style={[styles.checkedTextInput, { color: settings.getSettings().checkedTextColor }]} >{text}</Text> :
            <TextInput
                style={[
                    styles.textInput,
                    { color: settings.getSettings().textColor }
                ]}
                value={text}
                onChangeText={setText}
                onEndEditing={addToDatabase}
                onSubmitEditing={spawnNew}
                autoFocus={true}
            />
            }
            <Pressable onPress={removeRow} style={styles.pressable} >
                <Image
                    source={settings.useBlackImage() ? require("@/assets/x_black.png") : require("@/assets/x_white.png")}
                    style={styles.image}
                />
            </Pressable>
        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    textInput: {
        width: "70%",
        fontSize: 16,
    },
    checkedTextInput: {
        width: "70%",
        fontSize: 16,
        textDecorationLine: "line-through",
        verticalAlign: "middle",
    },
    noteArea: {
        flexDirection: "row",
        justifyContent: "space-evenly",
    },
    pressable: {
        padding: 5,
        paddingTop: 12,
        marginLeft: 5,
    },
    image: {
        width: 30,
        height: 25,
    },
    checkbox: {
        alignSelf: "center",
    },
})