import { Image, Pressable, SafeAreaView, StyleSheet, Text } from "react-native";
import { getDBConnection, insertIntoNoterow } from "@/db_tools/notes";
import Setting from "@/db_tools/Setting";
import { Rows } from "@/constants/types";

type Props = {
    row: Rows[];
    noteId: number;
    settings: Setting;
}

export default function NewItem({row, noteId, settings}: Props){
    const spawnNewRow = async () => {
        const db = await getDBConnection();
        await insertIntoNoterow(db, "", noteId, false);
    }

    return (
        <SafeAreaView style={styles.container} >
            <Pressable style={styles.pressable} onPress={spawnNewRow} >
                <Image
                    source={settings.useBlackImage() ? require("@/assets/plus_black.png") : require("@/assets/plus_white.png")}
                    style={styles.image}
                />
                <Text style={[styles.newItemText, { color: settings.getSettings().textColor }]} >New item</Text>
            </Pressable>
        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    container: {
        flexDirection: "row",
        paddingBottom: 50,
    },
    pressable: {
        flexDirection: "row",
        paddingLeft: 50,
        paddingTop: 5,
    },
    pressableText: { // Obsolete
        fontSize: 16,
        paddingLeft: 60,
    },
    newItemText: {
        fontSize: 16,
        marginLeft: 5,
        paddingTop: 1,
    },
    image: {
        width: 30,
        height: 25,
    }
})