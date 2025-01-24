import { deleteFromRows, getDBConnection, updateRowChecked, updateRowContent } from "@/db_tools/notes";
import { useEffect, useState } from "react";
import { Image, Pressable, SafeAreaView, StyleSheet, TextInput } from "react-native"
import Checkbox from "expo-checkbox"
import Setting from "@/db_tools/Setting";
import { Rows } from "@/constants/types";

type Props = {
    rows: Rows;
    settings: Setting;
}

export default function CheckboxRow(props: Props){
    const [text, setText] = useState(props.rows.content);
    const [checked, setChecked] = useState(props.rows.checked);
    const [settings, setSettings] = useState<Setting>(props.settings);

    const removeRow = async () => {
        const db = await getDBConnection();
        await deleteFromRows(db, props.rows.id);
    }
    const addToDatabase = async () => {
        const db = await getDBConnection();
        await updateRowContent(db, text, props.rows.id);
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
                color={checked ? "darkgrey" : "white"}
            />
            <TextInput
                style={[
                    checked ? styles.checkedTextInput : styles.textInput,
                    {
                        color: checked ? settings.getSettings().checkedTextColor : settings.getSettings().textColor
                    }
                ]}
                value={text}
                onChangeText={setText}
                onEndEditing={addToDatabase}
            />
            <Pressable onPress={removeRow} style={styles.pressable} >
                <Image
                    source={settings.useBlackImage() ? require("@/assets/x_black.png") : require("@/assets/x_white.png")}
                    style={styles.image}
                />
            </Pressable>
        </SafeAreaView>
    )
    /*
    if(checked){
        return (
            <SafeAreaView style={styles.noteArea}>
                <Checkbox
                    value={checked}
                    onValueChange={setChecked}
                    style={styles.checkbox}
                    color={"#606060"}           // CHANGE
                />
                <TextInput
                    style={[styles.checkedTextInput, { color: settings.getSettings().checkedTextColor }]}
                    value={text}
                    onChangeText={setText}
                    onEndEditing={addToDatabase}
                />
                <Pressable onPress={removeRow} style={styles.pressable} >
                    <Image
                        source={require("@/assets/x_white.png")}    // CHANGE to black X if background is white(ish)
                        style={styles.image}
                    />
                </Pressable>
            </SafeAreaView>
        )
    }
    else{
        return (
            <SafeAreaView style={styles.noteArea}>
                <Checkbox
                    value={checked}
                    onValueChange={setChecked}
                    style={styles.checkbox}
                    color={"white"}         // CHANGE
                />
                <TextInput
                    style={[styles.textInput, {color: settings.getSettings().textColor}]}
                    value={text}
                    onChangeText={setText}
                    onEndEditing={addToDatabase}
                />
                <Pressable onPress={removeRow} style={styles.pressable} >
                    <Image source={require("@/assets/x_white.png")} style={styles.image} />
                </Pressable>
            </SafeAreaView>
        )
    }
    */
}

const styles = StyleSheet.create({
    titleInput: {   // Obsolete?
        fontSize: 22,
        color: "white",
    },
    textInput: {
        width: "70%",
        fontSize: 16,
    },
    checkedTextInput: {
        width: "70%",
        fontSize: 16,
        textDecorationLine: "line-through",
    },
    noteArea: {
        flexDirection: "row",
        justifyContent: "space-evenly",
    },
    pressableText: { // Obsolete
        fontSize: 22,
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