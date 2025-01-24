import { RootStackParamList, Rows } from "@/constants/types";
import { getDBConnection, getRowsByNoteID } from "@/db_tools/notes";
import Setting from "@/db_tools/Setting";
import { useNavigation } from "@react-navigation/native";
import { StackNavigationProp } from "@react-navigation/stack";
import { useEffect, useState } from "react";
import { Pressable, SafeAreaView, StyleSheet, Text, View } from "react-native";

type Props = {
    title: string;
    id: number;
    settings: Setting;
}

type HomeScreenNavigationProp = StackNavigationProp<RootStackParamList, "Home">

export default function Note(props: Props){
    const navigation = useNavigation<HomeScreenNavigationProp>();
    const [rows, setRows] = useState<Rows[]>([])
    const [settings, setSettings] = useState<Setting>(props.settings);

    useEffect(() => {
        const retrieveRows = async () => {
            const db = await getDBConnection();
            const result = await getRowsByNoteID(db, props.id);
            setRows(result);
        }
        retrieveRows();
    }, [rows]);

    const handlePress = () => {
        navigation.navigate("NoteView", {
            noteID: props.id,
            noteTitle: props.title,
            rows: rows,
            settings: settings,
        });
    }

    return (
        <SafeAreaView style={styles.container}>
            <Pressable onPress={handlePress} >
                <View
                    style={[styles.card,
                            {
                                borderColor: settings.getSettings().borderColorNote,
                                backgroundColor: settings.getSettings().backgroundColor
                            }
                        ]}
                    >
                    <Text
                        style={[styles.titleText,
                                {
                                    backgroundColor: settings.getSettings().backgroundColor,
                                    color: settings.getSettings().textColor
                                }
                            ]}
                    >{props.title}
                    </Text>
                    <Text
                        numberOfLines={2}
                        style={[styles.previewText, { color: settings.getSettings().textColor }]}
                    >
                        {rows.map((row) => (row.content+"\n"))}
                    </Text>
                </View>
            </Pressable>
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    titleText: {
        fontSize: 20,
        fontWeight: "bold",
        paddingLeft: 15,
    },
    previewText: {
        paddingLeft: 15,
    },
    container: {
        padding: 10,
    },
    card: {
        padding: 5,
        borderWidth: 2,
        borderRadius: 20,
        maxHeight: 75,
    }
})