import { RootStackParamList } from "@/constants/types";
import { deleteFromNotes, getDBConnection } from "@/db_tools/notes";
import { RouteProp, useNavigation, useRoute } from "@react-navigation/native";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { Image, Pressable, StyleSheet } from "react-native";

type NoteViewProp = NativeStackNavigationProp<RootStackParamList, "NoteView">;
type NoteViewRouteProp = RouteProp<RootStackParamList, "NoteView">;

export default function DeleteNoteButton(){
    const navigation = useNavigation<NoteViewProp>();
    const route = useRoute<NoteViewRouteProp>();

    const deleteNote = async () => {
        const db = await getDBConnection();
        await deleteFromNotes(db, route.params.noteID);
        navigation.goBack();
    }

    return (
        <Pressable onPress={deleteNote}>
            <Image
                source={route.params.settings.useBlackImage() ? require("@/assets/x_black.png") : require("@/assets/x_white.png")}
                style={styles.image}
            />
        </Pressable>
    )
}

const styles = StyleSheet.create({
    image: {
        width: 30,
        height: 30,
    }
})