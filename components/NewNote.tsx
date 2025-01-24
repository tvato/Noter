import { RootStackParamList } from "@/constants/types";
import { getDBConnection, insertIntoNotes } from "@/db_tools/notes";
import Setting from "@/db_tools/Setting";
import { useNavigation } from "@react-navigation/native";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { useState } from "react";
import { Image, Pressable, StyleSheet } from "react-native";

type NavigationProp = NativeStackNavigationProp<RootStackParamList, "Home">

type Props = {
    settings: Setting;
}

export default function NewNote(props: Props){
    const navigation = useNavigation<NavigationProp>()
    const [settings, setSettings] = useState<Setting>(props.settings);

    const addNewNote = async () => {
        const db = await getDBConnection();
        const result = await insertIntoNotes(db, "")
        
        navigation.navigate("NoteView", {
            noteID: result.lastInsertRowId,
            noteTitle: "Title",
            rows: [],
            settings: settings
        })

    }
    return (
        <Pressable onPress={addNewNote}>
            <Image
                source={settings.useBlackImageNewNote() ? require("@/assets/plus_black.png") : require("@/assets/plus_white.png")}
                style={[styles.image,
                    {
                        borderColor: settings.getSettings().borderColorNewNote,
                        backgroundColor: settings.getSettings().backgroundColorNewNote
                    }
                ]}
            />
        </Pressable>
    )
}

const styles = StyleSheet.create({
    image: {
        borderWidth: 2,
        borderRadius: 15,
        width: 55,
        height: 50,
        alignSelf: "flex-end",
        margin: 15,
    }
})